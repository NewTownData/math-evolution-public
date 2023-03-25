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
package com.newtowndata.math.genetics.nodes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import com.newtowndata.math.genetics.MutationContext;
import com.newtowndata.math.genetics.SimplificationContext;
import com.newtowndata.math.genetics.nodes.core.LeafNode;
import com.newtowndata.math.genetics.nodes.core.LeafNodeTest;
import com.newtowndata.math.genetics.nodes.core.Node;
import org.junit.jupiter.api.Test;

class SpecialConstantNodeTest extends LeafNodeTest {

  @Test
  void mutate() {
    when(random.nextInt(anyInt(), anyInt())).thenReturn(0, 1);

    Node node = createLeafNode();
    assertEquals("pi", node.toString());

    Node result = node.mutate(mutationContext);
    assertEquals("e", result.toString());
  }

  @Test
  void allConstants() {
    Integer[] values = new Integer[17 - 1];
    for (int i = 0; i < values.length; i++) {
      values[i] = i + 1;
    }

    when(random.nextInt(anyInt(), anyInt())).thenReturn(0, values);

    assertConstant("pi", StrictMath.PI);
    assertConstant("e", StrictMath.E);
    assertConstant("_180", 180);
    assertConstant("_10", 10);
    assertConstant("_2", 2);
    assertConstant("_1", 1);
    assertConstant("t0", -273.15);
    assertConstant("k", 1.380649e-23);
    assertConstant("h", 6.62607015e-34);
    assertConstant("c", 299792458);
    assertConstant("q_e", 1.602176634e-19);
    assertConstant("f_cs", 9192631770.0);
    assertConstant("L", 6.02214076e23);
    assertConstant("a0", 5.29177210903e-11);
    assertConstant("alpha", 0.0072973525693);
    assertConstant("m_e", 9.1093837015e-31);
    assertConstant("m_p", 1.67262192369e-27);
  }

  @Test
  void evaluate() {
    Node node = createLeafNode();
    assertEquals(StrictMath.PI, node.evaluate(null), 1e-9);
  }

  @Test
  void testToString() {
    Node node = createLeafNode();
    assertEquals("pi", node.toString());
  }

  @Test
  void testSimplify() {
    Node node = createLeafNode();
    assertEquals(node, node.simplify(new SimplificationContext(false)).getNode());
  }

  @Override
  protected LeafNode createLeafNode(MutationContext mutationContext) {
    return new SpecialConstantNode(mutationContext);
  }

  private void assertConstant(String name, double value) {
    Node node = createLeafNode();
    assertEquals(name, node.toString());
    assertEquals(value, node.evaluate(null));
  }
}
