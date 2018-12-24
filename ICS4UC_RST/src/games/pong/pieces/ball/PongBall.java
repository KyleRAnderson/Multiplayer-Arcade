package games.pong.pieces.ball;

import games.pong.pieces.PongPiece;
import games.pong.pieces.Side;

/**
 * Class for representing and mimicking the behaviour of a pong ball.
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
public class PongBall implements PongPiece {
    /**
     * Velocity of the ball in units per second.
     */
    public static final int VELOCITY = 100;

    /* The velocities of the pong ball, in rise and runs per tick.
     * A positive rise means going up, a positive run means going left, opposite for negative rises and runs. */
    private double risePerSecond, runPerSecond;
    // Position of the pong ball in x and y, with x and y corresponding to the top left corner of the ball.
    private int x, y;

    // Radius of the ball.
    private final int radius;

    /**
     * Constructs a new pong ball with the given radius.
     *
     * @param radius The radius to construct the pong ball with.
     */
    public PongBall(final int radius) {
        this.radius = radius;
    }

    /**
     * Moves the ball according to its current velocity.
     *
     * @param secondsSinceLastTick The number of seconds elapsed since the last tick.
     */
    public void renderTick(double secondsSinceLastTick) {
        x += Math.round(runPerSecond * secondsSinceLastTick);
        y += Math.round(risePerSecond * secondsSinceLastTick);
    }

    /**
     * Gets the number of rises (vertical movement) per tick that the ball is moving.
     *
     * @return The number of rises per tick.
     */
    public double getRisePerSecond() {
        return risePerSecond;
    }

    /**
     * Gets the number of runs (horizontal movement) per tick that the ball is moving.
     *
     * @return The run of the ball.
     */
    public double getRunPerSecond() {
        return runPerSecond;
    }

    /**
     * Gets the ball's radius.
     *
     * @return The radius.
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Gets the total height of the pong ball.
     *
     * @return The height of the pong ball.
     */
    public int getHeight() {
        return radius * 2;
    }

    /**
     * Gets the width of the pong ball.
     *
     * @return The width of the pong ball.
     */
    public int getWidth() {
        return radius * 2;
    }

    /**
     * Gets the x position of the top left of the ball.
     *
     * @return The x position.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y position of the top left of the ball.
     *
     * @return The y position.
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the x coordinate of the ball at the given position.
     *
     * @param position The desired calculated position.
     * @return The x position of the ball.
     */
    public int getX(Side position) {
        return getX(getRadius(), getX(), position);
    }

    /**
     * Gets the y coordinate of the ball at the given position.
     *
     * @param position The desired calculated position.
     * @return The y position of the ball.
     */
    public int getY(Side position) {
        return getY(getRadius(), getY(), position);
    }

    /**
     * Sets the x-value for the pong ball manually.
     *
     * @param value    The new value for hte position.
     * @param position The position (left, right or center) to which the value is being applied.
     */
    @Override
    public void setX(int value, Side position) {
        switch (position) {
            case LEFT:
                setX(value);
                break;
            case RIGHT:
                setX(value - getWidth());
                break;
            case CENTER:
                setX(value - getRadius());
                break;
            default:
                throw new IllegalArgumentException("Position must be one of supported positions for this object.");
        }
    }

    /**
     * Sets hte x-value for the pong ball.
     *
     * @param value The x-value.
     */
    private void setX(int value) {
        this.x = value;
    }

    /**
     * Sets the y-position of the pong ball manually .
     *
     * @param value    The value to which the position should be set.
     * @param position The position (top, bottom or center) to which the value is being set to.
     */
    @Override
    public void setY(int value, Side position) {
        switch (position) {
            case TOP:
                setY(value);
                break;
            case BOTTOM:
                setY(value + getHeight());
                break;
            case CENTER:
                setY(value + getRadius());
                break;
            default:
                throw new IllegalArgumentException("Position must be one of supported positions for this object.");
        }
    }

    /**
     * Sets the y-value for the ball.
     *
     * @param value The new value.
     */
    private void setY(int value) {
        this.y = value;
    }

    /**
     * Gets the x coordinate of the ball at the given position.
     *
     * @param radius   The radius of the ball.
     * @param topLeftX The top left x coordinate of the ball.
     * @param position The desired calculated position.
     * @return The x position of the ball.
     */
    public static int getX(final int radius, final int topLeftX, Side position) {
        int posX;
        switch (position) {
            case LEFT:
                posX = topLeftX;
                break;
            case RIGHT:
                posX = topLeftX + 2 * radius;
                break;
            case CENTER:
                posX = topLeftX + radius;
                break;
            default:
                throw new IllegalArgumentException("Position must be one of supported positions for this object.");
        }
        return posX;
    }

    /**
     * Gets the y coordinate of the ball at the given position.
     *
     * @param radius   The radius of the ball.
     * @param topLeftY The top left y coordinate of the ball.
     * @param position The desired calculated position.
     * @return The y position of the ball.
     */
    public static int getY(final int radius, final int topLeftY, Side position) {
        int posY;
        switch (position) {
            case BOTTOM:
                posY = topLeftY - 2 * radius;
                break;
            case TOP:
                posY = topLeftY;
                break;
            case CENTER:
                posY = topLeftY - radius;
                break;
            default:
                throw new IllegalArgumentException("Position must be one of supported positions for this object.");
        }
        return posY;
    }

    /**
     * Sets the new velocity of the pong ball.
     *
     * @param risePerTick The amount to rise per tick. Positive means up, negative means down.
     * @param runPerTick  The amount to run (move horizontally) per tick. Positive means right, negative means left.
     */
    public void setVelocity(final double risePerTick, final double runPerTick) {
        this.risePerSecond = risePerTick;
        this.runPerSecond = runPerTick;
    }

    /**
     * Sets the velocity of the ball based off of the rebound angle.
     *
     * @param reboundAngle The rebound angle of the ball.
     * @param direction    The direction in which the ball should move. Positive for rightwards, negative for leftwards.
     */
    public void setVelocity(double reboundAngle, Side direction) {
        // For those who don't like math, skip looking at this.

        // Velocity (hypotenuse) * cos(reboundAngle) = velocity's x component.
        double vX = ((direction == Side.LEFT) ? -1 : 1) * VELOCITY * Math.cos(Math.toRadians(reboundAngle));
        // Velocity (hypotenuse) * sin(reboundAngle) = velocity's y component.
        double vY = VELOCITY * Math.sin(Math.toRadians(reboundAngle));

        // Set the velocity at the end.
        setVelocity(vY, vX);
    }
}
