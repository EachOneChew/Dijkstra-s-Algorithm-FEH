import java.util.ArrayList;

// EXTREMELY IMPORTANT NOTE:
// assuming a 2d array is looked at as a cartesian plane
// the coordinates (x, y) are actually accessed by doing [y][x]
// this is because 2d arrays are in the form [row][column]
// also, moving up in a 2d array is subtracting from index, vice versa
// so a 2d array is really a cartesian axis flipped upside down

public class PathCalculator
{
    private char[][] board;
    private Node[][] labeledBoard;
    // movetype of unit
    private char moveType;
    // the following are coordinates
    private Integer[] unit;
    private Integer[] target1;
    private Integer[] target2;
    private Integer[] target3;
    private Integer[] target4;
    // chart to lookup delays for movement type in relation to terrain type
    //    o   f   m   l   w   t
    // i
    // a
    // c
    // f
    private static final int[][] delayChart =
    {
        {0,  1, -1, -1, -1,  0}, 
        {0,  0, -1, -1, -1,  0}, 
        {0, -1, -1, -1, -1,  2}, 
        {0,  0,  0,  0, -1,  0} 
    };

    public PathCalculator
    (char[][] _board, char _moveType,
    Integer[] _unit, Integer[] _target1, Integer[] _target2, Integer[] _target3, Integer[] _target4)
    {
        board = _board;
        labeledBoard = new Node[_board.length][_board[0].length];
        moveType = _moveType;
        unit = _unit;
        target1 = _target1;
        target2 = _target2;
        target3 = _target3;
        target4 = _target4;
    }

    // convert a char[][] board "game field" of FEH into a labeledBoard
    public void labelBoard ()
    {
        int delayChartRowNumber = 0;
        // assigns a lookup row number for delayChart depending on the movement type
        if (moveType == 'i')
        {
            delayChartRowNumber = 0;
        }
        else if (moveType == 'a')
        {
            delayChartRowNumber = 1;
        }
        else if (moveType == 'c')
        {
            delayChartRowNumber = 2;
        }
        else
        {
            delayChartRowNumber = 3;
        }

        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board[i].length; j++)
            {
                char cur = board[i][j];
                // assigns a lookup column number depending on the current tile type
                int delayChartColumnNumber = 0;
                if (cur == 'o')
                {
                    delayChartColumnNumber = 0;
                }
                else if (cur == 'f')
                {
                    delayChartColumnNumber = 1;
                }
                else if (cur == 'm')
                {
                    delayChartColumnNumber = 2;
                }
                else if (cur == 'l')
                {
                    delayChartColumnNumber = 3;
                }
                else if (cur == 'w')
                {
                    delayChartColumnNumber = 4;
                }
                else
                {
                    delayChartColumnNumber = 5;
                }
                // look up the delay in the delayChart
                int delay = delayChart[delayChartRowNumber][delayChartColumnNumber];
                // label the node in the labeledBoard
                labeledBoard[i][j] = new Node(false, Integer.MAX_VALUE, delay);
            }
        }
    }

    // see README.md for what solveDistance does
    // should be private because it's wrapped inside solvePath
    public Integer[] solveDistance()
    {
        ArrayList<Integer[]> toVisit = new ArrayList<Integer[]>();
        // current node is initial unit position
        Integer[] currentCoordinates = {unit[0], unit[1]};
        // distance from current node to current node is 0
        findNode(currentCoordinates).setCurrentDistance(0);
        
        // now start the algorithm's loop
        while
        (!(findNode(target1).getIsTraversed()
        && findNode(target2).getIsTraversed()
        && findNode(target3).getIsTraversed()
        && findNode(target4).getIsTraversed()))
        {
            int currentNodeDistance = findNode(currentCoordinates).getCurrentDistance();

            ArrayList<Inteter[]> neighboursToCheck = new ArrayList<Inteter[]>();
            if (currentCoordinates[1] >= 1) {
                Integer[] upCoordinates = {currentCoordinates[0], currentCoordinates[1] - 1};
                neighboursToCheck.add(upCoordinates);
            }
            if (currentCoordinates[1] < labelBoard.length - 1) {
                Integer[] downCoordinates = {currentCoordinates[0], currentCoordinates[1] + 1};
                neighboursToCheck.add(downCoordinates);
            }
            if (currentCoordinates[0] >= 1) {
                Integer[] leftCoordinates = {currentCoordinates[0] - 1, currentCoordinates[1]};
                neighboursToCheck.add(leftCoordinates);
            }
            if (currentCoordinates[0] < labelBoard[0].length - 1) {
                Integer[] rightCoordinates = {currentCoordinates[0] + 1, currentCoordinates[1]};
                neighboursToCheck.add(rightCoordinates);
            }

            for (Inteter[] coord : neighboursToCheck) {
                if (!findNode(coord).getIsTraversed() && findNode(coord).getDelay() != -1) {
                    Node node = findNode(coord);
                    node.setCurrentDistance(Math.min(
                        currentNodeDistance + node.getDelay() + 1,
                        node.getCurrentDistance()
                    ));
                    if (!toVisit.contains(coord)) {
                        toVisit.add(coord);
                    }
                }
            }
            
            findNode(currentCoordinates).setIsTraversed(true);
            // moving onto the next node to visit
            currentCoordinates = findMinDistanceNode(toVisit);
            // delete it from toVisit
            toVisit.remove(currentCoordinates);
            if (toVisit.isEmpty())
            {
                break;
            }
        }

        // put the 4 target coordinates into an ArrayList
        ArrayList<Integer[]> targetCoordinates = new ArrayList<Integer[]>();
        targetCoordinates.add(target1);
        targetCoordinates.add(target2);
        targetCoordinates.add(target3);
        targetCoordinates.add(target4);
        // search the ArrayList for the one with the node of smallest distance
        return findMinDistanceNode(targetCoordinates);
    }

    // takes an ArrayList of int[] coordinates
    // returns coordinates of the node with smallest distance value
    private Integer[] findMinDistanceNode(ArrayList<Integer[]> input)
    {
        int curMininum = Integer.MAX_VALUE;
        Integer[] curMinimumCoordinates = new Integer[2];
        for (int i = 0; i < input.size(); i++)
        {
            int temp = findNode(input.get(i)).getCurrentDistance();
            if (temp < curMininum)
            {
                curMininum = temp;
                curMinimumCoordinates = input.get(i);
            }            
        }
        return curMinimumCoordinates;
    }

    // takes int[] coordinates and finds the corresponding node in labeledBoard
    private Node findNode(Integer[] coordinates)
    {
        return labeledBoard[coordinates[1]][coordinates[0]];
    }

    // getter methods
    public Node[][] getLabeledBoard()
    {
        return labeledBoard;
    }
}