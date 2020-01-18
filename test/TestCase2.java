import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class TestCase2 {

    @Test
    public void testSolution() {
        char[][] test =
        {
          {'o', 'o', 'w', 'o', 'o', 'o'}, 
          {'o', 'o', 'o', 'o', 'o', 'o'}, 
          {'w', 'w', 'w', 'o', 'w', 'o'}, 
          {'o', 'o', 'o', 'o', 'o', 'o'}, 
          {'o', 'o', 'o', 'o', 'o', 'o'}, 
          {'o', 'o', 'o', 'o', 'o', 'o'}, 
          {'o', 'o', 'o', 'o', 'o', 'o'}, 
          {'o', 'o', 'o', 'o', 'o', 'o'}
        };
  
        Integer[] _unit = {1, 1};
        Integer[] _target1 = {4, 4};
        Integer[] _target2 = {5, 5};
        Integer[] _target3 = {5, 6};
        Integer[] _target4 = {2, 1};

        int[][] expectedOutput = {
            {2         , 1         , 2147483647, 2147483647, 2147483647, 2147483647},
            {1         , 0         , 1         , 2147483647, 2147483647, 2147483647},
            {2147483647, 2147483647, 2147483647, 2147483647, 2147483647, 2147483647},
            {2147483647, 2147483647, 2147483647, 2147483647, 2147483647, 2147483647},
            {2147483647, 2147483647, 2147483647, 2147483647, 2147483647, 2147483647},
            {2147483647, 2147483647, 2147483647, 2147483647, 2147483647, 2147483647},
            {2147483647, 2147483647, 2147483647, 2147483647, 2147483647, 2147483647},
            {2147483647, 2147483647, 2147483647, 2147483647, 2147483647, 2147483647}
        };
  
        DistanceCalculator testDistanceCalculator = new DistanceCalculator
        (test, 'i', _unit, _target1, _target2, _target3, _target4);
  
        testDistanceCalculator.labelBoard();
        Node[][] testLabeledBoard = testDistanceCalculator.getLabeledBoard();
  
        testDistanceCalculator.solveDistance();
  
        for (int i = 0; i < testLabeledBoard.length; i++)
        {
            for (int j = 0; j < testLabeledBoard[i].length; j++)
            {
                assertEquals(testLabeledBoard[i][j].getCurrentDistance(), expectedOutput[i][j]);
            }
        }

        testDistanceCalculator.determineTarget(); // cover this dummy method
  
        ArrayList<Integer[]> testResults = testDistanceCalculator.solvePath(_target1);
        assert(testResults.isEmpty());
    }
}
