package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
     * Method to close the server
     *
     * @throws IOException Thrown when there is some sort of IO issue.
     */
    public void closeServer() throws IOException {
        ssServer.close();
    }

    /**
     * Closes the server.
     *
     * @throws IOException Thrown if there is some sort of error.
     */
    @Override
    public void close() throws IOException {
        super.close();
        closeServer();
    }
}
