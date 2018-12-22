package games;

import javafx.scene.input.KeyCode;

/**
 * Interface for representing key bindings. Going to be used mostly in enums.
 * @author Kyle Anderson
 * ICS4U RST
 */
public interface KeyBinding {
    /**
     * Gets the key code for the given binding.
     * @return The key code.
     */
    KeyCode getKey();

    /**
     * Gets the string name for the binding, which should be a nice name
     * displayable to the user.
     * @return The string name for the key binding.
     */
    String getName();


}
