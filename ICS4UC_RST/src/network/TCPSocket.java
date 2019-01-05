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

public class TCPSocket {
    public static final int DEFAULT_PORT = 3001;

    protected Socket stSocket;

    /**
     * Determines if this TCP socket is connected.
     * @return True if connected, false otherwise.
     */
    public boolean isConnected() {
        return stSocket != null && stSocket.isConnected();
    }

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

    /**
     * Sends data to the client.
     *
     * @param data The string data to be set.
     * @throws IOException Thrown when there is some sort of IO problem.
     */
    public void send(String data) throws IOException {
        // create a new output stream
        OutputStream osSocketOutputStream = stSocket.getOutputStream();

        // use PrintWriter to send a line of text to the outputStream
        PrintWriter pwOut = new PrintWriter(osSocketOutputStream, true);
        pwOut.println(data);

        // close printWriter
        pwOut.close();
    }

    /**
     * Closes the socket in use.
     *
     * @throws IOException Thrown when there is an IO problem.
     */
    public void close() throws IOException {
        stSocket.close();
    }
}
