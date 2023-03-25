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
import com.newtowndata.math.input.DataInput;

public class AppInput {

  private final DataInput dataInput;
  private final Configuration config;

  public AppInput(DataInput dataInput, Configuration config) {
    this.dataInput = dataInput;
    this.config = config;
  }

  public DataInput getDataInput() {
    return dataInput;
  }

  public Configuration getConfig() {
    return config;
  }
}
