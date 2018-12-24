package games.player;

import games.KeyBinding;
import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kyle Anderson
 * ICS4U RST
 */
public abstract class KeyboardPlayer implements Player {

    /**
     * Key bindings map for the keyboard player.
     */
    protected HashMap<KeyCode, ? extends KeyBinding> keyBindings;

    /**
     * Gets this keyboard player's key bindings.
     * @return The player's key bindings.
     */
    public HashMap<KeyCode, ? extends KeyBinding> getKeyBindings() {
        return keyBindings;
    }

    /**
     * Sets the key bindings for this player.
     * @param bindings The player's bindings.
     */
    public void setKeyBindings(HashMap<KeyCode, ? extends KeyBinding> bindings) {
        this.keyBindings = bindings;
    }

}
