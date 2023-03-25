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

import com.newtowndata.math.config.Configuration;
import com.newtowndata.math.genetics.MutationContext;
import com.newtowndata.math.genetics.nodefactory.core.LeafNodeFactory;
import com.newtowndata.math.genetics.nodes.ConstantNode;
import com.newtowndata.math.genetics.nodes.GroupConstantNode;
import com.newtowndata.math.genetics.nodes.IntConstantNode;
import com.newtowndata.math.genetics.nodes.SpecialConstantNode;
import com.newtowndata.math.genetics.nodes.VariableNode;
import com.newtowndata.math.genetics.nodes.core.Node;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LeafNodeFactories {

  private static final Logger LOG = Logger.getLogger(LeafNodeFactories.class.getName());

  private final List<LeafNodeFactory> factories;

  public LeafNodeFactories(Configuration config) {
    this.factories = new ArrayList<>(
        List.of(
            ConstantNode::new,
            VariableNode::new,
            SpecialConstantNode::new,
            IntConstantNode::new
        )
    );

    if (config.isGroupAllowed()) {
      LOG.info("Group node enabled");
      this.factories.add(GroupConstantNode::new);
    } else {
      LOG.info("Group node disabled");
    }
  }

  public Node createRandom(MutationContext mutationContext) {
    int total = this.factories.size();
    int selection = mutationContext.getRandom().nextInt(0, total - 1);
    return this.factories.get(selection).create(mutationContext);
  }

}
