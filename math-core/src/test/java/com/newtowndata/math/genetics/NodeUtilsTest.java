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
import static org.mockito.Mockito.when;

import com.newtowndata.math.genetics.nodes.ConstantNode;
import com.newtowndata.math.genetics.nodes.NegativeNode;
import com.newtowndata.math.genetics.nodes.PlusNode;
import com.newtowndata.math.genetics.nodes.core.AbstractNodeTest;
import com.newtowndata.math.genetics.nodes.core.Node;
import org.junit.jupiter.api.Test;

class NodeUtilsTest extends AbstractNodeTest {

  @Test
  void calculateSize() {
    Node root = new PlusNode(new NegativeNode(new ConstantNode(1)), new ConstantNode(3));
    int result = NodeUtils.calculateSize(root);
    assertEquals(4, result);
  }

  @Test
  void extractNode() {
    Node root = new PlusNode(new ConstantNode(3), new NegativeNode(new ConstantNode(1)));
    Node result;

    result = NodeUtils.extractNode(root, 0);
    assertEquals("(3.0+-1.0)", result.toString());

    result = NodeUtils.extractNode(root, 1);
    assertEquals("3.0", result.toString());

    result = NodeUtils.extractNode(root, 2);
    assertEquals("-1.0", result.toString());

    result = NodeUtils.extractNode(root, 3);
    assertEquals("1.0", result.toString());

    result = NodeUtils.extractNode(root, 10);
    assertEquals("(3.0+-1.0)", result.toString());
  }

  @Test
  void replaceNode() {
    Node root = new PlusNode(new ConstantNode(3), new NegativeNode(new ConstantNode(1)));
    Node result;

    result = NodeUtils.replaceNode(root, 0, node -> new NegativeNode(node));
    assertEquals("-(3.0+-1.0)", result.toString());
  }

  @Test
  void mutateNode() {
    when(mutationContext.getRandom()).thenReturn(random);
    when(random.nextDouble(-1, 1)).thenReturn(0.5);

    Node root = new PlusNode(new ConstantNode(3), new NegativeNode(new ConstantNode(1)));
    Node result;

    result = NodeUtils.mutateNode(root, 3, mutationContext);
    assertEquals("(3.0+-1.5)", result.toString());
  }

  @Test
  void mutateNodeOverLimit() {
    Node root = new PlusNode(new ConstantNode(3), new NegativeNode(new ConstantNode(1)));
    Node result;

    result = NodeUtils.mutateNode(root, 10, mutationContext);
    assertEquals("(3.0+-1.0)", result.toString());
  }
}
