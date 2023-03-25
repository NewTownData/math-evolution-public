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
package com.newtowndata.math.result;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.newtowndata.math.config.Configuration;
import com.newtowndata.math.genetics.nodes.ConstantNode;
import com.newtowndata.math.model.Model;
import com.newtowndata.math.model.RankedModel;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ResultWriterProviderTest {

  @TempDir
  Path tempDir;

  @Mock
  Clock clock;

  @Test
  void provideDummy() {
    try (ResultWriter result = ResultWriterProvider.provide(new Configuration(), clock)) {
      assertNotNull(result);
      assertEquals(DummyResultWriter.class, result.getClass());

      result.write(4, new RankedModel(new Model(new ConstantNode(1)), 1));
    }
  }

  @Test
  void provideFile() throws IOException {
    when(clock.instant()).thenReturn(Instant.ofEpochMilli(1613541473000L));

    String resultPrefix = Paths.get(tempDir.toString(), "result").toString();
    Configuration config = new Configuration();
    config.setResultOutputPrefix(resultPrefix);

    try (ResultWriter result = ResultWriterProvider.provide(config, clock)) {
      assertNotNull(result);
      assertEquals(FileResultWriter.class, result.getClass());

      result.write(4, new RankedModel(new Model(new ConstantNode(3)), 1));
    }

    String expectedOutput = resultPrefix + "-2021-02-17-05-57-53.tsv";
    Path expectedOutputPath = Paths.get(expectedOutput);

    assertTrue(Files.exists(expectedOutputPath));

    try (InputStream is = Files.newInputStream(expectedOutputPath)) {
      assertEquals("2021-02-17T05:57:53\t4\t1.000000\t3.0\t3.0\n",
          new String(is.readAllBytes(), StandardCharsets.UTF_8));
    }

    String expectedModelOutput = resultPrefix + "-2021-02-17-05-57-53-000001.model";
    Path expectedModelOutputPath = Paths.get(expectedModelOutput);

    assertTrue(Files.exists(expectedModelOutputPath));

    try (InputStream is = Files.newInputStream(expectedModelOutputPath)) {
      assertEquals("y000=ConstantNode(3.0)\n",
          new String(is.readAllBytes(), StandardCharsets.UTF_8));
    }
  }
}
