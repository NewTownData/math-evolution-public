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
import com.newtowndata.math.genetics.Random;
import com.newtowndata.math.genetics.SimplificationContext;
import com.newtowndata.math.genetics.nodes.core.LeafNode;
import com.newtowndata.math.genetics.nodes.core.Node;
import com.newtowndata.math.genetics.nodes.core.SimplifiedNode;
import com.newtowndata.math.input.DataRow;
import java.util.Objects;

public class IntConstantNode extends LeafNode {

  private final int constant;

  public IntConstantNode(int constant) {
    this.constant = constant;
  }

  public IntConstantNode(MutationContext mutationContext) {
    this.constant = mutationContext.getRandom().nextInt(-10000, 10000);
  }

  public int getConstant() {
    return constant;
  }

  @Override
  public Node mutate(MutationContext mutationContext) {
    Random random = mutationContext.getRandom();
    if (random.nextBoolean()) {
      // divide or multiply number by orders of 10
      int adjustment = (int) Math.pow(10, random.nextInt(-20, 20));
      return new IntConstantNode(constant * adjustment);
    }
    return new IntConstantNode(constant + random.nextInt(-10000, 10000));
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
    return new SimplifiedNode(new IntConstantNode(constant), true);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IntConstantNode node = (IntConstantNode) o;
    return Double.compare(node.constant, constant) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(constant);
  }
}
