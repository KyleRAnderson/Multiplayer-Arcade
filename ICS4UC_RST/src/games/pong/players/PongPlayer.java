package games.pong.players;

import games.player.Player;
import games.pong.Pong;
import games.pong.pieces.Paddle;
import games.pong.pieces.Side;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author Kyle Anderson
 * ICS4U RST
 */
public interface PongPlayer extends Player {
    /**
     * Sets an action to be called when the player attempts to move his/her paddle up.\
     *
     * @param action The action to be called when the event occurs. The first argument accepts the pong player
     *               who is calling the method. The second indicates whether or not to move the paddle or to stop moving
     *               it.
     */
    void setOnPaddleUp(BiConsumer<PongPlayer, Boolean> action);

    /**
     * Sets an action to be called when the player attempts to move his/her paddle down.
     *
     * @param action The action to be called when the event occurs. The first argument accepts the pong player
     *      *               who is calling the method. The second indicates whether or not to move the paddle or to stop moving
     *      *               it.
     */
    void setOnPaddleDown(BiConsumer<PongPlayer, Boolean> action);

    /**
     * Sets an action to be called when the player attempts to pause the game.
     *
     * @param action The action function to be called when the event occurs.
     */
    void setOnPause(Consumer<PongPlayer> action);

    /**
     * Gets the side on which this pong player is.
     *
     * @return {@link games.pong.pieces.Side#LEFT} or {@link games.pong.pieces.Side#RIGHT}.
     */
    Side getSide();

    /**
     * Sets the start side for this pong player.
     *
     * @param side {@link games.pong.pieces.Side#LEFT} or {@link games.pong.pieces.Side#RIGHT}.
     */
    void setSide(Side side);

    /**
     * Adds a point to this player's score.
     */
    void addPoint();

    /**
     * Gets this player's number of points.
     *
     * @return The number of points for this player.
     */
    int getPoints();

    /**
     * Gets this player's paddle.
     *
     * @return The player's paddle.
     */
    Paddle getPaddle();

    /**
     * Sets this player's paddle.
     */
    void setPaddle(Paddle paddle);

    /**
     * Gets the game to which this player belongs.
     *
     * @return
     */
    Pong getGame();
}
