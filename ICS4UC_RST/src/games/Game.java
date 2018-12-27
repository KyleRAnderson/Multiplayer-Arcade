package games;

import javafx.scene.Parent;
import javafx.scene.image.Image;

/**
 * @author Kyle Anderson
 */
public abstract class Game {
    /**
     * The name of this game, in plain english.
     */
    protected String gameName;

    /**
     * Begins a new instance of this game.
     */
    public abstract void start();

    /**
     * Determines if this game is currently in progress or not.
     *
     * @return
     */
    public abstract boolean isInProgress();

    /**
     * Ends the game.
     */
    public abstract void end();

    /**
     * Gets the score data from this game.
     *
     * @return The Score data.
     */
    public abstract Score getScore();

    /**
     * Gets the cover image for the
     *
     * @return
     */
    public abstract Image getCoverArt();

    public String getName() {
        return gameName;
    }

    /**
     * Gets the Parent for the game, which needs to be displayed to the user for it to work.
     *
     * @return The Parent window for this game.
     */
    public abstract Parent getWindow();
}
