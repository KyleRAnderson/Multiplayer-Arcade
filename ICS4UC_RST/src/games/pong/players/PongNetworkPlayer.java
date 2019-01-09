package games.pong.players;

import games.Game;
import games.player.NetworkPlayer;
import games.pong.pieces.Side;
import network.party.network.NetworkMessage;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Represents a network player of the pong game.
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
public class PongNetworkPlayer extends NetworkPlayer implements PongPlayer {
    @Override
    public void setOnActionChanged(BiConsumer<PongPlayer, Action> listener) {

    }

    @Override
    public void setOnPause(Consumer<PongPlayer> action) {

    }

    @Override
    public Side getSide() {
        return null;
    }

    @Override
    public void setSide(Side side) {

    }

    @Override
    public void addPoint() {

    }

    @Override
    public int getPoints() {
        return 0;
    }

    @Override
    public void gameUpdated(Game game) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void receiveData(NetworkMessage data) {

    }

    @Override
    public void hostDisconnecting() {
    }
}
