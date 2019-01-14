package preferences;

import java.util.Optional;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import menu.MainMenu;

/**
 * Preferences Menu for accessing and modifying user preferences from the UI.
 *
 * @author s405751 (Nicolas Hawrysh)
 * ICS4U RST
 */
public class PreferencesMenu extends Stage {
	
	// constant for the title
	private final String TITLE = "Preferences";
	
	public PreferencesMenu() {
		// create a stage and set the title and prevent from being resized
		Stage stgMainWindow = new Stage();
		stgMainWindow.setTitle(TITLE);
		stgMainWindow.setResizable(false);			

		// create new grid pane with spacing to look nice
		GridPane grdGridPane = new GridPane();
		grdGridPane.setPadding(new Insets(10, 10, 10, 10));
		grdGridPane.setVgap(8);
		grdGridPane.setHgap(10);
				
		// create a label for the title of the program to be in bold and in italics
		Label lbTitle = new Label(TITLE);
		lbTitle.setFont(Font.font(null, FontWeight.BOLD, FontPosture.ITALIC, 16));

		// place the title in the upper center field of the window
		GridPane.setConstraints(lbTitle, 0, 0, 5, 1, HPos.CENTER, VPos.CENTER);

		// create a new text field to store the file path of the file browsed by the
		// user
		TextField txtFilePath = new TextField();

		// prevent the text field from being editable
		txtFilePath.setEditable(false);

		// set size and place it in the window
		txtFilePath.setPrefColumnCount(100);
		GridPane.setConstraints(txtFilePath, 0, 2, 4, 1);

		// create a new button for the file that will be browsed
		Button btnBrowseFile = new Button("Browse File");

		// set size and place it in the window
		btnBrowseFile.setPrefWidth(80);
		GridPane.setConstraints(btnBrowseFile, 4, 2);

		// create label to simply say MD5 to be placed in front of the next text field
		Label lbMD5 = new Label("MD5:");
		GridPane.setConstraints(lbMD5, 0, 4);

		// create text field to store the MD5 checksum that will be generated
		TextField txtMD5Field = new TextField();

		// prevent the text field from being editable and place it right next to the
		// previous label
		txtMD5Field.setEditable(false);
		GridPane.setConstraints(txtMD5Field, 1, 4, 4, 1);

		// create label to say Status
		Label lbStatusTitle = new Label("Status:");

		// make the font bold and place it right below the previous label
		lbStatusTitle.setFont(Font.font(null, FontWeight.BOLD, 12));
		GridPane.setConstraints(lbStatusTitle, 0, 5);

		// create label to be in front of "Status:" which will display if the file is
		// Clean or Infected.
		Label lbStatus = new Label();
		GridPane.setConstraints(lbStatus, 1, 5);

		// add everything to grid
		grdGridPane.getChildren().addAll(lbTitle, txtFilePath, btnBrowseFile, lbMD5, txtMD5Field, lbStatusTitle,
				lbStatus);

		// create new scene with window dimensions
		Scene scnMainScene = new Scene(grdGridPane, 380, 136);
		stgMainWindow.setScene(scnMainScene);

		// show the main window

		stgMainWindow.show();
		// TextInputDialog txtInputDialogue = new
		// TextInputDialog(Preferences.getInstance().getHostName());
		// txtInputDialogue.setHeaderText("Preferences");
		// txtInputDialogue.setTitle("Preferences");
		// txtInputDialogue.setContentText("User Name: ");
		// Optional<String> result = txtInputDialogue.showAndWait();

		// result.ifPresent(s -> Preferences.getInstance().setHostName(s));
	}
}
