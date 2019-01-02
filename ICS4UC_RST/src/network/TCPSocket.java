package network;

import java.io.*;
import java.net.Socket;

/**
 * @author s405751 (Nicolas Hawrysh)
 * Date: Dec 27, 2018
 * ICS4U
 * TCPSocket.java
 * Class for socket which can send/listen for data
 */

class TCPSocket {
    protected Socket stSocket;
    protected PrintWriter pwOut = null;
    protected OutputStream osSocketOutputStream = null;
    protected InputStreamReader isInputStreamReader = null;
    protected BufferedReader brBufferedReader = null;

    /**
     * Listens for data being sent to this client.
     *
     * @return The string data sent to the client.
     * @throws IOException Thrown when there is some sort of IO problem.
     */
    public String listenForData() throws IOException {
        String strData;

        // create variable for the sockets input stream
        InputStream isSocketInputStream = stSocket.getInputStream();
        isInputStreamReader = new InputStreamReader(isSocketInputStream);

        // create a new buffered reader of the socket input stream reader
        brBufferedReader = new BufferedReader(new InputStreamReader(isSocketInputStream));

        // get string received
        strData = brBufferedReader.readLine();

        // return data received
        return strData;
    }

    /**
     * Sends data to the clienet.
     *
     * @param data The string data to be set.
     * @throws IOException Thrown when there is some sort of IO problem.
     */
    public void send(String data) throws IOException {
        // create a new output stream
        osSocketOutputStream = stSocket.getOutputStream();

        // use PrintWriter to send a line of text to the outputStream
        pwOut = new PrintWriter(osSocketOutputStream, true);
        pwOut.println(data);
        pwOut.flush();
    }

    /**
     * Closes the socket in use and other objects attached to it if they are defined.
     *
     * @throws IOException Thrown when there is an IO problem.
     */
	public void close() throws IOException {
		if (pwOut != null) {
			pwOut.close();
		}

		if (osSocketOutputStream != null) {
			osSocketOutputStream.close();
		}

		if (isInputStreamReader != null) {
			isInputStreamReader.close();
		}

		if (brBufferedReader != null) {
			brBufferedReader.close();
		}

		stSocket.close();
	}

}
