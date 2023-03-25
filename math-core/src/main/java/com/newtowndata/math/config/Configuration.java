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
package com.newtowndata.math.config;

import java.lang.reflect.Field;

public class Configuration {

  private int maxDurationInSec;
  private int maxIterations;
  private int minNodes;
  private int maxNodes;
  private int maxSameLoops;
  private int progressUpdateInSec;

  private boolean groupAllowed;

  private int populationSize;
  private int mutationSize;
  private int crossoverSize;
  private int survivalSize;
  private int replaceableSize;

  private boolean iterateMaxNodes;
  private int resultsInIteration;
  private String resultOutputPrefix;

  private int threadCount;

  private double modelSizeImportance;

  /**
   * Default configuration
   */
  public Configuration() {
    this.maxDurationInSec = 60;
    this.maxIterations = Integer.MAX_VALUE;
    this.minNodes = 1;
    this.maxNodes = 20;
    this.maxSameLoops = 1000;
    this.progressUpdateInSec = 5;

    this.groupAllowed = false;

    this.populationSize = 10000;
    this.mutationSize = 2000;
    this.crossoverSize = 4000;
    this.survivalSize = 1000;
    this.replaceableSize = 1000;

    this.iterateMaxNodes = false;
    this.resultsInIteration = 5;
    this.resultOutputPrefix = null;

    this.threadCount = 0; // = CPU

    this.modelSizeImportance = 1e-3; // 0.1%
  }

  public int getMaxDurationInSec() {
    return maxDurationInSec;
  }

  public void setMaxDurationInSec(int maxDurationInSec) {
    this.maxDurationInSec = maxDurationInSec;
  }

  public int getMaxIterations() {
    return maxIterations;
  }

  public void setMaxIterations(int maxIterations) {
    this.maxIterations = maxIterations;
  }

  public int getMaxNodes() {
    return maxNodes;
  }

  public void setMaxNodes(int maxNodes) {
    this.maxNodes = maxNodes;
  }

  public int getMinNodes() {
    return minNodes;
  }

  public void setMinNodes(int minNodes) {
    this.minNodes = minNodes;
  }

  public int getMaxSameLoops() {
    return maxSameLoops;
  }

  public void setMaxSameLoops(int maxSameLoops) {
    this.maxSameLoops = maxSameLoops;
  }

  public int getProgressUpdateInSec() {
    return progressUpdateInSec;
  }

  public void setProgressUpdateInSec(int progressUpdateInSec) {
    this.progressUpdateInSec = progressUpdateInSec;
  }

  public boolean isGroupAllowed() {
    return groupAllowed;
  }

  public void setGroupAllowed(boolean groupAllowed) {
    this.groupAllowed = groupAllowed;
  }

  public int getPopulationSize() {
    return populationSize;
  }

  public void setPopulationSize(int populationSize) {
    this.populationSize = populationSize;
  }

  public int getMutationSize() {
    return mutationSize;
  }

  public void setMutationSize(int mutationSize) {
    this.mutationSize = mutationSize;
  }

  public int getCrossoverSize() {
    return crossoverSize;
  }

  public void setCrossoverSize(int crossoverSize) {
    this.crossoverSize = crossoverSize;
  }

  public int getSurvivalSize() {
    return survivalSize;
  }

  public void setSurvivalSize(int survivalSize) {
    this.survivalSize = survivalSize;
  }

  public int getReplaceableSize() {
    return replaceableSize;
  }

  public void setReplaceableSize(int replaceableSize) {
    this.replaceableSize = replaceableSize;
  }

  public boolean isIterateMaxNodes() {
    return iterateMaxNodes;
  }

  public void setIterateMaxNodes(boolean iterateMaxNodes) {
    this.iterateMaxNodes = iterateMaxNodes;
  }

  public int getResultsInIteration() {
    return resultsInIteration;
  }

  public void setResultsInIteration(int resultsInIteration) {
    this.resultsInIteration = resultsInIteration;
  }

  public String getResultOutputPrefix() {
    return resultOutputPrefix;
  }

  public void setResultOutputPrefix(String resultOutputPrefix) {
    this.resultOutputPrefix = resultOutputPrefix;
  }

  public int getThreadCount() {
    return threadCount;
  }

  public void setThreadCount(int threadCount) {
    this.threadCount = threadCount;
  }

  public double getModelSizeImportance() {
    return modelSizeImportance;
  }

  public void setModelSizeImportance(double modelSizeImportance) {
    this.modelSizeImportance = modelSizeImportance;
  }

  @Override
  public String toString() {
    try {
      StringBuilder sb = new StringBuilder("Configuration{");

      Field[] fields = Configuration.class.getDeclaredFields();
      for (int i = 0; i < fields.length; i++) {
        if (i > 0) {
          sb.append(", ");
        }

        sb.append(fields[i].getName());
        sb.append('=');
        sb.append(fields[i].get(this));
      }

      sb.append('}');
      return sb.toString();
    } catch (IllegalAccessException e) {
      throw new IllegalStateException("Cannot create copy");
    }
  }

  public Configuration copy() {
    try {
      Configuration config = new Configuration();

      Field[] fields = Configuration.class.getDeclaredFields();
      for (Field field : fields) {
        field.set(config, field.get(this));

      }
      return config;
    } catch (IllegalAccessException e) {
      throw new IllegalStateException("Cannot create copy");
    }
  }
}
