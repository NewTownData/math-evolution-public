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
package com.newtowndata.math.analysis.command;

import com.newtowndata.math.input.DataRow;
import com.newtowndata.math.input.DataRowsFileLoader;
import com.newtowndata.math.model.Model;
import com.newtowndata.math.serialization.ModelSerializer;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ModelOutputCommand implements Command {

  private final ModelSerializer modelSerializer = new ModelSerializer();

  @Override
  public String name() {
    return "model_output";
  }

  @Override
  public void execute(String[] args) {
    if (args.length < 2) {
      throw new IllegalArgumentException("Missing arguments: <model path> <input path>");
    }

    String modelPath = args[0];
    String inputPath = args[1];

    computeModelOutput(modelPath, inputPath);
  }

  private void computeModelOutput(String modelPath, String inputPath) {
    Model model;
    try (FileInputStream fis = new FileInputStream(modelPath)) {
      String modelData = new String(fis.readAllBytes(), StandardCharsets.UTF_8);
      model = modelSerializer.deserialize(modelData);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to load model from path " + modelPath, e);
    }

    List<DataRow> dataInput = DataRowsFileLoader.loadInputFile(inputPath);
    computeModelOutput(model, dataInput);
  }

  private void computeModelOutput(Model model, List<DataRow> input) {
    for (DataRow row : input) {
      double y = model.evaluate(row);
      System.out.println(y + "\t" + row.getY() + "\t" + xValuesToOutput(row.getX()));
    }
  }

  private String xValuesToOutput(double[] x) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < x.length; i++) {
      if (i > 0) {
        sb.append('\t');
      }
      sb.append(x[i]);
    }
    return sb.toString();
  }
}
