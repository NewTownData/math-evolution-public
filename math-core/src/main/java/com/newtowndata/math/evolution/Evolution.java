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
package com.newtowndata.math.evolution;

import com.newtowndata.math.model.RankedModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Evolution {

  private final List<Integer> indexList;
  private final List<Integer> indexListCrossover;

  private List<RankedModel> population;
  private int evolutionStep = 0;

  public Evolution(int populationSize) {
    this.indexList = createPopulationIndex(populationSize);
    this.indexListCrossover = createPopulationIndex(populationSize);

    this.population = new ArrayList<>(populationSize);
  }

  public List<RankedModel> getPopulation() {
    return population;
  }

  public void setPopulation(List<RankedModel> population) {
    this.population = population;
    Collections.sort(this.population);
    evolutionStep++;
  }

  public int getEvolutionStep() {
    return evolutionStep;
  }

  public RankedModel getBestModel() {
    return population.get(0);
  }

  private List<Integer> createPopulationIndex(int populationSize) {
    List<Integer> list = new ArrayList<>(populationSize);
    for (int i = 0; i < populationSize; i++) {
      list.add(i);
    }
    return list;
  }

  public void shuffleIndices() {
    Collections.shuffle(indexList);
    Collections.shuffle(indexListCrossover);
  }

  public RankedModel getRandomModel(int index) {
    return population.get(indexList.get(index));
  }

  public RankedModel getRandomCrossoverModel(int index) {
    return population.get(indexListCrossover.get(index));
  }
}
