package games;

import games.player.Player;
import javafx.scene.image.Image;

/**
 * @author Kyle Anderson
 */
public abstract class Game {
    // True if the game is in progress, false otherwise.
    protected boolean inProgress;

    protected Player localPlayer;

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
     * @return True if this game is currently being played, false otherwise.
     */
    public boolean isInProgress() {
        return inProgress;
    }

    /**
     * Ends the game.
     */
    public abstract void end();

    /**
     * Gets the score data from this game.
     * @return The Score data.
     */
    public abstract Score getScore();

    /**
     * Gets the cover image for the
     *
     * @return Gets the cover art for this game.
     */
    public abstract Image getCoverArt();

    public String getName() {
        return gameName;
    }
}
