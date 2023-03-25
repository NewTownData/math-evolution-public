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

public class RandomImpl implements Random {

  private final java.util.Random random;

  public RandomImpl(java.util.Random random) {
    this.random = random;
  }

  @Override
  public boolean nextBoolean() {
    return random.nextBoolean();
  }

  @Override
  public int nextInt(int from, int to) {
    return random.nextInt(to - from + 1) + from;
  }

  @Override
  public double nextDouble(int from, int to) {
    return (to - from) * random.nextDouble() + from;
  }
}
