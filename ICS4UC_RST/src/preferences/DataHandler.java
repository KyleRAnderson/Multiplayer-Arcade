package preferences;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Class for saving/loading preferences using json
 *
 * @author s405751
 * ICS4U RST
 */
class DataHandler {
    private static final Gson gsonParser = new Gson();

    private final static String FILE_NAME = "pong_preferences.json",
            SAVE_DIR = System.getProperty("user.home");

    /**
     * method to save object to json file
     *
     * @param preferences The preferences object to be saved.
     * @throws IOException if file cannot be written/created
     */
    static void save(Preferences preferences) throws IOException {
        try (FileWriter writer = new FileWriter(Paths.get(SAVE_DIR, FILE_NAME).toString())) {
            gsonParser.toJson(preferences, writer);
        }
    }


    /**
     * Method to load preferences from the default file.
     *
     * @return preferences object
     * @throws IOException  problem with reading line or closing file reader and buffered reader
     * @throws JsonSyntaxException when there is an error with the syntax of the json in the read file.
     */
    static Preferences load() throws JsonSyntaxException, IOException {
        Preferences preferences = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(Paths.get(SAVE_DIR, FILE_NAME).toString()))) {
            preferences = gsonParser.fromJson(reader, Preferences.class);
        }


        return preferences;
    }

}
