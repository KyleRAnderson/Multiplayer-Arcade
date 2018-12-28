package games.pong.pieces.ball;

import com.sun.istack.internal.NotNull;

/**
 * Class for representing collisions between the pong ball and other objects.
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
public class Collision {
    private int x, y;

    private CollisionType type;

    /**
     * Gets the x position of the collision.
     *
     * @return x position of collision.
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x position of the collision
     *
     * @param x The x position of the collision
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the y position of the collision.
     *
     * @return y position of collision.
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y position of the collision
     *
     * @param y The y position of the collision
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gets the type of collision that occurred.
     *
     * @return The type of collision that occurred.
     */
    public CollisionType getType() {
        return type;
    }

    /**
     * Sets the type of collision that occurred.
     *
     * @param type The type of collision.
     */
    private void setType(CollisionType type) {
        this.type = type;
    }

    /**
     * Instantiates a new collision at the given location and with the given type.
     *
     * @param x    The x-position of the collision.
     * @param y    The y-position of the collision.
     * @param type The type of collision.
     */
    public Collision(int x, int y, @NotNull CollisionType type) {
        setX(x);
        setY(y);
        setType(type);
    }
}
