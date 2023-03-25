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
package com.newtowndata.math.analysis.command;

import com.newtowndata.math.config.Configuration;
import com.newtowndata.math.evolution.Evaluator;
import com.newtowndata.math.input.DataInput;
import com.newtowndata.math.input.DataRow;
import com.newtowndata.math.input.DataRowsFileLoader;
import com.newtowndata.math.model.Model;
import com.newtowndata.math.model.RankedModel;
import com.newtowndata.math.serialization.ModelSerializer;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FitnessCommand implements Command {

  private final ModelSerializer modelSerializer = new ModelSerializer();

  @Override
  public String name() {
    return "fitness";
  }

  @Override
  public void execute(String[] args) {
    if (args.length < 2) {
      throw new IllegalArgumentException("Missing arguments: <model path> <input path>");
    }

    String modelPath = args[0];
    String inputPath = args[1];

    computeFitness(modelPath, inputPath);
  }

  private void computeFitness(String modelPath, String inputPath) {
    Model model;
    try (FileInputStream fis = new FileInputStream(modelPath)) {
      String modelData = new String(fis.readAllBytes(), StandardCharsets.UTF_8);
      model = modelSerializer.deserialize(modelData);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to load model from path " + modelPath, e);
    }

    List<DataRow> dataInput = DataRowsFileLoader.loadInputFile(inputPath);
    computeFitness(model, dataInput);
  }

  private void computeFitness(Model model, List<DataRow> input) {
    Configuration configuration = new Configuration();
    configuration.setMaxNodes(Integer.MAX_VALUE);
    configuration.setModelSizeImportance(0);

    Evaluator evaluator = new Evaluator(configuration, new DataInput(input));
    RankedModel rankedModel = evaluator.evaluate(model);
    System.out.println("Fitness: " + rankedModel.getFitness());
  }
}
