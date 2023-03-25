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

import static com.newtowndata.math.genetics.nodes.core.NodeConstants.OUT_OF_RANGE;

import com.newtowndata.math.genetics.MutationContext;
import com.newtowndata.math.genetics.SimplificationContext;
import com.newtowndata.math.genetics.nodes.ConstantNode;
import com.newtowndata.math.input.DataRow;
import java.util.List;
import java.util.Objects;

public abstract class UnaryNode implements Node {

  protected final Node child;

  protected UnaryNode(Node child) {
    this.child = child;
  }

  @Override
  public Node mutate(MutationContext mutationContext) {
    return child;
  }

  @Override
  public List<Node> getChildren() {
    return List.of(child);
  }

  protected abstract Node create(Node child);

  @Override
  public Node replace(int nodeIndex, Node replacement) {
    if (nodeIndex != 0) {
      throw new IllegalArgumentException("Node index must be 0");
    }
    return create(replacement);
  }

  @Override
  public SimplifiedNode simplify(SimplificationContext context) {
    SimplifiedNode simplifiedChild = child.simplify(context);
    Node simplifiedNode = create(simplifiedChild.getNode());

    SimplifiedNode result;
    if (simplifiedChild.isConstant()) {
      double value = simplifiedNode.evaluate(null);
      result = new SimplifiedNode(new ConstantNode(value), true);
    } else {
      result = new SimplifiedNode(simplifiedNode, false);
    }

    return result;
  }

  protected abstract double calculate(double a);

  @Override
  public double evaluate(DataRow x) {
    double result = calculate(child.evaluate(x));
    if (Double.isFinite(result)) {
      return result;
    }
    throw OUT_OF_RANGE;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UnaryNode node = (UnaryNode) o;
    return Objects.equals(child, node.child);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getClass(), child);
  }
}
