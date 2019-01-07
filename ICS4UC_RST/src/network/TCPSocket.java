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

    private InputStreamReader isInputStreamReader;
    private BufferedReader brBufferedReader;
    private PrintWriter pwOut;

    /**
     * Determines if this TCP socket is connected.
     * @return True if connected, false otherwise.
     */
    public boolean isConnected() {
        return stSocket != null && stSocket.isConnected();
    }

    /**
     * Sets up the buffered reader.
     * @throws IOException Thrown if there's an issue.
     */
    private void setupBufferedReader() throws IOException {
        // create variable for the sockets input stream
        InputStream isSocketInputStream = stSocket.getInputStream();
        isInputStreamReader = new InputStreamReader(isSocketInputStream);

        // create a new buffered reader of the socket input stream reader
        brBufferedReader = new BufferedReader(new InputStreamReader(isSocketInputStream));
    }

    /**
     * Listens for data being sent to this client.
     *
     * @return The string data sent to the client.
     * @throws IOException Thrown when there is some sort of IO problem.
     */
    public String listenForData() throws IOException {
        if (brBufferedReader == null) {
            setupBufferedReader();
        }

        // return data received
        return brBufferedReader.readLine();
    }

    /**
     * Sets up the print writer for outputting to the socket.
     * @throws IOException Thrown when there is some sort of problem.
     */
    private void setupPrintWriter() throws IOException {
        // create a new output stream
        OutputStream osSocketOutputStream = stSocket.getOutputStream();

        // use PrintWriter to send a line of text to the outputStream
        pwOut = new PrintWriter(osSocketOutputStream, true);
    }

    /**
     * Sends data to the client.
     *
     * @param data The string data to be set.
     * @throws IOException Thrown when there is some sort of IO problem.
     */
    public void send(String data) throws IOException {
        if (pwOut == null) {
            setupPrintWriter();
        }

        pwOut.println(data);
        pwOut.flush();
    }

    /**
     * Closes the socket in use.
     *
     * @throws IOException Thrown when there is an IO problem.
     */
    public void close() throws IOException {
        // close InputStream and Buffered reader
        if (isInputStreamReader != null) {
            isInputStreamReader.close();
        }
        if (brBufferedReader != null) {
            brBufferedReader.close();
        }
        if (stSocket != null) {
            stSocket.close();
        }
        if (pwOut != null) {
            pwOut.close();
        }
    }
}
