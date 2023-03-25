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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class ConfigurationTest {

  @Test
  void setMaxDurationInSec() {
    Configuration config = new Configuration();
    assertEquals(60, config.getMaxDurationInSec());

    config.setMaxDurationInSec(10);
    assertEquals(10, config.getMaxDurationInSec());
  }

  @Test
  void setMaxIterations() {
    Configuration config = new Configuration();
    assertEquals(Integer.MAX_VALUE, config.getMaxIterations());

    config.setMaxIterations(10000);
    assertEquals(10000, config.getMaxIterations());
  }

  @Test
  void setMinNodes() {
    Configuration config = new Configuration();
    assertEquals(1, config.getMinNodes());

    config.setMinNodes(10);
    assertEquals(10, config.getMinNodes());
  }

  @Test
  void setMaxNodes() {
    Configuration config = new Configuration();
    assertEquals(20, config.getMaxNodes());

    config.setMaxNodes(10);
    assertEquals(10, config.getMaxNodes());
  }

  @Test
  void setMaxSameLoops() {
    Configuration config = new Configuration();
    assertEquals(1000, config.getMaxSameLoops());

    config.setMaxSameLoops(10);
    assertEquals(10, config.getMaxSameLoops());
  }

  @Test
  void setProgressUpdateInSec() {
    Configuration config = new Configuration();
    assertEquals(5, config.getProgressUpdateInSec());

    config.setProgressUpdateInSec(10);
    assertEquals(10, config.getProgressUpdateInSec());
  }

  @Test
  void setGroupAllowed() {
    Configuration config = new Configuration();
    assertEquals(false, config.isGroupAllowed());

    config.setGroupAllowed(true);
    assertEquals(true, config.isGroupAllowed());
  }

  @Test
  void setPopulationSize() {
    Configuration config = new Configuration();
    assertEquals(10000, config.getPopulationSize());

    config.setPopulationSize(10);
    assertEquals(10, config.getPopulationSize());
  }

  @Test
  void setMutationSize() {
    Configuration config = new Configuration();
    assertEquals(2000, config.getMutationSize());

    config.setMutationSize(10);
    assertEquals(10, config.getMutationSize());
  }

  @Test
  void setCrossoverSize() {
    Configuration config = new Configuration();
    assertEquals(4000, config.getCrossoverSize());

    config.setCrossoverSize(10);
    assertEquals(10, config.getCrossoverSize());
  }

  @Test
  void setSurvivalSize() {
    Configuration config = new Configuration();
    assertEquals(1000, config.getSurvivalSize());

    config.setSurvivalSize(10);
    assertEquals(10, config.getSurvivalSize());
  }

  @Test
  void setReplaceableSize() {
    Configuration config = new Configuration();
    assertEquals(1000, config.getReplaceableSize());

    config.setReplaceableSize(10);
    assertEquals(10, config.getReplaceableSize());
  }

  @Test
  void setIterateMaxNodes() {
    Configuration config = new Configuration();
    assertEquals(false, config.isIterateMaxNodes());

    config.setIterateMaxNodes(true);
    assertEquals(true, config.isIterateMaxNodes());
  }

  @Test
  void setResultsInIteration() {
    Configuration config = new Configuration();
    assertEquals(5, config.getResultsInIteration());

    config.setResultsInIteration(10);
    assertEquals(10, config.getResultsInIteration());
  }

  @Test
  void setResultOutputPrefix() {
    Configuration config = new Configuration();
    assertEquals(null, config.getResultOutputPrefix());

    config.setResultOutputPrefix("x/y/z");
    assertEquals("x/y/z", config.getResultOutputPrefix());
  }

  @Test
  void setThreadCount() {
    Configuration config = new Configuration();
    assertEquals(0, config.getThreadCount());

    config.setThreadCount(10);
    assertEquals(10, config.getThreadCount());
  }

  @Test
  void setModelSizeImportance() {
    Configuration config = new Configuration();
    assertEquals(1e-3, config.getModelSizeImportance(), 1e-9);

    config.setModelSizeImportance(1e-2);
    assertEquals(1e-2, config.getModelSizeImportance(), 1e-9);
  }

  @Test
  void testToString() {
    assertNotNull(new Configuration().toString());
  }

  @Test
  void copy() {
    Configuration config = new Configuration();
    assertEquals(1000, config.getReplaceableSize());

    config.setReplaceableSize(10);

    Configuration copy = config.copy();

    config.setReplaceableSize(20);

    assertEquals(10, copy.getReplaceableSize());
    assertEquals(20, config.getReplaceableSize());
  }
}
