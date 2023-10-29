/**
 * Sandwich entity within the game which the player can interact with to eat and gain energy
 */
public class Sandwich extends Entity{
    /**
     * State of the sandwich, helps determine whether to render it or not
     */
    private boolean isEaten;
    /**
     * Constructor for Sandwich, a sandwich always starts as un-eaten
     * @param x initial x position of the sandwich
     * @param y initial y position of the sandwich
     */
    public Sandwich (double x, double y) {
        super(x, y, "res/images/sandwich.png");
        this.isEaten = false;
    }
    /**
     * Setter for isEaten, changes the state of the sandwich
     * @param bool The new state of the sandwich
     */
    public void setIsEaten (boolean bool) {
        this.isEaten = bool;
    }
    /**
     * Getter for isEaten
     * @return Current state of the sandwich
     */
    public boolean getIsEaten() {
        return isEaten;
    }
}