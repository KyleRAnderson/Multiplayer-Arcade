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
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.HashMap;

/**
 * UI class for the pong game, for actually rendering the game for the user.
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
public class PongUI extends Pane implements Game {
    private Pong game;
    // How much the units in the pong game backend are scaled to make a nice looking UI.
    private double scaleFactor;

    private Circle ball;

    private Rectangle leftPaddle;
    private Rectangle rightPaddle;

    /**
     * Constructs a new PongUI with the given width and height and Game object.
     */
    public PongUI() {
        PongKeyboardPlayer p1 = new PongKeyboardPlayer(), p2 = new PongKeyboardPlayer();
        game = new Pong(p1, p2); // Initialize new pong game with the correct type of players.

        // TODO streamline automatic keybindings handled in the actual KeyboardPlayer class later.
        HashMap<KeyCode, PongKeyBinding> p1Bindings = new HashMap<>();
        p1Bindings.put(KeyCode.UP, PongKeyBinding.MOVE_UP);
        p1Bindings.put(KeyCode.DOWN, PongKeyBinding.MOVE_DOWN);

        HashMap<KeyCode, PongKeyBinding> p2Bindings = new HashMap<>();
        p2Bindings.put(KeyCode.Q, PongKeyBinding.MOVE_UP);
        p2Bindings.put(KeyCode.A, PongKeyBinding.MOVE_DOWN);
        p1.setKeyBindings(p1Bindings);
        p2.setKeyBindings(p2Bindings);

        p1.setOnPaddleDown(pongPlayer -> movePaddleDown(pongPlayer.getPaddle()));
        p2.setOnPaddleDown(pongPlayer -> movePaddleDown(pongPlayer.getPaddle()));
        p1.setOnPaddleUp(pongPlayer -> movePaddleUp(pongPlayer.getPaddle()));
        p2.setOnPaddleUp(pongPlayer -> movePaddleUp(pongPlayer.getPaddle()));

        leftPaddle = new Rectangle();
        rightPaddle = new Rectangle();
        ball = new Circle();
        getChildren().addAll(leftPaddle, rightPaddle, ball);
        // Update the locations of the things we just created.
        updatePaddleLocations();
        updateBallLocation();
    }

    @Override
    protected void setWidth(double value) {
        super.setWidth(value);
        // Only perform resizing if the ratio is dramatically off.
        if (getHeight() / getWidth() - (double) game.getBoardHeight() / (double) game.getBoardWidth() > 1) {
            // Calculate how much the height should be if we keep the same ratio between height and width.
            setHeight((double) game.getBoardHeight() / (double) game.getBoardWidth() * getWidth());
        }
        calculateScaleFactor();
    }


    @Override
    protected void setHeight(double value) {
        super.setHeight(value);
        // Only perform resizing if the ratio is dramatically off.
        if (getWidth() / getHeight() - (double) game.getBoardWidth() / (double) game.getBoardHeight() > 1) {
            // Calculate how much the height should be if we keep the same ratio between height and width.
            setHeight((double) game.getBoardWidth() / (double) game.getBoardHeight() * getHeight());
        }
        calculateScaleFactor();
    }

    /**
     * Re-calculates the scale factor for rendering and such.
     */
    private void calculateScaleFactor() {
        scaleFactor = getWidth() / (double) game.getBoardWidth();

        leftPaddle.setWidth(game.getLeftPaddle().getWidth() * scaleFactor);
        leftPaddle.setHeight(game.getLeftPaddle().getHeight() * scaleFactor);
        rightPaddle.setWidth(game.getRightPaddle().getWidth() * scaleFactor);
        rightPaddle.setHeight(game.getRightPaddle().getHeight() * scaleFactor);
        ball.setRadius(game.getBall().getRadius() * scaleFactor);

        updatePaddleLocations();
        updateBallLocation();
    }

    /**
     * Begins the game of pong.
     */
    @Override
    public void start() {
        calculateScaleFactor();
        Timeline tickTimer = new Timeline(new KeyFrame(Duration.millis(10), event -> tick()));
        tickTimer.setCycleCount(Timeline.INDEFINITE);
        tickTimer.play();
    }

    /**
     * Runs everything that needs to be run when the game ticks.
     */
    private void tick() {
        game.renderTick();
        updateBallLocation();
        updatePaddleLocations();
    }

    /**
     * Moves the given paddle down.
     *
     * @param paddle The paddle to be moved.
     */
    private void movePaddleDown(Paddle paddle) {
        game.paddleDown(paddle);
    }

    /**
     * Moves the given paddle up.
     *
     * @param paddle The paddle to be moved.
     */
    private void movePaddleUp(Paddle paddle) {
        game.paddleUp(paddle);
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
    public static double transformY(int boardHeight, PongPiece piece, Side desiredSide) {
        double newY;
        double topLeftY = boardHeight - piece.getY(Side.TOP);
        switch (desiredSide) {
            case TOP:
                newY = topLeftY;
                break;
            case BOTTOM:
                newY = topLeftY + (double) piece.getHeight();
                break;
            case CENTER:
                newY = topLeftY + (double) piece.getHeight() / 2;
                break;
            default:
                throw new IllegalArgumentException("Invalid selection for position.");
        }

        return newY;
    }

    @Override
    public boolean isInProgress() {
        return false;
    }

    @Override
    public void end() {
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
