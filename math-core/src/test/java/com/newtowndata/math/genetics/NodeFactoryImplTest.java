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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import com.newtowndata.math.config.Configuration;
import com.newtowndata.math.genetics.nodes.core.AbstractNodeTest;
import com.newtowndata.math.genetics.nodes.core.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NodeFactoryImplTest extends AbstractNodeTest {

  private NodeFactoryImpl nodeFactory;

  @BeforeEach
  public void setUp() {
    when(mutationContext.getRandom()).thenReturn(random);

    nodeFactory = new NodeFactoryImpl(mutationContext, new Configuration());
  }

  @Test
  void createVariable() {
    when(random.nextInt(anyInt(), anyInt())).thenReturn(1, 0);

    Node node = nodeFactory.createLeafNode();
    assertEquals("x1", node.toString());
  }

  @Test
  void createConstant() {
    when(random.nextInt(anyInt(), anyInt())).thenReturn(0, 2);

    Node node = nodeFactory.createLeafNode();
    assertEquals("2.0", node.toString());
  }
}
