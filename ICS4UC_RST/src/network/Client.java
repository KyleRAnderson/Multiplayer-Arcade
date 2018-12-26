/**
 * 
 */
package network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author s405751
 *
 */
public class Client {
	Socket stSocket;
	
	public void connect(String IP, int port) throws UnknownHostException, IOException {
		stSocket = new Socket(IP, port);
	}
	
	public void send(String data) throws IOException {
		PrintWriter pwOut = new PrintWriter(stSocket.getOutputStream());
		pwOut.println(data);
		
		pwOut.close();
	}
	
	public void close() throws IOException {
		stSocket.close();
	}
	
}
