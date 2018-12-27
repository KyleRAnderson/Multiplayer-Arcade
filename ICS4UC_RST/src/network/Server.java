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
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author s405751 (Nicolas Hawrysh)
 * Date: Dec 24, 2018
 * ICS4U
 * Server.java
 * Class for network server to send/get data. NOTE: several exceptions thrown for the server and client.
 */
public class Server {
	
	// define data fields for serverSocket and socket
	ServerSocket ssServer;
	Socket stSocket;
	
	// constructor to create a new server on a port and set a blocking timeout to 0
	public Server(int port) throws IOException {
		ssServer = new ServerSocket(port);
		ssServer.setSoTimeout(0);
	}
	
	// method to accept a socket connection
	public void accept() throws IOException {
		stSocket = ssServer.accept();
	}
	
	// method to listen for data sent from client
	public String listenForData() throws IOException {
		String strData = "";
		
		// create variable for the sockets input stream
		InputStream isSocketInputStream = stSocket.getInputStream();
		
		InputStreamReader isInputStreamReader = new InputStreamReader(isSocketInputStream);
		
		// create a new buffered reader of the socket input stream reader
		BufferedReader brBufferedReader = new BufferedReader(new InputStreamReader(isSocketInputStream));
		
		// get string received from client
        strData = brBufferedReader.readLine();
		
        // close InputStream and Buffered reader
		isInputStreamReader.close();
		brBufferedReader.close();
		
		// return data received
		return strData;
	}
	
	// method to send data to client
	public void send(String data) throws IOException {
		// create a new output stream
		OutputStream osSocketOutputStream = stSocket.getOutputStream();
		
		// use PrintWriter to send a line of text to the outputStream (client)
		PrintWriter pwOut = new PrintWriter(osSocketOutputStream, true);
		pwOut.println(data);
		
		// close printWriter
		pwOut.close();
	}
	
	// method to close the socket
	public void close() throws IOException {
		stSocket.close();
	}
	
	// method to close the server
	public void closeServer() throws IOException {
		ssServer.close();
	}
}
