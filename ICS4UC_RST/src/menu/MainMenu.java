package menu;

import games.Game;
import games.pong.ui.PongUI;
import impl.org.controlsfx.skin.NotificationBar;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import network.Server;
import network.TCPSocket;
import network.party.PartyHandler;
import network.party.network.HostStatus;
import network.party.network.NetworkMessage;
import network.party.network.ReceivedDataEvent;
import org.controlsfx.control.Notifications;
import preferences.Preferences;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

import static javafx.scene.control.ButtonType.YES;

/**
 * Main menu for users to use to launch whatever game they want to or to customize settings.
 * Acts as a portal to the rest of the application's features.
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
public class MainMenu extends Application {
    private static final double DEFAULT_WIDTH, DEFAULT_HEIGHT, SCREEN_HEIGHT, SCREEN_WIDTH, MIN_HEIGHT, MIN_WIDTH;

    static {
        Rectangle2D screenDimensions = Screen.getPrimary().getBounds();
        SCREEN_HEIGHT = screenDimensions.getHeight();
        SCREEN_WIDTH = screenDimensions.getWidth();
        DEFAULT_WIDTH = SCREEN_WIDTH / 2;
        DEFAULT_HEIGHT = SCREEN_HEIGHT / 2;
        MIN_HEIGHT = SCREEN_HEIGHT / 5;
        MIN_WIDTH = SCREEN_WIDTH / 5;

        // load custom arcade font
        InputStream stream = MainMenu.class.getResourceAsStream("/res/pong/fonts/arcade.ttf");
        Font.loadFont(stream, 10);
        try {
            stream.close();
        } catch (IOException e) {
            // Output error.
            System.err.println(String.format("Failed to close font loading stream.\n%s", Arrays.toString(e.getStackTrace())));
        }
    }


    private static final int GAP = 15;
    private static final Font
            HEADER_FONT = Font.font("ArcadeClassic", FontWeight.BOLD, FontPosture.REGULAR, 38),
            INPUT_FONT = Font.font("Book Antiqua", FontWeight.NORMAL, FontPosture.REGULAR, 12);
    private GridPane menuRoot;
    private Stage stage;

    private PartyMenuItem hostMenuItem, connectMenuItem;

    // The game that's currently being played.
    private Game currentGame;

    // Connection tasks
    private HostTask hostingTask;
    private ConnectTask connectTask;

    /**
     * True if disconnect was notified, false otherwise.
     */
    private boolean disconnectNotified;

    // Array of all the playable games.
    private final Game[] games = new Game[]{
            new PongUI()
    };

    /**
     * Constructs a new main menu object.
     */
    public MainMenu() {
        PartyHandler.setIncomingMessageListener(this::messageReceived);
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        initializeElements();
        primaryStage.show();
    }

    /**
     * Initializes all UI elements.
     */
    private void initializeElements() {
        stage.setTitle("Arcade");
        menuRoot = new GridPane();

        // Create the welcome button at the top.
        Text welcomeText = new Text("Welcome to the Arcade!");
        welcomeText.setTextAlignment(TextAlignment.CENTER);
        welcomeText.setFont(HEADER_FONT);
        StackPane welcomeButton = new StackPane();
        formatMenuItem(welcomeButton);
        welcomeButton.getChildren().add(welcomeText);
        // Blue background.
        welcomeButton.setBackground(new Background(new BackgroundFill(Color.DODGERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        menuRoot.add(welcomeButton, 0, 0, 2, 1); // (0, 0) with colspan of 2 to spread the entire width.

        // Now create the preferences menu item
        StackPane preferences = new StackPane();
        formatMenuItem(preferences);
        preferences.setOnMouseClicked(event -> displayPreferences());
        HBox contents = new HBox(GAP);
        contents.setAlignment(Pos.CENTER);
        Text labelText = new Text("Preferences");
        labelText.setTextAlignment(TextAlignment.CENTER);
        labelText.setFont(HEADER_FONT);
        ImageView image = new ImageView(getClass().getResource("/res/images/preferences.png").toString());
        image.setPreserveRatio(true);
        image.setFitWidth(50);
        contents.getChildren().addAll(labelText, image);
        preferences.getChildren().add(contents);
        preferences.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
        menuRoot.add(preferences, 0, 1); // Add to (0, 1)

        // Scores menu item.
        StackPane scores = new StackPane();
        formatMenuItem(scores);
        scores.setOnMouseClicked(event -> showScores());
        HBox scoresContent = new HBox(GAP);
        scoresContent.setAlignment(Pos.CENTER);
        Text scoresLabel = new Text("Scores");
        scoresLabel.setTextAlignment(TextAlignment.CENTER);
        scoresLabel.setFont(HEADER_FONT);
        ImageView scoresImage = new ImageView(getClass().getResource("/res/images/leaderboard.png").toString());
        scoresImage.setPreserveRatio(true);
        scoresImage.setFitWidth(50);
        scoresContent.getChildren().addAll(scoresImage, scoresLabel);
        scores.getChildren().add(scoresContent);
        scores.setBackground(new Background(new BackgroundFill(Color.MEDIUMPURPLE, CornerRadii.EMPTY, Insets.EMPTY)));
        menuRoot.add(scores, 1, 1); // Add to (1, 1)

        // Connect to party button
        connectMenuItem = new PartyMenuItem("Connect", "Connecting...");
        connectMenuItem.setPadding(new Insets(15));
        formatMenuItem(connectMenuItem);
        connectMenuItem.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
        Button connectButton = connectMenuItem.getActionButton();
        connectButton.setFont(INPUT_FONT);
        connectButton.setBackground(new Background(new BackgroundFill(Color.rgb(77, 77, 255), CornerRadii.EMPTY, Insets.EMPTY)));
        connectMenuItem.setOnAction(event -> connectToParty());
        connectMenuItem.getDisconnectButton().setOnAction(event -> disconnect(true));
        Text ipLabel = new Text("IP Address");
        ipLabel.setFont(INPUT_FONT);
        connectMenuItem.addIPField(ipLabel);
        TextField addressField = connectMenuItem.getIPField();
        addressField.setFont(INPUT_FONT);
        setupPort(connectMenuItem);
        menuRoot.add(connectMenuItem, 0, 2); // Add to (0, 2)

        // Host a party button
        // Add the host IP to the info.
        String hostIp;
        try {
            hostIp = " " + Server.getDefaultHostIP();
        } catch (UnknownHostException e) {
            hostIp = "";
            System.err.println("Couldn't get Host IP address.");
        }
        hostMenuItem = new PartyMenuItem(String.format("Host%s", hostIp), "Waiting for Players...");
        hostMenuItem.setPadding(new Insets(15));
        hostMenuItem.getDisconnectButton().setOnAction(event -> disconnect(true));
        formatMenuItem(hostMenuItem);
        hostMenuItem.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        Button hostButton = hostMenuItem.getActionButton();
        hostButton.setBackground(new Background(new BackgroundFill(Color.rgb(255, 77, 255), CornerRadii.EMPTY, Insets.EMPTY)));
        hostMenuItem.setOnAction(event -> hostParty());
        setupPort(hostMenuItem);
        menuRoot.add(hostMenuItem, 1, 2); // Add to (1, 2)

        // Now for the games themselves, held within a scroll pane.
        VBox games = new VBox(); // New VBox with no gap for displaying game menu items.
        games.setAlignment(Pos.TOP_CENTER);
        ScrollPane gamesScrollPane = new ScrollPane();
        gamesScrollPane.setFitToWidth(true);
        gamesScrollPane.setFitToHeight(true);
        gamesScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        gamesScrollPane.setContent(games);
        GridPane.setHgrow(gamesScrollPane, Priority.ALWAYS);
        GridPane.setVgrow(gamesScrollPane, Priority.ALWAYS);
        menuRoot.add(gamesScrollPane, 0, 3, 2, 1); // Add to (0, 3) with colspan and rowspan of 2.

        // Now for adding all of the games to the list.
        for (Game game : this.games) {
            StackPane menuItem = new StackPane();
            formatMenuItem(menuItem);
            menuItem.setOnMouseClicked(event -> playGame(game));
            Text menuText = game.getTextDisplay();
            menuText.setTextAlignment(TextAlignment.CENTER);
            VBox.setVgrow(menuItem, Priority.SOMETIMES);

            // Set the background up nicely.
            ImageView coverArtImage = new ImageView(game.getCoverArt());
            coverArtImage.fitWidthProperty().bind(menuItem.widthProperty());
            coverArtImage.fitHeightProperty().bind(menuItem.heightProperty());
            menuItem.getChildren().addAll(coverArtImage, menuText);
            games.getChildren().add(menuItem);
        }


        // Stage minimum size
        stage.setMinHeight(MIN_HEIGHT);
        stage.setMinWidth(MIN_WIDTH);
        // Create the scene and all.
        menuRoot.setMinSize(stage.getMinWidth(), stage.getMinHeight());
        menuRoot.setPrefSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        // Column and row constraints.
        ColumnConstraints column1 = createColumnConstraints(50);
        ColumnConstraints column2 = createColumnConstraints(50);
        RowConstraints row1 = createRowConstraints(10);
        RowConstraints row2 = createRowConstraints(20);
        RowConstraints row3 = createRowConstraints(20);
        RowConstraints row4 = createRowConstraints(50);
        menuRoot.getColumnConstraints().addAll(column1, column2);
        menuRoot.getRowConstraints().addAll(row1, row2, row3, row4);

        // Set the scene at the end.
        Scene scene = new Scene(menuRoot);
        setDisplay(scene);
    }

    /**
     * Disconnects from the party.
     * @param notifyOtherClient True to notify the connected computer that we're disconnecting, false otherwise.
     */
    private void disconnect(boolean notifyOtherClient) {
        disconnectNotified = true;
        if (hostingTask != null) {
            hostingTask.cancel(true);
        }
        if (connectTask != null) {
            connectTask.cancel(true);
        }

        if (notifyOtherClient) {
            NetworkMessage message = new NetworkMessage(HostStatus.DISCONNECTING);
            sendNetworkMessage(message);
            // Need to wait to ensure that the disconnect message is sent before closing the socket.
            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished( event -> PartyHandler.disconnect());
            delay.play();
        } else {
            PartyHandler.disconnect();
        }
        hostMenuItem.setConnected(false);
        connectMenuItem.setConnected(false);
        hostMenuItem.setDisable(false);
        connectMenuItem.setDisable(false);
        hostMenuItem.setActive(false);
        connectMenuItem.setActive(false);
    }

    /**
     * SEts up the port for the PartyMenuItem
     *
     * @param menuItem The PartyMenuItem for which the port should be set up.
     */
    private static void setupPort(PartyMenuItem menuItem) {
        Text portLabel = new Text("Port");
        portLabel.setFont(INPUT_FONT);
        menuItem.addPortField(TCPSocket.DEFAULT_PORT, portLabel);
        TextField portField = menuItem.getPortField();
        portField.setFont(INPUT_FONT);
        menuItem.setSpacing(GAP);
    }

    /**
     * Begins playing the provided game.
     *
     * @param game       The game to play.
     * @param wasInvited True if the player was invited to play this game and accepted the invite.
     */
    private void playGame(Game game, boolean wasInvited) {
        // If the party is connected, prepare for launching the game.
        if (PartyHandler.isConnected() && !wasInvited) {
            NetworkMessage message = new NetworkMessage(HostStatus.PENDING_GAME_INVITE);
            message.setCurrentGame(game.getClass().toString());
            sendNetworkMessage(message);
            Alert inviteSent = new Alert(Alert.AlertType.INFORMATION, "Game invite sent.", ButtonType.OK);
            inviteSent.showAndWait();
        } else {
            currentGame = game;
            currentGame.reset();
            currentGame.setOnEnd(this::gameEnded);
            Region window = currentGame.getWindow();
            window.setPrefWidth(menuRoot.getWidth());
            if (wasInvited) {
                currentGame.setNetworkGame();
            }
            if (currentGame.isNetworkGame()) {
                currentGame.getNetworkPlayer().setOnGameDataSend(this::sendGameData);
            }
            setDisplay(currentGame.getWorkingScene());
            currentGame.initializePlayers();
            currentGame.start();
        }
    }

    /**
     * Called when a game ends and is ready to exit.
     *
     * @param endedGame The game that ended.
     */
    private void gameEnded(Game endedGame) {
        // Only actually end if the ended game was the game being played.
        if (currentGame == endedGame) {
            currentGame = null;
            setDisplay(menuRoot.getScene());
        }
    }

    /**
     * Begins playing the provided game.
     *
     * @param game The game to play.
     */
    private void playGame(Game game) {
        playGame(game, false);
    }

    /**
     * Quits the current game, if there is one being played.
     */
    public void quitGame() {
        if (currentGame != null) {
            currentGame.end();
            currentGame = null;
        }
    }

    /**
     * Sets the display to be shown on the screen.
     *
     * @param scene The scene to be shown.
     */
    private void setDisplay(Scene scene) {
        stage.setScene(scene);
    }

    /**
     * Formats the provided stack pane in order to match styling for menu items.
     *
     * @param item The Stack Pane to be formatted to match a menu item's style.
     */
    private static void formatMenuItem(StackPane item) {
        item.setAlignment(Pos.CENTER);
        GridPane.setHgrow(item, Priority.ALWAYS);
        GridPane.setVgrow(item, Priority.ALWAYS);
    }

    /**
     * Method for quickly creating row constraints.
     *
     * @param percentHeight The percent height for the row.
     * @return The created row constraints object.
     */
    private static RowConstraints createRowConstraints(final int percentHeight) {
        RowConstraints constraints = new RowConstraints();
        constraints.setPercentHeight(percentHeight);
        constraints.setVgrow(Priority.ALWAYS);

        return constraints;
    }

    /**
     * Method for quickly creating column constraints.
     *
     * @param percentWidth The percent height for the row.
     * @return The created row constraints object.
     */
    @SuppressWarnings("SameParameterValue")
    private static ColumnConstraints createColumnConstraints(final int percentWidth) {
        ColumnConstraints constraints = new ColumnConstraints();
        constraints.setPercentWidth(percentWidth);
        constraints.setHgrow(Priority.ALWAYS);

        return constraints;
    }


    /**
     * Shows the scores menu.
     */
    private void showScores() {
    }

    /**
     * Displays the user preferences menu.
     */
    private void displayPreferences() {
        // TODO the code below is all temporary, eventually will make real preferences.
        TextInputDialog inputDialog = new TextInputDialog(Preferences.getInstance().getHostName());
        inputDialog.setHeaderText("Preferences");
        inputDialog.setTitle("Preferences");
        inputDialog.setContentText("User Name: ");
        Optional<String> result = inputDialog.showAndWait();

        result.ifPresent(s -> Preferences.getInstance().setHostName(s));
    }

    /**
     * Connects to a party at the currently set IP address and port.
     */
    private void connectToParty() {
        hostMenuItem.setDisable(true);
        // Need to connect in a separate thread.
        connectTask = new ConnectTask(connectMenuItem.getIpAddress(), connectMenuItem.getPort());
        connectTask.setOnFailed(event -> connectionOver(false));
        connectTask.setOnSucceeded(event -> connectionOver(connectTask.getValue()));

        ExecutorService executorService = PartyHandler.createFixedTimeoutExecutorService(1);
        executorService.execute(connectTask);
        executorService.shutdown();
    }

    /**
     * Called when the attempt to connect to a host user is ended.
     *
     * @param succeeded True if the connection attempt succeeded, false otherwise.
     */
    private void connectionOver(boolean succeeded) {
        final boolean allGood = PartyHandler.isConnected() && succeeded;
        if (!allGood) {
            disconnect(false);
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Failed to connect to host.", ButtonType.OK);
            errorAlert.showAndWait();
        } else {
            onConnection();
            // Disable the connection stuff once connected.
            hostMenuItem.setDisable(true);
            connectMenuItem.setActive(false);
            connectMenuItem.setConnected(true);
        }
    }

    /**
     * Hosts a party on the currently set port.
     */
    private void hostParty() {
        connectMenuItem.setDisable(true);
        hostingTask = new HostTask(hostMenuItem.getPort());
        hostingTask.setOnFailed(event -> hostingFailed());
        hostingTask.setOnSucceeded(event -> hostSuccessful());

        ExecutorService executorService = PartyHandler.createFixedTimeoutExecutorService(1);
        executorService.execute(hostingTask);
        executorService.shutdown();
    }

    /**
     * Called when the application fails to host.
     */
    private void hostingFailed() {
        disconnect(false);
        Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Failed to start host.", ButtonType.OK);
        errorAlert.showAndWait();
    }

    /**
     * Called when after hosting a party a player connects to the lobby.
     */
    private void hostSuccessful() {
        hostMenuItem.setActive(false);
        hostMenuItem.setConnected(true);
        onConnection();
    }

    /**
     * Prepares a network message for sending.
     *
     * @param message The message to be sent.
     */
    private void sendNetworkMessage(NetworkMessage message) {
        if (currentGame != null) {
            message.setCurrentGame(currentGame.getClass().toString());
        }
        message.setHostName(Preferences.getInstance().getHostName());
        PartyHandler.sendMessage(message);
    }

    /**
     * Called when connecting to another client has succeeded.
     */
    private void onConnection() {
        disconnectNotified = false;
        sendNetworkMessage(new NetworkMessage(HostStatus.CONNECTED));
    }

    /**
     * Called when data is received from the other client.
     *
     * @param receivedEvent The event of the received data.
     */
    public void messageReceived(ReceivedDataEvent receivedEvent) {
        // Determine if some of the data should be passed to the current game.
        final boolean shouldSendToGame = currentGame != null && currentGame.isNetworkGame();
        if (receivedEvent == ReceivedDataEvent.RECEIVED_DATA) {
            while (PartyHandler.hasIncomingMessages()) {
                NetworkMessage receivedMessage = PartyHandler.pollIncoming();

                switch (receivedMessage.getHostStatus()) {
                    case IN_GAME:
                        if (shouldSendToGame) {
                            currentGame.getNetworkPlayer().receiveData(receivedMessage);
                        }
                        break;
                    case DISCONNECTING:
                        remotePlayerDisconnecting(shouldSendToGame);
                        break;
                    case PENDING_GAME_INVITE:
                        // If the user accepts the game invite, we need to do certain things.
                        Game invitedGame = findGame(receivedMessage.getCurrentGame());
                        boolean userAccepted = gameInvite(receivedMessage.getHostName(), invitedGame);
                        HostStatus newStatus = (userAccepted) ? HostStatus.ACCEPTED_GAME_INVITE : HostStatus.DECLINED_GAME_INVITE;
                        NetworkMessage newMessage = new NetworkMessage(newStatus);
                        if (userAccepted) {
                            newMessage.setCurrentGame(receivedMessage.getCurrentGame());
                        }
                        sendNetworkMessage(newMessage);
                        if (userAccepted) {
                            playGame(invitedGame, true);
                        }
                        break;
                    case ACCEPTED_GAME_INVITE:
                        playGame(findGame(receivedMessage.getCurrentGame()), true);
                        break;
                    case DECLINED_GAME_INVITE:
                        // Show the user an error about the game invitation.
                        Alert declineAlert = new Alert(Alert.AlertType.INFORMATION, String.format("%s declined your invite to play. To play solo disconnect from the party.", receivedMessage.getHostName()), ButtonType.OK);
                        declineAlert.showAndWait();
                        break;
                    case CONNECTED:
                        // When we receive the connected signal from the other client, let's get their name and display it.
                        showNotification("", String.format("Connected to %s", receivedMessage.getHostName()));
                        break;
                    default:
                        break;
                }
            }
        } else if (receivedEvent == ReceivedDataEvent.DISCONNECTED) {
            remotePlayerDisconnecting(shouldSendToGame);
        }
    }

    /**
     * Called when the remote player disconnects.
     *
     * @param shouldSendToGame True to send the information to the game, false otherwise.
     */
    private void remotePlayerDisconnecting(boolean shouldSendToGame) {
        if (shouldSendToGame) {
            currentGame.getNetworkPlayer().hostDisconnecting();
        }
        // If we don't send data to the game, the main menu should be expected to handle notification.
        else {
            if (!disconnectNotified) {
                disconnectNotified = true;
                showNotification( "Disconnected", "Remote player disconnected.");
            }
        }
        disconnect(false);
    }

    /**
     * Called when a game wishes to send game data to the connected client.
     *
     * @param gameData The string data to be sent.
     */
    private void sendGameData(final String gameData) {
        if (PartyHandler.isConnected()) {
            sendNetworkMessage(new NetworkMessage(HostStatus.IN_GAME, gameData));
        }

    }

    /**
     * Invites this user to play a game initiated by the other user.
     *
     * @param otherPlayerName The gamer tag of the player inviting this player to play the game.
     * @param game            The game to which the user was invited.
     * @return True if this user accepts playing the game, false otherwise.
     */
    private boolean gameInvite(final String otherPlayerName, final Game game) {
        boolean userAccepted = false;
        if (game != null) {
            Alert inviteAlert = new Alert(Alert.AlertType.CONFIRMATION, String.format("%s has invited you to play %s. Do you accept?", otherPlayerName, game.getName()), YES, ButtonType.NO);
            Optional<ButtonType> result = inviteAlert.showAndWait();
            if (result.isPresent()) {
                ButtonType type = result.get();
                userAccepted = type == YES;
            }
        }

        return userAccepted;
    }

    /**
     * Finds the game based off of the class name.
     *
     * @param gameClassName The game's class name.
     * @return The game corresponding to the class name.
     */
    private Game findGame(final String gameClassName) {
        Game foundGame = null;
        for (Game game : games) {
            if (game.getClass().toString().equals(gameClassName)) {
                foundGame = game;
                break;
            }
        }
        return foundGame;
    }

    /**
     * Shows a notification with the given title and text.
     * @param title The title of the notification.
     * @param content The notification's content text.
     */
    private void showNotification(final String title, final String content) {
        Notifications notifications = Notifications.create();
        notifications.text(content);
        notifications.title(title);
        notifications.show();
    }

    /**
     * Main method for the program.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
