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
package com.newtowndata.math;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;

class AppTest {

  private static String getTestPath(String resource) {
    try {
      return Paths.get(AppTest.class.getResource(resource).toURI()).toString();
    } catch (URISyntaxException e) {
      throw new IllegalStateException("Cannot load test path");
    }
  }

  @Test
  void main() throws ExecutionException, InterruptedException {
    App.main(
        new String[] {getTestPath("test_data_input.csv"), getTestPath("test_config.properties")});
  }
}
