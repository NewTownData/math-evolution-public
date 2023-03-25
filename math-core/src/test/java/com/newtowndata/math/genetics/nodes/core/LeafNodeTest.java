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

import com.newtowndata.math.genetics.MutationContext;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class LeafNodeTest extends AbstractNodeTest {

  protected abstract LeafNode createLeafNode(MutationContext mutationContext);

  protected LeafNode createLeafNode() {
    return createLeafNode(mutationContext);
  }

  @BeforeEach
  public void setUp() {
    when(mutationContext.getRandom()).thenReturn(random);
  }

  @Test
  void getChildren() {
    Node node = createLeafNode();
    assertEquals(Collections.emptyList(), node.getChildren());
  }

  @Test
  void replaceFail() {
    Node node = createLeafNode();
    assertThrows(IllegalArgumentException.class, () -> node.replace(0, createLeafNode()));
  }

  @Test
  void testEquality() {
    Node node1 = createLeafNode();
    Node node2 = createLeafNode();

    assertEquals(node1, node2);
    assertEquals(node1.hashCode(), node2.hashCode());
  }
}
