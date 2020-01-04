class Main
{
  public static void main(String[] args)
  {
      // testing stuff
      char[][] test =
      {
        {'o', 'o', 'o', 't', 'o'}, 
        {'o', 'o', 'w', 'o', 'o'}, 
        {'o', 'o', 't', 'o', 'o'}, 
        {'o', 'o', 'o', 'f', 'o'}
      };

      Integer[] _unit = {0, 1};
      Integer[] _target1 = {4, 0};
      Integer[] _target2 = {4, 1};
      Integer[] _target3 = {4, 2};
      Integer[] _target4 = {4, 3};

      PathCalculator testPathCalculator = new PathCalculator
      (test, 'c', _unit, _target1, _target2, _target3, _target4);

      testPathCalculator.labelBoard();
      Node[][] testLabeledBoard = testPathCalculator.getLabeledBoard();

      System.out.println(testPathCalculator.solveDistance()[0] + " " + testPathCalculator.solveDistance()[1]);

      // print the labeledBoard
      for (int i = 0; i < testLabeledBoard.length; i++)
      {
          for (int j = 0; j < testLabeledBoard[i].length; j++)
          {
              Node temp = testLabeledBoard[i][j];
              System.out.print(temp.getCurrentDistance() + " ");
          }
          System.out.println("");
      }
  }
}