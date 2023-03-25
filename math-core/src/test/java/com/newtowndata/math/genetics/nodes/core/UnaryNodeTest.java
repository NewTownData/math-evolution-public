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
package com.newtowndata.math.genetics.nodes.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.newtowndata.math.genetics.SimplificationContext;
import com.newtowndata.math.genetics.nodes.SpecialConstantNode;
import java.util.List;
import org.junit.jupiter.api.Test;

public abstract class UnaryNodeTest extends AbstractNodeTest {

  protected static final Node TEST_CHILD = SpecialConstantNode.of("e");
  protected static final Node TEST_ANOTHER_CHILD = SpecialConstantNode.of("t0");

  protected abstract UnaryNode createUnaryNode(Node child);

  protected UnaryNode createUnaryNode() {
    return createUnaryNode(TEST_CHILD);
  }

  @Test
  void replaceFail() {
    Node node = createUnaryNode();
    assertThrows(IllegalArgumentException.class, () -> node.replace(1, createUnaryNode()));
  }

  @Test
  void getChildren() {
    Node node = createUnaryNode();
    assertEquals(List.of(TEST_CHILD), node.getChildren());
  }

  @Test
  void testEquality() {
    Node node1 = createUnaryNode();
    Node node2 = createUnaryNode();

    assertEquals(node1, node2);
    assertEquals(node1.hashCode(), node2.hashCode());
  }

  @Test
  void simplify() {
    Node node = createUnaryNode();
    Node result = node.simplify(new SimplificationContext(false)).getNode();
    assertEquals(node, result);
  }

  @Test
  void mutate() {
    Node node = createUnaryNode();
    Node result = node.mutate(mutationContext);
    assertEquals("e", result.toString());
  }
}
