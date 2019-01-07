package games.player;

import network.party.network.NetworkMessage;

import java.util.function.Consumer;

/**
 * Represents a player is who is playing with the current machine's local player over network.
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
public abstract class NetworkPlayer implements Player {
    protected Consumer<String> gameDataListener;

    /**
     * Should be called when a client sends this client data over the multiplayer network.
     *
     * @param data The data received.
     */
    public abstract void receiveData(NetworkMessage data);

    /**
     * Should be called when a host connected to this host is or has disconnected.
     */
    public abstract void hostDisconnecting();

    /**
     * Sets a method to be called when this game wants to send game data to the connected host.
     *
     * @param listener The listener to accept the game data to be sent.
     */
    public void setOnGameDataSend(Consumer<String> listener) {
        this.gameDataListener = listener;
    }
}
