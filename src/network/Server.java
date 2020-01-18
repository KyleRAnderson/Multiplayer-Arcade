package network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

/**
 * @author s405751 (Nicolas Hawrysh)
 * Date: Dec 24, 2018
 * ICS4U
 * Server.java
 * Class for network server
 */
public class Server extends TCPSocket {

    // define data field for server
    private ServerSocket ssServer;

    /**
     * Instantiates a new server on the given port and set a blocking timeout to 0.
     *
     * @param port The port to be used in the connection.
     * @throws IOException Thrown when there is some sort of IO problem.
     */
    public Server(int port) throws IOException {
        ssServer = new ServerSocket(port);
        ssServer.setSoTimeout(0);
    }

    /**
     * Method to accept a socket connection. Notice - Blocks the running of the rest of the application until
     * another machine connects.
     *
     * @throws IOException Thrown when there is some sort of IO issue.
     */
    public void accept() throws IOException {
        stSocket = ssServer.accept();
    }

    /**
     * Closes the server.
     *
     * @throws IOException Thrown if there is some sort of error.
     */
    @Override
    public void close() throws IOException {
        super.close();
        ssServer.close();
    }

    /**
     * Method to get the host IP
     *
     * @return the local IP address
     * @throws UnknownHostException if there is no host
     */
    public static String getDefaultHostIP() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }
}
