package games.pong.pieces;

/**
 * Class for representing and simulating a pong paddle.
 * @author Kyle Anderson
 * ICS4U RST
 */
public class Paddle implements PongPiece {
    private int x, y;
    private final int width, height;

    /**
     * Constructs a new Paddle with the given coordinates.
     * @param x The x position of the paddle to start at.
     * @param y The y position of the paddle to start at.
     * @param width The width of the paddle.
     * @param height The height of the paddle.
     */
    public Paddle(final int x, final int y, final int width, final int height) {
        setX(x);
        setY(y);
        this.height = height;
        this.width = width;
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
        } else {
            throw new IllegalArgumentException("position must be either Side.RIGHT or Side.LEFT.");
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
        } else {
            throw new IllegalArgumentException("position must be either Side.BOTTOM or Side.TOP.");
        }

        return yValue;
    }
}
