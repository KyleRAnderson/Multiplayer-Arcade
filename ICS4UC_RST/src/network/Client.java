/**
 * 
 */
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
	
	// method to connect to server
	public void connect(String IP, int port) throws UnknownHostException, IOException {
		stSocket = new Socket(IP, port);
	}
}
