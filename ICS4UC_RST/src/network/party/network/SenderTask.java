package network.party.network;

import javafx.concurrent.Task;
import network.Server;
import network.TCPSocket;
import network.party.PartyHandler;

import java.util.Queue;

/**
 * Task for easily sending updates to the multiplayer network.
 * @author Kyle Anderson
 * ICS4U RST
 */
public class SenderTask extends Task<Void> {
    private TCPSocket socket;
    private Queue<String> queue;

    /**
     * Instantiates a new sender task for the provided socket and with the given queue.
     * @param socket The socket.
     * @param outgoing The queue.
     */
    public SenderTask(TCPSocket socket, Queue<String> outgoing) {
        this.socket = socket;
        queue = outgoing;
    }


    @Override
    protected Void call() throws Exception {
        System.out.println("Entered sender loop..."); // TODO remove
        while (PartyHandler.isConnected()) {
            String message = queue.poll();
            if (message != null) {
                System.out.println("Sending: " + message + " Time: " + System.currentTimeMillis()); // TODO Remove.
                socket.send(message);
            }
        }
        System.out.println("Exited sender loop..."); // TODO remove
        return null;
    }
}
