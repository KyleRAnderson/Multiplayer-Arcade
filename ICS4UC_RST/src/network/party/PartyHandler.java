package network.party;

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
            socket = new Server(port);
        }
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
}
