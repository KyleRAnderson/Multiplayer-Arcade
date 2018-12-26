/**
 * 
 */
package network;

import java.io.BufferedReader;
import simpleIO.Console;
import simpleIO.Dialog;

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
	
	public void create(int port) throws IOException {
		ssListener = new ServerSocket(port);
		ssListener.setSoTimeout(0);
	}
	
	public void accept() throws IOException {
		stSocket = ssListener.accept();
	}
	
	public String listenForData() throws IOException {
		InputStreamReader isInputStreamReader = new InputStreamReader(stSocket.getInputStream());
		BufferedReader brBufferedReader = new BufferedReader(isInputStreamReader);
		String strData = brBufferedReader.readLine();
		
		isInputStreamReader.close();
		brBufferedReader.close();
		
		return strData;
	}
	
	public void close() throws IOException {
		ssListener.close();
		stSocket.close();
	}
}
