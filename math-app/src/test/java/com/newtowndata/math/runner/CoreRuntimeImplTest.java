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
package com.newtowndata.math.runner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.newtowndata.math.config.Configuration;
import com.newtowndata.math.genetics.nodes.ConstantNode;
import com.newtowndata.math.model.Model;
import com.newtowndata.math.model.RankedModel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Test;

class CoreRuntimeImplTest {

  @Test
  public void testAll() throws ExecutionException, InterruptedException {
    try (CoreRuntimeImpl runtime = new CoreRuntimeImpl(new Configuration())) {
      assertTrue(runtime.getClock().millis() > 0L);
      runtime.createRandom().nextBoolean();

      Future<RankedModel> modelFuture = runtime
          .submit(() -> new RankedModel(new Model(new ConstantNode(1)), 1));
      RankedModel model = modelFuture.get();
      assertEquals(1, model.getFitness());
    }
  }

}
