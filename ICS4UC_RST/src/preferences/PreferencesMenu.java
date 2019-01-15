package preferences;

import java.net.UnknownHostException;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import network.TCPSocket;

/**
 * Preferences Menu for accessing and modifying user preferences from the UI.
 *
 * @author s405751 (Nicolas Hawrysh)
 * ICS4U RST
 */
public class PreferencesMenu extends Stage {
	
	// constant for the title
	private final String TITLE = "Preferences";
	
	// constant for min username chars
	private final int MIN_USERNAME_CHARS = 3;
	
	private Stage stgMainWindow;
	
	/**
     * Constructor to create the preferences menu
     *
     */
	
	public PreferencesMenu() {
		// create a stage and set the title and prevent from being resized
		stgMainWindow = new Stage();
		stgMainWindow.setTitle(TITLE);
		stgMainWindow.setResizable(false);

		// create new grid pane with spacing to look nice
		GridPane grdGridPane = new GridPane();
		grdGridPane.setPadding(new Insets(10, 10, 10, 10));
		grdGridPane.setVgap(8);
		grdGridPane.setHgap(10);
				
		// create a label for the title of the program to be in bold and in italics
		Label lbTitle = new Label(TITLE);
		lbTitle.setFont(Font.font(null, FontWeight.BOLD, FontPosture.ITALIC, 20));

		// place the title in the upper center field of the window
		GridPane.setConstraints(lbTitle, 0, 0, 5, 1, HPos.CENTER, VPos.CENTER);
		
		// label for username
		Label lbUserName = new Label("Username: ");
		GridPane.setConstraints(lbUserName, 0, 2);

		// textfield for the username
		TextField txtUserName = new TextField();
		
		// get host name from preferences
		txtUserName.setText(Preferences.getInstance().getHostName());
		
		// if the text is null or empty
		if (txtUserName.getText() == null || txtUserName.getText().isEmpty()) {
			// try to get the local host name and set as default text
			try {
				txtUserName.setText(TCPSocket.getLocalHostName());
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// set size and place it in the window
		GridPane.setConstraints(txtUserName, 1, 2, 3, 1);

		// button when user is finished entering data
		Button btnOK = new Button("OK");
		btnOK.setOnAction(evt -> validate(txtUserName.getText()));
		GridPane.setConstraints(btnOK, 0, 3);

		// add everything to grid
		grdGridPane.getChildren().addAll(lbTitle, lbUserName, txtUserName, btnOK);

		// create new scene with window dimensions
		Scene scnMainScene = new Scene(grdGridPane, 250, 120);
		stgMainWindow.setScene(scnMainScene);

		// show the main window
		stgMainWindow.show();
	}

	/**
     * Constructor to create the preferences menu
     *
     * @param username chosen
     */
	private void validate(String name) {
		// check to make sure that name is not blank and as a size of at least three characters and does not contain any spaces
		if (name.isEmpty() || name == null || name.length() < MIN_USERNAME_CHARS || name.contains(" ")) {
			// if so, show error message box
			errorMessageBox("You must have a name at least " + MIN_USERNAME_CHARS + " characters long and have no spaces.", "Error");
		} else {
			// set the host name
			Preferences.getInstance().setHostName(name);
			// close the window
			stgMainWindow.close();
		}
	}
	
	
	/**
     * method to display error message box\
     * 
     * @param message to display
     * @param message box title
     */
	public static void errorMessageBox(String message, String title) {
		// create new message box with an error icon
		Alert altAlert = new Alert(AlertType.ERROR);

		// set the title and message to display
		altAlert.setTitle(title);
		altAlert.setHeaderText(null);
		altAlert.setContentText(message);

		// show the message box
		altAlert.showAndWait();
	}
}
