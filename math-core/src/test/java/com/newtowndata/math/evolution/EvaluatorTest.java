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
package com.newtowndata.math.evolution;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.newtowndata.math.config.Configuration;
import com.newtowndata.math.genetics.nodes.ConstantNode;
import com.newtowndata.math.genetics.nodes.ExpNode;
import com.newtowndata.math.genetics.nodes.MultiplicationNode;
import com.newtowndata.math.genetics.nodes.PlusNode;
import com.newtowndata.math.genetics.nodes.SinNode;
import com.newtowndata.math.genetics.nodes.ToSquareNode;
import com.newtowndata.math.genetics.nodes.VariableNode;
import com.newtowndata.math.input.DataInput;
import com.newtowndata.math.input.DataRow;
import com.newtowndata.math.model.Model;
import com.newtowndata.math.model.RankedModel;
import java.util.List;
import org.junit.jupiter.api.Test;

class EvaluatorTest {

  // 2.0*sin(2.0+x1)+(x2^2)
  private static final Model TEST_MODEL = new Model(new PlusNode(
      new MultiplicationNode(
          new ConstantNode(2.0),
          new SinNode(new PlusNode(
              new VariableNode(0),
              new ConstantNode(2)
          ))
      ),
      new ToSquareNode(new VariableNode(1))
  ));

  private static final DataInput EXAMPLE_SIN_INPUT = new DataInput(List.of(
      computeRow(TEST_MODEL, 0, 0),
      computeRow(TEST_MODEL, 0.4, 3),
      computeRow(TEST_MODEL, 1.6, 9),
      computeRow(TEST_MODEL, 2.2, -3),
      computeRow(TEST_MODEL, 3.6, 1)
  ));

  private static DataRow computeRow(Model model, double... x) {
    double y = model.evaluate(new DataRow(0, x));
    return new DataRow(y, x);
  }

  @Test
  void evaluateZeroMatch() {
    Configuration configuration = new Configuration();

    Evaluator evaluator = new Evaluator(configuration, new DataInput(List.of(new DataRow(0, 0))));
    RankedModel result = evaluator.evaluate(new Model(new ConstantNode(0)));
    assertEquals(0.99995, result.getFitness(), 1e-9);
  }

  @Test
  void evaluateZeroOff() {
    Configuration configuration = new Configuration();

    Evaluator evaluator = new Evaluator(configuration, new DataInput(List.of(new DataRow(0, 0))));
    RankedModel result = evaluator.evaluate(new Model(new ConstantNode(1)));
    assertEquals(9.5e-4, result.getFitness(), 1e-9);
  }

  @Test
  void evaluateZeroIntegers() {
    Configuration configuration = new Configuration();

    Evaluator evaluator = new Evaluator(configuration,
        new DataInput(List.of(new DataRow(0, 0), new DataRow(100, 100))));
    RankedModel result = evaluator.evaluate(new Model(new ConstantNode(1)));
    assertEquals(0.6810824491232214, result.getFitness(), 1e-9);
  }

  @Test
  void evaluateCorrelationOnly() {
    Configuration configuration = new Configuration();
    configuration.setModelSizeImportance(0);

    Evaluator evaluator = new Evaluator(configuration, EXAMPLE_SIN_INPUT);
    RankedModel result = evaluator.evaluate(TEST_MODEL);
    assertEquals(1, result.getFitness(), 1e-9);
  }

  @Test
  void evaluateModelSizeOnly() {
    Configuration configuration = new Configuration();
    configuration.setModelSizeImportance(1);
    configuration.setMaxNodes(TEST_MODEL.getSize() + 1);

    Evaluator evaluator = new Evaluator(configuration, EXAMPLE_SIN_INPUT);
    RankedModel result = evaluator.evaluate(TEST_MODEL);
    assertEquals(0.1, result.getFitness(), 1e-9);
  }

  @Test
  void evaluateModelTooLarge() {
    Configuration configuration = new Configuration();
    configuration.setMaxNodes(TEST_MODEL.getSize() - 1);

    Evaluator evaluator = new Evaluator(configuration, EXAMPLE_SIN_INPUT);
    RankedModel result = evaluator.evaluate(TEST_MODEL);
    assertEquals(0, result.getFitness(), 1e-9);
  }

  @Test
  void evaluateCorrelationAndModelSizeOnly() {
    Configuration configuration = new Configuration();
    configuration.setModelSizeImportance(0.5);
    configuration.setMaxNodes(TEST_MODEL.getSize());

    Evaluator evaluator = new Evaluator(configuration, EXAMPLE_SIN_INPUT);
    RankedModel result = evaluator.evaluate(TEST_MODEL);
    assertEquals(0.5, result.getFitness(), 1e-9);
  }

  @Test
  void evaluateWrong() {
    Configuration configuration = new Configuration();
    configuration.setModelSizeImportance(0);

    Evaluator evaluator = new Evaluator(configuration, EXAMPLE_SIN_INPUT);
    RankedModel result = evaluator.evaluate(new Model(new ToSquareNode(new VariableNode(1))));
    assertEquals(0.6058317262410949, result.getFitness(), 1e-9);
  }

  @Test
  void evaluateWrongExp() {
    Configuration configuration = new Configuration();
    configuration.setModelSizeImportance(0);

    Evaluator evaluator = new Evaluator(configuration, EXAMPLE_SIN_INPUT);
    RankedModel result = evaluator
        .evaluate(new Model(new ExpNode(new ConstantNode(706))));
    assertEquals(0.0, result.getFitness(), 1e-9);
  }
}
