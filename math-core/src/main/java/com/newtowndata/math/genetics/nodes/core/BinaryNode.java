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

public abstract class BinaryNode implements Node {

  protected final Node left;
  protected final Node right;

  protected BinaryNode(Node left, Node right) {
    this(left, right, false);
  }

  protected BinaryNode(Node left, Node right, boolean swap) {
    if (swap) {
      this.left = right;
      this.right = left;
    } else {
      this.left = left;
      this.right = right;
    }
  }

  @Override
  public Node mutate(MutationContext mutationContext) {
    return mutationContext.getRandom().nextBoolean() ? left : right;
  }

  @Override
  public List<Node> getChildren() {
    return List.of(left, right);
  }

  protected abstract Node create(Node left, Node right);

  @Override
  public Node replace(int nodeIndex, Node replacement) {
    if (nodeIndex != 0 && nodeIndex != 1) {
      throw new IllegalArgumentException("Node index must be 0 or 1");
    }
    return create(nodeIndex == 0 ? replacement : left, nodeIndex == 1 ? replacement : right);
  }

  @Override
  public SimplifiedNode simplify(SimplificationContext context) {
    SimplifiedNode simplifiedLeft = left.simplify(context);
    SimplifiedNode simplifiedRight = right.simplify(context);

    Node simplifiedNode = create(simplifiedLeft.getNode(), simplifiedRight.getNode());

    SimplifiedNode result;
    if (simplifiedLeft.isConstant() && simplifiedRight.isConstant()) {
      double value = simplifiedNode.evaluate(null);
      result = new SimplifiedNode(new ConstantNode(value), true);
    } else {
      result = new SimplifiedNode(simplifiedNode, false);
    }

    return result;
  }

  protected abstract double calculate(double a, double b);

  @Override
  public double evaluate(DataRow x) {
    double result = calculate(left.evaluate(x), right.evaluate(x));
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
    BinaryNode node = (BinaryNode) o;
    return Objects.equals(left, node.left) && Objects.equals(right, node.right);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getClass(), left, right);
  }
}
