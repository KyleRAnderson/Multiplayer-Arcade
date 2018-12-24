package games.pong.players;

import games.Game;
import games.player.KeyboardPlayer;
import games.player.PongKeyBinding;
import games.pong.Pong;
import games.pong.pieces.Paddle;
import games.pong.pieces.Side;
import javafx.scene.input.KeyCode;

import java.util.function.Consumer;

/**
 * Class for representing and controlling a player playing pong with the keyboard (local player).
 * @author Kyle Anderson
 * ICS4U RST
 */
public class PongKeyboardPlayer extends KeyboardPlayer implements PongPlayer {
    private Side side;
    private int points;
    private Paddle paddle;

    private Pong game;

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
    public Paddle getPaddle() {
        return this.paddle;
    }

    @Override
    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    @Override
    public Pong getGame() {
        return this.game;
    }

    public void setGame(Pong game) {
        this.game = game;
    }

    @Override
    public Side getSide() {
        return this.side;
    }

    Consumer<PongPlayer> paddleUpListener;
    @Override
    public void setOnPaddleUp(Consumer<PongPlayer> action) {
        paddleUpListener = action;
    }

    Consumer<PongPlayer> paddleDownListener;
    @Override
    public void setOnPaddleDown(Consumer<PongPlayer> action) {
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
            paddleDownListener.accept(this);
        }
    }

    /**
     * Calls the listeners for the pong player's paddle moving up.
     */
    private void moveUp() {
        if (paddleUpListener != null) {
            paddleUpListener.accept(this);
        }
    }

    @Override
    public String getName() {
        return null;
    }

    /**
     * Called by the UI when a key is pressed to determine what action should ensue.
     * @param keyPressed The key that was pressed.
     * @return The binding that will be invoked on this key press.
     */
    public PongKeyBinding onKeyPressed(KeyCode keyPressed) {
        PongKeyBinding binding = (PongKeyBinding)getKeyBindings().get(keyPressed);
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

        return binding;
    }
}
