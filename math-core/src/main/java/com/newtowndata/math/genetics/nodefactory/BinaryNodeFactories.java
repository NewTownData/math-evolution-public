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
package com.newtowndata.math.genetics.nodefactory;

import com.newtowndata.math.genetics.MutationContext;
import com.newtowndata.math.genetics.nodefactory.core.BinaryNodeFactory;
import com.newtowndata.math.genetics.nodes.DivisionNode;
import com.newtowndata.math.genetics.nodes.MinusNode;
import com.newtowndata.math.genetics.nodes.MultiplicationNode;
import com.newtowndata.math.genetics.nodes.PlusNode;
import com.newtowndata.math.genetics.nodes.PowerNode;
import com.newtowndata.math.genetics.nodes.core.Node;
import java.util.List;

public class BinaryNodeFactories {

  private final List<BinaryNodeFactory> factories;

  public BinaryNodeFactories() {
    this.factories = List.of(
        PlusNode::new,
        MultiplicationNode::new,
        DivisionNode::new,
        MinusNode::new,
        PowerNode::new
    );
  }

  public Node createRandom(MutationContext mutationContext, Node left, Node right) {
    int total = this.factories.size() + 2;
    int selection = mutationContext.getRandom().nextInt(0, total - 1);
    if (selection < this.factories.size()) {
      return this.factories.get(selection).create(left, right);
    }

    if (selection == this.factories.size()) {
      return left;
    } else {
      return right;
    }
  }
}
