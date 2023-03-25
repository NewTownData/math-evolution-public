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
import com.newtowndata.math.genetics.nodes.helper.Constant;
import com.newtowndata.math.input.DataRow;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SpecialConstantNode extends LeafNode {

  private static final List<Constant> CONSTANTS = List.of(
      new Constant("pi", StrictMath.PI),
      new Constant("e", StrictMath.E),
      new Constant("_180", 180),
      new Constant("_10", 10),
      new Constant("_2", 2),
      new Constant("_1", 1),
      new Constant("t0", -273.15), // absolute zero
      new Constant("k", 1.380649e-23), // Boltzmann constant
      new Constant("h", 6.62607015e-34), // Planck constant
      new Constant("c", 299792458), // Speed of light
      new Constant("q_e", 1.602176634e-19), // Elementary charge
      new Constant("f_cs", 9192631770.0), // Caesium standard
      new Constant("L", 6.02214076e23), // Avogadro constant
      new Constant("a0", 5.29177210903e-11), // Bohr radius
      new Constant("alpha", 0.0072973525693), // Fine-structure constant
      new Constant("m_e", 9.1093837015e-31), // Electron mass
      new Constant("m_p", 1.67262192369e-27) // Proton mass
  );

  private final Constant constant;

  public SpecialConstantNode(MutationContext mutationContext) {
    this.constant = CONSTANTS.get(mutationContext.getRandom().nextInt(0, CONSTANTS.size() - 1));
  }

  public SpecialConstantNode(Constant constant) {
    this.constant = constant;
  }

  public static SpecialConstantNode of(String name) {
    Optional<Constant> constant = CONSTANTS.stream().filter(c -> c.toString().equals(name))
        .findFirst();
    return new SpecialConstantNode(constant.orElseThrow());
  }

  public Constant getConstant() {
    return constant;
  }

  @Override
  public Node mutate(MutationContext mutationContext) {
    return new SpecialConstantNode(mutationContext);
  }

  @Override
  public double evaluate(DataRow x) {
    return constant.getValue();
  }

  @Override
  public String toString() {
    return constant.toString();
  }

  @Override
  public SimplifiedNode simplify(SimplificationContext context) {
    if (context.isAllConstants()) {
      return new SimplifiedNode(new ConstantNode(constant.getValue()), true);
    } else {
      return new SimplifiedNode(this, false);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SpecialConstantNode node = (SpecialConstantNode) o;
    return Objects.equals(constant, node.constant);
  }

  @Override
  public int hashCode() {
    return Objects.hash(constant);
  }
}
