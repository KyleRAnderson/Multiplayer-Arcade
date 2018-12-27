/**
 * 
 */
package network;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author s405751 (Nicolas Hawrysh)
 * Date: Dec 24, 2018
 * ICS4U
 * Server.java
 * Class for network server
 */
public class Server extends TCPSocket {
	
	// define data field for server
	ServerSocket ssServer;
	
	// constructor to create a new server on a port and set a blocking timeout to 0
	public Server(int port) throws IOException {
		ssServer = new ServerSocket(port);
		ssServer.setSoTimeout(0);
	}
	
	// method to accept a socket connection
	public void accept() throws IOException {
		stSocket = ssServer.accept();
	}
	
	// method to close the server
	public void closeServer() throws IOException {
		ssServer.close();
	}
}
