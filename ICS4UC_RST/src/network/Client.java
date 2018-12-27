/**
 * 
 */
package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author s405751 (Nicolas Hawrysh)
 * Date: Dec 24, 2018
 * ICS4U
 * Client.java
 * Class for network client to send/get data. Refer to Server.java for information on methods.
 */

public class Client {
	Socket stSocket;
	
	// method to connect to server
	public void connect(String IP, int port) throws UnknownHostException, IOException {
		stSocket = new Socket(IP, port);
	}
	
	public void send(String data) throws IOException {
		OutputStream osSocketOutputStream = stSocket.getOutputStream();
		PrintWriter pwOut = new PrintWriter(osSocketOutputStream, true);
		pwOut.println(data);
		
		pwOut.close();
	}
	
	public String listenForData() throws IOException {
		String strData = "";
		
		InputStream isSocketInputStream = stSocket.getInputStream();
		
		InputStreamReader isInputStreamReader = new InputStreamReader(isSocketInputStream);
		BufferedReader brBufferedReader = new BufferedReader(new InputStreamReader(isSocketInputStream));
        strData = brBufferedReader.readLine();
		
		isInputStreamReader.close();
		brBufferedReader.close();
		
		return strData;
	}
	
	public void close() throws IOException {
		stSocket.close();
	}
	
}
