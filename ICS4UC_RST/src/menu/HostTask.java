package menu;

import javafx.concurrent.Task;
import network.party.PartyHandler;

/**
 * Task for handling hosting a user.
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
public class HostTask extends Task {
    final int port;

    /**
     * Instantiates a host taks with the given port.
     *
     * @param port The port on which to host.
     */
    public HostTask(final int port) {
        this.port = port;
    }

    @Override
    protected Object call() throws Exception {
        PartyHandler.host(port);
        return null;
    }
}
