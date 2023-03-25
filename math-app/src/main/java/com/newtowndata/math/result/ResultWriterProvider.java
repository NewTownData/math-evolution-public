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

import com.newtowndata.math.config.Configuration;
import java.time.Clock;
import java.util.logging.Logger;

public final class ResultWriterProvider {

  private static final Logger LOG = Logger.getLogger(ResultWriterProvider.class.getName());

  private ResultWriterProvider() {
  }

  public static ResultWriter provide(Configuration configuration, Clock clock) {
    String outputPrefix = configuration.getResultOutputPrefix();

    if (outputPrefix == null || outputPrefix.isEmpty()) {
      LOG.info("Using dummy result writer");
      return new DummyResultWriter();
    } else {
      LOG.info("Using file result writer of prefix " + outputPrefix);
      return new FileResultWriter(outputPrefix, clock);
    }
  }
}
