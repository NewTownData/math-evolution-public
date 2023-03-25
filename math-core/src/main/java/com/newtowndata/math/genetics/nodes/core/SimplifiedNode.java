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

public class SimplifiedNode {

  private final Node node;
  private final boolean constant;

  public SimplifiedNode(Node node, boolean constant) {
    this.node = node;
    this.constant = constant;
  }

  public boolean isConstant() {
    return constant;
  }

  public Node getNode() {
    return node;
  }

  @Override
  public String toString() {
    return node.toString() + (constant ? " <!" : " <?");
  }
}
