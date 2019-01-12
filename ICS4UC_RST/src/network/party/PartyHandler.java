package network.party;

import advancedIO.AdvancedIO;
import network.Client;
import network.Server;
import network.TCPSocket;
import network.party.network.NetworkMessage;
import network.party.network.ReceivedDataEvent;
import network.party.network.ReceiverTask;
import network.party.network.SenderTask;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Class for handling a party with another user.
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
public class PartyHandler {
    private static PartyRole role;
    private static TCPSocket socket;

    // Queues for communicating cross-thread.
    private static Queue<NetworkMessage> outgoingQueue, incomingQueue;
    private static ReceiverTask incomingTask;
    private static Consumer<ReceivedDataEvent> incomingListener;

    /**
     * Begins a party session with the user at the given IP address.
     *
     * @param ip   The ip address.
     * @param port The port on which the connection should be made.
     * @return True if the connection attempt is successful, false otherwise.
     */
    public static boolean connect(final String ip, final int port) {
        boolean didConnect = false;
        if (!isConnected()) {
            try {
                Client client = new Client();
                client.connect(ip, port);
                didConnect = true;
                socket = client;
            } catch (IOException ignored) {
            }
        }
        if (didConnect) {
            role = PartyRole.CLIENT;
            setupConnection();
        }

        return didConnect;
    }

    /**
     * Begins to host a party on this user's machine. NOTE - Will hang machine, so run in separate thread.
     *
     * @param port The port on which the hosting should be done.
     * @throws IOException if creating the server fails.
     */
    public static void host(final int port) throws IOException {
        if (!isConnected()) {
            role = PartyRole.SERVER;
            Server server = new Server(port);
            socket = server;
            server.accept();
            if (isConnected()) {
                setupConnection();
            }
        }
    }

    /**
     * Determines if the other player is connected.
     *
     * @return True if the other player is connected, false otherwise.
     */
    public static boolean isOtherPlayerConnected() {
        return socket.isConnected();
    }

    /**
     * Disconnects from the party.
     */
    public static void disconnect() {
        // Only try to disconnect if connected.
        if (isConnected()) {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Failed to close socket.");
            }
        }
        role = null;
    }

    /**
     * Gets the role of this user in the party, either client or server.
     *
     * @return The role of the local user.
     */
    public static PartyRole getRole() {
        return role;
    }

    /**
     * Determines if there is currently a party in session.
     *
     * @return True if there is a party in session, false otherwise.
     */
    public static boolean isConnected() {
        return role != null && socket.isConnected();
    }

    /**
     * Gets the client object for the client local party member, if the local user is a client.
     *
     * @return The Client object.,
     */
    public static Client getClient() {
        return (getRole() == PartyRole.CLIENT && socket instanceof Client) ? (Client) socket : null;
    }

    /**
     * Gets the server object for the server local party member, if the local user is a server.
     *
     * @return The Server object.
     */
    public static Server getServer() {
        return (getRole() == PartyRole.SERVER && socket instanceof Server) ? (Server) socket : null;
    }

    /**
     * Gets the TCP socket object for the party.
     *
     * @return The TCP socket object.
     */
    public static TCPSocket getTCPSocket() {
        return socket;
    }

    /**
     * Sends a message to the other client.
     *
     * @param message The string to be sent.
     */
    public static void sendMessage(NetworkMessage message) {
        outgoingQueue.add(message);
    }

    /**
     * Polls for incoming messages from the other client, removing them once accessed.
     *
     * @return The incoming message.
     */
    public static NetworkMessage pollIncoming() {
        return incomingQueue.poll();
    }

    /**
     * Determines if there are incoming messages from the other client waiting.
     *
     * @return The incoming messages to be read.
     */
    public static boolean hasIncomingMessages() {
        return !incomingQueue.isEmpty();
    }

    /**
     * Adds a listener which will be called when the application receives data from the other client.
     *
     * @param listener The listener to be called when data is received.
     */
    public static void setIncomingMessageListener(Consumer<ReceivedDataEvent> listener) {
        incomingListener = listener;
        if (incomingTask != null) {
            incomingTask.addListener(listener);
        }
    }

    /**
     * Sets up everything necessary for the multiplayer connection to be monitored.
     */
    private static void setupConnection() {
        outgoingQueue = new ArrayBlockingQueue<>(15);
        SenderTask outgoingTask = new SenderTask(getTCPSocket(), outgoingQueue);

        incomingQueue = new ArrayBlockingQueue<>(15);
        incomingTask = new ReceiverTask(getTCPSocket(), incomingQueue);
        incomingTask.setOnSucceeded(event -> receiverClosed());
        if (incomingListener != null) {
            incomingTask.addListener(incomingListener);
        }

        ExecutorService executorService = createFixedTimeoutExecutorService(2);
        executorService.execute(outgoingTask);
        executorService.execute(incomingTask);
        executorService.shutdown();
    }

    /**
     * Creates an executor service with a fixed pool size, that will time
     * out after a certain period of inactivity.
     *
     * @param poolSize The core- and maximum pool size
     * @return The executor service
     */
    public static ExecutorService createFixedTimeoutExecutorService(
            int poolSize) {
        return Executors.newFixedThreadPool(poolSize,
                r -> {
                    Thread t = Executors.defaultThreadFactory().newThread(r);
                    t.setDaemon(true);
                    return t;
                });
    }

    /**
     * Called when the incoming messages task closes.
     */
    private static void receiverClosed() {
        disconnect();
    }

    /**
     * Method for allowing this module to be tested and tried out without the rest of the application.
     *
     * @param args Command-line arguments.
     * @throws IOException if there is some sort of IOException.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        AdvancedIO.print("Arguments: [client/server (c for client, s for server)] [port] [ip address (if client)]");
        if (args.length == 0) {
            args = AdvancedIO.readStringArray("Enter arguments separated by spaces.\n-->", 2, 3);
        }

        if (!(2 <= args.length && args.length <= 3)) {
            AdvancedIO.print("Invalid argument length.");
        } else {
            args[0] = args[0].toLowerCase();
            int port = -1;
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {
            }

            if (port >= 0) {
                switch (args[0]) {
                    case "c":
                        if (args.length == 3) {
                            String ip = args[2];
                            connect(ip, port);
                        } else {
                            AdvancedIO.print("Invalid number of arguments. Need all 3 arguments for client.");
                        }
                        break;
                    case "s":
                        host(port);

                        boolean waiting;
                        do {
                            waiting = isOtherPlayerConnected();
                            if (waiting) {
                                AdvancedIO.print("Waiting for other user to join...");
                            }
                            Thread.sleep(1000);
                        } while (waiting);

                        break;
                    default:
                        AdvancedIO.print("Invalid selection of client or server. Enter \"c\" for client and \"s\" for server.");
                        break;
                }
            } else {
                AdvancedIO.print("Second argument must be an integer corresponding to the port.");
            }
        }
    }
}
