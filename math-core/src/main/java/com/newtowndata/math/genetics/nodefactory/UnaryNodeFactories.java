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
import com.newtowndata.math.genetics.SimplificationContext;
import com.newtowndata.math.genetics.nodefactory.core.UnaryNodeFactory;
import com.newtowndata.math.genetics.nodes.ArcCosNode;
import com.newtowndata.math.genetics.nodes.ArcCoshNode;
import com.newtowndata.math.genetics.nodes.ArcSinNode;
import com.newtowndata.math.genetics.nodes.ArcSinhNode;
import com.newtowndata.math.genetics.nodes.ArcTanNode;
import com.newtowndata.math.genetics.nodes.ArcTanhNode;
import com.newtowndata.math.genetics.nodes.CosNode;
import com.newtowndata.math.genetics.nodes.CoshNode;
import com.newtowndata.math.genetics.nodes.ExpNode;
import com.newtowndata.math.genetics.nodes.Log10Node;
import com.newtowndata.math.genetics.nodes.LogNode;
import com.newtowndata.math.genetics.nodes.NegativeNode;
import com.newtowndata.math.genetics.nodes.PowerOf10Node;
import com.newtowndata.math.genetics.nodes.PowerOf2Node;
import com.newtowndata.math.genetics.nodes.ReciprocalNode;
import com.newtowndata.math.genetics.nodes.SinNode;
import com.newtowndata.math.genetics.nodes.SinhNode;
import com.newtowndata.math.genetics.nodes.SqrtNode;
import com.newtowndata.math.genetics.nodes.TanNode;
import com.newtowndata.math.genetics.nodes.TanhNode;
import com.newtowndata.math.genetics.nodes.ToSquareNode;
import com.newtowndata.math.genetics.nodes.core.Node;
import java.util.List;

public class UnaryNodeFactories {

  private final List<UnaryNodeFactory> factories;

  public UnaryNodeFactories() {
    this.factories = List.of(
        NegativeNode::new,
        ToSquareNode::new,
        ArcSinNode::new,
        ArcCosNode::new,
        ArcTanNode::new,
        SinNode::new,
        CosNode::new,
        TanNode::new,
        LogNode::new,
        Log10Node::new,
        ExpNode::new,
        ReciprocalNode::new,
        SqrtNode::new,
        PowerOf10Node::new,
        PowerOf2Node::new,
        CoshNode::new,
        SinhNode::new,
        TanhNode::new,
        ArcCoshNode::new,
        ArcSinhNode::new,
        ArcTanhNode::new
    );
  }

  public Node createRandom(MutationContext mutationContext, Node child) {
    int total = this.factories.size() + 2;
    int selection = mutationContext.getRandom().nextInt(0, total - 1);
    if (selection < this.factories.size()) {
      return this.factories.get(selection).create(child);
    } else if (selection == this.factories.size()) {
      try {
        return child.simplify(new SimplificationContext(false)).getNode();
      } catch (IllegalStateException e) {
        return child;
      }
    }

    return child;
  }
}
