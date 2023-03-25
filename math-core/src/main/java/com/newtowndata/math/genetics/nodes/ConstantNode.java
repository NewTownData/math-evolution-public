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

import com.newtowndata.math.genetics.MutationContext;
import com.newtowndata.math.genetics.SimplificationContext;
import com.newtowndata.math.genetics.nodes.core.LeafNode;
import com.newtowndata.math.genetics.nodes.core.Node;
import com.newtowndata.math.genetics.nodes.core.SimplifiedNode;
import com.newtowndata.math.genetics.nodes.helper.DoubleMutation;
import com.newtowndata.math.input.DataRow;
import java.util.Objects;

public class ConstantNode extends LeafNode {

  private final double constant;

  public ConstantNode(double constant) {
    this.constant = constant;
  }

  public ConstantNode(MutationContext mutationContext) {
    this.constant = DoubleMutation.create(mutationContext);
  }

  public double getConstant() {
    return constant;
  }

  @Override
  public Node mutate(MutationContext mutationContext) {
    return new ConstantNode(DoubleMutation.mutate(mutationContext, constant));
  }

  @Override
  public double evaluate(DataRow x) {
    return constant;
  }

  @Override
  public String toString() {
    return Double.toString(constant);
  }

  @Override
  public SimplifiedNode simplify(SimplificationContext context) {
    return new SimplifiedNode(new ConstantNode(constant), true);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConstantNode node = (ConstantNode) o;
    return Double.compare(node.constant, constant) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(constant);
  }
}
