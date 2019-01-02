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
    private static final int GAP = 15;
    private static final Font HEADER_FONT = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 48);
    private GridPane root;

    // Array of all the playable games.
    private Game[] games = new Game[]{
            new PongUI()
    };

    @Override
    public void start(Stage primaryStage) {
        initializeElements(primaryStage);
        primaryStage.show();
    }

    /**
     * Initializes all UI elements.
     */
    private void initializeElements(Stage stage) {
        stage.setTitle("Arcade");

        root = new GridPane();

        // Create the welcome button at the top.
        Text welcomeText = new Text("Welcome to the Arcade!");
        welcomeText.setTextAlignment(TextAlignment.CENTER);
        welcomeText.setFont(HEADER_FONT);
        StackPane welcomeButton = getMenuItem(null);
        welcomeButton.getChildren().add(welcomeText);
        // Blue background.
        welcomeButton.setBackground(new Background(new BackgroundFill(Color.DODGERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        root.add(welcomeButton, 0, 0, 2, 1); // (0, 0) with colspan of 2 to spread the entire width.

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
        root.add(preferences, 0, 1); // Add to (0, 1)

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
        root.add(scores, 1, 1); // Add to (1, 1)

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
        root.add(gamesScrollPane, 0, 2, 2, 1); // Add to (0, 2) with colspan and rowspan of 2.

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

        Rectangle2D screenDimensions = Screen.getPrimary().getBounds();
        // Stage minimum size
        stage.setMinHeight(screenDimensions.getHeight() / 5);
        stage.setMinWidth(screenDimensions.getWidth() / 5);
        // Create the scene and all.
        root.setMaxSize(screenDimensions.getWidth(), screenDimensions.getHeight());
        root.setMinSize(stage.getMinWidth(), stage.getMinHeight());
        root.setPrefSize(screenDimensions.getWidth() / 2, screenDimensions.getHeight() / 2);
        // Column and row constraints.
        ColumnConstraints column1 = createColumnConstraints(50);
        ColumnConstraints column2 = createColumnConstraints(50);
        RowConstraints row1 = createRowConstraints(25);
        RowConstraints row2 = createRowConstraints(25);
        RowConstraints row3 = createRowConstraints(50);
        root.getColumnConstraints().addAll(column1, column2);
        root.getRowConstraints().addAll(row1, row2, row3);

        // Set the scene at the end.
        stage.setScene(new Scene(root));
    }

    /**
     * Begins playing the provided game.
     *
     * @param game The game to play.
     */
    private void playGame(Game game) {

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
