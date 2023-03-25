/*
 * Copyright 2023 Voyta Krizek, https://github.com/NewTownData
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.newtowndata.math.runner;

import com.newtowndata.math.config.Configuration;
import com.newtowndata.math.evolution.Evaluator;
import com.newtowndata.math.evolution.Evolution;
import com.newtowndata.math.genetics.MutationContextImpl;
import com.newtowndata.math.genetics.NodeFactory;
import com.newtowndata.math.genetics.NodeFactoryImpl;
import com.newtowndata.math.genetics.NodeUtils;
import com.newtowndata.math.genetics.nodes.core.Node;
import com.newtowndata.math.input.DataInput;
import com.newtowndata.math.model.HumanReadableModel;
import com.newtowndata.math.model.Model;
import com.newtowndata.math.model.RankedModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class Core {

  private static final Logger LOG = Logger.getLogger(Core.class.getName());

  private final CoreRuntime coreRuntime;
  private final MutationContextImpl mutationContext;
  private final Configuration config;
  private final NodeFactory nodeFactory;
  private final Evaluator evaluator;
  private final long updateEveryMs;

  public Core(CoreRuntime coreRuntime, DataInput dataInput, Configuration config) {
    this.coreRuntime = coreRuntime;
    this.config = config;

    this.mutationContext = new MutationContextImpl(dataInput.getXLength(),
        dataInput.computeGroups(),
        coreRuntime::createRandom);
    this.nodeFactory = new NodeFactoryImpl(mutationContext, config);

    this.evaluator = new Evaluator(config, dataInput);
    this.updateEveryMs = config.getProgressUpdateInSec() * 1000L;
  }

  private Model createRandomModel() {
    return new Model(nodeFactory.createLeafNode());
  }

  private long now() {
    return coreRuntime.getClock().millis();
  }

  public List<RankedModel> runEvolution() throws ExecutionException, InterruptedException {
    Evolution evolution = new Evolution(config.getPopulationSize());
    evolution.setPopulation(createInitialPopulation());

    double bestFitness = evolution.getBestModel().getFitness();
    int sameLoops = 0;
    long lastUpdate = coreRuntime.getClock().millis();

    final long endTime = now() + config.getMaxDurationInSec() * 1000L;

    while (now() < endTime) {
      Set<RankedModel> newModels = new HashSet<>();

      evolution.shuffleIndices();

      List<Future<RankedModel>> mutatedModels = new ArrayList<>();
      for (int i = 0; i < config.getMutationSize(); i++) {
        RankedModel model = evolution.getRandomModel(i);
        mutatedModels
            .add(coreRuntime.submit(createMutationTask(model)));
      }

      for (Future<RankedModel> mutatedModel : mutatedModels) {
        RankedModel model = mutatedModel.get();
        newModels.add(model);
      }

      List<Future<RankedModel>> crossoveredModels = new ArrayList<>();
      for (int i = 0; i < config.getCrossoverSize(); i++) {
        RankedModel model = evolution.getRandomModel(config.getPopulationSize() - i - 1);
        RankedModel otherModel = evolution.getRandomCrossoverModel(i);
        crossoveredModels
            .add(coreRuntime
                .submit(createCrossoverTask(model, otherModel)));
      }

      for (Future<RankedModel> crossoveredModel : crossoveredModels) {
        RankedModel model = crossoveredModel.get();
        newModels.add(model);
      }

      List<RankedModel> intermediatePopulation = new ArrayList<>(evolution.getPopulation());
      intermediatePopulation.addAll(newModels);
      Collections.sort(intermediatePopulation);

      List<RankedModel> newPopulation = new ArrayList<>(
          intermediatePopulation.subList(0,
              config.getPopulationSize()
                  - config.getReplaceableSize()
                  - config.getSurvivalSize()));

      Collections.shuffle(intermediatePopulation);

      newPopulation.addAll(intermediatePopulation.subList(0, config.getSurvivalSize()));

      for (int i = 0; i < config.getReplaceableSize(); i++) {
        Model model = createRandomModel();
        newPopulation.add(evaluator.evaluate(model));
      }

      evolution.setPopulation(newPopulation);

      double fitness = evolution.getBestModel().getFitness();
      if (fitness > bestFitness) {
        bestFitness = fitness;
        sameLoops = 0;
      } else {
        sameLoops++;
      }

      if (sameLoops > config.getMaxSameLoops()
          || evolution.getEvolutionStep() > config.getMaxIterations()) {
        break;
      }

      long now = now();
      if (now - lastUpdate > updateEveryMs) {
        LOG.info("Best " + HumanReadableModel.modelToString(evolution.getBestModel()));
        lastUpdate = now;
      }
    }

    LOG.info("Total loops: " + evolution.getEvolutionStep());

    return evolution.getPopulation();
  }

  private List<RankedModel> createInitialPopulation() {
    double fitness = evaluator.evaluate(createRandomModel()).getFitness();
    LOG.info(String.format(Locale.ENGLISH, "Initial fitness: %.6f", fitness));

    List<RankedModel> population = new ArrayList<>();
    for (int i = 0; i < config.getPopulationSize(); i++) {
      Model model = createRandomModel();
      population.add(evaluator.evaluate(model));
    }
    return population;
  }

  private Callable<RankedModel> createMutationTask(RankedModel model) {
    return () -> {
      Model result = mutateModel(model.getModel());
      return evaluator.evaluate(result);
    };
  }

  private Callable<RankedModel> createCrossoverTask(RankedModel model, RankedModel otherModel) {
    return () -> {
      Model result = crossoverModel(model.getModel(), otherModel.getModel());
      return evaluator.evaluate(result);
    };
  }

  private Model mutateModel(Model model) {
    int nodeNumber = mutationContext.getRandom().nextInt(0, model.getSize() - 1);
    if (mutationContext.getRandom().nextBoolean()) {
      return new Model(
          NodeUtils
              .replaceNode(model.getRoot(), nodeNumber, nodeFactory::createUnaryNode));
    } else {
      return new Model(NodeUtils.mutateNode(model.getRoot(), nodeNumber, mutationContext));
    }
  }

  private Model crossoverModel(Model model, Model otherModel) {
    int nodeNumber = mutationContext.getRandom().nextInt(0, model.getSize() - 1);
    int otherNodeNumber = mutationContext.getRandom().nextInt(0, otherModel.getSize() - 1);
    Node otherNode = NodeUtils.extractNode(otherModel.getRoot(), otherNodeNumber);
    return new Model(NodeUtils
        .replaceNode(model.getRoot(), nodeNumber,
            node -> nodeFactory.createAggregateNode(node, otherNode)));
  }


}
