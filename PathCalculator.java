public class PathCalculator
{
    private char[][] board;
    private Node[][] labeledBoard;
    // movetype of unit
    private char moveType;
    // the following are coordinates
    private int[] unit;
    private int[] target1;
    private int[] target2;
    private int[] target3;
    private int[] target4;
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
    int[] _unit, int[] _target1, int[] _target2, int[] _target3, int[] _target4)
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
                int delay = delayChart[delayChartRowNumber][delayChartColumnNumber];
                labeledBoard[i][j] = new Node(false, Integer.MAX_VALUE, delay);
            }
        }
    }

    // getter methods
    public Node[][] getLabeledBoard()
    {
        return labeledBoard;
    }
}