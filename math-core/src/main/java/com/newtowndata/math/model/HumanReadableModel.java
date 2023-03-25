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

import java.util.Locale;
import com.newtowndata.math.genetics.SimplificationContext;
import com.newtowndata.math.genetics.nodes.core.Node;

public class HumanReadableModel {

  public static String modelToString(RankedModel model) {
    return "Model\t" + String.format(Locale.ENGLISH, "%.6f", model.getFitness()) + "\n"
        + nodeToString(model.getModel().getRoot());
  }

  public static String nodeToString(Node root) {
    return "Default\t" + root + "\n" + "Simplified\t"
        + root.simplify(new SimplificationContext(true)).getNode();
  }

  public static String modelToTsv(RankedModel model) {
    return String.format(Locale.ENGLISH, "%.6f\t", model.getFitness())
        + nodeToTsv(model.getModel().getRoot());
  }

  public static String nodeToTsv(Node root) {
    return root + "\t" + root.simplify(new SimplificationContext(true)).getNode();
  }

}
