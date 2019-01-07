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
     * Method to accept a socket connection
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
     * Method to get the host IP
     *
     * @return the local IP address
     * @throws UnknownHostException if there is no host
     */
    public String getDefaultHostIP() throws UnknownHostException {   	
		String strIP = InetAddress.getLocalHost().getHostAddress().toString();
    	
    	return strIP;
    }
    
    
    /**
     * Method to get the client's IP
     *
     * @return the clients IP as string
     */
    public String getClientIP() {
    	return stSocket.getInetAddress().getHostAddress();
    }
}
