package network.party.network;

import com.google.gson.Gson;

/**
 * Class representing a message to be sent or received to/from a multiplayer client.
 * @author Kyle Anderson
 * ICS4U RST
 */
public class NetworkMessage {
    private String hostName;
    private HostStatus hostStatus;
    private String gameData;

    /**
     * Constructs a new NetworkMessage object.
     * @param hostName The host name.
     * @param status The status of the application.
     * @param gameData The string game data.
     */
    public NetworkMessage(String hostName, HostStatus status, String gameData) {
        setHostName(hostName);
        setGameData(gameData);
        setHostStatus(status);
    }

    /**
     * Constructs a new NetworkMessage object.
     * @param hostName The host name.
     * @param status The status of the application.
     */
    public NetworkMessage(String hostName, HostStatus status) {
        this(hostName, status, null);
    }

    /**
     * Gets the name of the machine sending the message.
     * @return The host name.
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Sets the host name of the machine sending the message.
     * @param hostName The host name.
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * Gets the game data sent by the other client.
     * @return The game data string.
     */
    public String getGameData() {
        return gameData;
    }

    /**
     * Sets the game data to be sent over to the other machine.
     * @param gameData The game data.
     */
    public void setGameData(String gameData) {
        this.gameData = gameData;
    }

    public HostStatus getHostStatus() {
        return hostStatus;
    }

    public void setHostStatus(HostStatus hostStatus) {
        this.hostStatus = hostStatus;
    }

    /**
     * Converts the network message into json string.
     * @return The json string representation of this object.
     */
    public String toJsonString() {
        return toJSONString(this);
    }

    /**
     * Instantiates a new NetworkMessage from json string.
     * @param jsonString The json string to parse.
     * @return The parsed NetworkMessage.
     */
    public static NetworkMessage fromJson(final String jsonString) {
        return new Gson().fromJson(jsonString, NetworkMessage.class);
    }


    /**
     * Converts the given network message into json string.
     * @param message The message to be converted.
     * @return The json string representation of teh messsage.
     */
    public static String toJSONString(final NetworkMessage message) {
        return new Gson().toJson(message);
    }
}