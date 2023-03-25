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

import com.newtowndata.math.config.Configuration;
import com.newtowndata.math.genetics.nodefactory.BinaryNodeFactories;
import com.newtowndata.math.genetics.nodefactory.LeafNodeFactories;
import com.newtowndata.math.genetics.nodefactory.UnaryNodeFactories;
import com.newtowndata.math.genetics.nodes.core.Node;

public class NodeFactoryImpl implements NodeFactory {

  private final MutationContext mutationContext;

  private final LeafNodeFactories leafNodeFactories;
  private final UnaryNodeFactories unaryNodeFactories;
  private final BinaryNodeFactories binaryNodeFactories;

  public NodeFactoryImpl(MutationContext mutationContext, Configuration configuration) {
    this.mutationContext = mutationContext;

    this.leafNodeFactories = new LeafNodeFactories(configuration);
    this.unaryNodeFactories = new UnaryNodeFactories();
    this.binaryNodeFactories = new BinaryNodeFactories();
  }

  @Override
  public Node createLeafNode() {
    return leafNodeFactories.createRandom(mutationContext);
  }

  @Override
  public Node createUnaryNode(Node child) {
    return unaryNodeFactories.createRandom(mutationContext, child);
  }

  @Override
  public Node createAggregateNode(Node left, Node right) {
    return binaryNodeFactories.createRandom(mutationContext, left, right);
  }

}
