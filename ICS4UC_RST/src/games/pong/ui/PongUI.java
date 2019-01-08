package games.pong.ui;

import games.Game;
import games.Score;
import games.player.PongKeyBinding;
import games.pong.Pong;
import games.pong.pieces.Paddle;
import games.pong.pieces.PongPiece;
import games.pong.pieces.Side;
import games.pong.players.PongKeyboardPlayer;
import games.pong.players.PongNetworkPlayer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import menu.MainMenu;

import java.util.HashMap;

/**
 * UI class for the pong game, for actually rendering the game for the user.
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
public class PongUI extends Pane implements Game {

    private static final double
            CYCLE_TIME = 5, // How long between ticks.
            FPS = 60; // Frames per second
    
    // load custom blocky font
    static {
    	Font.loadFont(MainMenu.class.getResource("../res/pong/pong.ttf").toExternalForm(), 10);
    }
    
    // Font used around the UI.
    private static final Font FONT = Font.font("Bit5x3", FontWeight.BOLD, FontPosture.REGULAR, 120);
    private Pong game;
    // How much the units in the pong game backend are scaled to make a nice looking UI.
    private double scaleFactor;

    private Rectangle ball;
    private Divider divider;

    private Rectangle leftPaddle;
    private Rectangle rightPaddle;
    private Scoreboard scoreboard;

    // Timers to be used when rendering the game to the user.
    private Timeline tickTimer, renderFrameTimer;

    /**
     * Constructs a new PongUI with the given width and height and Game object.
     */
    public PongUI() {
    	// set background
        setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
    	
        PongKeyboardPlayer p1 = new PongKeyboardPlayer(), p2 = new PongKeyboardPlayer();
        game = new Pong(p1, p2); // Initialize new pong game with the correct type of players.

        // TODO streamline automatic key bindings handled in the actual KeyboardPlayer class later.
        HashMap<KeyCode, PongKeyBinding> p2Bindings = new HashMap<>();
        p2Bindings.put(KeyCode.UP, PongKeyBinding.MOVE_UP);
        p2Bindings.put(KeyCode.DOWN, PongKeyBinding.MOVE_DOWN);

        HashMap<KeyCode, PongKeyBinding> p1Bindings = new HashMap<>();
        p1Bindings.put(KeyCode.Q, PongKeyBinding.MOVE_UP);
        p1Bindings.put(KeyCode.A, PongKeyBinding.MOVE_DOWN);
        p1.setKeyBindings(p1Bindings);
        p2.setKeyBindings(p2Bindings);

        p1.setOnPaddleDown((pongPlayer, move) -> movePaddleDown(pongPlayer.getPaddle(), move));
        p2.setOnPaddleDown((pongPlayer, move) -> movePaddleDown(pongPlayer.getPaddle(), move));
        p1.setOnPaddleUp((pongPlayer, move) -> movePaddleUp(pongPlayer.getPaddle(), move));
        p2.setOnPaddleUp((pongPlayer, move) -> movePaddleUp(pongPlayer.getPaddle(), move));

        // init paddles, ball and scoreboard
        leftPaddle = new Rectangle();
        leftPaddle.setFill(Color.WHITE);
        rightPaddle = new Rectangle();
        rightPaddle.setFill(Color.WHITE);
        
        divider = new Divider(Color.WHITE);
        
        ball = new Rectangle();
        ball.setFill(Color.WHITE);
        scoreboard = initializeScoreboard();
        
        // set the color for the paddles and ball
        
        
        getChildren().addAll(divider.getChildren());
        getChildren().addAll(leftPaddle, rightPaddle, ball, scoreboard);
        // Update the locations of the things we just created.
        updatePaddleLocations();
        updateBallLocation();

        setOnKeyPressed(this::keyPressed);
        setOnKeyReleased(this::keyReleased);

        prefWidthProperty().addListener((observable, oldValue, newValue) -> {
            // Only resize if the changed width is the same as the old width.
            if (!oldValue.equals(newValue)) {
                // Maintain the same height/width ratio.
                setPrefHeight(game.getBoardHeight() / game.getBoardWidth() * getPrefWidth());
            }
        });

        prefHeightProperty().addListener((observable, oldValue, newValue) -> {
            // Only resize if the changed width is the same as the old width.
            if (!oldValue.equals(newValue)) {
                // Maintain the same height/width ratio.
                setPrefWidth(game.getBoardWidth() / game.getBoardHeight() * getPrefHeight());
            }
        });
    }

    /**
     * Initializes the scoreboard object to be used in the game.
     *
     * @return The Scoreboard object.
     */
    private Scoreboard initializeScoreboard() {
        Scoreboard board = new Scoreboard();
        board.setFont(FONT);
        board.setFontFill(Color.WHITE);

        return board;
    }

    /**
     * Called when a key is pressed.
     *
     * @param event The keydown event.
     */
    private void keyPressed(KeyEvent event) {
        if (game.getLocalPlayer() instanceof PongKeyboardPlayer) {
            ((PongKeyboardPlayer) game.getLocalPlayer()).onKeyPressed(event.getCode());
        }
        if (game.getPlayer2() instanceof PongKeyboardPlayer) {
            ((PongKeyboardPlayer) game.getPlayer2()).onKeyPressed(event.getCode());
        }
    }

    /**
     * Called when a key is released.
     *
     * @param event The keyup event.
     */
    private void keyReleased(KeyEvent event) {
        if (game.getLocalPlayer() instanceof PongKeyboardPlayer) {
            ((PongKeyboardPlayer) game.getLocalPlayer()).onKeyReleased(event.getCode());
        }
        if (game.getPlayer2() instanceof PongKeyboardPlayer) {
            ((PongKeyboardPlayer) game.getPlayer2()).onKeyReleased(event.getCode());
        }
    }

    /**
     * Re-calculates the scale factor for rendering and such.
     */
    private void calculateScaleFactor() {
        scaleFactor = getWidth() / game.getBoardWidth();
        leftPaddle.setWidth(game.getLeftPaddle().getWidth() * scaleFactor);
        leftPaddle.setHeight(game.getLeftPaddle().getHeight() * scaleFactor);
        rightPaddle.setWidth(game.getRightPaddle().getWidth() * scaleFactor);
        rightPaddle.setHeight(game.getRightPaddle().getHeight() * scaleFactor);
        ball.setWidth(game.getBall().getWidth() * scaleFactor);
        ball.setHeight(game.getBall().getHeight() * scaleFactor);
        scoreboard.setLayoutX(getWidth() / 2 - scoreboard.getWidth() / 2);
        scoreboard.setLayoutY(getHeight() * 0.01);
        scoreboard.setSpacing(getWidth() * 0.2);
        divider.calculate(getWidth(), getHeight());
        updatePaddleLocations();
        updateBallLocation();
    }

    /**
     * Begins the game of pong.
     */
    @Override
    public void start() {
        requestFocus();
        calculateScaleFactor();
        tickTimer = new Timeline(new KeyFrame(Duration.millis(CYCLE_TIME), event -> tick()));
        tickTimer.setCycleCount(Timeline.INDEFINITE);
        renderFrameTimer = new Timeline(new KeyFrame(Duration.millis(1000.0 / FPS), event -> renderFrame()));
        renderFrameTimer.setCycleCount(Timeline.INDEFINITE);

        // Start all timelines.
        tickTimer.play();
        renderFrameTimer.play();
    }

    /**
     * Runs everything that needs to be run when the game ticks.
     */
    private void tick() {
        game.renderTick();
    }

    /**
     * Renders a new frame on screen.
     */
    private void renderFrame() {
        updateBallLocation();
        updatePaddleLocations();
        updateScoreboard();
    }

    /**
     * Updates the scoreboard display with the correct scores.
     */
    private void updateScoreboard() {
        scoreboard.setLeftScore(game.getLeftPlayer().getPoints());
        scoreboard.setRightScore(game.getRightPlayer().getPoints());
    }

    /**
     * Moves the given paddle down.
     *
     * @param paddle The paddle to be moved.
     * @param move   True to move the paddle, false to stop moving the paddle.
     */
    private void movePaddleDown(Paddle paddle, boolean move) {
        paddle.setVelY((move) ? -Pong.PADDLE_MOVEMENT_RATE : 0);
    }

    /**
     * Moves the given paddle up.
     *
     * @param paddle The paddle to be moved.
     * @param move   True to move the paddle, false to stop moving the paddle.
     */
    private void movePaddleUp(Paddle paddle, boolean move) {
        paddle.setVelY((move) ? Pong.PADDLE_MOVEMENT_RATE : 0);
    }

    /**
     * Updates the on-screen locations of the paddles.
     */
    private void updatePaddleLocations() {
        leftPaddle.setX(game.getLeftPaddle().getX() * scaleFactor);
        leftPaddle.setY(transformY(game.getBoardHeight(), game.getLeftPaddle(), Side.TOP) * scaleFactor);
        rightPaddle.setX(game.getRightPaddle().getX() * scaleFactor);
        rightPaddle.setY(transformY(game.getBoardHeight(), game.getRightPaddle(), Side.TOP) * scaleFactor);
    }

    /**
     * Updates hte on-screen location of the pong ball.
     */
    private void updateBallLocation() {
        ball.setX(game.getBall().getX(Side.RIGHT) * scaleFactor);
        ball.setY(transformY(game.getBoardHeight(), game.getBall(), Side.TOP) * scaleFactor);
    }

    /**
     * Translates a y value from being measured from the bottom to the top to top to bottom.
     *
     * @param boardHeight The height of the pong board.
     * @param piece       The piece whose coordinates shall be transformed.
     * @param desiredSide The desired coordinate (Side.TOP, Side.CENTER, Side.BOTTOM) that needs to be calculated.
     * @return The transformed value.
     */
    public static double transformY(double boardHeight, PongPiece piece, Side desiredSide) {
        double newY;
        double topLeftY = boardHeight - piece.getY(Side.TOP);
        switch (desiredSide) {
            case TOP:
                newY = topLeftY;
                break;
            case BOTTOM:
                newY = topLeftY + piece.getHeight();
                break;
            case CENTER:
                newY = topLeftY + piece.getHeight() / 2;
                break;
            default:
                throw new IllegalArgumentException("Invalid selection for position.");
        }

        return newY;
    }

    @Override
    public void end() {
        tickTimer.stop();
        renderFrameTimer.stop();
    }

    @Override
    public Score getScore() {
        return null;
    }

    @Override
    public Image getCoverArt() {
        return null;
    }

    @Override
    public String getName() {
        return "Pong";
    }

    /**
     * Determines if this game is a network game (if one of the players is playing with this one online).
     *
     * @return True if this is a network game, false otherwise.
     */
    @Override
    public boolean isNetworkGame() {
        return game.getPlayer2() instanceof PongNetworkPlayer || game.getLocalPlayer() instanceof PongNetworkPlayer;
    }
}
