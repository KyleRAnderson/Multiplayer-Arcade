package preferences;

import java.io.IOException;

/**
 * Utility class for a singleton object for modifying preferences.
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
public class Preferences {
    private static Preferences currentInstance;

    /**
     * Gets the current instance of the preferences.
     *
     * @return The current preferences instance.
     */
    public static Preferences getInstance() {
        if (currentInstance == null) {
            reloadPreferences();
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
     *
     * @return The user's host name.
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Sets the host name (user name) for the user.
     *
     * @param hostName The user's profile name.
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * Saves current object to json file
     *
     * @throws IOException if there is trouble accessing/reading the file.
     */
    public void save() throws IOException {
        DataHandler.save(this);
    }


    /**
     * Reloads preferences from file, setting the new preferences object to what was loaded.
     */
    private static void reloadPreferences() {
        try {
            currentInstance = DataHandler.load();
        } catch (IOException e) {
            // If we fail to load, then assume there's nothing to load and go with blank preferences.
            currentInstance = new Preferences();
        }
    }
}
