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
package com.newtowndata.math.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.newtowndata.math.genetics.nodes.ConstantNode;
import com.newtowndata.math.genetics.nodes.GroupConstantNode;
import com.newtowndata.math.genetics.nodes.IntConstantNode;
import com.newtowndata.math.genetics.nodes.PlusNode;
import com.newtowndata.math.genetics.nodes.SpecialConstantNode;
import com.newtowndata.math.genetics.nodes.SqrtNode;
import com.newtowndata.math.genetics.nodes.VariableNode;
import com.newtowndata.math.genetics.nodes.core.Node;
import com.newtowndata.math.genetics.nodes.helper.Constant;
import com.newtowndata.math.model.Model;
import com.newtowndata.math.serialization.ModelSerializer.IndexedNode;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ModelSerializerTest {

  private ModelSerializer serializer;

  @BeforeEach
  void setUp() {
    serializer = new ModelSerializer();
  }

  @Test
  void serialize() {
    String result = serializer.serialize(new Model(
            new PlusNode(
                new SqrtNode(
                    new ConstantNode(1)
                ),
                new ConstantNode(2)
            )
        )
    );

    assertEquals("y003=ConstantNode(1.0)\n"
        + "y002=SqrtNode(y003)\n"
        + "y001=ConstantNode(2.0)\n"
        + "y000=PlusNode(y001,y002)\n", result);
  }

  @Test
  void deserialize() {
    Model model = serializer.deserialize("y003=ConstantNode(1.0)\n"
        + "y002=SqrtNode(y003)\n"
        + "y001=ConstantNode(2.0)\n"
        + "y000=PlusNode(y001,y002)\n");

    assertEquals("(2.0+sqrt(1.0))", model.toString());
  }

  @Test
  void deserializeLeafNode() {
    Map<String, Node> nodes = new HashMap<>();
    Node node;

    node = serializer.deserializeNode(nodes, "y003=ConstantNode(1.0)");
    assertEquals("1.0", node.toString());

    node = serializer.deserializeNode(nodes, "y003=IntConstantNode(1)");
    assertEquals("1.0", node.toString());

    node = serializer.deserializeNode(nodes, "y003=SpecialConstantNode(pi[3.14])");
    assertEquals("pi", node.toString());

    node = serializer.deserializeNode(nodes, "y003=GroupConstantNode(abc[1.0],def[2.0])");
    assertEquals("group(abc=1.0,def=2.0)", node.toString());

    node = serializer.deserializeNode(nodes, "y003=VariableNode(1)");
    assertEquals("x2", node.toString());

    assertThrows(IllegalArgumentException.class,
        () -> serializer.deserializeLeafNode(String.class, "a"));
  }

  @Test
  void serializeNode() {
    assertEquals(
        "y001=ConstantNode(10.0)",
        serializer.serializeNode(
            new IndexedNode(1, new ConstantNode(10)),
            Collections.emptyList()
        )
    );

    assertEquals(
        "y002=PlusNode(y003,y004)",
        serializer.serializeNode(
            new IndexedNode(2, new PlusNode(new ConstantNode(1), new ConstantNode(2))),
            List.of(
                new IndexedNode(3, new ConstantNode(1)),
                new IndexedNode(4, new ConstantNode(2))
            )
        )
    );

    assertThrows(
        IllegalArgumentException.class,
        () -> serializer.serializeNode(
            new IndexedNode(1, new PlusNode(new ConstantNode(1), new ConstantNode(2))),
            Collections.emptyList()
        )
    );
  }

  @Test
  void serializeLeafNode() {
    assertEquals("10", serializer.serializeLeafNode(new IntConstantNode(10)));
    assertEquals("2.4", serializer.serializeLeafNode(new ConstantNode(2.4)));
    assertEquals("pi[3.141592653589793]",
        serializer.serializeLeafNode(SpecialConstantNode.of("pi")));
    assertEquals("2", serializer.serializeLeafNode(new VariableNode(2)));
    assertEquals("abc[2.0],def[4.0]", serializer.serializeLeafNode(
        new GroupConstantNode(List.of(
            new Constant("abc", 2),
            new Constant("def", 4))
        )
    ));
    assertThrows(IllegalArgumentException.class,
        () -> serializer.serializeLeafNode(new SqrtNode(new ConstantNode(2))));
  }

  @Test
  void serializeIndex() {
    assertEquals("y000", serializer.serializeIndex(0));
    assertEquals("y100", serializer.serializeIndex(100));
  }
}
