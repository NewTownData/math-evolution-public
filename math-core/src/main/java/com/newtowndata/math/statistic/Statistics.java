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
package com.newtowndata.math.statistic;

import java.util.List;

public class Statistics {

  private final int count;
  private final double[] values;

  private double sum;

  public Statistics(int count) {
    this.count = count;
    this.values = new double[count];

    this.sum = 0;
  }

  public static <T> Statistics forList(List<T> items, ValueFunction<T> valueFunction) {
    Statistics statistics = new Statistics(items.size());
    for (int i = 0; i < statistics.count; i++) {
      statistics.setValue(i, valueFunction.getValue(items.get(i)));
    }
    return statistics;
  }

  public void setValue(int position, double value) {
    if (position >= count) {
      throw new IndexOutOfBoundsException("Invalid position - out of bounds");
    }

    sum += value;
    values[position] = value;
  }

  public int getCount() {
    return count;
  }

  public double[] getValues() {
    return values;
  }

  public double getMean() {
    return sum / (double) count;
  }

  public double getStandardDeviation() {
    final double mean = getMean();

    double standardDeviationSum = 0;
    for (int i = 0; i < count; i++) {
      double difference = values[i] - mean;
      standardDeviationSum += difference * difference;
    }

    return Math.sqrt(standardDeviationSum / (double) count);
  }

  public double getMinMaxDistance() {
    if (count == 0) {
      return 0;
    }

    double min = values[0];
    double max = values[0];
    for (int i = 1; i < count; i++) {
      if (values[i] < min) {
        min = values[i];
      }
      if (values[i] > max) {
        max = values[i];
      }
    }
    return max - min;
  }
}
