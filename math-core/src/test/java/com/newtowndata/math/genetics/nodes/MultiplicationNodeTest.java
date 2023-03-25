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
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.newtowndata.math.genetics.nodes.core.BinaryNode;
import com.newtowndata.math.genetics.nodes.core.BinaryNodeTest;
import com.newtowndata.math.genetics.nodes.core.Node;
import org.junit.jupiter.api.Test;

class MultiplicationNodeTest extends BinaryNodeTest {

  @Test
  void evaluate() {
    Node node = new MultiplicationNode(new ConstantNode(3), new ConstantNode(2));
    double result = node.evaluate(null);
    assertEquals(6, result, 1e-9);
  }

  @Test
  void evaluateOutOfRange() {
    Node node = new MultiplicationNode(new ConstantNode(Double.MAX_VALUE), new ConstantNode(Double.MAX_VALUE));
    assertThrows(IllegalStateException.class, () -> node.evaluate(null));
  }

  @Test
  void replace() {
    Node node = createBinaryNode();
    Node result = node.replace(0, TEST_ANOTHER_CHILD);
    assertEquals("(t0*t0)", result.toString());
  }

  @Test
  void testToString() {
    Node node = createBinaryNode();
    assertEquals("(e*t0)", node.toString());
  }

  @Override
  protected BinaryNode createBinaryNode(Node left, Node right) {
    return new MultiplicationNode(left, right);
  }
}
