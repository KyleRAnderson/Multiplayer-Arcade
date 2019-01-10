/**
 * 
 */
package preferences;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.google.gson.Gson;

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
	
	private static Gson gsJsonS = new Gson();
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
	 * static method to load preferences
	 * 
	 * @throws IOException incase cannot convert byte data to utf-8 List string
	 */
	public static Preferences load(final String savedir) throws IOException {
		Path pthSaveFilePath = Paths.get(savedir + "\\" + FILE_NAME);
		List<String> lisFileString = Files.readAllLines(pthSaveFilePath, StandardCharsets.UTF_8);
		return gsJsonS.fromJson(lisFileString.toString(), Preferences.class);
	}

}
