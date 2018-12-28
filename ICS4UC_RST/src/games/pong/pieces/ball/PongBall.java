package games.pong.pieces.ball;

import games.pong.Pong;
import games.pong.pieces.PongPiece;
import games.pong.pieces.Side;

/**
 * Class for representing and mimicking the behaviour of a pong ball.
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
public class PongBall implements PongPiece {
    /* The velocities of the pong ball, in rise and runs per tick.
     * A positive rise means going up, a positive run means going left, opposite for negative rises and runs. */
    private double risePerSecond, runPerSecond;
    // Position of the pong ball in x and y, with x and y corresponding to the top left corner of the ball.
    private double x, y;

    // Radius of the ball.
    private final double radius;

    /**
     * Constructs a new pong ball with the given radius.
     *
     * @param radius The radius to construct the pong ball with.
     */
    public PongBall(final double radius) {
        this.radius = radius;
    }

    /**
     * Moves the ball according to its current velocity.
     *
     * @param millisSinceLastTick The number of milliseconds elapsed since the last tick.
     */
    public void renderTick(long millisSinceLastTick) {
        x += runPerSecond / 1000 * millisSinceLastTick;
        y += risePerSecond / 1000 * millisSinceLastTick;
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
     * Determines the velocity of the ball.
     *
     * @return The velocity of the ball, in units/second.
     */
    public double getVelocity() {
        return Math.sqrt(Math.pow(getRisePerSecond(), 2) + Math.pow(getRunPerSecond(), 2));
    }

    /**
     * Gets the ball's radius.
     *
     * @return The radius.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Gets the total height of the pong ball.
     *
     * @return The height of the pong ball.
     */
    public double getHeight() {
        return radius * 2;
    }

    /**
     * Gets the width of the pong ball.
     *
     * @return The width of the pong ball.
     */
    public double getWidth() {
        return radius * 2;
    }

    /**
     * Gets the x position of the top left of the ball.
     *
     * @return The x position.
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the y position of the top left of the ball.
     *
     * @return The y position.
     */
    public double getY() {
        return y;
    }

    /**
     * Gets the x coordinate of the ball at the given position.
     *
     * @param position The desired calculated position.
     * @return The x position of the ball.
     */
    public double getX(Side position) {
        return getX(getRadius(), getX(), position);
    }

    /**
     * Gets the y coordinate of the ball at the given position.
     *
     * @param position The desired calculated position.
     * @return The y position of the ball.
     */
    public double getY(Side position) {
        return getY(getRadius(), getY(), position);
    }

    /**
     * Sets the x-value for the pong ball manually.
     *
     * @param value    The new value for hte position.
     * @param position The position (left, right or center) to which the value is being applied.
     */
    @Override
    public void setX(double value, Side position) {
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
    private void setX(double value) {
        this.x = value;
    }

    /**
     * Sets the y-position of the pong ball manually .
     *
     * @param value    The value to which the position should be set.
     * @param position The position (top, bottom or center) to which the value is being set to.
     */
    @Override
    public void setY(double value, Side position) {
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
    private void setY(double value) {
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
    public static double getX(final double radius, final double topLeftX, Side position) {
        double posX;
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
    public static double getY(final double radius, final double topLeftY, Side position) {
        double posY;
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
        double vX = ((direction == Side.LEFT) ? -1 : 1) * Pong.PONG_BALL_VELOCITY * Math.cos(Math.toRadians(reboundAngle));
        // Velocity (hypotenuse) * sin(reboundAngle) = velocity's y component.
        double vY = Pong.PONG_BALL_VELOCITY * Math.sin(Math.toRadians(reboundAngle));

        // Set the velocity at the end.
        setVelocity(vY, vX);
    }
}
