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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.newtowndata.math.config.Configuration;
import com.newtowndata.math.input.DataRow;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;

class AppInputProviderTest {

  private static String getTestPath(String resource) {
    try {
      return Paths.get(AppInputProviderTest.class.getResource(resource).toURI()).toString();
    } catch (URISyntaxException e) {
      throw new IllegalStateException("Cannot load test path");
    }
  }

  @Test
  void testProvideNoArgs() {
    AppInput result = new AppInputProvider(new String[0]).provide();

    assertEquals(new Configuration().toString(), result.getConfig().toString());
    assertEquals(AppInputProvider.DEFAULT_INPUT, result.getDataInput().getRows());
  }

  @Test
  void testProvideDataInputArg() {
    AppInput result = new AppInputProvider(new String[] {getTestPath("data_input.csv")}).provide();

    assertEquals(new Configuration().toString(), result.getConfig().toString());
    assertEquals(List.of(new DataRow(1, 2)), result.getDataInput().getRows());
  }

  @Test
  void testProvideDataInputArgWrongFile() {
    AppInputProvider provider = new AppInputProvider(
        new String[] {Paths.get(getTestPath("data_input.csv"), "test").toString()});

    assertThrows(IllegalArgumentException.class, provider::provide);
  }

  @Test
  void testProvideDataInputAndConfigArgs() {
    AppInput result = new AppInputProvider(
        new String[] {getTestPath("data_input.csv"), getTestPath("config.properties")}).provide();

    assertEquals(300, result.getConfig().getMaxDurationInSec());
    assertEquals(List.of(new DataRow(1, 2)), result.getDataInput().getRows());
  }

}
