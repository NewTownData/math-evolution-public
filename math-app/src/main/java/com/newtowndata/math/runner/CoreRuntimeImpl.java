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

import com.newtowndata.math.config.Configuration;
import com.newtowndata.math.genetics.Random;
import com.newtowndata.math.genetics.RandomImpl;
import com.newtowndata.math.model.RankedModel;
import java.time.Clock;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CoreRuntimeImpl implements CoreRuntime {

  private final Clock clock;
  private final ExecutorService executor;

  public CoreRuntimeImpl(Configuration configuration) {
    this.clock = Clock.systemUTC();

    int requiredThreadCount = configuration.getThreadCount();
    if (requiredThreadCount <= 0) {
      requiredThreadCount = Runtime.getRuntime().availableProcessors();
    }

    this.executor = Executors.newFixedThreadPool(requiredThreadCount);
  }


  @Override
  public void close() {
    executor.shutdownNow();
  }

  @Override
  public Clock getClock() {
    return clock;
  }

  @Override
  public Random createRandom() {
    return new RandomImpl(new java.util.Random());
  }

  @Override
  public Future<RankedModel> submit(Callable<RankedModel> modelTask) {
    return executor.submit(modelTask);
  }
}
