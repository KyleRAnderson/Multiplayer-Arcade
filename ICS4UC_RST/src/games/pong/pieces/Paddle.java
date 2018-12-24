package games.pong.pieces;

/**
 * Class for representing and simulating a pong paddle.
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
public class Paddle implements PongPiece {
    private int x, y;
    private final int width, height;

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
    public Paddle(final int x, final int y, final int width, final int height, Side side) {
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
    public Paddle(int width, int height, Side side) {
        this(0, 0, width, height, side);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }


    /**
     * Sets the x-value for the pong paddle manually.
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
    public void setY(int value, Side position) {
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
    public int getX() {
        return x;
    }

    /**
     * Gets the y-value position corresponding to the top of the paddle.
     *
     * @return The position's y-value component.
     */
    public int getY() {
        return y;
    }

    @Override
    public int getX(Side position) {
        int xValue;

        if (position == Side.RIGHT) {
            xValue = x + width;
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
    public int getY(Side position) {
        int yValue;

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
}
