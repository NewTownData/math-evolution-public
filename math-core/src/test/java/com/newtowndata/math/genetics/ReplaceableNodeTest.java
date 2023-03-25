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

import com.newtowndata.math.genetics.nodes.ConstantNode;
import com.newtowndata.math.genetics.nodes.PlusNode;
import com.newtowndata.math.genetics.nodes.core.Node;
import org.junit.jupiter.api.Test;

class ReplaceableNodeTest {

  @Test
  void replaceRoot() {
    ConstantNode n1 = new ConstantNode(2);
    ConstantNode n2 = new ConstantNode(3);

    ReplaceableNode root = new ReplaceableNode(n1);
    Node result = root.replace(n2);

    assertEquals("3.0", result.toString());
  }

  @Test
  void replaceChild() {
    ConstantNode n1 = new ConstantNode(2);
    ConstantNode n2 = new ConstantNode(3);
    ConstantNode n3 = new ConstantNode(4);
    PlusNode plusNode = new PlusNode(n1, n2);

    ReplaceableNode root = new ReplaceableNode(plusNode);
    ReplaceableNode child = new ReplaceableNode(root, 1, n2);
    Node result = child.replace(n3);

    assertEquals("(2.0+4.0)", result.toString());
  }
}
