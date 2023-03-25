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

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.newtowndata.math.config.Configuration;
import java.util.List;
import org.junit.jupiter.api.Test;

class CoreRuntimeConfigProviderTest {

  @Test
  void provideNoIteration() {
    List<CoreRuntimeConfig> result = CoreRuntimeConfigProvider.provide(new Configuration());
    assertEquals(1, result.size());
    assertEquals(20, result.get(0).getMaxNodes());
  }

  @Test
  void provideIteration() {
    Configuration config = new Configuration();
    config.setIterateMaxNodes(true);
    config.setResultsInIteration(2);
    config.setMinNodes(2);
    config.setMaxNodes(5);

    List<CoreRuntimeConfig> result = CoreRuntimeConfigProvider.provide(config);
    assertEquals(8, result.size());
    assertEquals(2, result.get(0).getMaxNodes());
    assertEquals(2, result.get(1).getMaxNodes());
    assertEquals(3, result.get(2).getMaxNodes());
    assertEquals(3, result.get(3).getMaxNodes());
    assertEquals(5, result.get(6).getMaxNodes());
    assertEquals(5, result.get(7).getMaxNodes());
  }
}
