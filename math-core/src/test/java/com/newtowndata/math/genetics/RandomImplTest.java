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
package com.newtowndata.math.genetics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RandomImplTest {

  private RandomImpl random;

  @BeforeEach
  void setUp() {
    random = new RandomImpl(new Random(1L));
  }

  @Test
  void nextBoolean() {
    Set<Boolean> values = new HashSet<>();
    for (int i = 0; i < 10; i++) {
      values.add(random.nextBoolean());
    }

    assertEquals(2, values.size());
  }

  @Test
  void nextInt() {
    Set<Integer> values = new HashSet<>();
    for (int i = 0; i < 50; i++) {
      values.add(random.nextInt(-2, 2));
    }

    assertEquals(Set.of(-2, -1, 0, 1, 2), values);
  }

  @Test
  void nextDouble() {
    double min = 10;
    double max = -10;

    for (int i = 0; i < 50; i++) {
      double x = random.nextDouble(-1, 1);
      if (x < min) {
        min = x;
      }
      if (x > max) {
        max = x;
      }
    }

    assertTrue(min < -0.9);
    assertTrue(max > 0.9);
  }
}
