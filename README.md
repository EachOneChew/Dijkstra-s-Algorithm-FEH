[![Build Status](https://travis-ci.org/EachOneChew/Dijkstra-s-Algorithm-FEH.svg?branch=master)](https://travis-ci.org/EachOneChew/Dijkstra-s-Algorithm-FEH)

# How to build

After checking out the repo, run `./build.sh`. Folder `build` would contain compiled Java classes. The build process includes style checking of code, compilation, and running `Main` class as a test.

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

checkstyle:
[checkstyle] Running Checkstyle 8.28 on 3 files

compile:

test:
     [java] 1          2          3          6          2147483647
     [java] 0          1          2147483647 7          2147483647
     [java] 1          2          5          6          2147483647
     [java] 2          3          4          2147483647 2147483647
     [java] 3          2147483647 2147483647 2147483647 2147483647
     [java] 4          5          2147483647 2147483647 2147483647

BUILD SUCCESSFUL
Total time: 1 second
```

# How to Use DistanceCalculator

A DistanceCalculator's `solveDistance` method takes in the following inputs:

* char[ ][ ] "board" where:
    * 'o' represents a regular tile
    * 'f' represents a forest
    * 'm' represents a mountain
    * 'l' represents a liquid (water/pond/lava)
    * 'w' represents a wall
    * 't" represents a trench
* char "moveType" where:
    * 'i' represents infantry
    * 'a' represents armor
    * 'c' represents cavalry
    * 'f" represents flying
* int[2] "unit" where:
    * index 0 represents the x coordinate of unit
    * index 1 represents the y coordinate of unit
* the following are of the same form as above
    * int[2] "target1"
    * int[2] "target2"
    * int[2] "target3"
    * int[2] "target4"

Upon solveDistance's completion, the following has happened:

* the DistanceCalculator's Node[ ][ ] "labeledBoard" has been populated appropriately with distances according to Dijkstra's Algorithm

Next, determineTarget should be run.

* DETAILS TBA

Now, in order to determine a unit's path, pass the results into solvePath, which takes the inputs:

* int[2] "unit"
* int[2] "target"

solvePath returns:

* ArrayList<ArrayList<Integer>> representing the shortest paths from unit to target - often only 1 element

Note that the way solvePath works means that it can solve for the shortest path between any two points. Ergo, solvePath does not require the board to be labelled since it calls solveDistance centered on the target coordinates itself anyways.

Once everything is done, "clearFields" should be called to clear all fields of a DistanceCalculator. Alternatively, a new DistanceCalculator can be created and the current one discarded.

# TODO

   - [ ] create basic unit tests
   - [x] set up build script
   - [x] hook up to CI (travis or circleci)
