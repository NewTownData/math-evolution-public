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

import java.util.Arrays;
import java.util.Objects;

public class DataRow {

  private final double y;
  private final String group;
  private final double[] x;

  public DataRow(double y, double... x) {
    this(y, null, x);
  }

  public DataRow(double y, String group, double... x) {
    this.y = y;
    this.group = group;
    this.x = x;
  }

  public double[] getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  @Override
  public String toString() {
    return y +
        " = f(" + Arrays.toString(x) + ",{" + group + "}" +
        ')';
  }

  public String getGroup() {
    return group;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataRow row = (DataRow) o;
    return Double.compare(row.y, y) == 0 && Objects.equals(group, row.group)
        && Arrays.equals(x, row.x);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(y, group);
    result = 31 * result + Arrays.hashCode(x);
    return result;
  }
}
