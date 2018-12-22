package games.pong.players;

import games.Game;
import games.player.NetworkPlayer;
import games.pong.pieces.Paddle;

import java.util.function.Consumer;

/**
 * @author Kyle Anderson
 * ICS4U RST
 */
public class PongNetworkPlayer extends NetworkPlayer implements PongPlayer {
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
    public int getSide() {
        return 0;
    }

    @Override
    public void setSide(int side) {

    }

    @Override
    public void addPoint() {

    }

    @Override
    public int getPoints() {
        return 0;
    }

    @Override
    public Paddle getPaddle() {
        return null;
    }

    @Override
    public void setPaddle(Paddle paddle) {

    }

    @Override
    public void gameUpdated(Game game) {

    }

    @Override
    public String getName() {
        return null;
    }
}
