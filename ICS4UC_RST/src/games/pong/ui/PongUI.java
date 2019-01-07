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
import games.pong.players.PongPlayer;
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
import javafx.scene.text.Text;
import javafx.util.Duration;
import network.party.PartyHandler;
import network.party.PartyRole;

import java.util.ArrayList;
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
    // Font used around the UI.
    private static final Font FONT = Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 24);
    private Pong game;
    // How much the units in the pong game backend are scaled to make a nice looking UI.
    private double scaleFactor;

    private Circle ball;

    private Rectangle leftPaddle;
    private Rectangle rightPaddle;
    private Scoreboard scoreboard;

    // Set up key bindings list.
    private ArrayList<HashMap<KeyCode, PongKeyBinding>> keyBindings;

    // Timers to be used when rendering the game to the user.
    private Timeline tickTimer, renderFrameTimer;

    /**
     * Constructs a new PongUI with the given width and height and Game object.
     */
    public PongUI() {
        reset();

        leftPaddle = new Rectangle();
        rightPaddle = new Rectangle();
        ball = new Circle();
        scoreboard = initializeScoreboard();
        getChildren().addAll(leftPaddle, rightPaddle, ball, scoreboard);
        setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        setOnKeyPressed(this::keyPressed);
        setOnKeyReleased(this::keyReleased);

        prefWidthProperty().addListener((observable, oldValue, newValue) -> {
            // Only resize if the changed width is the same as the old width.
            if (!oldValue.equals(newValue)) {
                // Maintain the same height/width ratio.
                setPrefHeight(game.getBoardHeight() / game.getBoardWidth() * getPrefWidth());
                calculateScaleFactor();
            }
        });

        prefHeightProperty().addListener((observable, oldValue, newValue) -> {
            // Only resize if the changed width is the same as the old width.
            if (!oldValue.equals(newValue)) {
                // Maintain the same height/width ratio.
                setPrefWidth(game.getBoardWidth() / game.getBoardHeight() * getPrefHeight());
                calculateScaleFactor();
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
        board.setFontFill(Color.RED);
        board.setSpacing(20); // Set a certain amount of space between the numbers on the scoreboard.

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
        scaleFactor = getWorkingWidth() / game.getBoardWidth();

        leftPaddle.setWidth(game.getLeftPaddle().getWidth() * scaleFactor);
        leftPaddle.setHeight(game.getLeftPaddle().getHeight() * scaleFactor);
        rightPaddle.setWidth(game.getRightPaddle().getWidth() * scaleFactor);
        rightPaddle.setHeight(game.getRightPaddle().getHeight() * scaleFactor);
        ball.setRadius(game.getBall().getRadius() * scaleFactor);
        scoreboard.setLayoutX(getWorkingWidth() / 2 - scoreboard.getWidth() / 2);
        scoreboard.setLayoutY(getWorkingHeight() * 0.01);

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
     * Gets the workable area of width for this game.
     *
     * @return The workable size of width.
     */
    private double getWorkingWidth() {
        return getWidth();
    }

    /**
     * Gets the workable area of height for this game.
     *
     * @return The workable size of height.
     */
    private double getWorkingHeight() {
        return getHeight();
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
        ball.setCenterX(game.getBall().getX(Side.CENTER) * scaleFactor);
        ball.setCenterY(transformY(game.getBoardHeight(), game.getBall(), Side.CENTER) * scaleFactor);
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
        return new Image(getClass().getResource("/res/pong/coverart.png").toString());
    }

    @Override
    public String getName() {
        return "Pong";
    }

    @Override
    public Text getTextDisplay() {
        Text text = new Text(getName());
        text.setFont(Font.font("Consolas", FontWeight.BLACK, FontPosture.REGULAR, 72));
        text.setFill(Color.ORANGE);

        return text;
    }

    /**
     * Determines if this game is a network game (if one of the players is playing with this one online).
     *
     * @return True if this is a network game, false otherwise.
     */
    @Override
    public boolean isNetworkGame() {
        return game.getPlayer2() instanceof PongNetworkPlayer;
    }

    @Override
    public PongUI createNew() {
        return new PongUI();
    }

    @Override
    public PongUI getWindow() {
        return this;
    }

    @Override
    public void reset() {
        game = new Pong(); // Initialize new pong game with the correct type of players
        resetKeyBindings();
    }

    /**
     * Resets the key bindings.
     */
    private void resetKeyBindings() {
        keyBindings = new ArrayList<>();

        HashMap<KeyCode, PongKeyBinding> p1Bindings = new HashMap<>();
        p1Bindings.put(KeyCode.UP, PongKeyBinding.MOVE_UP);
        p1Bindings.put(KeyCode.DOWN, PongKeyBinding.MOVE_DOWN);

        HashMap<KeyCode, PongKeyBinding> p2Bindings = new HashMap<>();
        p2Bindings.put(KeyCode.Q, PongKeyBinding.MOVE_UP);
        p2Bindings.put(KeyCode.A, PongKeyBinding.MOVE_DOWN);

        keyBindings.add(p1Bindings);
        keyBindings.add(p2Bindings);
    }

    /**
     * Initializes the players in the game, if not already done.
     */
    @Override
    public void initializePlayers() {
        PongPlayer p1 = game.getLocalPlayer(), p2 = game.getPlayer2();

        if (p1 == null) {
            p1 = new PongKeyboardPlayer();
        }
        if (p2 == null) {
            p2 = new PongKeyboardPlayer();
        }
        if (p1 instanceof PongKeyboardPlayer) {
            PongKeyboardPlayer player = new PongKeyboardPlayer();
            setupBindings(player);
            p1 = player;
            game.setLocalPlayer(p1);
        }
        if (p2 instanceof PongKeyboardPlayer) {
            PongKeyboardPlayer player = new PongKeyboardPlayer();
            setupBindings(player);
            p2 = player;
            game.setPlayer2(p2);
        }

        game.initialize(); // Initialize pong game now that players are set up.

        p1.setOnPaddleDown((pongPlayer, move) -> movePaddleDown(game.getPaddle(pongPlayer), move));
        p2.setOnPaddleDown((pongPlayer, move) -> movePaddleDown(game.getPaddle(pongPlayer), move));
        p1.setOnPaddleUp((pongPlayer, move) -> movePaddleUp(game.getPaddle(pongPlayer), move));
        p2.setOnPaddleUp((pongPlayer, move) -> movePaddleUp(game.getPaddle(pongPlayer), move));
    }

    /**
     * Sets up key bindings for the given player.
     * @param player The player to set up.
     */
    private void setupBindings(PongKeyboardPlayer player) {
        HashMap<KeyCode, PongKeyBinding> bindings = keyBindings.get(0);
        keyBindings.remove(0);
        player.setKeyBindings(bindings);
    }

    @Override
    public void setNetworkGame() {
        PongKeyboardPlayer p1 = new PongKeyboardPlayer();
        PongNetworkPlayer p2 = new PongNetworkPlayer();

        game.setLocalPlayer(p1);
        game.setPlayer2(p2);

        // Set the side depending on who's hosting.
        if (PartyHandler.getRole() == PartyRole.SERVER) {
            game.getLocalPlayer().setSide(Side.RIGHT);
        } else {
            game.getLocalPlayer().setSide(Side.LEFT);
        }
    }

    /**
     * Gets the network player playing this game.
     * @return The PongNetworkPlayer in this game, or null if there isn't one.
     */
    @Override
    public PongNetworkPlayer getNetworkPlayer() {
        PongNetworkPlayer player = null;
        if (isNetworkGame()) {
            player = (PongNetworkPlayer)game.getPlayer2();
        }
        return player;
    }
}
