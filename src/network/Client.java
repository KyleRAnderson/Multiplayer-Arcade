package network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author s405751 (Nicolas Hawrysh)
 * Date: Dec 24, 2018
 * ICS4U
 * Client.java
 * Class for network client
 */
public class Client extends TCPSocket {
    /**
     * Method for connecting to a server.
     *
     * @param IP   The IP address of the server host.
     * @param port The port to use during the connection.
     * @throws UnknownHostException Thrown when the host address is not found or is malformed.
     * @throws IOException          Thrown when there is some sort of Input-Output issue.
     */
    public void connect(String IP, int port) throws UnknownHostException, IOException {
        stSocket = new Socket(IP, port);
    }
}
