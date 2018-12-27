/**
 * 
 */
package network;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * @author s405751 (Nicolas Hawrysh)
 * Date: Dec 27, 2018
 * ICS4U
 * TCPSocket.java
 * Class for socket which can send/listen for data
 */

public class TCPSocket {
	Socket stSocket;

	// method to listen for data sent
	public String listenForData() throws IOException {
		String strData = "";

		// create variable for the sockets input stream
		InputStream isSocketInputStream = stSocket.getInputStream();
		InputStreamReader isInputStreamReader = new InputStreamReader(isSocketInputStream);

		// create a new buffered reader of the socket input stream reader
		BufferedReader brBufferedReader = new BufferedReader(new InputStreamReader(isSocketInputStream));

		// get string received
		strData = brBufferedReader.readLine();

		// close InputStream and Buffered reader
		isInputStreamReader.close();
		brBufferedReader.close();

		// return data received
		return strData;
	}

	// method to send data
	public void send(String data) throws IOException {
		// create a new output stream
		OutputStream osSocketOutputStream = stSocket.getOutputStream();

		// use PrintWriter to send a line of text to the outputStream
		PrintWriter pwOut = new PrintWriter(osSocketOutputStream, true);
		pwOut.println(data);

		// close printWriter
		pwOut.close();
	}

	// method to close the socket
	public void close() throws IOException {
		stSocket.close();
	}
}
