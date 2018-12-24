package games.pong.pieces;

/**
 * @author Kyle Anderson
 */
public interface PongPiece {
    int getX(Side position);
    int getY(Side position);
    void setX(int value, Side position);
    void setY(int value, Side position);
    int getHeight();
    int getWidth();
}
