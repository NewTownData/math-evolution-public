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
import org.junit.jupiter.api.Test;

class ConstantNodeTest extends LeafNodeTest {

  @Test
  void testMutate() {
    when(random.nextDouble(-1, 1)).thenReturn(0.4);

    ConstantNode node = new ConstantNode(4);
    Node result = node.mutate(mutationContext);

    assertEquals("5.6", result.toString());
  }

  @Test
  void testEvaluate() {
    Node node = createLeafNode();
    double result = node.evaluate(null);
    assertEquals(0, result, 1e-6);
  }

  @Test
  void testToString() {
    Node node = createLeafNode();
    assertEquals("0.0", node.toString());
  }

  @Test
  void testSimplify() {
    Node node = createLeafNode();
    assertEquals(node, node.simplify(new SimplificationContext(false)).getNode());
  }

  @Override
  protected LeafNode createLeafNode(MutationContext mutationContext) {
    return new ConstantNode(mutationContext);
  }
}
