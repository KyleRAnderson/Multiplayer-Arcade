package games.player;

import games.KeyBinding;
import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kyle Anderson
 * ICS4U RST
 */
public abstract class KeyboardPlayer {

    /**
     * Key bindings map for the keyboard player.
     */
    protected HashMap<KeyCode, KeyBinding> keyBindings;

    /**
     * Gets this keyboard player's key bindings.
     * @return The player's key bindings.
     */
    public Map<KeyCode, KeyBinding> getKeyBindings() {
        return keyBindings;
    }

}
