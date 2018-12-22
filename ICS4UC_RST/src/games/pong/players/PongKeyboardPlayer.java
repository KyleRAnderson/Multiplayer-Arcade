package games.pong.players;

import games.Game;
import games.player.KeyboardPlayer;
import games.pong.Pong;
import games.pong.pieces.Paddle;
import games.pong.pieces.Side;

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
    public Side getSide() {
        return this.side;
    }

    @Override
    public void setOnPaddleUp(Consumer<PongPlayer> action) {

    }

    @Override
    public void setOnPaddleDown(Consumer<PongPlayer> action) {

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
}
