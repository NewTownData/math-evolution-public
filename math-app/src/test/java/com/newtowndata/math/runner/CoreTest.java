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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.newtowndata.math.config.Configuration;
import com.newtowndata.math.genetics.Random;
import com.newtowndata.math.input.DataInput;
import com.newtowndata.math.input.DataRow;
import com.newtowndata.math.model.RankedModel;
import java.time.Clock;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CoreTest {

  private static final DataInput TEST_INPUT = new DataInput(List.of(new DataRow(1, 1)));
  private static final Configuration TEST_CONFIG = createTestConfig();

  @Mock
  Clock clock;

  @Mock
  Random random;

  @Mock
  CoreRuntime coreRuntime;

  private Core core;
  private ExecutorService executor;

  @BeforeEach
  void setUp() {
    core = new Core(coreRuntime, TEST_INPUT, TEST_CONFIG);
    executor = Executors.newSingleThreadExecutor();
  }

  @AfterEach
  void tearDown() {
    executor.shutdownNow();
  }

  @Test
  void runEvolution() throws ExecutionException, InterruptedException {
    when(coreRuntime.getClock()).thenReturn(clock);
    when(coreRuntime.createRandom()).thenReturn(random);
    when(coreRuntime.submit(any())).thenAnswer(invocation -> executor.submit(
        (Callable<RankedModel>) invocation.getArgument(0)));

    List<RankedModel> result = core.runEvolution();
    assertEquals(4, result.size());
    assertEquals(0.3675115617302709, result.get(0).getFitness(), 1e-9);
    assertEquals("0.0", result.get(0).getModel().toString());

    verify(coreRuntime, times(2)).submit(any());
  }

  @Test
  void runEvolutionLonger() throws ExecutionException, InterruptedException {
    when(coreRuntime.getClock()).thenReturn(clock);
    when(clock.millis()).thenReturn(0L, 1000000L, 2000000L);
    when(coreRuntime.createRandom()).thenReturn(random);

    List<RankedModel> result = core.runEvolution();
    assertEquals(4, result.size());
    assertEquals(0.3675115617302709, result.get(0).getFitness(), 1e-9);
    assertEquals("0.0", result.get(0).getModel().toString());

    verify(coreRuntime, times(0)).submit(any());
  }

  private static Configuration createTestConfig() {
    Configuration config = new Configuration();
    config.setMaxIterations(1);
    config.setPopulationSize(4);
    config.setMutationSize(1);
    config.setCrossoverSize(1);
    config.setReplaceableSize(1);
    config.setSurvivalSize(1);
    config.setMaxNodes(1);
    return config;
  }
}
