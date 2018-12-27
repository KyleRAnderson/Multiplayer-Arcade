package games.player;

import games.KeyBinding;
import javafx.scene.input.KeyCode;

/**
 * The default key bindings for the game of pong, which can be reset if necessary.
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
public enum PongKeyBinding implements KeyBinding {
    MOVE_UP("Paddle Move Up", KeyCode.UP), MOVE_DOWN("Paddle Move Down", KeyCode.DOWN);

    private KeyCode key;
    private final String name;

    PongKeyBinding(String name, KeyCode key) {
        this.name = name;
        this.key = key;
    }

    @Override
    public KeyCode getKey() {
        return this.key;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
