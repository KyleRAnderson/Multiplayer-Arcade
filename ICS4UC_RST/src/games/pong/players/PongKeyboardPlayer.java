package games.pong.players;

import games.Game;
import games.player.KeyboardPlayer;
import games.player.PongKeyBinding;
import games.pong.pieces.Side;
import javafx.scene.input.KeyCode;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Class for representing and controlling a player playing pong with the keyboard (local player).
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
public class PongKeyboardPlayer extends KeyboardPlayer implements PongPlayer {
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

    private BiConsumer<PongPlayer, Action> actionListener;

    /**
     * Sets a method to be called when the player's paddle's action should change.
     * @param actionListener The listener to be called.
     */
    @Override
    public void setOnActionChanged(BiConsumer<PongPlayer, Action> actionListener) {
        this.actionListener = actionListener;
    }

    /**
     * Called when the player's action should change.
     * @param newAction The new action that the player should take.
     */
    private void actionChanged(Action newAction) {
        if (actionListener != null) {
            actionListener.accept(this, newAction);
        }
    }

    /**
     * Sets the keys that are currently being pressed down.
     * @param keysDown The keys that are currently pressed down.
     */
    public void setKeysDown(List<KeyCode> keysDown) {
        Action newAction;
        if (keysDown.size() > 0) {
            PongKeyBinding binding = (PongKeyBinding) getKeyBindings().get(keysDown.get(keysDown.size() - 1));
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
        } else {
            newAction = Action.STOP;
        }
        actionChanged(newAction);
    }
}
