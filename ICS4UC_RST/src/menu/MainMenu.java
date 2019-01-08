package menu;

import games.pong.ui.PongUI;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
    
    private GridPane root;

    @Override
    public void start(Stage primaryStage) {
    	
        primaryStage.setTitle("Arcade");
        root = new GridPane();
        root.setPadding(new Insets(GAP));
        root.setVgap(GAP);
        root.setHgap(GAP);

        PongUI pong = new PongUI();
        pong.setPrefWidth(1000);
        root.add(pong, 0, 0);

        primaryStage.setScene(new Scene(root));

        primaryStage.show();

        pong.start();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
