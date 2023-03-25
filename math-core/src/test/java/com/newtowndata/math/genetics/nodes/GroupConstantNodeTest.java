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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import com.newtowndata.math.genetics.MutationContext;
import com.newtowndata.math.genetics.SimplificationContext;
import com.newtowndata.math.genetics.nodes.core.LeafNode;
import com.newtowndata.math.genetics.nodes.core.LeafNodeTest;
import com.newtowndata.math.genetics.nodes.core.Node;
import com.newtowndata.math.input.DataRow;
import java.util.List;
import org.junit.jupiter.api.Test;

class GroupConstantNodeTest extends LeafNodeTest {

  @Test
  void mutate() {
    when(random.nextInt(anyInt(), anyInt())).thenReturn(2, 1);

    Node node = createLeafNode();
    assertEquals("group(abc=2.0,def=2.0)", node.toString());

    Node result = node.mutate(mutationContext);
    assertEquals("group(abc=2.0,def=12.0)", result.toString());
  }

  @Test
  void evaluate() {
    when(mutationContext.getGroups()).thenReturn(List.of("a", "b"));

    Node node = createLeafNode();
    assertEquals(0.0, node.evaluate(new DataRow(0, "abc", 0)), 1e-9);
  }

  @Test
  void evaluateOutOfRange() {
    Node node = createLeafNode();
    assertThrows(IllegalStateException.class, () -> node.evaluate(null));
    assertThrows(IllegalStateException.class, () -> node.evaluate(new DataRow(0, "b", 0)));
  }

  @Test
  void testToString() {
    when(mutationContext.getGroups()).thenReturn(List.of("a", "b"));

    Node node = createLeafNode();
    assertEquals("group(abc=0.0,def=0.0)", node.toString());
  }

  @Test
  void testSimplify() {
    when(random.nextInt(anyInt(), anyInt())).thenReturn(2, 1);

    Node node = createLeafNode();
    assertEquals(new ConstantNode(2.0), node.simplify(new SimplificationContext(false)).getNode());

    Node result = node.mutate(mutationContext);
    assertEquals(result, result.simplify(new SimplificationContext(false)).getNode());
  }

  @Override
  protected LeafNode createLeafNode(MutationContext mutationContext) {
    when(mutationContext.getGroups()).thenReturn(List.of("abc", "def"));
    return new GroupConstantNode(mutationContext);
  }
}
