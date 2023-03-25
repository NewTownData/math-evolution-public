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
package com.newtowndata.math.analysis.runner;

import com.newtowndata.math.analysis.command.Command;
import com.newtowndata.math.analysis.command.FitnessCommand;
import com.newtowndata.math.analysis.command.ModelOutputCommand;
import java.util.Arrays;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CliRunner implements CommandLineRunner {

  private static final List<Command> COMMANDS = List.of(
      new ModelOutputCommand(),
      new FitnessCommand()
  );

  @Override
  public void run(String... args) throws Exception {
    if (args.length < 1) {
      throw new IllegalArgumentException("Missing <command>");
    }

    String commandString = args[0];
    Command command =
        COMMANDS.stream().filter(cmd -> cmd.name().equals(commandString)).findFirst().orElseThrow();
    command.execute(Arrays.copyOfRange(args, 1, args.length));
  }

}
