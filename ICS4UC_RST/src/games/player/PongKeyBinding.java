package games.player;

import games.KeyBinding;

/**
 * The default key bindings for the game of pong, which can be reset if necessary.
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
public enum PongKeyBinding implements KeyBinding {
    MOVE_UP("Paddle Move Up"), MOVE_DOWN("Paddle Move Down");

    private final String name;

    PongKeyBinding(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
