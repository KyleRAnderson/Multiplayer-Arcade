package games.pong.pieces;

/**
 * @author Kyle Anderson
 */
public interface PongPiece {
    double getX(Side position);
    double getY(Side position);
    void setX(double value, Side position);
    void setY(double value, Side position);
    double getHeight();
    double getWidth();
}
