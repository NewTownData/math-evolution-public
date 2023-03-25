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
package com.newtowndata.math.shell;

import com.newtowndata.math.app.AppInput;
import com.newtowndata.math.app.AppInputProvider;
import com.newtowndata.math.config.Configuration;
import com.newtowndata.math.model.HumanReadableModel;
import com.newtowndata.math.model.RankedModel;
import com.newtowndata.math.result.ResultWriter;
import com.newtowndata.math.result.ResultWriterProvider;
import com.newtowndata.math.runner.Core;
import com.newtowndata.math.runner.CoreRuntime;
import com.newtowndata.math.runner.CoreRuntimeConfig;
import com.newtowndata.math.runner.CoreRuntimeConfigProvider;
import com.newtowndata.math.runner.CoreRuntimeImpl;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RunCommand implements CommandLineRunner {

  private static final Logger LOG = LoggerFactory.getLogger(RunCommand.class);

  @Override
  public void run(String... args) throws Exception {
    final AppInput appInput = new AppInputProvider(args).provide();

    List<CoreRuntimeConfig> runtimeConfigs =
        CoreRuntimeConfigProvider.provide(appInput.getConfig());

    try (CoreRuntime coreRuntime = new CoreRuntimeImpl(appInput.getConfig());
        ResultWriter resultWriter =
            ResultWriterProvider.provide(appInput.getConfig(), coreRuntime.getClock())) {

      long start = coreRuntime.getClock().millis();

      for (CoreRuntimeConfig runtimeConfig : runtimeConfigs) {
        LOG.info("Runtime Config: " + runtimeConfig);

        Configuration currentConfig = appInput.getConfig().copy();
        currentConfig.setMaxNodes(runtimeConfig.getMaxNodes());

        Core core = new Core(coreRuntime, appInput.getDataInput(), currentConfig);
        List<RankedModel> results = core.runEvolution();

        if (!results.isEmpty()) {
          resultWriter.write(runtimeConfig.getMaxNodes(), results.get(0));
          LOG.info(HumanReadableModel.modelToString(results.get(0)));
        }
      }

      long end = coreRuntime.getClock().millis();
      LOG.info("Completed in " + (end - start) + " ms");
    }

    LOG.info("Done");
  }
}
