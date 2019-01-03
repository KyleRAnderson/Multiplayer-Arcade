package menu;

import javafx.concurrent.Task;
import network.party.PartyHandler;

/**
 * Task for connecting to the destined user.
 * @author Kyle Anderson
 * ICS4U RST
 */
public class ConnectTask extends Task<Boolean> {
    private final String ip;
    private final int port;

    /**
     * Initializes a new ConnectTask destined to connect to the given IP and port.
     * @param ip The ip address to connect to.
     * @param port The port over which the connection should be made.
     */
    public ConnectTask(final String ip, final int port) {
        this.ip = ip;
        this.port = port;
    }


    /**
     * Calls for the execution of this task.
     * @return The result of the connection attempt.
     */
    @Override
    protected Boolean call() {
        return PartyHandler.connect(ip, port);
    }
}
