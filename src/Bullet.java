import bagel.util.Point;

/**
 * Bullet is the object that the player shoots to kill the zombies
 */
public class Bullet extends Entity implements Movable {
    /**
     * Stores whether or not the bullet is currently active so we know when to render it
     */
    private boolean isFired;
    /**
     * the constant distance the bullet moves in each tick it is active
     */
    private static final int STEP_SIZE = 25;
    /**
     * Constructor for Bullet
     * The bullet takes initial position (0,0) and is changed later when it needs to be rendered
     */
    public Bullet() {
        super(0, 0, "res/images/shot.png");
        this.isFired = false;
    }
    /**
     * Similar to the Player moveTo method, but using a different step size
     * @param goal the entity which the bullet is moving towards
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
    /**
     * Sets a new position of the bullet object
     * @param x the new x position of the bullet
     * @param y the new y position of the bullet
     */
    public void setPosition(double x, double y) {
        this.position = new Point(x, y);
    }
    /**
     * Changes the state of the bullet object
     * @param bool a boolean dictating if the bullet is active
     */
    public void setIsFired(boolean bool) {
        this.isFired = bool;
    }
    /**
     * Gets the state of the bullet object
     * @return the current state of the bullet
     */
    public boolean getIsFired() {
        return this.isFired;
    }
}