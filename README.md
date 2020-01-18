[![Build Status](https://travis-ci.com/EachOneChew/Dijkstra-s-Algorithm-FEH.svg?branch=master)](https://travis-ci.com/EachOneChew/Dijkstra-s-Algorithm-FEH)

![codecov](https://codecov.io/gh/EachOneChew/Dijkstra-s-Algorithm-FEH/branch/master/graph/badge.svg)


![GitHub contributors](https://img.shields.io/github/contributors/EachOneChew/Dijkstra-s-Algorithm-FEH)
![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/EachOneChew/Dijkstra-s-Algorithm-FEH?include_prereleases)
![GitHub repo size](https://img.shields.io/github/repo-size/EachOneChew/Dijkstra-s-Algorithm-FEH)
![GitHub](https://img.shields.io/github/license/EachOneChew/Dijkstra-s-Algorithm-FEH)


# How to Build

After checking out the repo, run `./build.sh`. Folder `build` would contain compiled Java classes. The build process includes style checking of code, compilation on both source files and test files, and running `Main` class as a test in a separate folder `test-build`.

We choose `ant` as the build system, which is shipped in the repo's folder `vendor/apache-ant-1.10.7`.

You can also test your code by running:

```
vendor/apache-ant-1.10.7/bin/ant test
```

Likely you will see output below:

```
zqiu02vl:Dijkstra-s-Algorithm zqiu$ vendor/apache-ant-1.10.7/bin/ant test
Buildfile: /Users/zqiu/temp/Dijkstra-s-Algorithm/build.xml

init:
    [mkdir] Created dir: /Users/zqiu/temp/Dijkstra-s-Algorithm/build
    [mkdir] Created dir: /Users/zqiu/temp/Dijkstra-s-Algorithm/test-build

checkstyle:
[checkstyle] Running Checkstyle 8.28 on 2 files

compile:
    [javac] Compiling 2 source files to /Users/zqiu/temp/Dijkstra-s-Algorithm/build

test-compile:
    [javac] Compiling 1 source file to /Users/zqiu/temp/Dijkstra-s-Algorithm/test-build

test:
     [java] 2          1          2147483647 2147483647 2147483647 2147483647
     [java]
     [java] 1          0          1          2147483647 2147483647 2147483647
     [java]
     [java] 2147483647 2147483647 2147483647 2147483647 2147483647 2147483647
     [java]
     [java] 2147483647 2147483647 2147483647 2147483647 2147483647 2147483647
     [java]
     [java] 2147483647 2147483647 2147483647 2147483647 2147483647 2147483647
     [java]
     [java] 2147483647 2147483647 2147483647 2147483647 2147483647 2147483647
     [java]
     [java] 2147483647 2147483647 2147483647 2147483647 2147483647 2147483647
     [java]
     [java] 2147483647 2147483647 2147483647 2147483647 2147483647 2147483647
     [java]

BUILD SUCCESSFUL
Total time: 2 seconds
```

You can find code coverage report in folder `test-report/report/index.html`.

If you want to clean up your build, run command below:

```
vendor/apache-ant-1.10.7/bin/ant clean
```

# How to Use DistanceCalculator

A DistanceCalculator's solveDistance method takes in the following inputs:

* `char[][]` "board" where:
    * 'o' represents a regular tile
    * 'f' represents a forest
    * 'm' represents a mountain
    * 'l' represents a liquid (water/pond/lava)
    * 'w' represents a wall
    * 't" represents a trench
* `char` "moveType" where:
    * 'i' represents infantry
    * 'a' represents armor
    * 'c' represents cavalry
    * 'f" represents flying
* `int[2]` "unit" where:
    * index 0 represents the x coordinate of unit
    * index 1 represents the y coordinate of unit
* the following are of the same form as above
    * `int[2]` "target1"
    * `int[2]` "target2"
    * `int[2]` "target3"
    * `int[2]` "target4"

Upon solveDistance's completion, the following has happened:

* the DistanceCalculator's `Node[][]` "labeledBoard" has been populated appropriately with distances according to Dijkstra's Algorithm

Next, determineTarget should be run.

* DETAILS TBA

Now, in order to determine a unit's path, pass the results into solvePath, which takes the inputs:

* `int[2]` "unit"
* `int[2]` "target"

solvePath returns:

* `ArrayList<ArrayList<Integer>>` representing the shortest paths from unit to target - often only 1 element

Note that the way solvePath works means that it can solve for the shortest path between any two points. Ergo, solvePath does not require the board to be labelled since it calls solveDistance centered on the target coordinates itself anyways.

Once everything is done, "clearFields" should be called to clear all fields of a DistanceCalculator. Alternatively, a new DistanceCalculator can be created and the current one discarded.

# How to Release

Before you tag a point, check it out to your local repo and run `./build.sh`. You will find the release file `dist/distance-calculator.jar` for attaching to your release as binary.

Tag your point and follow the normal release process in Github to cut a new release.

# Example

See file `test/Main.java` for more details.

To build the example file: `./build.sh`. You will find its class file in folder `test-build`.

To run the example: `java -classpath build:test-build Main`. You will see the output in console.
