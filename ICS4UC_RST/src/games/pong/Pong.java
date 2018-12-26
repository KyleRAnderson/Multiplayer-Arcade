package games.pong;

import com.sun.istack.internal.NotNull;
import games.pong.pieces.Paddle;
import games.pong.pieces.PongPiece;
import games.pong.pieces.Side;
import games.pong.pieces.ball.PongBall;
import games.pong.players.PongKeyboardPlayer;
import games.pong.players.PongPlayer;

/**
 * @author Kyle Anderson
 * ICS4U RST
 */
public class Pong {
    // Default width and height for the pong game.
    private static final int WIDTH = 200, HEIGHT = 150;
    // How far the paddles are from the side.
    private static final int PADDLE_DISTANCE = 2;
    /**
     * Maximum ball rebound angle, in degrees.
     */
    private static final int MAX_REBOUND_ANGLE = 75;
    // Ratios for distances and speeds.
    private static double
            BALL_RADIUS_RATIO = 0.01,
            PADDLE__SCREEN_HEIGHT__RATIO = 0.15, // Ratio between the paddle size (height) and the screen height.
            PADDLE_MOVEMENT_RATIO = 0.5; // How many units the paddle moves while the button is being held down.

    private final PongBall ball;

    /**
     * Gets the local player for the game.
     *
     * @return The local game player.
     */
    public PongPlayer getLocalPlayer() {
        return localPlayer;
    }

    /**
     * Gets the second player in the game.
     *
     * @return The second player.
     */
    public PongPlayer getPlayer2() {
        return player2;
    }

    private final PongPlayer localPlayer;
    private final PongPlayer player2;

    private final double width, height;

    /**
     * Constructs a new pong game with the given players.
     *
     * @param localPlayer The local player, who will use keyboard and mouse input.
     * @param player2     The second player, either a bot, another keyboard player or a network player.
     */
    public Pong(PongKeyboardPlayer localPlayer, PongPlayer player2) {
        this(localPlayer, player2, WIDTH, HEIGHT);
    }

    /**
     * Instantiates a new pong game.
     *
     * @param localPlayer The local player (the player playing on this computer).
     * @param player2     The other player. Either a bot or a network player pretty much.
     * @param width       The width of the board.
     * @param height      The height of the board.
     */
    public Pong(@NotNull PongPlayer localPlayer, @NotNull PongPlayer player2, int width, int height) {
        this.width = width;
        this.height = height;
        this.localPlayer = localPlayer;
        this.player2 = player2;

        // The ball should be a certain ration to the size of the board's diagonal dimension.
        ball = new PongBall((int) Math.floor(getScreenSize() * BALL_RADIUS_RATIO));

        // Ensure that the players have their sides set up.

        if (localPlayer.getSide() == null) {
            if (player2.getSide() == Side.LEFT) {
                localPlayer.setSide(Side.RIGHT);
            } else if (player2.getSide() == Side.RIGHT) {
                localPlayer.setSide(Side.LEFT);
            } else {
                // Default first player to the le ft side.
                localPlayer.setSide(Side.LEFT);
            }
        }
        if (player2.getSide() == null) {
            if (localPlayer.getSide() == Side.LEFT) {
                player2.setSide(Side.RIGHT);
            } else if (player2.getSide() == Side.RIGHT) {
                player2.setSide(Side.LEFT);
            } else {
                // Default second player to the right side.
                player2.setSide(Side.RIGHT);
            }
        }

        // Needs to be called after players have a side.
        setupPaddles();
        // Send the ball to the person on the right first
        resetBall(Side.RIGHT);
    }

    /**
     * Gets the width of the board.
     *
     * @return The width of the board.
     */
    public double getBoardWidth() {
        return width;
    }

    /**
     * Gets the height of the board.
     *
     * @return The height of the board.
     */
    public double getBoardHeight() {
        return height;
    }

    /**
     * Sets up paddles for the players, instantiating the Paddle objects if necessary and putting them back in the
     * start (center) position.
     */
    private void setupPaddles() {
        PongPlayer leftPlayer = getLeftPlayer();
        PongPlayer rightPlayer = getRightPlayer();
        if (leftPlayer.getPaddle() == null) {
            getLeftPlayer().setPaddle(new Paddle(PADDLE_DISTANCE, (int) Math.floor(getBoardHeight() * PADDLE__SCREEN_HEIGHT__RATIO), Side.LEFT));
        }
        if (rightPlayer.getPaddle() == null) {
            getRightPlayer().setPaddle(new Paddle(PADDLE_DISTANCE, (int) Math.floor(getBoardHeight() * PADDLE__SCREEN_HEIGHT__RATIO), Side.RIGHT));
        }

        leftPlayer.getPaddle().setX(PADDLE_DISTANCE);
        leftPlayer.getPaddle().setY(getBoardHeight() / 2, Side.CENTER);
        rightPlayer.getPaddle().setX(getBoardWidth() - PADDLE_DISTANCE, Side.RIGHT);
        rightPlayer.getPaddle().setY(getBoardHeight() / 2, Side.CENTER);
    }

