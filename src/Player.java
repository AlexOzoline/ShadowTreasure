import bagel.util.Point;

/**
 * The main entity in the game, can eat sandwiches, shoot bullets at zombies, and collect the treasure to win
 */
public class Player extends Entity implements Movable{
    /**
     * The amount of energy the player has from eating sandwiches / shooting bullets
     */
    private int energy;
    /**
     * The length that the player moves in 1 tick
     */
    private static final int STEP_SIZE = 10;
    /**
     * Constructor for Player
     * @param x the initial x coordinate of the player
     * @param y the initial y coordinate of the player
     * @param startEnergy the initial energy of the player
     */
    public Player (double x, double y, int startEnergy) {
        super(x, y, "res/images/player.png");
        this.energy = startEnergy;
    }
    /**
     * Changes the energy of the player after it has been updated by interacting with another entity in the game
     * @param e the new energy value for the player
     */
    public void setEnergy (int e) {
        this.energy = e;
    }
    /**
     * Gets the stored energy value of the player at a given moment
     * @return the current energy the player has
     */
    public int getEnergy() {
        return energy;
    }

    /**
     * Changes the position of the player to a new Point in the game
     * @param x the new x position of the player
     * @param y the new y position of the player
     */
    public void setPosition(double x, double y) {
        this.position = new Point(x, y);
    }
    /**
     * Moves the player to its next 'goal' entity using the player's step size
     * @param goal the entity which the player is moving towards
     */
    @Override
    public void moveTo(Point goal) {
        Point step;
        double DirectionX;
        double DirectionY;
        double len = this.getPosition().distanceTo(goal);
        DirectionX = (goal.x - this.getPosition().x)/len;
        DirectionY = (goal.y - this.getPosition().y)/len;
        step = new Point(DirectionX * STEP_SIZE, DirectionY * STEP_SIZE);
        this.setPosition(this.getPosition().x + step.x, this.getPosition().y + step.y);
    }
}