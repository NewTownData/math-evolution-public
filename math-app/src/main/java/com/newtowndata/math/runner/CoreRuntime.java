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

import com.newtowndata.math.genetics.Random;
import com.newtowndata.math.model.RankedModel;
import java.io.Closeable;
import java.time.Clock;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface CoreRuntime extends Closeable {

  void close();

  Clock getClock();

  Random createRandom();

  Future<RankedModel> submit(Callable<RankedModel> modelTask);

}
