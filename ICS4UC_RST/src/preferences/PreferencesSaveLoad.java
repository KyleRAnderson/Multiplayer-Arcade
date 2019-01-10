/**
 * 
 */
package preferences;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Class for saving/loading preferences using json
 * 
 * @author s405751
 * 
 * ICS4U
 */
public class PreferencesSaveLoad {
	private Gson gsJson = new Gson();
	private Preferences pfPreferences;
	private String strSaveDir;
	
	private final static String FILE_NAME = "pong_preferences.json";
	
	/**
	 * constructor to load preferences
	 * 
	 * @param preferences object
	 * @param save directory
	 */
	public PreferencesSaveLoad(Preferences preferences, final String savedir) {
		pfPreferences = preferences;
		strSaveDir = savedir;
	}
	
	/**
	 * method to save object to json file
	 * 
	 * @throws IOException if file cannot be written/created
	 */
	public void save() throws IOException {
		FileWriter fwFileWriter = new FileWriter(strSaveDir + "\\" + FILE_NAME);
		fwFileWriter.write(gsJson.toJson(pfPreferences));
		fwFileWriter.close();
	}
	
	
	/**
	 * method to load preferences
	 * 
	 * @return preferences object
	 * @throws IOException  problem with reading line or closing file reader and buffered reader
	 * @throws JsonSyntaxException 
	 */
	public Preferences load() throws JsonSyntaxException, IOException {
		FileReader frFileReader = new FileReader(strSaveDir + "\\" + FILE_NAME);
		BufferedReader brBufferedReader = new BufferedReader(frFileReader);
		Preferences pfPreferences = gsJson.fromJson(brBufferedReader.readLine(), Preferences.class);
		frFileReader.close();
		brBufferedReader.close();
		
		return pfPreferences;
	}

}
