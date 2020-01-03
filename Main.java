class Main
{
  public static void main(String[] args)
  {
      // testing stuff
      char[][] testBoard =
      {
          {'o', 't', 'f', 'm', 'o'}, 
          {'f', 'm', 'l', 'w', 'o'}, 
          {'o', 'w', 'm', 'l', 'f'}
      };

      int[] testCoord = new int[2];
      testCoord[0] = 0;
      testCoord[1] = 0;

      PathCalculator testPathCalculator
      = new PathCalculator(testBoard, 'i', testCoord, testCoord, testCoord, testCoord, testCoord);

      testPathCalculator.labelBoard();
      Node[][] testLabeledBoard = testPathCalculator.getLabeledBoard();

      // print the labeledBoard
      for (int i = 0; i < testLabeledBoard.length; i++)
      {
          for (int j = 0; j < testLabeledBoard[i].length; j++)
          {
              Node temp = testLabeledBoard[i][j];
              System.out.print(temp.getDelay() + " ");
          }
          System.out.println("");
      }
  }
}