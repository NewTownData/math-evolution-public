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
package com.newtowndata.math.app;

import com.newtowndata.math.config.Configuration;
import com.newtowndata.math.config.ConfigurationLoader;
import com.newtowndata.math.input.DataInput;
import com.newtowndata.math.input.DataRow;
import com.newtowndata.math.input.DataRowsFileLoader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class AppInputProvider {

  private static final Logger LOG = Logger.getLogger(AppInputProvider.class.getName());

  public static final List<DataRow> DEFAULT_INPUT =
      List.of(new DataRow(3, 0), new DataRow(-17, 5), new DataRow(15, -3));

  private final String[] args;

  public AppInputProvider(String[] args) {
    this.args = args;
  }

  public AppInput provide() {
    DataInput dataInput = createFromArg(0,
        (dataInputFile) -> new DataInput(DataRowsFileLoader.loadInputFile(dataInputFile)),
        () -> new DataInput(DEFAULT_INPUT));

    Configuration config = createFromArg(1, (configFile) -> {
      try (FileInputStream fis = new FileInputStream(configFile)) {
        return ConfigurationLoader.fromStream(fis);
      } catch (IOException e) {
        throw new IllegalArgumentException("Cannot load file " + configFile);
      }
    }, () -> new Configuration());

    LOG.info("Loaded " + dataInput.getRows().size() + " rows");
    LOG.info("Configuration: " + config);

    return new AppInput(dataInput, config);
  }

  private <T> T createFromArg(int position, Function<String, T> createInput,
      Supplier<T> defaultInput) {
    if (this.args.length <= position) {
      return defaultInput.get();
    } else {
      return createInput.apply(this.args[position]);
    }
  }

}
