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
import static org.mockito.Mockito.when;

import com.newtowndata.math.genetics.MutationContext;
import com.newtowndata.math.genetics.SimplificationContext;
import com.newtowndata.math.genetics.nodes.core.LeafNode;
import com.newtowndata.math.genetics.nodes.core.LeafNodeTest;
import com.newtowndata.math.genetics.nodes.core.Node;
import com.newtowndata.math.input.DataRow;
import org.junit.jupiter.api.Test;

class VariableNodeTest extends LeafNodeTest {

  @Test
  void testMutateToX2() {
    when(mutationContext.getXLength()).thenReturn(2);
    when(random.nextInt(0, 1)).thenReturn(1);

    VariableNode node = new VariableNode(0);
    Node result = node.mutate(mutationContext);

    assertEquals("x2", result.toString());
  }

  @Test
  void testMutateToX1() {
    when(mutationContext.getXLength()).thenReturn(2);
    when(random.nextInt(0, 1)).thenReturn(0);

    VariableNode node = new VariableNode(0);
    Node result = node.mutate(mutationContext);

    assertEquals("x1", result.toString());
  }

  @Test
  void testEvaluate() {
    Node node = createLeafNode();
    double result = node.evaluate(new DataRow(0, 2, 3));
    assertEquals(2, result, 1e-6);
  }


  @Test
  void testToString() {
    Node node = createLeafNode();
    assertEquals("x1", node.toString());
  }

  @Test
  void testSimplify() {
    Node node = createLeafNode();
    assertEquals(node, node.simplify(new SimplificationContext(false)).getNode());
  }

  @Override
  protected LeafNode createLeafNode(MutationContext mutationContext) {
    return new VariableNode(mutationContext);
  }
}
