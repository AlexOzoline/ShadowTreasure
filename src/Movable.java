/**
 * interface implemented by Bullet and Player as they are the only entities capable of movement
 */

import bagel.util.Point;

public interface Movable {
    void moveTo(Point goal);
}
