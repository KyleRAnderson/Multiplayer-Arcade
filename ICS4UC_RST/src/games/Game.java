package games;

import games.player.Player;
import javafx.scene.image.Image;

/**
 * @author Kyle Anderson
 */
public interface Game {
    /**
     * Begins a new instance of this game.
     */
    void start();

    /**
     * Determines if this game is currently in progress or not.
     *
     * @return True if this game is currently being played, false otherwise.
     */
    boolean isInProgress();

    /**
     * Ends the game.
     */
    void end();

    /**
     * Gets the score data from this game.
     * @return The Score data.
     */
   Score getScore();

    /**
     * Gets the cover image for the
     *
     * @return Gets the cover art for this game.
     */
    Image getCoverArt();

    String getName();

    /**
     * Determines if this game is a network game.
     * @return True if it's a network game, false otherwise.
     */
    boolean isNetworkGame();
}
