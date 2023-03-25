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
import com.newtowndata.math.input.DataRow;
import java.util.Objects;

public class VariableNode extends LeafNode {

  private final int xIndex;

  public VariableNode(int xIndex) {
    this.xIndex = xIndex;
  }

  public VariableNode(MutationContext mutationContext) {
    this.xIndex = mutationContext.getRandom().nextInt(0, mutationContext.getXLength() - 1);
  }

  public int getXIndex() {
    return xIndex;
  }

  @Override
  public Node mutate(MutationContext mutationContext) {
    return new VariableNode(mutationContext);
  }

  @Override
  public double evaluate(DataRow x) {
    return x.getX()[xIndex];
  }

  @Override
  public String toString() {
    return "x" + (xIndex + 1);
  }

  @Override
  public SimplifiedNode simplify(SimplificationContext context) {
    return new SimplifiedNode(this, false);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VariableNode node = (VariableNode) o;
    return xIndex == node.xIndex;
  }

  @Override
  public int hashCode() {
    return Objects.hash(xIndex);
  }
}
