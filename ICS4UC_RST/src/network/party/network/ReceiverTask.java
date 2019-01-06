package network.party.network;

import javafx.concurrent.Task;
import network.Server;
import network.TCPSocket;
import network.party.PartyHandler;

import java.util.Queue;

/**
 * @author Kyle Anderson
 */
public class ReceiverTask extends Task<ReceivedDataEvent> {

    private final TCPSocket socket;
    private final Queue<String> queue;

    /**
     * Constructs a new ReceiverTask for creating a thread to monitor the multiplayer network.
     * @param incoming The queue to convey incoming messages.
     * @param socket The socket on which data will be set.
     */
    public ReceiverTask(TCPSocket socket, Queue<String> incoming) {
        this.socket = socket;
        queue = incoming;
    }

    @Override
    protected ReceivedDataEvent call() throws Exception {
        System.out.println("Entered receiver loop..."); // TODO remove.
        while (PartyHandler.isConnected()) {
            System.out.println("Listening for data." + System.currentTimeMillis()); // TODO remove
            String receivedMessage = socket.listenForData();
            System.out.println("Received: " + receivedMessage); // TODO Remove
            queue.add(receivedMessage);
            updateValue(ReceivedDataEvent.RECEIVED_DATA); // Update the value, triggering possible listeners.
        }
        System.out.println("Exited receiver loop..."); // TODO remove
        return ReceivedDataEvent.DISCONNECTED;
    }
}
