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

import static com.newtowndata.math.genetics.nodes.core.NodeConstants.OUT_OF_RANGE;

import com.newtowndata.math.genetics.MutationContext;
import com.newtowndata.math.genetics.SimplificationContext;
import com.newtowndata.math.genetics.nodes.core.LeafNode;
import com.newtowndata.math.genetics.nodes.core.Node;
import com.newtowndata.math.genetics.nodes.core.SimplifiedNode;
import com.newtowndata.math.genetics.nodes.helper.Constant;
import com.newtowndata.math.genetics.nodes.helper.DoubleMutation;
import com.newtowndata.math.input.DataRow;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class GroupConstantNode extends LeafNode {

  private final List<String> groups;
  private final double[] values;

  public GroupConstantNode(MutationContext mutationContext) {
    List<String> groups = mutationContext.getGroups();
    if (groups.isEmpty()) {
      throw new IllegalArgumentException("At least one group must be provided");
    } else {
      this.groups = groups;
      this.values = new double[groups.size()];

      double initialValue = DoubleMutation.create(mutationContext);
      for (int i = 0; i < this.values.length; i++) {
        this.values[i] = initialValue;
      }
    }
  }

  public GroupConstantNode(List<Constant> constants) {
    List<String> groups = new ArrayList<>();
    double[] values = new double[constants.size()];

    for (int i = 0; i < constants.size(); i++) {
      groups.add(constants.get(i).toString());
      values[i] = constants.get(i).getValue();
    }

    this.groups = groups;
    this.values = values;
  }

  private GroupConstantNode(GroupConstantNode groupConstantNode, int index, double newValue) {
    this.groups = groupConstantNode.groups;
    this.values = Arrays.copyOf(groupConstantNode.values, groupConstantNode.values.length);
    this.values[index] = newValue;
  }

  public List<Constant> getConstants() {
    List<Constant> constants = new ArrayList<>();
    for (int i = 0; i < values.length; i++) {
      constants.add(new Constant(groups.get(i), values[i]));
    }
    return constants;
  }

  @Override
  public Node mutate(MutationContext mutationContext) {
    int index = mutationContext.getRandom().nextInt(0, values.length - 1);
    double newValue = DoubleMutation.mutate(mutationContext, values[index]);
    return new GroupConstantNode(this, index, newValue);
  }

  @Override
  public double evaluate(DataRow x) {
    if (x == null) {
      throw OUT_OF_RANGE;
    }

    int index = Collections.binarySearch(groups, x.getGroup());
    if (index < 0) {
      throw OUT_OF_RANGE;
    }

    return values[index];
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("group(");
    for (int i = 0; i < values.length; i++) {
      if (i > 0) {
        sb.append(',');
      }
      sb.append(groups.get(i));
      sb.append('=');
      sb.append(values[i]);
    }
    sb.append(')');
    return sb.toString();
  }

  @Override
  public SimplifiedNode simplify(SimplificationContext context) {
    if (values.length == 1) {
      return new SimplifiedNode(new ConstantNode(values[0]), true);
    }

    double first = values[0];
    for (int i = 1; i < values.length; i++) {
      if (values[i] != first) {
        return new SimplifiedNode(this, false);
      }
    }
    return new SimplifiedNode(new ConstantNode(first), true);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GroupConstantNode node = (GroupConstantNode) o;
    return Objects.equals(groups, node.groups) && Arrays
        .equals(values, node.values);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(groups);
    result = 31 * result + Arrays.hashCode(values);
    return result;
  }
}
