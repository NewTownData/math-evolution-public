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
import static org.mockito.Mockito.when;

import com.newtowndata.math.genetics.SimplificationContext;
import com.newtowndata.math.genetics.nodes.SpecialConstantNode;
import java.util.List;
import org.junit.jupiter.api.Test;

public abstract class BinaryNodeTest extends AbstractNodeTest {

  protected static final Node TEST_CHILD = SpecialConstantNode.of("e");
  protected static final Node TEST_ANOTHER_CHILD = SpecialConstantNode.of("t0");

  protected abstract BinaryNode createBinaryNode(Node left, Node right);

  protected BinaryNode createBinaryNode() {
    return createBinaryNode(TEST_CHILD, TEST_ANOTHER_CHILD);
  }

  @Test
  void replaceFail() {
    Node node = createBinaryNode();
    assertThrows(IllegalArgumentException.class, () -> node.replace(2, createBinaryNode()));
  }

  @Test
  void getChildren() {
    Node node = createBinaryNode();
    assertEquals(List.of(TEST_CHILD, TEST_ANOTHER_CHILD), node.getChildren());
  }

  @Test
  void testEquality() {
    Node node1 = createBinaryNode();
    Node node2 = createBinaryNode();

    assertEquals(node1, node2);
    assertEquals(node1.hashCode(), node2.hashCode());
  }

  @Test
  void simplify() {
    Node node = createBinaryNode();
    Node result = node.simplify(new SimplificationContext(false)).getNode();
    assertEquals(node, result);
  }

  @Test
  void mutateLeft() {
    when(mutationContext.getRandom()).thenReturn(random);
    when(random.nextBoolean()).thenReturn(true);

    Node node = createBinaryNode();
    Node result = node.mutate(mutationContext);
    assertEquals("e", result.toString());
  }

  @Test
  void mutateRight() {
    when(mutationContext.getRandom()).thenReturn(random);

    Node node = createBinaryNode();
    Node result = node.mutate(mutationContext);
    assertEquals("t0", result.toString());
  }

}
