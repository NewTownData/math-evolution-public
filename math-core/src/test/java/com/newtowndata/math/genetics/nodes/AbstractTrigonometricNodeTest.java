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

import com.newtowndata.math.genetics.nodes.core.Node;
import com.newtowndata.math.genetics.nodes.core.UnaryNodeTest;
import org.junit.jupiter.api.Test;

abstract class AbstractTrigonometricNodeTest extends UnaryNodeTest {

  @Test
  void replace() {
    Node node = createUnaryNode();
    Node result = node.replace(0, TEST_ANOTHER_CHILD);
    assertEquals(getStringName() + "(t0)", result.toString());
  }

  @Test
  void testToString() {
    Node node = createUnaryNode();
    assertEquals(getStringName() + "(e)", node.toString());
  }

  protected abstract String getStringName();
}