    /**
     * Calculates the screen size of a pong board with the given width and height.
     *
     * @return The screen size (diagonally) of the pong board.
     */
    public double getScreenSize() {
        return getScreenSize(width, height);
    }

    /**
     * Calculates the screen size of a pong board with the given width and height.
     *
     * @param width  The width of the pong board.
     * @param height The height of hte pong board.
     * @return The screen size (diagonally) of the pong board.
     */
    public static double getScreenSize(double width, double height) {
        return Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
    }

    /**
     * The last tick time in seconds.
     */
    private long lastTickTime = 0;

    /**
     * Completes a tick of the game.
     */
    public void renderTick() {
        long tempLastTick = lastTickTime;
        lastTickTime = System.currentTimeMillis();
        final long timeSinceLastTick = System.currentTimeMillis() - tempLastTick;
        ball.renderTick(timeSinceLastTick); // Render a tick for the ball.

        Paddle rightPaddle = getRightPaddle(), leftPaddle = getLeftPaddle();
        rightPaddle.setY(rightPaddle.getY() + rightPaddle.getVelY() / 1000 * timeSinceLastTick);
        leftPaddle.setY(leftPaddle.getY() + leftPaddle.getVelY() / 1000 * timeSinceLastTick);
        rightPaddle.setX(rightPaddle.getX() + rightPaddle.getVelX() / 1000 * timeSinceLastTick);
        leftPaddle.setX(leftPaddle.getX() + leftPaddle.getVelX() / 1000 * timeSinceLastTick);

        // Check if the ball has hit the vertical bounds of the board.
        checkBallBounds();
        // Make sure the paddle is in bounds as well.
        checkPaddleBounds();

        // Check if the ball has hit one of the paddles
        renderBallCollision();
    }

    /**
     * Renders any collisions of the ball with the pong paddle.
     */
    private void renderBallCollision() {
        Paddle touchedPaddle;
        if (doesIntersect(ball, touchedPaddle = getLeftPlayer().getPaddle())) {
            applyNewBallVelocity(touchedPaddle);
        } else if (doesIntersect(ball, touchedPaddle = getRightPlayer().getPaddle())) {
            applyNewBallVelocity(touchedPaddle);
        }
    }

    /**
     * Calculates and applies the new ball velocity for the ball after hitting a paddle.
     *
     * @param paddleHit The paddle with which the ball entered a collision.
     */
    private void applyNewBallVelocity(Paddle paddleHit) {
        final double centerPaddle = paddleHit.getY(Side.CENTER);
        final double centerBall = ball.getY(Side.CENTER);

        /* Get the difference between the center y-value of the ball and the paddle.
        If ball is higher, this will be positive, else it will be negative.*/
        final double difference = centerBall - centerPaddle;

        /* Get the percentage difference (how far the centers
        are from each other as a percentage of the maximum possible distance).*/
        double percentageDifference = (double) difference / ((double) paddleHit.getHeight() / 2);
        double angle = percentageDifference * (double) MAX_REBOUND_ANGLE;

        // Go ahead and set the ball's velocity.
        ball.setVelocity(angle, (paddleHit.getSide() == Side.LEFT) ? Side.RIGHT : Side.LEFT);
    }

    /**
     * Determines if the two given rectangular pong pieces intersect or not.
     *
     * @param piece1 The first piece.
     * @param piece2 The second piece.
     * @return True if the pieces intersect (overlap), false otherwise.
     */
    private boolean doesIntersect(PongPiece piece1, PongPiece piece2) {
        return !(piece1.getX(Side.RIGHT) < piece2.getX(Side.LEFT) ||
                piece1.getX(Side.LEFT) > piece2.getX(Side.RIGHT) ||
                piece1.getY(Side.BOTTOM) > piece2.getY(Side.TOP) ||
                piece1.getY(Side.TOP) < piece2.getY(Side.BOTTOM));
    }

