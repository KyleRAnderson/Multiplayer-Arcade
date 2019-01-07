package network.party.network;

import javafx.concurrent.Task;
import network.TCPSocket;
import network.party.PartyHandler;

import java.util.Queue;

/**
 * Task for easily sending updates to the multiplayer network.
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
public class SenderTask extends Task<Void> {
    private final TCPSocket socket;
    private final Queue<String> queue;

    /**
     * Instantiates a new sender task for the provided socket and with the given queue.
     *
     * @param socket   The socket.
     * @param outgoing The queue.
     */
    public SenderTask(TCPSocket socket, Queue<String> outgoing) {
        this.socket = socket;
        queue = outgoing;
    }


    @Override
    protected Void call() throws Exception {
        while (PartyHandler.isConnected()) {
            String message = queue.poll();
            if (message != null) {
                socket.send(message);
            }
        }
        return null;
    }
}
