/**
 * Zombie object in the game which the player can shoot
 */
public class Zombie extends Entity{
    /**
     * Boolean storing the state of the zombie within the game
     */
    private boolean shotDead;
    /**
     * Constructor for Zombie, a zombie always starts as alive
     * @param x the initial x position of the zombie
     * @param y the initial y position of the zombie
     */
    public Zombie (double x, double y) {
        super(x, y, "res/images/zombie.png");
        this.shotDead = false;
    }
    /**
     * Setter for the state of the zombie
     * @param bool the new state of the zombie (alive or not)
     */
    public void setShotDead (boolean bool) {
        this.shotDead = bool;
    }
    /**
     * Getter for the state of the zombie
     * @return The state of the zombie within the game
     */
    public boolean getShotDead() {
        return shotDead;
    }
}
