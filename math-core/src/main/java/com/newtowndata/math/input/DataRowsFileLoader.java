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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataRowsFileLoader {

  private static final Logger LOG = Logger.getLogger(DataRowsFileLoader.class.getName());

  private static final Pattern NUMBER_MATCHER = Pattern.compile("^[-0-9.].*$");
  private static final Pattern DELIMITER = Pattern.compile("[,\t]");

  public static List<DataRow> loadInputFile(String path) {
    Path inputPath = Paths.get(path);
    if (!Files.isReadable(inputPath)) {
      throw new IllegalArgumentException("Path " + path + " is not a readable file.");
    }

    try (InputStream is = Files.newInputStream(inputPath, StandardOpenOption.READ)) {
      return loadInputFile(is);
    } catch (Exception e) {
      throw new IllegalStateException("Cannot read file " + path, e);
    }
  }

  public static List<DataRow> loadInputFile(InputStream is) {
    try (BufferedInputStream bis = new BufferedInputStream(is);
        InputStreamReader isr = new InputStreamReader(bis, Charset.forName("ASCII"));
        BufferedReader br = new BufferedReader(isr)
    ) {
      List<DataRow> rows = new ArrayList<>();

      String line;
      while ((line = br.readLine()) != null) {
        line = line.trim();
        if (line.length() == 0) {
          LOG.info("Skipping empty line");
          continue;
        }

        if (!NUMBER_MATCHER.matcher(line).find()) {
          LOG.info("Skipping non-digit line: " + line);
          continue;
        }

        Matcher lineMatcher = DELIMITER.matcher(line);
        List<Double> values = new ArrayList<>();
        String group = null;

        int start = 0;
        while (lineMatcher.find()) {
          Object value = parseValue(line, start, lineMatcher.start());
          if (value instanceof Double) {
            values.add((Double) value);
          } else {
            if (group != null) {
              throw new IllegalStateException("Group is already defined");
            }
            group = (String) value;
          }
          start = lineMatcher.end();
        }

        if (start != line.length()) {
          Object value = parseValue(line, start, line.length());
          if (value instanceof Double) {
            values.add((Double) value);
          } else {
            if (group != null) {
              throw new IllegalStateException("Group is already defined");
            }
            group = (String) value;
          }
        }

        if (values.size() < 1) {
          throw new IllegalStateException("No values found!");
        }

        rows.add(
            new DataRow(values.get(0), group,
                convertToDoubleArray(values.subList(1, values.size()))));
      }

      return rows;
    } catch (Exception e) {
      throw new IllegalStateException("Cannot read input", e);
    }
  }

  private static double[] convertToDoubleArray(List<Double> list) {
    if (list.isEmpty()) {
      return new double[0];
    }

    double[] array = new double[list.size()];
    for (int i = 0; i < array.length; i++) {
      array[i] = list.get(i);
    }
    return array;
  }

  private static Object parseValue(String line, int start, int end) {
    String valueString = line.substring(start, end).trim();
    try {
      double value = Double.parseDouble(valueString);
      if (!Double.isFinite(value)) {
        throw new IllegalStateException("Invalid value: " + valueString);
      }
      return value;
    } catch (NumberFormatException e) {
      return valueString;
    }
  }

}
