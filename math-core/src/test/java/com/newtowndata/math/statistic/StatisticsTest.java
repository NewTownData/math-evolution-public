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

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class StatisticsTest {

  @Test
  void testAll() {
    Statistics statistics = new Statistics(3);
    statistics.setValue(0, -5);
    statistics.setValue(1, 10);
    statistics.setValue(2, 25);

    assertEquals(3, statistics.getCount());
    assertArrayEquals(new double[]{-5, 10, 25}, statistics.getValues());
    assertEquals(10, statistics.getMean());
    assertEquals(12.247, statistics.getStandardDeviation(), 1e-3);
  }

  @Test
  void testForList() {
    Statistics statistics = Statistics
        .forList(List.of("1", "2", "3"), input -> Integer.parseInt(input));

    assertEquals(3, statistics.getCount());
    assertArrayEquals(new double[]{1, 2, 3}, statistics.getValues());
    assertEquals(2, statistics.getMean());
    assertEquals(0.816, statistics.getStandardDeviation(), 1e-3);
  }

}
