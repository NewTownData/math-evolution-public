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

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static java.time.temporal.ChronoField.YEAR;
import com.newtowndata.math.model.HumanReadableModel;
import com.newtowndata.math.model.Model;
import com.newtowndata.math.model.RankedModel;
import com.newtowndata.math.serialization.ModelSerializer;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class FileResultWriter implements ResultWriter {

  private static final Logger LOG = Logger.getLogger(FileResultWriter.class.getName());

  private static final String SUFFIX = ".tsv";
  private static final String MODEL_SUFFIX = ".model";

  private static final DateTimeFormatter FILE_DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
      .appendValue(YEAR, 4).appendLiteral('-').appendValue(MONTH_OF_YEAR, 2).appendLiteral('-')
      .appendValue(DAY_OF_MONTH, 2).appendLiteral('-').appendValue(HOUR_OF_DAY, 2)
      .appendLiteral('-').appendValue(MINUTE_OF_HOUR, 2).appendLiteral('-')
      .appendValue(SECOND_OF_MINUTE, 2).toFormatter().withZone(ZoneId.of("UTC"));

  private static final DateTimeFormatter TSV_DATE_TIME_FORMATTER =
      DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.of("UTC"));

  private final FileWriter fileWriter;
  private final Clock clock;
  private final String outputPrefix;

  private final AtomicInteger lineCounter = new AtomicInteger(0);
  private final ModelSerializer modelSerializer = new ModelSerializer();

  public FileResultWriter(String resultOutputPrefix, Clock clock) {
    this.clock = clock;
    this.outputPrefix = resultOutputPrefix + "-" + FILE_DATE_TIME_FORMATTER.format(clock.instant());

    String outputFile = this.outputPrefix + SUFFIX;
    try {
      LOG.info("Creating file " + outputFile);
      this.fileWriter = new FileWriter(outputFile, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to create result file " + outputFile);
    }
  }

  @Override
  public void write(int maxNodes, RankedModel model) {
    try {
      fileWriter.write(TSV_DATE_TIME_FORMATTER.format(clock.instant()) + "\t" + maxNodes + "\t"
          + HumanReadableModel.modelToTsv(model) + "\n");
      fileWriter.flush();
    } catch (IOException e) {
      throw new IllegalStateException("Failed to write model", e);
    }

    int line = lineCounter.incrementAndGet();
    writeModel(line, model.getModel());
  }

  private void writeModel(int line, Model model) {
    String modelString = modelSerializer.serialize(model);
    String file = outputPrefix + "-" + String.format(Locale.ENGLISH, "%06d", line) + MODEL_SUFFIX;

    LOG.info("Creating model file " + file);
    try (FileWriter modelWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
      modelWriter.write(modelString);
    } catch (IOException e) {
      throw new IllegalArgumentException("Failed to write model output to " + file, e);
    }
  }

  @Override
  public void close() {
    try {
      fileWriter.close();
    } catch (IOException e) {
      throw new IllegalStateException("Failed to close writer");
    }
  }
}
