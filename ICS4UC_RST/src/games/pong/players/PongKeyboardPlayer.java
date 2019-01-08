package games.pong.players;

import games.Game;
import games.KeyBinding;
import games.player.KeyboardPlayer;
import games.player.PongKeyBinding;
import games.pong.pieces.Side;
import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Class for representing and controlling a player playing pong with the keyboard (local player).
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
public class PongKeyboardPlayer extends KeyboardPlayer<PongKeyBinding> implements PongPlayer {
    private Side side;
    private int points;

    public PongKeyboardPlayer() {
    }

    @Override
    public void setSide(Side side) {
        this.side = side;
    }

    @Override
    public void addPoint() {
        points++;
    }

    @Override
    public int getPoints() {
        return this.points;
    }

    @Override
    public Side getSide() {
        return this.side;
    }

    @Override
    public void setOnPause(Consumer<PongPlayer> action) {

    }

    @Override
    public void gameUpdated(Game game) {

    }

    @Override
    public String getName() {
        return null;
    }

    // The listener for when the action changes.
    private BiConsumer<PongPlayer, Action> actionListener;

    /**
     * Sets a method to be called when the player's paddle's action should change.
     *
     * @param actionListener The listener to be called.
     */
    @Override
    public void setOnActionChanged(BiConsumer<PongPlayer, Action> actionListener) {
        this.actionListener = actionListener;
    }

    /**
     * Called when the player's action should change.
     *
     * @param newAction The new action that the player should take.
     */
    private void actionChanged(Action newAction) {
        if (actionListener != null) {
            actionListener.accept(this, newAction);
        }
    }

    // Last action that was put through.
    private Action lastAction;

    /**
     * Sets the keys that are currently being pressed down.
     *
     * @param keysDown The keys that are currently pressed down.
     */
    public void setKeysDown(List<KeyCode> keysDown) {
        Action newAction = Action.STOP;
        if (keysDown.size() > 0) {
            final HashMap<KeyCode, PongKeyBinding> bindings = getKeyBindings();
            List<KeyCode> goodKeys = keysDown.stream().filter(bindings.keySet()::contains).collect(Collectors.toList());

            if (goodKeys.size() > 0) {
                PongKeyBinding binding = bindings.get(goodKeys.get(goodKeys.size() - 1));
                switch (binding) {
                    case MOVE_DOWN:
                        newAction = Action.MOVE_DOWN;
                        break;
                    case MOVE_UP:
                        newAction = Action.MOVE_UP;
                        break;
                    default:
                        newAction = Action.STOP;
                        break;
                }
            }
        }
        if (newAction != lastAction) {
            actionChanged(newAction);
        }
        lastAction = newAction;
    }
}
