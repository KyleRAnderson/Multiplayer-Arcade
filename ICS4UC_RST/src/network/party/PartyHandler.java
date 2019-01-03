package network.party;

import advancedIO.AdvancedIO;
import network.Client;
import network.Server;
import network.TCPSocket;

import java.io.IOException;

/**
 * Class for handling a party with another user.
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
public class PartyHandler {
    private static PartyRole role;
    private static TCPSocket socket;
    private static Thread joinThread;

    /**
     * Begins a party session with the user at the given IP address.
     *
     * @param ip   The ip address.
     * @param port The port on which the connection should be made.
     * @return True if the connection attempt is successful, false otherwise.
     */
    public static boolean connect(final String ip, final int port) {
        boolean didConnect = false;
        if (!isConnected()) {
            socket = new Client();
            try {
                ((Client) socket).connect(ip, port);
                didConnect = true;
            } catch (IOException e) {
            }
        }
        if (didConnect) {
            role = PartyRole.CLIENT;
        }

        return didConnect;
    }

    /**
     * Begins to host a party on this user's machine.
     *
     * @param port The port on which the hosting should be done.
     * @throws IOException if creating the server fails.
     */
    public static void host(final int port) throws IOException {
        if (!isConnected()) {
            role = PartyRole.SERVER;
            socket = new Server(port);
            allowJoining();
        }
    }

    /**
     * Allows incoming requests to join the party, processing them in a separate thread.
     */
    private static void allowJoining() {
        // Only do stuff if we're a server.
        if (getRole() == PartyRole.SERVER) {
            joinThread = new Thread(new Joiner(getServer()));
            joinThread.start();
        }
    }

    /**
     * Determines if the joiner thread is still waiting for the other player to join.
     *
     * @return True if the thread is still waiting on the other player, false otherwise.
     */
    public static boolean isStillWaitingForOtherPlayer() {
        try {
            joinThread.join(10);
        } catch (InterruptedException ignored) {
        }
        return joinThread.getState() != Thread.State.TERMINATED;
    }

    /**
     * Disconnects from the party.
     *
     * @throws IOException if there is an error.
     */
    public static void disconnect() throws IOException {
        // Only try to disconnect if connected.
        if (isConnected()) {
            socket.close();
        }
        role = null;
    }

    /**
     * Gets the role of this user in the party, either client or server.
     *
     * @return The role of the local user.
     */
    public static PartyRole getRole() {
        return role;
    }

    /**
     * Determines if there is currently a party in session.
     *
     * @return True if there is a party in session, false otherwise.
     */
    public static boolean isConnected() {
        return role != null;
    }

    /**
     * Gets the client object for the client local party member, if the local user is a client.
     *
     * @return The Client object.,
     */
    public static Client getClient() {
        return (getRole() == PartyRole.CLIENT && socket instanceof Client) ? (Client) socket : null;
    }

    /**
     * Gets the server object for the server local party member, if the local user is a server.
     *
     * @return The Server object.
     */
    public static Server getServer() {
        return (getRole() == PartyRole.SERVER && socket instanceof Server) ? (Server) socket : null;
    }

    /**
     * Method for allowing this module to be tested and tried out without the rest of the application.
     *
     * @param args Command-line arguments.
     * @throws IOException if there is some sort of IOException.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        AdvancedIO.print("Arguments: [client/server (c for client, s for server)] [port] [ip address (if client)]");
        if (args.length == 0) {
            args = AdvancedIO.readStringArray("Enter arguments separated by spaces.\n-->", 2, 3);
        }

        if (!(2 <= args.length && args.length <= 3)) {
            AdvancedIO.print("Invalid argument length.");
        } else {
            args[0] = args[0].toLowerCase();
            int port = -1;
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {
            }

            if (port >= 0) {
                switch (args[0]) {
                    case "c":
                        if (args.length == 3) {
                            String ip = args[2];
                            connect(ip, port);
                        } else {
                            AdvancedIO.print("Invalid number of arguments. Need all 3 arguments for client.");
                        }
                        break;
                    case "s":
                        host(port);

                        boolean waiting;
                        do {
                            waiting = isStillWaitingForOtherPlayer();
                            if (waiting) {
                                AdvancedIO.print("Waiting for other user to join...");
                            }
                            Thread.sleep(1000);
                        } while (waiting);

                        break;
                    default:
                        AdvancedIO.print("Invalid selection of client or server. Enter \"c\" for client and \"s\" for server.");
                        break;
                }
            } else {
                AdvancedIO.print("Second argument must be an integer corresponding to the port.");
            }
        }
    }
}
