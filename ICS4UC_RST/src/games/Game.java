package games;

import javafx.scene.image.Image;

/**
 * Enforces structure for games of all sort.
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
public interface Game {
    /**
     * Begins a new instance of this game.
     */
    void start();

    /**
     * Ends the game.
     */
    void end();

    /**
     * Gets the score data from this game.
     *
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
     *
     * @return True if it's a network game, false otherwise.
     */
    boolean isNetworkGame();
}
