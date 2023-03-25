# Math Evolution

Artificial intelligence tool that uses evolutionary algorithm to compute a symbolic mathematical model for input data values.

The tool should be able to find a function `f` of `y=f(X)` for the measured values `y` and `X` where `y` is a real number and `X` vector of real numbers e.g., `(x1, x2)`.

It is written in Java 17.

## Prerequisites

* Java 17 - provided by e.g., [Amazon Corretto 17](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html)
* [Apache Maven](https://maven.apache.org/) 3.8.3+

## How to use

All commands should be executed in the project root.

### Compile source code

```bash
mvn clean package
```

### Run model fitting

**Inputs**:
* numeric values for function `y=f(X)` where `y` is a real number and `X` vector of real numbers e.g., `(x1, x2)`
* configuration file

**Supported input formats**:
* Tab-separated values with or without header
* Comma-separated values without quotes with or without header

Examples are at [math-core/src/test/resources/com/newtowndata/math/input](math-core/src/test/resources/com/newtowndata/math/input)

**Configuration values**:
```properties
# maximum duration of the evolution run
max_duration_in_sec=300

# maximum number of the evolution generations
max_iterations=100

# minimum number of function nodes
# (e.g., x1+x2 are three nodes - two for variables, one for addition)
min_nodes=1

# maximum number of function nodes
max_nodes=20

# maximum number of generation without the best offspring change
max_same_loops=20

# how often should a log line be produces
progress_update_in_sec=3

# if groups are allowed
# (groups allow to differentiate some variables by a binary switch e.g., material-based variable)
group_allowed=false

# population size
population_size=10000

# size of the population used for mutation
mutation_size=2000

# size of the population used for crossover mutation
crossover_size=4000

# size of the population that will survive no matter what fitness
survival_size=1000

# size of the population that will be replaced by new random offspring
replaceable_size=1000

# if true, model will be restricted iteratively to maximum nodes in between `<min_nodes,max_nodes>`
iterate_max_nodes=false

# number of evolution runs produced for each `max_node`
# this options works only for `iterate_max_nodes=true`
results_in_iteration=5

# output name prefix
result_output_prefix=results

# number of CPUs used
# if set to 0, all available processors will be used
thread_count=0

# penalty for bigger models (to prefer smaller solutions)
model_size_importance=1e-3
```

**Command to execute the computation:**
```bash
java -jar math-app/target/math-app-1.0.0.jar doc/input.csv doc/config.properties 
```

Expected output (removed timestamps):
```plain
++ Math Evolution ++
com.newtowndata.math.App                 : Starting App v1.0.0 using Java 17.0.6 with PID *REDACTED*
com.newtowndata.math.App                 : No active profile set, falling back to 1 default profile: "default"
com.newtowndata.math.App                 : Started App in 1.132 seconds (process running for 1.567)
c.n.math.input.DataRowsFileLoader        : Skipping non-digit line: y,x1        
c.newtowndata.math.app.AppInputProvider  : Loaded 3 rows
c.newtowndata.math.app.AppInputProvider  : Configuration: Configuration{maxDurationInSec=300, maxIterations=100, minNodes=1, maxNodes=20, maxSameLoops=20, progressUpdateInSec=3, groupAllowed=false, populationSize=10000, mutationSize=2000, crossoverSize=4000, survivalSize=1000, replaceableSize=1000, iterateMaxNodes=false, resultsInIteration=5, resultOutputPrefix=results, threadCount=0, modelSizeImportance=0.001}
c.n.math.result.ResultWriterProvider     : Using file result writer of prefix results
c.n.math.result.FileResultWriter         : Creating file results-2023-01-01-09-00-00.tsv
com.newtowndata.math.shell.RunCommand    : Runtime Config: CoreRuntimeConfig{maxNodes=20}
c.n.m.g.nodefactory.LeafNodeFactories    : Group node disabled
com.newtowndata.math.runner.Core         : Initial fitness: 0.368462
com.newtowndata.math.runner.Core         : Total loops: 75
c.n.math.result.FileResultWriter         : Creating model file results-2023-01-01-09-00-00-000001.model
com.newtowndata.math.shell.RunCommand    : Model        0.999600
Default (-((m_p--4.0)*x1)--3.0)
Simplified      (-(4.0*x1)--3.0)
com.newtowndata.math.shell.RunCommand    : Completed in 2115 ms
com.newtowndata.math.shell.RunCommand    : Done
```

Please, be aware every run is randomized, so it might take longer and/or produce a different result.

The model run produces the following two results:
* Machine-readable model result per each evolution run: `results-<timestamp>-<run_id>.model` (Example: [results-2023-01-01-09-01-01-000001.model](doc/results-2023-01-01-09-01-01-000001.model))
* Tab-separated values list of the best results: `results-<timestamp>.tsv` (Example: [results-2023-01-01-09-01-01.tsv](doc/results-2023-01-01-09-01-01.tsv))

Tab-separated values list of the best results has the following headers:
```plain
timestamp  max_nodes  fitness  result  simplified_result
```

### Analysis

It is possible to compute model fitness for another input data set (testing data).

**Command to execute the computation:**
```bash
java -jar math-analysis/target/math-analysis-1.0.0.jar fitness doc/results-2023-01-01-09-01-01-000001.model doc/input.csv
```

Expected output:
```plain
Fitness: 1.0

```

It is also possible to compute `y` from the model output and compare it to the provided `y`.

**Command to execute the computation:**
```bash
java -jar math-analysis/target/math-analysis-1.0.0.jar model_output doc/results-2023-01-01-09-01-01-000001.model doc/input.csv
```

Expected output:
```plain
3.0     3.0     0.0
-17.0   -17.0   5.0
15.0    15.0    -3.0

```

Tab-separated values output has the following headers:
```plain
y_from_model  y_from_input  x1
```

## License

Apache License, Version 2.0. See [LICENSE](LICENSE) for more details.


## For developers

### How to update dependencies

The following two commands list new versions of dependencies available.

Project dependencies:
```bash
mvn versions:display-dependency-updates
```

Plugins:
```bash
mvn versions:display-plugin-updates
```

### Compiling a submodule

```bash
mvn -pl math-core -am package
```

### Debugging

```bash
java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005 -jar math-app/target/math-app-1.0.0.jar doc/input.tsv doc/config.properties
```

## Bugs

Please, open an issue at https://github.com/NewTownData/math-evolution/issues

## Questions

If you have any question, feel free to send an e-mail at info@newtowndata.com

## Author

Voyta Krizek (see Questions above for an e-mail)
