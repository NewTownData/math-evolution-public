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
package com.newtowndata.math.model;

import java.util.Locale;
import java.util.Objects;

public class RankedModel implements Comparable<RankedModel> {

  private final Model model;
  private final double fitness;

  public RankedModel(Model model, double fitness) {
    this.model = model;
    this.fitness = fitness;
  }

  public Model getModel() {
    return model;
  }

  public double getFitness() {
    return fitness;
  }

  @Override
  public int compareTo(RankedModel o) {
    return Double.compare(o.fitness, fitness);
  }

  @Override
  public String toString() {
    return String.format(Locale.ENGLISH, "{%.6f, %s}", fitness, model);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RankedModel model1 = (RankedModel) o;
    return Objects.equals(model, model1.model);
  }

  @Override
  public int hashCode() {
    return Objects.hash(model);
  }
}
