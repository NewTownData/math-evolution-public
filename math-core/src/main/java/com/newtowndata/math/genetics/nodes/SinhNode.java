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

import com.newtowndata.math.genetics.nodes.core.Node;
import com.newtowndata.math.genetics.nodes.core.UnaryNode;

public class SinhNode extends UnaryNode {

  public SinhNode(Node child) {
    super(child);
  }

  @Override
  protected Node create(Node child) {
    return new SinhNode(child);
  }

  @Override
  protected double calculate(double a) {
    return StrictMath.sinh(a);
  }

  @Override
  public String toString() {
    return "sinh(" + child.toString() + ")";
  }
}
