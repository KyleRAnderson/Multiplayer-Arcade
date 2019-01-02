package games;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.text.Text;

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
     * Gets the text object to be displayed for this game on its menu.
     *
     * @return The text object.
     */
    Text getTextDisplay();

    /**
     * Determines if this game is a network game.
     *
     * @return True if it's a network game, false otherwise.
     */
    boolean isNetworkGame();

    /**
     * Creates a new game to be played.
     *
     * @param <T> The type of the game.
     * @return A new game instance.
     */
    <T extends Game> T createNew();

    /**
     * Gets the displaying window for the game.
     *
     * @return The display window for the game.
     */
    Parent getWindow();
}
