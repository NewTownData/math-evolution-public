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

import com.newtowndata.math.config.Configuration;
import com.newtowndata.math.input.DataInput;
import com.newtowndata.math.input.DataRow;
import com.newtowndata.math.model.Model;
import com.newtowndata.math.model.RankedModel;
import com.newtowndata.math.statistic.Statistics;

public class Evaluator {

  private static final double MINIMUM_VALUE = 1e-100;
  private static final double NO_CORRELATION = 0;

  private final int maxNodes;

  private final DataRow[] input;

  private final int yCount;
  private final double[] yValues;
  private final double[] yAbsValues;

  private final double correlationRatio;
  private final double modelSizeRatio;

  public Evaluator(Configuration configuration, DataInput dataInput) {
    this.maxNodes = configuration.getMaxNodes();

    this.input = dataInput.getRows().toArray(new DataRow[0]);

    if (configuration.getModelSizeImportance() > 1.0) {
      throw new IllegalArgumentException("Model size importance must be lower or equal to 1");
    }

    this.correlationRatio = 1.0 - configuration.getModelSizeImportance();
    this.modelSizeRatio = configuration.getModelSizeImportance();

    Statistics statistics = Statistics.forList(dataInput.getRows(), DataRow::getY);
    this.yCount = statistics.getCount();
    this.yValues = statistics.getValues();

    double minMaxDistance = statistics.getMinMaxDistance();
    if (minMaxDistance < MINIMUM_VALUE) {
      minMaxDistance = MINIMUM_VALUE;
    }

    this.yAbsValues = new double[yCount];
    for (int i = 0; i < yCount; i++) {
      yAbsValues[i] = Math.abs(yValues[i]);
      if (yAbsValues[i] < MINIMUM_VALUE) {
        yAbsValues[i] = minMaxDistance;
      }
    }
  }

  public RankedModel evaluate(Model model) {
    if (model.getSize() > maxNodes) {
      return new RankedModel(model, NO_CORRELATION);
    }

    double similaritySum = 0;
    for (int i = 0; i < yCount; i++) {
      double result;
      try {
        result = model.evaluate(input[i]);
        if (!Double.isFinite(result)) {
          result = Long.MAX_VALUE;
        }
      } catch (IllegalStateException e) {
        result = Long.MAX_VALUE;
      }

      similaritySum += Math.exp(-Math.abs(yValues[i] - result) / yAbsValues[i]);
    }

    double correlation = similaritySum / (double) yCount;

    if (!Double.isFinite(correlation)) {
      correlation = NO_CORRELATION;
    }

    double modelSize = (maxNodes - model.getSize()) / (double) maxNodes;
    double fitness = correlation * correlationRatio + modelSize * modelSizeRatio;

    return new RankedModel(model, fitness);
  }

}
