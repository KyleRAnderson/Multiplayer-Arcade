package network.party.network;

import javafx.concurrent.Task;
import network.TCPSocket;

import java.io.IOException;
import java.util.Queue;

/**
 * Task for easily sending updates to the multiplayer network.
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
public class SenderTask extends Task<Void> {
    private final TCPSocket socket;
    private final Queue<NetworkMessage> queue;

    /**
     * Instantiates a new sender task for the provided socket and with the given queue.
     *
     * @param socket   The socket.
     * @param outgoing The queue.
     */
    public SenderTask(TCPSocket socket, Queue<NetworkMessage> outgoing) {
        this.socket = socket;
        queue = outgoing;
    }


    @Override
    protected Void call() {
        boolean exit = false;
        while (!exit) {
            NetworkMessage message = queue.poll();
            if (message != null) {
                exit = message.getHostStatus() == HostStatus.DISCONNECTING;
                try {
                    socket.send(message.toJsonString());
                } catch (IOException e) {
                    exit = true;
                }
            }
        }
        return null;
    }
}
