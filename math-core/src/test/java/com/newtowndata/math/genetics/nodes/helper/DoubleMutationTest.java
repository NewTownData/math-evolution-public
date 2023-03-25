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
package com.newtowndata.math.genetics.nodes.helper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.newtowndata.math.genetics.nodes.core.AbstractNodeTest;
import org.junit.jupiter.api.Test;

class DoubleMutationTest extends AbstractNodeTest {

  @Test
  void create() {
    when(mutationContext.getRandom()).thenReturn(random);
    when(random.nextInt(-1000, 1000)).thenReturn(2);

    double result = DoubleMutation.create(mutationContext);
    assertEquals(2, result, 1e-9);
  }

  @Test
  void mutate() {
    when(mutationContext.getRandom()).thenReturn(random);

    when(random.nextInt(0, 4)).thenReturn(0);
    when(random.nextDouble(-1, 1)).thenReturn(-0.1);
    double result = DoubleMutation.mutate(mutationContext, 20);
    assertEquals(18, result, 1e-9);

    when(random.nextInt(0, 4)).thenReturn(1);
    when(random.nextInt(-100, 100)).thenReturn(3);
    result = DoubleMutation.mutate(mutationContext, 20);
    assertEquals(1020, result, 1e-9);

    when(random.nextInt(0, 4)).thenReturn(2);
    when(random.nextInt(-100, 100)).thenReturn(3);
    result = DoubleMutation.mutate(mutationContext, 20);
    assertEquals(-980, result, 1e-9);

    when(random.nextInt(0, 4)).thenReturn(3);
    result = DoubleMutation.mutate(mutationContext, 20.55);
    assertEquals(21, result, 1e-9);

    when(random.nextInt(0, 4)).thenReturn(4);
    when(random.nextInt(-100, 100)).thenReturn(3);
    result = DoubleMutation.mutate(mutationContext, 20);
    assertEquals(20000, result, 1e-9);
  }
}
