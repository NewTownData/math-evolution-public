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
package com.newtowndata.math.genetics.nodes.helper;

import com.newtowndata.math.genetics.MutationContext;
import com.newtowndata.math.genetics.Random;

public final class DoubleMutation {

  private DoubleMutation() {
  }

  public static double create(MutationContext mutationContext) {
    return mutationContext.getRandom().nextInt(-1000, 1000);
  }

  public static double mutate(MutationContext mutationContext, double value) {
    Random random = mutationContext.getRandom();
    int variant = random.nextInt(0, 4);

    switch (variant) {
      case 0:
        // change in <-1,1> of current value
        return value + random.nextDouble(-1, 1) * value;
      case 1:
        // adjust value in interval <1e-100, 1e100>
        double plusAdjustment = Math.pow(10, random.nextInt(-100, 100));
        return value + plusAdjustment;
      case 2:
        // adjust value in interval <-1e100, -1e-100>
        double minusAdjustment = Math.pow(10, random.nextInt(-100, 100));
        return value - minusAdjustment;
      case 3:
        return Math.round(value);
      default:
        // divide or multiply number by orders of 10
        double exponent = Math.pow(10, random.nextInt(-100, 100));
        return value * exponent;
    }
  }

}
