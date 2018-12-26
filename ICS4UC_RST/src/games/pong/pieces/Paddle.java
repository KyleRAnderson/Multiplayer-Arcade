package games.pong.pieces;

/**
 * Class for representing and simulating a pong paddle.
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
public class Paddle implements PongPiece {
    private double x, y;
    private final double width, height;
    // Current velocities of the paddle.
    private double velX = 0, velY = 0;

    private Side side;

    /**
     * Constructs a new Paddle with the given coordinates.
     *
     * @param x      The x position of the paddle to start at.
     * @param y      The y position of the paddle to start at.
     * @param width  The width of the paddle.
     * @param height The height of the paddle.
     * @param side   The start side (left or right) of the paddle.
     */
    public Paddle(final double x, final double y, final double width, final double height, Side side) {
        setX(x);
        setY(y);
        this.height = height;
        this.width = width;
        this.side = side;
    }

    /**
     * Constructs a new Paddle with the given coordinates.
     *
     * @param width  The width of the paddle.
     * @param height The height of the paddle.
     * @param side   The start side (left or right) of the paddle.
     */
    public Paddle(double width, double height, Side side) {
        this(0, 0, width, height, side);
    }

    /**
     * Gets the paddle's width.
     * @return The width of the paddle in units.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Gets the paddle's height.
     * @return The height of the paddle in units.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Gets the side on which the paddle is.
     * @return The side on which the paddle is.
     */
    public Side getSide() {
        return side;
    }

    /**
     * Sets the side on which the paddle is.
     * @param side The side to which the paddle belongs.
     */
    public void setSide(Side side) {
        this.side = side;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }


    /**
     * Sets the x-value for the pong paddle manually.
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
                setX(value - getWidth() / 2);
                break;
            default:
                throw new IllegalArgumentException("Position must be one of supported positions for this object.");
        }
    }

    /**
     * Sets the y-position of the pong paddle manually .
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
                setY(value + getHeight() / 2);
                break;
            default:
                throw new IllegalArgumentException("Position must be one of supported positions for this object.");
        }
    }

    /**
     * Gets the x-value position corresponding to the left of the paddle.
     *
     * @return The position's x-value component.
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the y-value position corresponding to the top of the paddle.
     *
     * @return The position's y-value component.
     */
    public double getY() {
        return y;
    }

    @Override
    public double getX(Side position) {
        double xValue;

        if (position == Side.RIGHT) {
            xValue = x + getWidth();
        } else if (position == Side.LEFT) {
            xValue = x;
        } else if (position == Side.CENTER) {
            xValue = x + width / 2;
        } else {
            throw new IllegalArgumentException("position must be one of supported sides for this object.");
        }

        return xValue;
    }

    @Override
    public double getY(Side position) {
        double yValue;

        if (position == Side.BOTTOM) {
            yValue = y - getHeight();
        } else if (position == Side.TOP) {
            yValue = y;
        } else if (position == Side.CENTER) {
            yValue = y - getHeight() / 2;
        } else {
            throw new IllegalArgumentException("position must be one of supported sides for this object.");
        }

        return yValue;
    }

    /**
     * Gets the current horizontal velocity of the paddle in units/second.
     * @return The current velocity of the paddle.
     */
    public double getVelX() {
        return velX;
    }

    /**
     * Sets the current horizontal velocity of the paddle in units/second.
     * @param velX The current velocity of the paddle.
     */
    public void setVelX(double velX) {
        this.velX = velX;
    }

    /**
     * Gets the current vertical velocity of the paddle in units/second.
     * @return The current velocity of the paddle.
     */
    public double getVelY() {
        return velY;
    }

    /**
     * Sets the current vertical velocity of the paddle in units/second.
     * @param velY The current velocity of the paddle.
     */
    public void setVelY(double velY) {
        this.velY = velY;
    }
}
