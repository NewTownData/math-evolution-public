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

import com.newtowndata.math.genetics.SimplificationContext;
import com.newtowndata.math.genetics.nodes.core.AbstractNodeTest;
import com.newtowndata.math.genetics.nodes.core.Node;
import com.newtowndata.math.genetics.nodes.core.SimplifiedNode;
import org.junit.jupiter.api.Test;

class NegativeNodeTest extends AbstractNodeTest {

  @Test
  void mutate() {
    Node node = new NegativeNode(new ConstantNode(4));
    Node result = node.mutate(mutationContext);
    assertEquals("4.0", result.toString());
  }

  @Test
  void evaluate() {
    Node node = new NegativeNode(new ConstantNode(4));
    double result = node.evaluate(null);
    assertEquals(-4.0, result, 1e-6);
  }

  @Test
  void replace() {
    Node node = new NegativeNode(new ConstantNode(4));
    Node result = node.replace(0, new ConstantNode(6));
    assertEquals("-6.0", result.toString());
  }

  @Test
  void simplify() {
    Node node = new NegativeNode(new ConstantNode(4));
    SimplifiedNode simplifiedNode = node.simplify(new SimplificationContext(true));

    assertEquals("-4.0", simplifiedNode.getNode().toString());
    assertEquals(true, simplifiedNode.isConstant());
  }

  @Test
  void simplifyVariable() {
    Node node = new NegativeNode(new VariableNode(2));
    SimplifiedNode simplifiedNode = node.simplify(new SimplificationContext(true));

    assertEquals("-x3", simplifiedNode.getNode().toString());
    assertEquals(false, simplifiedNode.isConstant());
  }
}
