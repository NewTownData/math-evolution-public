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
package com.newtowndata.math.input;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class DataRowsFileLoaderTest {

  @Test
  void loadInputFileExampleWithHeaderCsv() {
    List<DataRow> result = DataRowsFileLoader
        .loadInputFile(DataRowsFileLoaderTest.class.getResourceAsStream("example_with_header.csv"));
    assertResult(result);

    assertEquals("a", result.get(0).getGroup());
    assertEquals("b", result.get(1).getGroup());
  }

  @Test
  void loadInputFileExampleWithHeaderTsv() {
    List<DataRow> result = DataRowsFileLoader
        .loadInputFile(DataRowsFileLoaderTest.class.getResourceAsStream("example_with_header.tsv"));
    assertResult(result);
  }

  @Test
  void loadInputFileExampleWithoutHeaderCsv() {
    List<DataRow> result = DataRowsFileLoader
        .loadInputFile(DataRowsFileLoaderTest.class.getResourceAsStream("example_without_header.csv"));
    assertResult(result);
  }

  @Test
  void loadInputFileExampleWithoutHeaderTsv() {
    List<DataRow> result = DataRowsFileLoader
        .loadInputFile(DataRowsFileLoaderTest.class.getResourceAsStream("example_without_header.tsv"));
    assertResult(result);
  }

  private void assertResult(List<DataRow> result) {
    assertEquals(2, result.size());

    assertEquals(10.2, result.get(0).getY(), 1e-6);
    assertEquals(2, result.get(0).getX().length);
    assertEquals(20.2, result.get(0).getX()[0], 1e-6);
    assertEquals(30.2, result.get(0).getX()[1], 1e-6);

    assertEquals(-10.1, result.get(1).getY(), 1e-6);
    assertEquals(2, result.get(1).getX().length);
    assertEquals(-20.1, result.get(1).getX()[0], 1e-6);
    assertEquals(-30.1, result.get(1).getX()[1], 1e-6);
  }
}
