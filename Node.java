public class Node
{
    private boolean isTraversed;
    private int currentDistance;
    // delay is determined when the char[ ][ ] board is translated to Node[ ][ ]
    // delay is set to -1 for untraversable tiles
    private int delay;

    public Node(boolean _isTraversed, int _currentDistance, int _delay)
    {
        isTraversed = _isTraversed;
        currentDistance = _currentDistance;
        delay = _delay;
    }

    // getter methods
    public boolean getIsTraversed()
    {
        return isTraversed;
    }

    public int getCurrentDistance()
    {
        return currentDistance;
    }

    public int getDelay()
    {
        return delay;
    }

    // setter methods
    public void setIsTraversed(boolean input)
    {
        isTraversed = input;
    }

    public void setCurrentDistance(int input)
    {
        currentDistance = input;
    }

    public void setDelay (int input)
    {
        delay = input;
    }
}