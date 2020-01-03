## How to Use PathCalculator

A PathCalculator's solveDistance method takes in the following inputs:

* char[ ][ ] "board" where:
    * 'o' represents a regular tile
    * 'x' represents a tile occupied by an allied unit
    * 'y" represents a tile occupied by an enemy unit
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

* the PathCalculator's Node[ ][ ] "labeledBoard" has been populated appropriately with distances according to Dijkstra's Algorithm
* an int[2] has been returned containing the coordinates of the closest target to the input unit

Now, in order to determine a unit's path, pass the results into solvePath, which takes the inputs:

* Node[ ][ ] "labelledBoard"
* int[2] "unit"
* int[2] "target"

solvePath returns:

* ArrayList<ArrayList<Integer>> representing the shortest paths from unit to target - often only 1 element

Once everything is done, "clearLabeledBoard" should be called to clear that field of a PathCalculator.