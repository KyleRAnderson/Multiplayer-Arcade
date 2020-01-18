package menu;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for easily creating a menu item for party handlers.
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
class PartyMenuItem extends StackPane {
    private static final Pattern IP_VALIDATOR = Pattern.compile("\\A((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\z");


    private final Button actionButton;
    private final ProgressIndicator loadingAnimation;
    private final GridPane organizer;
    private final String inactiveText, activeText;
    private boolean active;

    private final Button disconnectButton;

    private EventHandler<ActionEvent> actionHandler;

    // Last column occupied.
    int lastColumn = 0;

    private TextField portField, ipField;

    /**
     * Constructs a new Party Menu Item with the provided text for the action button when active and inactive.
     *
     * @param actionButtonText       The text displayed on the action button while inactive.
     * @param activeActionButtonText The text displayed on the action button while active.
     */
    public PartyMenuItem(String actionButtonText, String activeActionButtonText) {
        loadingAnimation = new ProgressIndicator();
        this.inactiveText = actionButtonText;
        this.activeText = activeActionButtonText;
        actionButton = new Button(actionButtonText);
        actionButton.setOnAction(this::actionButtonPressed);
        disconnectButton = new Button("Disconnect");

        organizer = new GridPane();
        organizer.setAlignment(Pos.CENTER);
        getChildren().add(organizer);

        organizer.add(actionButton, 0, 1);
    }

    /**
     * Gets the action button. Warning - Don't modify things which may interfere with the operation of this menu item.
     *
     * @return The action button.
     */
    public Button getActionButton() {
        return actionButton;
    }

    private void actionButtonPressed(ActionEvent actionEvent) {
        setActive(true);
        actionHandler.handle(actionEvent);
    }

    public void setOnAction(EventHandler<ActionEvent> handler) {
        this.actionHandler = handler;
    }

    /**
     * Sets the active state of this menu item. Active state is when the button has been pressed
     * and the application is now loading whatever Party operation is was requested to do.
     *
     * @param active True to be active, false otherwise.
     */
    public void setActive(boolean active) {
        this.active = active;
        actionButton.setText((active) ? activeText : inactiveText);
        actionButton.setDisable(active); // Disable the button while active, enable it while not.

        ObservableList<Node> children = getChildren();
        if (active) {
            if (!children.contains(loadingAnimation)) {
                children.add(loadingAnimation);
            }
            if (!children.contains(disconnectButton)) {
                children.add(disconnectButton);
            }

        } else {
            children.remove(loadingAnimation);
            children.remove(disconnectButton);
        }
    }

    /**
     * Determines if this menu item's button has been pressed and it's actively loading something.
     *
     * @return True if the menu item is active, false otherwise.
     */
    public boolean isActive() {
        return this.active;
    }


    /**
     * Adds a port field to the menu item.
     *
     * @param defaultPort The default port to be displayed.
     */
    public void addPortField(final int defaultPort, Text portLabel) {
        if (!isValidPort(defaultPort)) {
            throw new IllegalArgumentException("Port provided must be valid.");
        }

        portField = new TextField(String.valueOf(defaultPort));
        portField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isValidPort(newValue) && !newValue.isEmpty()) {
                portField.setText(oldValue);
            }
        });

        swapElements(portLabel, portField);
    }

    /**
     * Gets the port field.
     *
     * @return The port text field object.
     */
    public TextField getPortField() {
        return portField;
    }

    /**
     * Adds a field for the ip address.
     */
    public void addIPField(Text ipLabel) {
        ipField = new TextField();
        ipField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isPartialValidIPAddress(newValue)) {
                ipField.setText(oldValue);
            }
        });

        swapElements(ipLabel, ipField);
    }

    /**
     * Swaps the provided field elements into a position.
     *
     * @param nodes The nodes to be swapped out.
     */
    private void swapElements(Node... nodes) {
        organizer.getChildren().remove(actionButton);
        for (int i = 0; i < nodes.length; i++) {
            organizer.add(nodes[i], lastColumn, i);
        }
        lastColumn++;
        organizer.add(actionButton, lastColumn, 1);
    }

    /**
     * Gets the IP address field.
     *
     * @return The ip address text field object.
     */
    public TextField getIPField() {
        return ipField;
    }

    /**
     * Determines if the provided port is a valid port number.
     *
     * @param port The string port.
     * @return True if the port is valid, false otherwise.
     */
    private static boolean isValidPort(String port) {
        boolean valid;
        try {
            int intPort = Integer.parseInt(port);
            valid = isValidPort(intPort);
        } catch (NumberFormatException ignored) {
            valid = false;
        }

        return valid;
    }

    /**
     * Determines if the provided port is a valid port number.
     *
     * @param port The port.
     * @return True if the port is valid, false otherwise.
     */
    private static boolean isValidPort(final int port) {
        return 0 <= port && port <= 65535;
    }

    /**
     * Determines if the provided IP address is a valid looking ip address (doesn't actually check using network,
     * only checks formatting).
     *
     * @param ip The ip address to be checked.
     * @return True if the IP address is valid, false otherwise.
     */
    private static boolean isValidIPAddress(String ip) {
        return IP_VALIDATOR.matcher(ip).matches();
    }

    /**
     * Determines if the provided IP address is a valid looking ip address or if it is a partial match (as in
     * it might be a match if it had more).
     *
     * @param ip The ip address to be tested.
     * @return True if the ip address is a match or partial match, false otherwise.
     */
    private static boolean isPartialValidIPAddress(String ip) {
        Matcher ipMatcher = IP_VALIDATOR.matcher(ip);
        return ipMatcher.matches() || ipMatcher.hitEnd();
    }

    /**
     * Gets the port currently set.
     *
     * @return The port.
     */
    public int getPort() {
        String portText = portField.getText();
        return (portText.isEmpty()) ? 0 : Integer.parseInt(portText);
    }

    /**
     * Gets the ip address currently set.
     *
     * @return The ip address.
     */
    public String getIpAddress() {
        return ipField.getText();
    }

    /**
     * Sets the gap for the GridPane organizer.
     *
     * @param spacing The amount of spacing between rows and columns.
     */
    public void setSpacing(double spacing) {
        organizer.setHgap(spacing);
        organizer.setVgap(spacing);
    }

    /**
     * Gets the disconnect button
     *
     * @return The disconnect button.
     */
    public Button getDisconnectButton() {
        return disconnectButton;
    }

    /**
     * Sets whether or not this menu item is connected now.
     *
     * @param connected True if connected, false otherwise.
     */
    public void setConnected(boolean connected) {
        // Disable the organizer if there's no
        organizer.setDisable(connected);
        if (connected && !getChildren().contains(disconnectButton)) {
            getChildren().add(disconnectButton);
        } else if (!connected) {
            getChildren().remove(disconnectButton);
        }
    }


}
