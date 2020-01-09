import java.util.ArrayList;

public class Main
{
  public static void main(String[] args)
  {
      // testing stuff
      char[][] test =
      {
        {'o', 'o', 'o', 'o', 'o', 'o'}, 
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

      DistanceCalculator testDistanceCalculator = new DistanceCalculator
      (test, 'i', _unit, _target1, _target2, _target3, _target4);

      testDistanceCalculator.labelBoard();
      Node[][] testLabeledBoard = testDistanceCalculator.getLabeledBoard();

      testDistanceCalculator.solveDistance();

      // print the labeledBoard
      for (int i = 0; i < testLabeledBoard.length; i++)
      {
          for (int j = 0; j < testLabeledBoard[i].length; j++)
          {
              Node temp = testLabeledBoard[i][j];
              if (temp.getCurrentDistance() > 9999)
              {
                System.out.print(temp.getCurrentDistance() + " ");
              }
              else if (temp.getCurrentDistance() > 9)
              {
                System.out.print(temp.getCurrentDistance() + "         ");
              }
              else
              {
                System.out.print(temp.getCurrentDistance() + "          ");
              }
          }
          System.out.println("");
          System.out.println("");
      }

      ArrayList<Integer[]> testResults = testDistanceCalculator.solvePath(_target1);

      for (Integer[] i: testResults)
      {
        System.out.println(i[0] + " " + i[1]);
      }
  }
}
