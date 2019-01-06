package menu;

import advancedIO.AdvancedIO;
import games.Game;
import games.pong.ui.PongUI;
import javafx.application.Application;
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
import network.TCPSocket;
import network.party.PartyHandler;
import network.party.network.HostStatus;
import network.party.network.NetworkMessage;
import network.party.network.ReceivedDataEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    }


    private static final int GAP = 15;
    private static final Font
            HEADER_FONT = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 48),
            INPUT_FONT = Font.font("Book Antiqua", FontWeight.NORMAL, FontPosture.REGULAR, 12);
    private GridPane menuRoot;
    private Stage stage;

    private PartyMenuItem hostMenuItem, connectMenuItem;

    // The game that's currently being played.
    private Game currentGame;

    // Array of all the playable games.
    private Game[] games = new Game[]{
            new PongUI()
    };

    /**
     * Constructs a new main menu object.
     */
    public MainMenu() {
        PartyHandler.setIncomingMessageListener(receivedDataEvent -> dataReceived(receivedDataEvent));
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
        ImageView image = new ImageView(getClass().getResource("/res/preferences.png").toString());
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
        ImageView scoresImage = new ImageView(getClass().getResource("/res/leaderboard.png").toString());
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
        Text ipLabel = new Text("IP Address");
        ipLabel.setFont(INPUT_FONT);
        connectMenuItem.addIPField(ipLabel);
        TextField addressField = connectMenuItem.getIPField();
        addressField.setFont(INPUT_FONT);
        setupPort(connectMenuItem);
        menuRoot.add(connectMenuItem, 0, 2); // Add to (0, 2)

        // Host a party button
        hostMenuItem = new PartyMenuItem("Host", "Waiting for Players...");
        hostMenuItem.setPadding(new Insets(15));
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
        setDisplay(menuRoot);
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
     * @param game The game to play.
     */
    private void playGame(Game game) {
        currentGame = game;
        currentGame.reset();
        currentGame.setOnGameDataSend(this::sendGameData);
        Region window = currentGame.getWindow();
        window.setPrefWidth(menuRoot.getWidth());
        window.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        setDisplay(window);
        currentGame.start();
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

    private void setDisplay(Region parent) {
        Scene newScene = new Scene(parent);
        stage.setScene(newScene);
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

    }

    /**
     * Connects to a party at the currently set IP address and port.
     */
    private void connectToParty() {
        System.out.println("Connecting..."); // TODO remove
        hostMenuItem.setDisable(true);
        // Need to connect in a separate thread.
        ConnectTask task = new ConnectTask(connectMenuItem.getIpAddress(), connectMenuItem.getPort());
        task.setOnFailed(event -> connectionOver(false));
        task.setOnSucceeded(event -> connectionOver(task.getValue()));

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(task);
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
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Failed to connect to host.", ButtonType.OK);
            errorAlert.showAndWait();
        } else {
            onConnection();
        }
        // Disable the connection stuff once connected.
        hostMenuItem.setDisable(allGood);
        connectMenuItem.setDisable(allGood);
        connectMenuItem.setActive(false);
    }

    /**
     * Hosts a party on the currently set port.
     */
    private void hostParty() {
        connectMenuItem.setDisable(true);
        System.out.println("Hosting..."); // TODO remove
        HostTask task = new HostTask(hostMenuItem.getPort());
        task.setOnFailed(event -> hostingFailed());
        task.setOnSucceeded(event -> hostSuccessful());

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(task);
        executorService.shutdown();
    }

    /**
     * Called when the application fails to host.
     */
    private void hostingFailed() {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Failed to start host.", ButtonType.OK);
        errorAlert.showAndWait();
    }

    /**
     * Called when after hosting a party a player connects to the lobby.
     */
    private void hostSuccessful() {
        hostMenuItem.setActive(false);
        hostMenuItem.setDisable(true);
        onConnection();
    }

    /**
     * Called when connecting to another client has succeeded.
     */
    private void onConnection() {
        // TODO get working host name.
        NetworkMessage message = new NetworkMessage("Testing", HostStatus.CONNECTED);
        PartyHandler.sendMessage(message.toJsonString());
    }

    /**
     * Called when data is received from the other client.
     *
     * @param receivedEvent The event of the received data.
     */
    public void dataReceived(ReceivedDataEvent receivedEvent) {
        if (receivedEvent == ReceivedDataEvent.RECEIVED_DATA) {
            while (PartyHandler.hasIncomingMessages()) {
                NetworkMessage receivedMessage = NetworkMessage.fromJson(PartyHandler.pollIncoming());
                // Determine if some of the data should be passed to the current game.
                final boolean shouldSendToGame = currentGame != null && currentGame.isNetworkGame();

                switch (receivedMessage.getHostStatus()) {
                    case IN_GAME:
                        if (shouldSendToGame) {
                            currentGame.receiveData(receivedMessage);
                        }
                        break;
                    case DISCONNECTING:
                        if (shouldSendToGame) {
                            currentGame.hostDisconnecting();
                        }
                        break;
                    case PENDING_GAME_INVITE:
                        break;
                    case CONNECTED:
                        // TODO make official
                        System.out.println("Host name of connected client: " + receivedMessage.getHostName());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * Called when a game wishes to send game data to the connected client.
     * @param gameData The string data to be sent.
     */
    private void sendGameData(final String gameData) {
        if (PartyHandler.isConnected()) {
            // TODO implement working host name.
            final String hostName = "Test";
            NetworkMessage message = new NetworkMessage(hostName, HostStatus.IN_GAME, gameData);
            PartyHandler.sendMessage(message.toJsonString());
        }

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
