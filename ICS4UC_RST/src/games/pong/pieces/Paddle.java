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
            yValue = y - height;
        } else if (position == Side.TOP) {
            yValue = y;
        } else if (position == Side.CENTER) {
            yValue = y - height / 2;
        } else {
            throw new IllegalArgumentException("position must be one of supported sides for this object.");
        }

        return yValue;
    }
}
