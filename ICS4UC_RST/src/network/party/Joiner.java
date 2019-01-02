package network.party;

import network.Server;

import java.io.IOException;

/**
 * Runnable for letting other users join the party.
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
public class Joiner implements Runnable {
    private Server server;

    /**
     * Instantiates a new joiner object.
     *
     * @param server The server object which will be accepting the joining users.
     */
    public Joiner(Server server) {
        this.server = server;
    }

    /**
     * Runs the main action.
     */
    @Override
    public void run() {
        try {
            server.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
