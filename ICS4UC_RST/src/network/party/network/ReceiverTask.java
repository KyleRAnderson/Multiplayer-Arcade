package network.party.network;

import javafx.application.Platform;
import javafx.concurrent.Task;
import network.TCPSocket;
import network.party.PartyHandler;
import sun.nio.ch.Net;

import java.util.Queue;
import java.util.function.Consumer;

/**
 * @author Kyle Anderson
 */
public class ReceiverTask extends Task<ReceivedDataEvent> {

    private final TCPSocket socket;
    private final Queue<NetworkMessage> queue;
    private Consumer<ReceivedDataEvent> listener;

    public void addListener(Consumer<ReceivedDataEvent> listener) {
        this.listener = listener;
    }

    /**
     * Constructs a new ReceiverTask for creating a thread to monitor the multiplayer network.
     *
     * @param incoming The queue to convey incoming messages.
     * @param socket   The socket on which data will be set.
     */
    public ReceiverTask(TCPSocket socket, Queue<NetworkMessage> incoming) {
        this.socket = socket;
        queue = incoming;
    }

    @Override
    protected ReceivedDataEvent call() throws Exception {
        while (PartyHandler.isConnected()) {
            String receivedMessage = socket.listenForData();
            queue.add(NetworkMessage.fromJson(receivedMessage));
            ReceivedDataEvent event = ReceivedDataEvent.RECEIVED_DATA;
            // Notify of the received value.
            if (listener != null) {
                Platform.runLater(() -> listener.accept(event));
            }
        }
        return ReceivedDataEvent.DISCONNECTED;
    }
}
