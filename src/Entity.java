import bagel.Image;
import bagel.util.Point;

/**
 * Abstract class which provides the common attributes/methods of all the other entities in the game
 */
public abstract class Entity {
    /**
     * The Image that the entity will produce on in game window
     */
    private Image image;
    /**
     * The position of the entity on the screen
     */
    protected Point position;
    /**
     * Constructor for Entity
     * @param x the initial x position of the entity
     * @param y the initial y position of the entity
     * @param image the image filename for the entity
     */
    public Entity (double x, double y, String image) {
        this.position = new Point (x, y);
        this.image = new Image(image);
    }
    /**
     * Draws the entity to the screen at it's defined position
     */
    public void drawEntity() {
        this.image.draw(this.getPosition().x, this.getPosition().y);
    }
    /**
     * Getter for position
     * @return The current position of the entity
     */
    public Point getPosition() {
        return position;
    }
}