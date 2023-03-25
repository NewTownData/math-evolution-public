package com.newtowndata.math.analysis;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

public class AnalyzeTest {

  private static String getTestPath(String resource) {
    try {
      return Paths.get(AnalyzeTest.class.getResource(resource).toURI()).toString();
    } catch (URISyntaxException e) {
      throw new IllegalStateException("Cannot load test path");
    }
  }

  @Test
  void testFitness() {
    Analyze
        .main(new String[] {"fitness", getTestPath("example.model"), getTestPath("example.csv")});
  }

  @Test
  void testModelOutput() {
    Analyze.main(
        new String[] {"model_output", getTestPath("example.model"), getTestPath("example.csv")});
  }
}
