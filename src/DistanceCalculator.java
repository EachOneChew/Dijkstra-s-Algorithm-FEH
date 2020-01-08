import java.util.ArrayList;
import java.util.HashSet;

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

    private int getMoveRangeFromMoveType(char moveType) {
        if (moveType == 'c') {
            return 3;
        }
        else if (moveType == 'i' || moveType == 'f') {
            return 2;
        }
        else {
            return 1;
        }
    }

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
        moveRange = getMoveRangeFromMoveType(_moveType);
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
        moveRange = getMoveRangeFromMoveType(_moveType);
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

        // enemies get "special" delay label as -2
        // which means their distance will still be set by solveDistance
        // however, they cannot be traversed: they are not added to toVisit in solveDistance
        // if not single target, relations of target and unit and functionality as obstacles
        // will be completely different and addressed by solvePath
        if (!singleTarget)
        {
            findNode(labeledBoard, target1).setDelay(-2);
            findNode(labeledBoard, target2).setDelay(-2);
            findNode(labeledBoard, target3).setDelay(-2);
            findNode(labeledBoard, target4).setDelay(-2);
        }
    }

    // see README.md for what solveDistance does
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

            for (Integer[] coord: adjacentNodes)
            {
                Node temp = findNode(labeledBoard, coord);
                // if the delay is -2, the node is occupied by an enemy unit
                // thus, it's distance should still be calculated, BUT
                // the node should not be considered as being along any path
                // Tl;Dr this tile is now an untraversable obstacle with a distance value
                if (temp.getDelay() == -2)
                {
                    temp.setCurrentDistance
                    (Math.min(currentNodeDistance + 1, temp.getCurrentDistance()));
                }
                else if (!temp.getIsTraversed() && temp.getDelay() != -1)
                {
                    temp.setCurrentDistance
                    (Math.min(currentNodeDistance + temp.getDelay() + 1, temp.getCurrentDistance()));
                    // BUG https://github.com/EachOneChew/Dijkstra-s-Algorithm-FEH/issues/16
                    toVisit.add(coord);
                    dedupeArrayList(toVisit); // TODO if coord is already in toVisit, we may need to update
                                              // its currentDistance if we can get it smaller with currentNodeDistance 
                }
            }
            
            // delete it from toVisit
            toVisit.remove(currentCoordinates);
            findNode(labeledBoard, currentCoordinates).setIsTraversed(true);
            // moving onto the next node to visit - update currentCoordinates
            currentCoordinates = findMinDistanceNode(labeledBoard, toVisit);
            
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
        Node[][] localLabeledBoard = centeredOnTarget.getLabeledBoard();
        // why am I doing this?
        findNode(localLabeledBoard, target1).setDelay(-2);
        findNode(localLabeledBoard, target2).setDelay(-2);
        findNode(localLabeledBoard, target3).setDelay(-2);
        findNode(localLabeledBoard, target4).setDelay(-2);
        centeredOnTarget.solveDistance();

        ArrayList<Integer[]> closestTilesToTarget = new ArrayList<Integer[]>();

        // at this point the board should be marked appropriately with distances
        tracePathsRecursive(centeredOnTarget.getLabeledBoard(), closestTilesToTarget, unit, moveRange);

        return dedupeArrayList(closestTilesToTarget);
        // return closestTilesToTarget;
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
            
            // although staying in place is an option, we do not consider it in this method
            // also, in order for a node on a path to be eligible, it cannot be:
            // a) a wall
            // b) an enemy (target)
            // the reason this conditional check is here instead of the for loop after is because
            // if the check is performed then, eligibleThreshold would be incorrect
            if (currentNode[1] >= 1)
            {
                Integer[] upCoordinates = {currentNode[0], currentNode[1] - 1};
                Node upNode = findNode(_labeledBoard, upCoordinates);
                if (upNode.getDelay() >= 0
                && upNode.getCurrentDistance() < Integer.MAX_VALUE)
                {
                    candidateNodes.add(upCoordinates);
                }
            }
            if (currentNode[1] < _labeledBoard.length - 1)
            {
                Integer[] downCoordinates = {currentNode[0], currentNode[1] + 1};
                Node downNode = findNode(_labeledBoard, downCoordinates);
                if (downNode.getDelay() >= 0
                && downNode.getCurrentDistance() < Integer.MAX_VALUE)
                {
                    candidateNodes.add(downCoordinates);
                }
            }
            if (currentNode[0] >= 1)
            {
                Integer[] leftCoordinates = {currentNode[0] - 1, currentNode[1]};
                Node leftNode = findNode(_labeledBoard, leftCoordinates);
                if (leftNode.getDelay() >= 0
                && leftNode.getCurrentDistance() < Integer.MAX_VALUE)
                {
                    candidateNodes.add(leftCoordinates);
                }
            }
            if (currentNode[0] < _labeledBoard[0].length - 1)
            {
                Integer[] rightCoordinates = {currentNode[0] + 1, currentNode[1]};
                Node rightNode = findNode(_labeledBoard, rightCoordinates);
                if (rightNode.getDelay() >= 0
                && rightNode.getCurrentDistance() < Integer.MAX_VALUE)
                {
                    candidateNodes.add(rightCoordinates);
                }
            }

            // find the smallest distance value you can reach with one step
            int eligibleThreshold = findMinDistance(_labeledBoard, candidateNodes);

            // go through the candidates
            // if they're along the shortest path, "trace" them
            for (int i = 0; i < candidateNodes.size(); i++)
            {
                Integer[] temp = candidateNodes.get(i);
                Node tempNode = findNode(_labeledBoard, temp);
                if (tempNode.getCurrentDistance() <= eligibleThreshold)
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

    // exactly what it sounds like
    // needs to be like this because the Integer[]s I make aren't the same object
    private ArrayList<Integer[]> dedupeArrayList(ArrayList<Integer[]> input)
    {
        ArrayList<Integer[]> result = new ArrayList<Integer[]>();
        // ArrayList<Integer[]> alreadyAppeared = new ArrayList<Integer[]>();

        // for (Integer[] coord: input)
        // {
        //     boolean present = false;
        //     for (Integer[] compareCoord: alreadyAppeared)
        //     {
        //         if (coord[0] == compareCoord[0] && coord[1] == compareCoord[1])
        //         {
        //             present = true;
        //         }
        //     }
        //     if (!present)
        //     {
        //         result.add(coord);
        //         alreadyAppeared.add(coord);
        //     }
        // }

        HashSet<String> alreadyAppeared = new HashSet<String>();
        for (Integer[] coord: input) {
            String hashcode = coord[0].toString() + "," + coord[1].toString();
            if (!alreadyAppeared.contains(hashcode)) {
                result.add(coord);
                alreadyAppeared.add(hashcode);
            }
        }

        return result;
    }

    // getter methods
    public Node[][] getLabeledBoard()
    {
        return labeledBoard;
    }
}
