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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataInput {

  private final List<DataRow> rows;
  private final int xLength;

  public DataInput(List<DataRow> rows) {
    this.rows = rows;
    this.xLength = calculateXLength(rows);
  }

  private int calculateXLength(List<DataRow> input) {
    int len = -1;
    for (DataRow row : input) {
      int l = row.getX().length;
      if (len == -1) {
        len = l;
      } else if (len != l) {
        throw new IllegalStateException("Invalid input length. Expected " + len + ", found " + l);
      }
    }
    return len;
  }

  public List<String> computeGroups() {
    Set<String> groups = new HashSet<>();
    rows.forEach(row -> {
      if (row.getGroup() != null) {
        groups.add(row.getGroup());
      }
    });
    List<String> groupList = new ArrayList<>(groups);
    Collections.sort(groupList);
    return groupList;
  }

  public int getXLength() {
    return xLength;
  }

  public List<DataRow> getRows() {
    return rows;
  }
}
