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
package com.newtowndata.math.model;

import com.newtowndata.math.genetics.NodeUtils;
import com.newtowndata.math.genetics.nodes.core.Node;
import com.newtowndata.math.input.DataRow;
import java.util.Objects;

public class Model {

  private final int size;
  private final Node root;
  private final String modelOutput;

  public Model(Node root) {
    this.root = root;
    this.size = NodeUtils.calculateSize(this.root);
    this.modelOutput = this.root.toString();
  }

  public double evaluate(DataRow x) {
    return root.evaluate(x);
  }

  public int getSize() {
    return size;
  }

  public Node getRoot() {
    return root;
  }

  @Override
  public String toString() {
    return modelOutput;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Model model = (Model) o;
    return Objects.equals(modelOutput, model.modelOutput);
  }

  @Override
  public int hashCode() {
    return Objects.hash(modelOutput);
  }
}
