package preferences;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import menu.MainMenu;

/**
 * Preferences Menu for accessing and modifying user preferences from the UI.
 *
 * @author s405751 (Nicolas Hawrysh)
 * ICS4U RST
 */
public class PreferencesMenu extends GridPane {

    private final Runnable closeListener;

    /**
     * Constructor to create the preferences menu
     *
     * @param closeListener  A listener to be set when the preferences menu wants to close.
     * @param inputFontSize  The input font size double property.
     * @param headerFontSize The size property for header font.
     * @param headerFont     The header font.
     * @param inputFont      The input font.
     */
    public PreferencesMenu(final Runnable closeListener, final DoubleProperty inputFontSize, final DoubleProperty headerFontSize, final Font headerFont, final Font inputFont) {
        this.closeListener = closeListener;
        setPadding(new Insets(MainMenu.GAP));
        setVgap(MainMenu.GAP);
        setHgap(MainMenu.GAP);

        setAlignment(Pos.CENTER);
        setBackground(new Background(new BackgroundFill(Color.MEDIUMSLATEBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        Text headerText = new Text("Preferences ");
        MainMenu.setupFont(headerText, headerFontSize, headerFont);
        GridPane.setConstraints(headerText, 0, 0, 2, 1);

        // label for username
        Text userNameLabel = new Text("Username: ");
        userNameLabel.setFill(Color.WHITE);
        MainMenu.setupFont(userNameLabel, headerFontSize, headerFont);
        GridPane.setConstraints(userNameLabel, 0, 1);

        // textfield for the username
        TextField usernameField = new TextField();
        MainMenu.setupFont(usernameField, inputFontSize, inputFont);
        GridPane.setConstraints(usernameField, 1, 1);
        // get host name from preferences
        usernameField.setText(Preferences.getInstance().getHostName());

        // button when user is finished entering data
        Button okButton = new Button("Save");
        MainMenu.setupFont(okButton, headerFontSize, headerFont);
        okButton.setOnAction(evt -> validate(usernameField.getText()));
        GridPane.setConstraints(okButton, 0, 2);

        Button cancelButton = new Button("Cancel");
        MainMenu.setupFont(cancelButton, headerFontSize, headerFont);
        cancelButton.setOnAction(event -> close());
        GridPane.setConstraints(cancelButton, 1, 2);

        // add everything to grid
        getChildren().addAll(headerText, userNameLabel, usernameField, okButton, cancelButton);
    }

    /**
     * Constructor to create the preferences menu
     *
     * @param name The name to be validated.
     */
    private void validate(String name) {
        /*
        Check to make sure that name is not blank and as a size of at least three characters and does not contain any spaces
        constant for min username chars.
        */
        final int MIN_USERNAME_CHARS = 3;
        if (name.isEmpty() || name.length() < MIN_USERNAME_CHARS || name.contains(" ")) {
            // if so, show error message box
            showError("You must have a name at least " + MIN_USERNAME_CHARS + " characters long and have no spaces.", "Error");
        } else {
            // set the host name
            Preferences.getInstance().setHostName(name);
            Preferences.getInstance().save();
            // close the window
            close();
        }
    }

    /**
     * Closes the preferences menu.
     */
    private void close() {
        closeListener.run();
    }


    /**
     * Method to display error message.
     *
     * @param message to display
     * @param title   box title
     */
    private static void showError(String message, String title) {
        MainMenu.showNotification(AlertType.ERROR, title, message);
    }
}
