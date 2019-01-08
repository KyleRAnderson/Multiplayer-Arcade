package games.player;

import games.KeyBinding;
import javafx.scene.input.KeyCode;

import java.util.HashMap;

/**
 * Physical player of the game of pong.
 *
 * @author Kyle Anderson
 * @param <T> The type of key bindings that this player deals in.
 * ICS4U RST
 */
public abstract class KeyboardPlayer<T extends KeyBinding> implements Player {

    /**
     * Key bindings map for the keyboard player.
     */
    protected HashMap<KeyCode, T> keyBindings;

    /**
     * Gets this keyboard player's key bindings.
     *
     * @return The player's key bindings.
     */
    public HashMap<KeyCode, T> getKeyBindings() {
        return keyBindings;
    }

    /**
     * Sets the key bindings for this player.
     *
     * @param bindings The player's bindings.
     */
    public void setKeyBindings(HashMap<KeyCode, T> bindings) {
        this.keyBindings = bindings;
    }

}
