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
import org.junit.jupiter.api.Test;

class CoshNodeTest extends AbstractTrigonometricNodeTest {

  @Test
  void evaluate() {
    Node node = new CoshNode(new ConstantNode(4));
    double result = node.evaluate(null);
    assertEquals(27.308232836016487, result, 1e-9);
  }

  @Test
  void evaluateOutOfRange() {
    Node node = new CoshNode(new ConstantNode(Double.MAX_VALUE));
    assertThrows(IllegalStateException.class, () -> node.evaluate(null));
  }

  @Override
  protected String getStringName() {
    return "cosh";
  }

  @Override
  protected UnaryNode createUnaryNode(Node child) {
    return new CoshNode(child);
  }
}
