package preferences;

/**
 * Utility class for a singleton object for modifying preferences.
 * @author Kyle Anderson
 * ICS4U RST
 */
public class Preferences {
    private static Preferences currentInstance = new Preferences();

    /**
     * Gets the current instance of the preferences.
     * @return The current preferences instance.
     */
    public static Preferences getInstance() {
        if (currentInstance == null) {
            currentInstance = new Preferences();
        }
        return currentInstance;
    }

    // The user's name.
    private String hostName;

    /**
     * Constructs a new preferences object.
     */
    private Preferences() {
    }

    /**
     * Gets the host name (user name) of the user.
     * @return The user's host name.
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Sets the host name (user name) for the user.
     * @param hostName The user's profile name.
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}
