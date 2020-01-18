package games.pong.pieces;

/**
 * Interface for representing pieces part of the pong game.
 *
 * @author Kyle Anderson
 * ICS4U RSt
 */
public interface PongPiece {
    double getX(Side position);

    double getY(Side position);

    void setX(double value, Side position);

    void setY(double value, Side position);

    double getHeight();

    double getWidth();
}
