package menu;

import games.Game;
import games.pong.ui.PongUI;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

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
    private static final Font HEADER_FONT = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 48);
    private GridPane menuRoot;
    private Stage stage;

    // The game that's currently being played.
    private Game currentGame;

    // Array of all the playable games.
    private Game[] games = new Game[]{
            new PongUI()
    };

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
        StackPane welcomeButton = getMenuItem(null);
        welcomeButton.getChildren().add(welcomeText);
        // Blue background.
        welcomeButton.setBackground(new Background(new BackgroundFill(Color.DODGERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        menuRoot.add(welcomeButton, 0, 0, 2, 1); // (0, 0) with colspan of 2 to spread the entire width.

        // Now create the preferences menu item
        StackPane preferences = getMenuItem(event -> displayPreferences());
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
        StackPane scores = getMenuItem(event -> showScores());
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
        menuRoot.add(gamesScrollPane, 0, 2, 2, 1); // Add to (0, 2) with colspan and rowspan of 2.

        // Now for adding all of the games to the list.
        for (Game game : this.games) {
            StackPane menuItem = getMenuItem(event -> playGame(game));
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
        RowConstraints row1 = createRowConstraints(25);
        RowConstraints row2 = createRowConstraints(25);
        RowConstraints row3 = createRowConstraints(50);
        menuRoot.getColumnConstraints().addAll(column1, column2);
        menuRoot.getRowConstraints().addAll(row1, row2, row3);

        // Set the scene at the end.
        setDisplay(menuRoot);
    }

    /**
     * Begins playing the provided game.
     *
     * @param game The game to play.
     */
    private void playGame(Game game) {
        currentGame = game;
        currentGame.reset();
        Region window = currentGame.getWindow();
        window.setPrefWidth(menuRoot.getWidth());
        window.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        setDisplay(window);
        currentGame.start();
    }

    private void setDisplay(Region parent) {
        Scene newScene = new Scene(parent);
        stage.setScene(newScene);
    }

    /**
     * Gets a stackpane properly formatted for a menu item.
     *
     * @param clickAction The action to be called when the menu item is clicked.
     * @return The created Stackpane.
     */
    private static StackPane getMenuItem(EventHandler<MouseEvent> clickAction) {
        StackPane pane = new StackPane();
        pane.setOnMouseClicked(clickAction);
        pane.setAlignment(Pos.CENTER);
        GridPane.setHgrow(pane, Priority.ALWAYS);
        GridPane.setVgrow(pane, Priority.ALWAYS);

        return pane;
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
}
