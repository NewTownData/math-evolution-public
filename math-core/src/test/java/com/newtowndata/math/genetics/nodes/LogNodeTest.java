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

import com.newtowndata.math.genetics.nodes.core.Node;
import com.newtowndata.math.genetics.nodes.core.UnaryNode;
import com.newtowndata.math.genetics.nodes.core.UnaryNodeTest;
import org.junit.jupiter.api.Test;

class LogNodeTest extends UnaryNodeTest {

  @Test
  void replace() {
    Node node = createUnaryNode();
    Node result = node.replace(0, TEST_ANOTHER_CHILD);
    assertEquals("log(t0)", result.toString());
  }

  @Test
  void evaluate() {
    Node node = createUnaryNode();
    double result = node.evaluate(null);
    assertEquals(1, result, 1e-9);
  }

  @Test
  void evaluateOutOfRange() {
    Node node = new LogNode(new ConstantNode(0));
    assertThrows(IllegalStateException.class, () -> node.evaluate(null));
  }

  @Test
  void testToString() {
    Node node = createUnaryNode();
    assertEquals("log(e)", node.toString());
  }

  @Override
  protected UnaryNode createUnaryNode(Node child) {
    return new LogNode(child);
  }
}
