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
package com.newtowndata.math.genetics;

import java.util.List;
import java.util.function.Supplier;

public class MutationContextImpl implements MutationContext {

  private final ThreadLocal<Random> random;
  private final int xLength;
  private final List<String> groups;

  public MutationContextImpl(int xLength, List<String> groups, Supplier<Random> randomSupplier) {
    this.xLength = xLength;
    this.groups = groups;
    this.random = ThreadLocal.withInitial(randomSupplier);
  }

  @Override
  public int getXLength() {
    return xLength;
  }

  @Override
  public Random getRandom() {
    return random.get();
  }

  @Override
  public List<String> getGroups() {
    return groups;
  }
}
