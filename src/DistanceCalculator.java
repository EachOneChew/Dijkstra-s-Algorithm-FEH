import java.util.ArrayList;

// EXTREMELY IMPORTANT NOTE:
// assuming a 2d array is looked at as a cartesian plane
// the coordinates (x, y) are actually accessed by doing [y][x]
// this is because 2d arrays are in the form [row][column]
// also, moving up in a 2d array is subtracting from index, vice versa
// so a 2d array is really a cartesian axis flipped upside down

public class DistanceCalculator
{
    private char[][] board;
    private Node[][] labeledBoard;
    // movetype of unit
    private char moveType;
    private int moveRange;
    // the following are coordinates
    private Integer[] unit;
    private Integer[] target1;
    private Integer[] target2;
    private Integer[] target3;
    private Integer[] target4;
    private boolean singleTarget;
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

    public DistanceCalculator
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
        singleTarget = false;
        if (_moveType == 'c')
        {
            moveRange = 3;
        }
        else if (_moveType == 'i' || _moveType == 'f')
        {
            moveRange = 2;
        }
        else
        {
            moveRange = 1;
        }
    }

    // for just distance from a to b
    public DistanceCalculator
    (char[][] _board, char _moveType,
    Integer[] _unit, Integer[] _target1)
    {
        board = _board;
        labeledBoard = new Node[_board.length][_board[0].length];
        moveType = _moveType;
        unit = _unit;
        target1 = _target1;
        singleTarget = true;
        if (_moveType == 'c')
        {
            moveRange = 3;
        }
        else if (_moveType == 'i' || _moveType == 'f')
        {
            moveRange = 2;
        }
        else
        {
            moveRange = 1;
        }
    }

    // convert a char[][] board "game field" of FEH into a labeledBoard
    public void labelBoard()
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

        // enemies are effectively walls
        findNode(labeledBoard, target1).setDelay(-1);
        if (!singleTarget)
        {
            findNode(labeledBoard, target2).setDelay(-1);
            findNode(labeledBoard, target3).setDelay(-1);
            findNode(labeledBoard, target4).setDelay(-1);
        }
    }

    // see README.md for what solveDistance does
    // should be private because it's wrapped inside solvePath
    public void solveDistance()
    {
        ArrayList<Integer[]> toVisit = new ArrayList<Integer[]>();
        // current node is initial unit position
        Integer[] currentCoordinates = {unit[0], unit[1]};
        // distance from current node to current node is 0
        findNode(labeledBoard, currentCoordinates).setCurrentDistance(0);
        
        // now start the algorithm's loop
        // might as well do the whole board
        while (true)
        // (!(findNode(labeledBoard, target1).getIsTraversed()
        // && findNode(labeledBoard, target2).getIsTraversed()
        // && findNode(labeledBoard, target3).getIsTraversed()
        // && findNode(labeledBoard, target4).getIsTraversed()))
        {
            int currentNodeDistance = findNode(labeledBoard, currentCoordinates).getCurrentDistance();

            ArrayList<Integer[]> adjacentNodes = new ArrayList<Integer[]>();
            if (currentCoordinates[1] >= 1)
            {
                Integer[] upCoordinates = {currentCoordinates[0], currentCoordinates[1] - 1};
                adjacentNodes.add(upCoordinates);
            }
            if (currentCoordinates[1] < labeledBoard.length - 1)
            {
                Integer[] downCoordinates = {currentCoordinates[0], currentCoordinates[1] + 1};
                adjacentNodes.add(downCoordinates);
            }
            if (currentCoordinates[0] >= 1)
            {
                Integer[] leftCoordinates = {currentCoordinates[0] - 1, currentCoordinates[1]};
                adjacentNodes.add(leftCoordinates);
            }
            if (currentCoordinates[0] < labeledBoard[0].length - 1)
            {
                Integer[] rightCoordinates = {currentCoordinates[0] + 1, currentCoordinates[1]};
                adjacentNodes.add(rightCoordinates);
            }

            for (Integer[] curCoordinates: adjacentNodes)
            {
                if (!findNode(labeledBoard, curCoordinates).getIsTraversed()
                && findNode(labeledBoard, curCoordinates).getDelay() != -1)
                {
                    Node temp = findNode(labeledBoard, curCoordinates);
                    temp.setCurrentDistance(Math.min
                    (currentNodeDistance + temp.getDelay() + 1, temp.getCurrentDistance()));
                    if (!toVisit.contains(curCoordinates))
                    {
                        toVisit.add(curCoordinates);
                    }
                }
            }
            
            findNode(labeledBoard, currentCoordinates).setIsTraversed(true);
            // moving onto the next node to visit
            currentCoordinates = findMinDistanceNode(labeledBoard, toVisit);
            // delete it from toVisit
            toVisit.remove(currentCoordinates);
            if (toVisit.isEmpty())
            {
                break;
            }
        }
    }

    // when done this should be multipurpose, for both enemy and assist target selection
    public Integer[] determineTarget()
    {
        Integer[] boo = {0, 0};
        return boo;
    }

    // solvePath does not need unit coordinates because that's already a field
    public ArrayList<Integer[]> solvePath (Integer[] _target)
    {
        DistanceCalculator centeredOnTarget = new DistanceCalculator(board, moveType, _target, unit);
        centeredOnTarget.labelBoard();
        centeredOnTarget.solveDistance();
        ArrayList<Integer[]> closestTilesToTarget = new ArrayList<Integer[]>();

        // at this point the board should be marked appropriately with distances
        tracePathsRecursive(centeredOnTarget.getLabeledBoard(), closestTilesToTarget, unit, moveRange);

        return closestTilesToTarget;
    }

    private void tracePathsRecursive
    (Node[][] _labeledBoard, ArrayList<Integer[]> resultCoordinates, Integer[] currentNode, int stepsLeft)
    {
        if (stepsLeft == 0)
        {
            resultCoordinates.add(currentNode);
        }
        else
        {
            ArrayList<Integer[]> candidateNodes = new ArrayList<Integer[]>();
            
            // go through neighboring nodes
            if (currentNode[1] >= 1)
            {
                Integer[] upCoordinates = {currentNode[0], currentNode[1] - 1};
                candidateNodes.add(upCoordinates);
            }
            if (currentNode[1] < _labeledBoard.length - 1)
            {
                Integer[] downCoordinates = {currentNode[0], currentNode[1] + 1};
                candidateNodes.add(downCoordinates);
            }
            if (currentNode[0] >= 1)
            {
                Integer[] leftCoordinates = {currentNode[0] - 1, currentNode[1]};
                candidateNodes.add(leftCoordinates);
            }
            if (currentNode[0] < _labeledBoard[0].length - 1)
            {
                Integer[] rightCoordinates = {currentNode[0] + 1, currentNode[1]};
                candidateNodes.add(rightCoordinates);
            }
            
            // find the smallest distance value you can reach with one step
            int eligibleThreshold = findMinDistance(_labeledBoard, candidateNodes);

            // go through the candidates
            // if they're along the shortest path, "trace" them
            for (int i = 0; i < candidateNodes.size(); i++)
            {
                Integer[] temp = candidateNodes.get(i);
                if (findNode(_labeledBoard, temp).getCurrentDistance()
                <= eligibleThreshold)
                {
                    tracePathsRecursive
                    (_labeledBoard, resultCoordinates, temp, stepsLeft - 1);
                }
            }
        }
    }

    // takes an ArrayList of int[] coordinates
    // returns coordinates of the node with smallest distance value
    private Integer[] findMinDistanceNode(Node[][] _labeledBoard, ArrayList<Integer[]> input)
    {
        int curMininum = Integer.MAX_VALUE;
        Integer[] curMinimumCoordinates = new Integer[2];
        for (int i = 0; i < input.size(); i++)
        {
            int temp = findNode(_labeledBoard, input.get(i)).getCurrentDistance();
            if (temp < curMininum)
            {
                curMininum = temp;
                curMinimumCoordinates = input.get(i);
            }            
        }
        return curMinimumCoordinates;
    }

    // takes an ArrayList of int[] coordinates
    // returns the minimum distance value of the nodes in the ArrayList
    private int findMinDistance(Node[][] _labeledBoard, ArrayList<Integer[]> input)
    {
        int curMininum = Integer.MAX_VALUE;
        for (int i = 0; i < input.size(); i++)
        {
            int temp = findNode(_labeledBoard, input.get(i)).getCurrentDistance();
            if (temp < curMininum)
            {
                curMininum = temp;
            }            
        }
        return curMininum;
    }
    
    // takes int[] coordinates and finds the corresponding node in labeledBoard
    private Node findNode(Node[][] _labeledBoard, Integer[] coordinates)
    {
        return _labeledBoard[coordinates[1]][coordinates[0]];
    }

    // getter methods
    public Node[][] getLabeledBoard()
    {
        return labeledBoard;
    }
}