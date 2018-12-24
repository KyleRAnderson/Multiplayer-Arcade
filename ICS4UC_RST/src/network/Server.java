/**
 * 
 */
package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author s405751
 *
 */
public class Server {
	
	ServerSocket ssListener;
	Socket stSocket;
	String strData = "";
	
	public void create() throws IOException {
		ssListener = new ServerSocket();
	}
	
	public void bind(int port) throws IOException {
		ssListener.bind(new InetSocketAddress("0.0.0.0", port));
	}
	
	public void accept() throws IOException {
		ssListener.accept();
	}
	
	public void listen() throws IOException {
		stSocket = ssListener.accept();
		
		InputStreamReader isInputStreamReader = new InputStreamReader(stSocket.getInputStream());
		BufferedReader brBufferedReader = new BufferedReader(isInputStreamReader);
		strData = brBufferedReader.readLine();
		
		System.out.println("sdf");
		
		isInputStreamReader.close();
		brBufferedReader.close();
	}
	
	public String getData() {
		return strData;
	}
	
	public void close() throws IOException {
		ssListener.close();
		stSocket.close();
	}
}