    /**
     * Checks that the pong ball is in bounds.
     */
    private void checkBallBounds() {
        // Check the top and bottom points to ensure that the ball has not hit a horizontal barrier.
        if (ball.getY(Side.TOP) >= HEIGHT) {
            ball.setVelocity(-Math.abs(ball.getRisePerSecond()), ball.getRunPerSecond());
        } else if (ball.getY(Side.BOTTOM) <= 0) {
            ball.setVelocity(Math.abs(ball.getRisePerSecond()), ball.getRunPerSecond());
        }

        // Now check to see if the ball has hit a vertical barrier.
        if (ball.getX(Side.LEFT) <= 0) {
            getLeftPlayer().addPoint();
            resetBall(Side.LEFT);
        }
        // If the ball hit the left side, then add to the player on the right.
        else if (ball.getX(Side.RIGHT) >= WIDTH) {
            getRightPlayer().addPoint();
            resetBall(Side.RIGHT);
        }
    }

    /**
     * Checks each of the paddles in the game to ensure that they are within the boundaries of the
     * board and places them back in the boundaries if they are not.
     */
    private void checkPaddleBounds() {
        Paddle[] paddles = new Paddle[] { getLeftPaddle(), getRightPaddle() };

        // Iterate through all of the paddles checking their boundaries.
        for (Paddle paddle : paddles) {
            if (paddle.getY(Side.TOP) > getBoardHeight()) {
                paddle.setY(getBoardHeight(), Side.TOP);
            }
            if (paddle.getY(Side.BOTTOM) < 0) {
                paddle.setY(0, Side.BOTTOM);
            }
            if (paddle.getX(Side.RIGHT) > getBoardWidth()) {
                paddle.setX(getBoardWidth(), Side.RIGHT);
            }
            if (paddle.getX(Side.LEFT) < 0) {
                paddle.setX(0, Side.LEFT);
            }
        }
    }

    /**
     * Resets the position and velocity of the ball after a player scores a point.
     *
     * @param scoringPlayer The player who just scored a point, left or right.
     */
    private void resetBall(final Side scoringPlayer) {
        if (!(scoringPlayer == Side.LEFT || scoringPlayer == Side.RIGHT)) {
            throw new IllegalArgumentException("Scoring player side must be either left or right.");
        }

        ball.setX(getBoardWidth() / 2, Side.CENTER);
        ball.setY(getBoardHeight() / 2, Side.CENTER);
        ball.setVelocity(0, (scoringPlayer == Side.LEFT) ? -PongBall.VELOCITY : PongBall.VELOCITY);
    }

    /**
     * Gets the player on the right side of the board, controlling the right paddle.
     *
     * @return The player on the right side.
     */
    private PongPlayer getRightPlayer() {
        PongPlayer player;
        if (localPlayer.getSide() == Side.RIGHT) {
            player = localPlayer;
        } else {
            player = player2;
        }
        return player;
    }

    /**
     * Gets the player on the left side of the board, controlling the left paddle.
     *
     * @return The player on the left side.
     */
    private PongPlayer getLeftPlayer() {
        PongPlayer player;
        if (localPlayer.getSide() == Side.LEFT) {
            player = localPlayer;
        } else {
            player = player2;
        }
        return player;
    }

    /**
     * Moves the given paddle down. Recommend using {@link games.pong.pieces.Paddle#setVelY(double)} instead for smoother movements.
     *
     * @param paddle The paddle to be moved.
     */
    public void paddleDown(Paddle paddle) {
        paddle.setY(paddle.getY() - getBoardHeight() * PADDLE_MOVEMENT_RATIO);
    }

    /**
     * Moves the given paddle up.
     *
     * @param paddle The paddle to be moved.
     */
    public void paddleUp(Paddle paddle) {
        paddle.setY(paddle.getY() + 1);
    }

    /**
     * Gets the pong ball for this game.
     *
     * @return The ball.
     */
    public PongBall getBall() {
        return this.ball;
    }

    /**
     * Gets the paddle on the right.
     * @return The right paddle.
     */
    public Paddle getRightPaddle() {
        return getRightPlayer().getPaddle();
    }

    /**
     * Gets the left paddle.
     * @return The paddle on the left of the board.
     */
    public Paddle getLeftPaddle() {
        return getLeftPlayer().getPaddle();
    }

    /**
     * Gets the rate (in units/second) at which a paddle should rightly move up or down.
     * @return The speed, in units/second.
     */
    public double getPaddleVelocity() {
        return PADDLE_MOVEMENT_RATIO * getBoardHeight();
    }
}
