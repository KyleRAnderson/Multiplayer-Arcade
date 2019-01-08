package games.pong.players;

import games.Game;
import games.player.KeyboardPlayer;
import games.player.PongKeyBinding;
import games.pong.pieces.Side;
import javafx.scene.input.KeyCode;

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

    private BiConsumer<PongPlayer, Boolean> paddleUpListener;

    @Override
    public void setOnPaddleUp(BiConsumer<PongPlayer, Boolean> action) {
        paddleUpListener = action;
    }

    private BiConsumer<PongPlayer, Boolean> paddleDownListener;

    @Override
    public void setOnPaddleDown(BiConsumer<PongPlayer, Boolean> action) {
        paddleDownListener = action;
    }

    @Override
    public void setOnPause(Consumer<PongPlayer> action) {

    }

    @Override
    public void gameUpdated(Game game) {

    }

    /**
     * Calls the listeners for the pong player's paddle moving down.
     */
    private void moveDown() {
        if (paddleDownListener != null) {
            paddleDownListener.accept(this, true);
        }
    }

    /**
     * Calls the listeners for the pong player's paddle moving up.
     */
    private void moveUp() {
        if (paddleUpListener != null) {
            paddleUpListener.accept(this, true);
        }
    }

    /**
     * Calls the listeners for the pong player's cancel paddle moving down.
     */
    private void cancelMoveDown() {
        if (paddleDownListener != null) {
            paddleDownListener.accept(this, false);
        }
    }

    /**
     * Calls the listeners for the pong player's cancel paddle moving up.
     */
    private void cancelMoveUp() {
        if (paddleUpListener != null) {
            paddleUpListener.accept(this, false);
        }
    }

    @Override
    public String getName() {
        return null;
    }

    /**
     * Called by the UI when a key is pressed to determine what action should ensue.
     *
     * @param keyPressed The key that was pressed.
     * @return The binding that will be invoked on this key press.
     */
    public void onKeyPressed(KeyCode keyPressed) {
        PongKeyBinding binding = (PongKeyBinding) getKeyBindings().get(keyPressed);
        if (binding != null) {
            switch (binding) {
                case MOVE_DOWN:
                    moveDown();
                    break;
                case MOVE_UP:
                    moveUp();
                    break;
                default:
                    break;
            }
        }

    }

    public void onKeyReleased(KeyCode keyReleased) {
        PongKeyBinding binding = (PongKeyBinding) getKeyBindings().get(keyReleased);
        if (binding != null) {
            switch (binding) {
                case MOVE_DOWN:
                    cancelMoveDown();
                    break;
                case MOVE_UP:
                    cancelMoveUp();
                    break;
                default:
                    break;
            }
        }

    }
}
