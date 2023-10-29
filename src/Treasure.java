/**
 * Treasure object that is the ultimate goal to reach in the game
 */
public class Treasure extends Entity{
    /**
     * Constructor for treasure
     * @param x the initial x position of the treasure
     * @param y the initial y position of the treasure
     */
    public Treasure(double x, double y) {
    super(x,y, "res/images/treasure.png");
    }
}
