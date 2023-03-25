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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Test;

class ConfigurationLoaderTest {

  @Test
  void fromStream() throws IOException {
    Configuration config;
    try (InputStream is = ConfigurationTest.class.getResourceAsStream("test_config.properties")) {
      config = ConfigurationLoader.fromStream(is);
    }

    assertEquals(300, config.getMaxDurationInSec());
    assertEquals(10, config.getMaxIterations());
    assertEquals(10, config.getMinNodes());
    assertEquals(100, config.getMaxNodes());
    assertEquals(20, config.getMaxSameLoops());
    assertEquals(3, config.getProgressUpdateInSec());
    assertEquals(true, config.isGroupAllowed());
    assertEquals(1000, config.getPopulationSize());
    assertEquals(100, config.getMutationSize());
    assertEquals(99, config.getCrossoverSize());
    assertEquals(50, config.getSurvivalSize());
    assertEquals(20, config.getReplaceableSize());
    assertEquals(true, config.isIterateMaxNodes());
    assertEquals(6, config.getResultsInIteration());
    assertEquals("/tmp/results", config.getResultOutputPrefix());
    assertEquals(2, config.getThreadCount());
    assertEquals(1e-2, config.getModelSizeImportance());
  }

  @Test
  void fromStreamInvalidInt() throws IOException {
    try (InputStream is = ConfigurationTest.class
        .getResourceAsStream("test_config_wrong_int.properties")) {
      assertThrows(IllegalArgumentException.class, () -> ConfigurationLoader.fromStream(is));
    }
  }

  @Test
  void fromStreamNoData() throws IOException {
    Configuration config;
    try (InputStream is = new ByteArrayInputStream(new byte[0])) {
      config = ConfigurationLoader.fromStream(is);
    }

    assertEquals(60, config.getMaxDurationInSec());
    assertEquals(Integer.MAX_VALUE, config.getMaxIterations());
    assertEquals(20, config.getMaxNodes());
    assertEquals(1000, config.getMaxSameLoops());
    assertEquals(5, config.getProgressUpdateInSec());
    assertEquals(false, config.isGroupAllowed());
    assertEquals(10000, config.getPopulationSize());
    assertEquals(2000, config.getMutationSize());
    assertEquals(4000, config.getCrossoverSize());
    assertEquals(1000, config.getSurvivalSize());
    assertEquals(1000, config.getReplaceableSize());
  }

  @Test
  void fieldToPropertyName() {
    assertEquals("name", ConfigurationLoader.fieldToPropertyName("name"));
    assertEquals("test_name", ConfigurationLoader.fieldToPropertyName("testName"));
    assertEquals("some_other_name", ConfigurationLoader.fieldToPropertyName("someOtherName"));
  }
}
