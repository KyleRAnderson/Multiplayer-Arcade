package games.pong;

import com.sun.istack.internal.NotNull;
import games.pong.pieces.Paddle;
import games.pong.pieces.PongBall;
import games.pong.pieces.PongPiece;
import games.pong.pieces.Side;
import games.pong.players.PongKeyboardPlayer;
import games.pong.players.PongPlayer;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Class to render all pong game calculations and logic.
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
public class Pong {
    // Default width and height for the pong game.
    private static final int WIDTH = 512, HEIGHT = 256;
    // How far the paddles are from the side.
    private static final int PADDLE_DISTANCE = 5, PADDLE_WIDTH = 2, PADDLE_HEIGHT = 40;

    /**
     * Maximum ball rebound angle, in degrees.
     */
    private static final double MAX_REBOUND_ANGLE = 75;
    // Ratios for distances and speeds.
    private static final double BALL_RADIUS = 4;
    // How many units per second the paddle moves while the button is being held down.
    public static final double PADDLE_MOVEMENT_RATE = 200;
    // Velocity of the pong ball in units per second.
    public static final double PONG_BALL_VELOCITY = 250;
    // How many milliseconds to pause after a player scores.
    private static final long SCORE_PAUSE = 3000; // Set score pause to 3 seconds.
    /**
     * Number of points needed to win.
     */
    private static final int WINNING_POINTS = 11;

    private final PongBall ball;

    private PongPlayer localPlayer;
    private PongPlayer player2;

    private Paddle leftPaddle;
    private Paddle rightPaddle;

    private final double width, height;

    // Listener for when the ball collides.
    private final ArrayList<Consumer<PongEvent>> pongEventListeners = new ArrayList<>();

    /**
     * The last tick time in nanoseconds.
     */
    private long lastTickTime = 0;

    private Paddle lastHitPaddle;

    /**
     * True if the game has started, false otherwise.
     */
    private boolean hasBegun;
    private boolean readyNotified;

    /**
     * The time at which the pause should end, in milliseconds.
     */
    private long unpauseTime;

    /**
     * Whether or not the game has ended.
     */
    private boolean ended;
    /**
     * The reason for which the game was ended.
     */
    private EndReason endReason;
    /**
     * An action to be run after each tick.
     */
    private ArrayList<Runnable> tickActions = new ArrayList<>();

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
     * Initializes the most basic game of pong.
     */
    public Pong() {
        this(WIDTH, HEIGHT);
    }

    /**
     * Instantiates a new pong game.
     *
     * @param localPlayer The local player (the player playing on this computer).
     * @param player2     The other player. Either a bot or a network player pretty much.
     * @param width       The width of the board.
     * @param height      The height of the board.
     */
    public Pong(@NotNull PongPlayer localPlayer, @NotNull PongPlayer player2, final int width, final int height) {
        this(width, height);
        setLocalPlayer(localPlayer);
        setPlayer2(player2);
    }

    /**
     * Instantiates a new pong game.
     *
     * @param width  The width of the board.
     * @param height The height of the board.
     */
    public Pong(final int width, final int height) {
        this.width = width;
        this.height = height;

        // The ball should be a certain ration to the size of the board's diagonal dimension.
        ball = new PongBall((int) Math.floor(BALL_RADIUS));

        setupPaddles();
        // Send the ball to the person on the right first
        resetBall(Side.RIGHT);
    }

    /**
     * Initializes the pong game, setting up anything that needs to be set before playing the game.
     */
    public void initialize() {
        // Ensure that the players have their sides set up.
        if (localPlayer.getSide() == null) {
            if (player2.getSide() == Side.LEFT) {
                localPlayer.setSide(Side.RIGHT);
            } else if (player2.getSide() == Side.RIGHT) {
                localPlayer.setSide(Side.LEFT);
            } else {
                // Default first player to the left side.
                localPlayer.setSide(Side.LEFT);
            }
        }
        if (player2.getSide() == null) {
            if (localPlayer.getSide() == Side.LEFT) {
                player2.setSide(Side.RIGHT);
            } else if (localPlayer.getSide() == Side.RIGHT) {
                player2.setSide(Side.LEFT);
            } else {
                // Default second player to the right side.
                player2.setSide(Side.RIGHT);
            }
        }
    }

    /**
     * Starts the game, letting ticks render.
     */
    public void begin() {
        hasBegun = true;
        setPauseDuration(SCORE_PAUSE);
        callEvent(new PongEvent(PongEvent.EventType.GAME_BEGUN));
    }

    /**
     * Determines if this game has begun.
     *
     * @return True if the game has started, false otherwise.
     */
    public boolean hasBegun() {
        return hasBegun;
    }

    /**
     * Gets the local player for the game.
     *
     * @return The local game player.
     */
    public PongPlayer getLocalPlayer() {
        return localPlayer;
    }

    /**
     * Sets the PongPlayer for the local player.
     *
     * @param player The local player.
     */
    public void setLocalPlayer(PongPlayer player) {
        this.localPlayer = player;
        this.localPlayer.setGame(this);
    }

    /**
     * Gets the second player in the game.
     *
     * @return The second player.
     */
    public PongPlayer getPlayer2() {
        return player2;
    }

    /**
     * Sets the second player.
     *
     * @param player The second player.
     */
    public void setPlayer2(PongPlayer player) {
        this.player2 = player;
        this.player2.setGame(this);
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
        if (leftPaddle == null) {
            leftPaddle = new Paddle(PADDLE_WIDTH, PADDLE_HEIGHT, Side.LEFT);
        }
        if (rightPaddle == null) {
            rightPaddle = new Paddle(PADDLE_WIDTH, PADDLE_HEIGHT, Side.RIGHT);
        }

        leftPaddle.setX(PADDLE_DISTANCE);
        leftPaddle.setY(getBoardHeight() / 2, Side.CENTER);
        rightPaddle.setX(getBoardWidth() - PADDLE_DISTANCE, Side.RIGHT);
        rightPaddle.setY(getBoardHeight() / 2, Side.CENTER);
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
     * Completes a tick of the game.
     *
     * @param timeSinceLastTick The time since the last tick, in nanoseconds.
     */
    public void renderTick(final long timeSinceLastTick) {
        if (hasBegun && !checkPause() && !ended) {
            ball.renderTick(timeSinceLastTick); // Render a tick for the ball.

            getRightPaddle().renderTick(timeSinceLastTick);
            getLeftPaddle().renderTick(timeSinceLastTick);

            // Check if the ball has hit one of the paddles
            renderBallCollision(timeSinceLastTick);
            // Check if the ball has hit the vertical bounds of the board.
            checkBallBounds();
            // Make sure the paddle is in bounds as well.
            checkPaddleBounds();

            if (tickActions.size() > 0) {
                tickActions.forEach(Runnable::run);
            }
        } else if (!hasBegun && !readyNotified) {
            callEvent(new PongEvent(PongEvent.EventType.GAME_READY));
            readyNotified = true;
        }
    }

    /**
     * Completes a tick of the game.
     */
    public void renderTick() {
        long tempLastTick = lastTickTime;
        lastTickTime = System.nanoTime(); // Set last tick time to now.
        final long timeSinceLastTick = (tempLastTick > 0) ? lastTickTime - tempLastTick : 0;
        renderTick(timeSinceLastTick);
    }

    /**
     * Sets an action to be run at the end of each tick. The action should run quickly so as not to delay the ticks.
     *
     * @param action The action to be run.
     */
    public void setOnTick(Runnable action) {
        tickActions.add(action);
    }

    /**
     * Gets the last time at which a tick was calculated.
     *
     * @return The last tick time, in nanoseconds.
     */
    public long getLastTickTime() {
        return lastTickTime;
    }

    /**
     * Renders any collisions of the ball with the pong paddle.
     *
     * @param nanosPassed The time passed (in nanoseconds) since the last tick.
     */
    private void renderBallCollision(final long nanosPassed) {
        Paddle touchedPaddle;
        if (testBallCollision(touchedPaddle = leftPaddle, nanosPassed)) {
            callBallCollided(touchedPaddle);
        } else if (testBallCollision(touchedPaddle = rightPaddle, nanosPassed)) {
            callBallCollided(touchedPaddle);
        }
    }

    /**
     * Sets a consumer to be invoked when the ball enters a collision with a paddle.
     *
     * @param listener The listener for the action. Accepts a parameter containing the PongEvent data.
     */
    public void addEventListener(Consumer<PongEvent> listener) {
        pongEventListeners.add(listener);
    }

    /**
     * Notifies listeners that the ball has collided with a paddle.
     *
     * @param touchedPaddle The paddle that the ball collided with.
     */
    public void callBallCollided(Paddle touchedPaddle) {
        // Don't call the event twice for the same paddle.
        if (pongEventListeners.size() > 0 && touchedPaddle != lastHitPaddle) {
            lastHitPaddle = touchedPaddle;
            PongEvent event = new PongEvent(PongEvent.EventType.BALL_HIT_PADDLE);
            event.setBall(getBall());
            event.setPaddle(touchedPaddle);
            callEvent(event);
        }
    }

    /**
     * Forms the right event to notify listeners that a player has scored.
     *
     * @param player The player that scored.
     */
    private void callPlayerScored(PongPlayer player) {
        if (pongEventListeners.size() > 0) {
            PongEvent event = new PongEvent(PongEvent.EventType.PLAYER_SCORED);
            event.setPlayer(player);
            callEvent(event);
        }
    }


    /**
     * Calls a paddle movement event. This indicates that a paddle has started moving.
     *
     * @param paddle    The paddle being moved.
     * @param direction The direction of the paddle's movement. Positive for up, negative for down, 0 for stopped.
     */
    private void callPaddleMoved(Paddle paddle, final int direction) {
        if (pongEventListeners.size() > 0) {
            PongEvent.EventType eventType;
            if (direction > 0) {
                eventType = PongEvent.EventType.PADDLE_MOVED_UP;
            } else if (direction < 0) {
                eventType = PongEvent.EventType.PADDLE_MOVED_DOWN;
            } else {
                eventType = PongEvent.EventType.PADDLE_STOPPED;
            }
            PongEvent event = new PongEvent(eventType);
            event.setPaddle(paddle);
            callEvent(event);
        }
    }

    /**
     * Notifies listeners that a pong event has occurred.
     *
     * @param event The PongEvent.
     */
    private void callEvent(PongEvent event) {
        if (pongEventListeners.size() > 0) {
            pongEventListeners.forEach(pongEventConsumer -> pongEventConsumer.accept(event));
        }
    }

    /**
     * Does retracing of the paddle's and the ball's positions in order to determine if the ball and the paddle
     * are currently colliding or did previously collide.
     * Once this has been determined, the appropriate velocity is applied to the ball and it is reflected back
     * to where it would have reflected.
     *
     * @param testingPaddle      The paddle to test against.
     * @param nanosSinceLastTick The time passed (in milliseconds) since the last tick.
     * @return True if the ball had entered a collision, false otherwise.
     */
    private boolean testBallCollision(Paddle testingPaddle, final long nanosSinceLastTick) {
        boolean didIntersect;

        // Determine the old x position of the ball.
        final double oldBallX = ball.getX() - ball.getRunPerNanoSecond() * nanosSinceLastTick;

        // Some points we need to keep track of.
        final double paddlePoint, ballPoint;
        if (testingPaddle.getSide() == Side.RIGHT) {
            paddlePoint = testingPaddle.getX(Side.LEFT);
            ballPoint = PongBall.getX(ball.getRadius(), oldBallX, Side.RIGHT);
        } else {
            paddlePoint = testingPaddle.getX(Side.RIGHT);
            ballPoint = oldBallX;
        }

        // If the ball was in intersect range during the last tick, we don't need to deal with it now.
        didIntersect = ((ballPoint >= paddlePoint && ball.getX(Side.LEFT) < paddlePoint && testingPaddle.getSide() == Side.LEFT) ||
                (ballPoint <= paddlePoint && ball.getX(Side.RIGHT) > paddlePoint && testingPaddle.getSide() == Side.RIGHT));

        // Otherwise, we need to run more calculations.
        if (didIntersect) {
            // Determine how long it's been since the ball would've collided. time = distance / velocity
            final double timePassedSinceCollision = (Math.abs(ball.getX() - oldBallX)) / (ball.getRunPerNanoSecond());
            // Determine where the paddle would have been at that time. distance = velocity * time.
            final double paddleTopAtTime = testingPaddle.getY(Side.TOP) - timePassedSinceCollision * testingPaddle.getVelYNanos();
            // Also get the bottom position here.
            final double paddleBottomAtTime = Paddle.getY(testingPaddle.getHeight(), paddleTopAtTime, Side.BOTTOM);
            // Determine the ball's height at that time.
            final double ballTopYAtTime = ball.getY(Side.TOP) - timePassedSinceCollision * (ball.getRisePerNanoSecond());
            final double ballCenterAtTime = PongBall.getY(ball.getRadius(), ballTopYAtTime, Side.CENTER);
            double goodBallPos;
            if (ballCenterAtTime > paddleTopAtTime) {
                goodBallPos = ballTopYAtTime;
            } else if (ballCenterAtTime < paddleBottomAtTime) {
                goodBallPos = PongBall.getY(ball.getRadius(), ballTopYAtTime, Side.BOTTOM);
            } else {
                goodBallPos = ballCenterAtTime;
            }

            didIntersect = doesBallIntersect(goodBallPos, paddleTopAtTime, paddleBottomAtTime);

            // If there was an intersection, we need to continue even more with determining the ball's new location.
            if (didIntersect) {
                ball.setX(oldBallX);
                ball.setY(ballCenterAtTime, Side.CENTER);
                final double tempPaddleHeight = testingPaddle.getY();
                testingPaddle.setY(paddleTopAtTime);
                applyNewBallVelocity(testingPaddle);// Apply new ball velocity.
                testingPaddle.setY(tempPaddleHeight);
                ball.renderTick(nanosSinceLastTick - (long) timePassedSinceCollision);
            }
        }

        return didIntersect;
    }

    /**
     * Calculates and applies the new ball velocity for the ball after hitting a paddle.
     *
     * @param paddleHit The paddle with which the ball entered a collision.
     */
    private void applyNewBallVelocity(Paddle paddleHit) {
        final double centerPaddle = paddleHit.getY(Side.CENTER);
        /* If the ball is higher than the paddle's center, use the bottom of the ball's vertical point.
        Else use its top. */
        final double ballCenter = ball.getY(Side.CENTER);
        final double ballPoint;
        if (ballCenter > centerPaddle) {
            ballPoint = ball.getY(Side.BOTTOM);
        } else if (ballCenter < centerPaddle) {
            ballPoint = ball.getY(Side.TOP);
        } else {
            ballPoint = ballCenter;
        }

        /* Get the difference between the center y-value of the ball and the paddle.
        If ball is higher, this will be positive, else it will be negative.*/
        final double difference = ballPoint - centerPaddle;

        /* Get the percentage difference (how far the centers
        are from each other as a percentage of the maximum possible distance).*/
        double percentageDifference = difference / (paddleHit.getHeight() / 2);
        double angle = percentageDifference * MAX_REBOUND_ANGLE;

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
    private static boolean doesIntersect(PongPiece piece1, PongPiece piece2) {
        return !(piece1.getX(Side.RIGHT) < piece2.getX(Side.LEFT) ||
                piece1.getX(Side.LEFT) > piece2.getX(Side.RIGHT) ||
                piece1.getY(Side.BOTTOM) >= piece2.getY(Side.TOP) ||
                piece1.getY(Side.TOP) <= piece2.getY(Side.BOTTOM));
    }

    /**
     * Determines if the ball intersects with the paddle, assuming the ball has already been determined to intersect
     * horizontally with the paddle.
     *
     * @param ballPoint    The y coordinate point of the ball to test against.
     * @param topPaddle    The top y coordinate of the paddle.
     * @param bottomPaddle The bottom y coordinate of the paddle.
     * @return True if the ball and paddle intersect, false otherwise.
     */
    private static boolean doesBallIntersect(final double ballPoint, final double topPaddle, final double bottomPaddle) {
        return bottomPaddle <= ballPoint && ballPoint <= topPaddle;
    }

    /**
     * Checks that the pong ball is in bounds.
     */
    private void checkBallBounds() {
        // Check the top and bottom points to ensure that the ball has not hit a horizontal barrier.
        if (ball.getY(Side.TOP) >= getBoardHeight()) {
            ball.setVelocity(-Math.abs(ball.getRisePerSecond()), ball.getRunPerSecond());
            ball.setY(2 * getBoardHeight() - ball.getY(Side.TOP), Side.TOP);
            callEvent(new PongEvent(getBall(), PongEvent.EventType.BALL_HIT_TOP_WALL));
        } else if (ball.getY(Side.BOTTOM) <= 0) {
            ball.setVelocity(Math.abs(ball.getRisePerSecond()), ball.getRunPerSecond());
            ball.setY(-ball.getY(Side.BOTTOM), Side.BOTTOM);
            callEvent(new PongEvent(getBall(), PongEvent.EventType.BALL_HIT_BOTTOM_WALL));
        }

        // Now check to see if the ball has hit a vertical barrier.
        if (ball.getX(Side.LEFT) <= 0) {
            playerScored(getRightPlayer());
        }
        // If the ball hit the right side, then add to the player on the left.
        else if (ball.getX(Side.RIGHT) >= WIDTH) {
            playerScored(getLeftPlayer());
        }
    }

    /**
     * Should be called when a player scores.
     *
     * @param player    The player who scored.
     * @param newPoints The new amount of points the player should have.
     */
    public void playerScored(final PongPlayer player, final int newPoints) {
        final PongPlayer otherPlayer = (getLocalPlayer().equals(player)) ? getPlayer2() : getLocalPlayer();
        // Only call the listeners and do the stuff if something has actually changed.
        if (player.getPoints() != newPoints && otherPlayer.canBeScoredOn()) {
            player.setPoints(newPoints);
            resetBall((player.getSide() == Side.LEFT) ? Side.RIGHT : Side.LEFT);
            setPauseDuration(SCORE_PAUSE);
            // Important that listeners are called last.
            callPlayerScored(player);

            // Since scores have been changed, check to see if there's a winner.
            checkWinner();
        }
    }

    /**
     * Checks to see if there's a winner in the game. If there's a winner, the game is ended.
     */
    public void checkWinner() {
        if (!ended) {
            if (getLocalPlayer().getPoints() >= WINNING_POINTS || getPlayer2().getPoints() >= WINNING_POINTS) {
                end(EndReason.SCORE_LIMIT_REACHED);
            }
        }
    }

    /**
     * Determines the winner of the pong game.
     *
     * @return The winner of the pong game.
     */
    public PongPlayer getWinner() {
        PongPlayer winner;
        if (getLocalPlayer().getPoints() >= WINNING_POINTS) {
            winner = getLocalPlayer();
        } else if (getPlayer2().getPoints() >= WINNING_POINTS) {
            winner = getPlayer2();
        } else {
            winner = null;
        }
        return winner;
    }

    /**
     * Should be called when a player scores. Increases the player's score by one and calls necessary events.
     *
     * @param player The player who scored.
     */
    public void playerScored(PongPlayer player) {
        playerScored(player, player.getPoints() + 1);
    }

    /**
     * Checks each of the paddles in the game to ensure that they are within the boundaries of the
     * board and places them back in the boundaries if they are not.
     */
    private void checkPaddleBounds() {
        Paddle[] paddles = new Paddle[]{getLeftPaddle(), getRightPaddle()};

        // Iterate through all of the paddles checking their boundaries.
        for (Paddle paddle : paddles) {
            if (paddle.getY(Side.TOP) > getBoardHeight()) {
                paddle.setY(getBoardHeight(), Side.TOP);
                paddle.setVelY(0); // Get rid of velocity so the paddle isn't constantly trying to go off screen.
            }
            if (paddle.getY(Side.BOTTOM) < 0) {
                paddle.setY(0, Side.BOTTOM);
                paddle.setVelY(0); // Get rid of velocity
            }
            if (paddle.getX(Side.RIGHT) > getBoardWidth()) {
                paddle.setX(getBoardWidth(), Side.RIGHT);
                paddle.setVelX(0); // Get rid of velocity once the paddle hits the side.
            }
            if (paddle.getX(Side.LEFT) < 0) {
                paddle.setX(0, Side.LEFT);
                paddle.setVelX(0);
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
        ball.setVelocity(0, (scoringPlayer == Side.LEFT) ? -PONG_BALL_VELOCITY : PONG_BALL_VELOCITY);
    }

    /**
     * Gets the player on the right side of the board, controlling the right paddle.
     *
     * @return The player on the right side.
     */
    public PongPlayer getRightPlayer() {
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
    public PongPlayer getLeftPlayer() {
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
        // Only change and update speed if it is actually changing.
        if (paddle.getVelY() != -PADDLE_MOVEMENT_RATE) {
            paddle.setVelY(-PADDLE_MOVEMENT_RATE);
            callPaddleMoved(paddle, -1);
        }
    }

    /**
     * Moves the given paddle up.
     *
     * @param paddle The paddle to be moved.
     */
    public void paddleUp(Paddle paddle) {
        // Only change and update listeners if there is actually a change.
        if (paddle.getVelY() != PADDLE_MOVEMENT_RATE) {
            paddle.setVelY(PADDLE_MOVEMENT_RATE);
            callPaddleMoved(paddle, 1);
        }
    }

    /**
     * Stops the paddle from moving.
     *
     * @param paddle The paddle to be stopped.
     */
    public void stopPaddle(Paddle paddle) {
        paddle.setVelY(0);
        paddle.setVelX(0);
        callPaddleMoved(paddle, 0);
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
     *
     * @return The right paddle.
     */
    public Paddle getRightPaddle() {
        return rightPaddle;
    }

    /**
     * Gets the left paddle.
     *
     * @return The paddle on the left of the board.
     */
    public Paddle getLeftPaddle() {
        return leftPaddle;
    }


    /**
     * Gets the pong paddle belonging to the given player.
     *
     * @param player The player.
     * @return The paddle belonging to the player.
     */
    public Paddle getPaddle(PongPlayer player) {
        Paddle paddle;
        switch (player.getSide()) {
            case LEFT:
                paddle = getLeftPaddle();
                break;
            case RIGHT:
                paddle = getRightPaddle();
                break;
            default:
                paddle = null;
                break;
        }
        return paddle;
    }

    /**
     * Sets a static pause.
     *
     * @param pause True to pause until told to unpause, false to unpause if paused.
     */
    public void setPause(boolean pause) {
        setPause((pause) ? -1 : 0);
    }

    /**
     * Sets a pause corresponding to the specified number of milliseconds.
     *
     * @param millisecondsPause The number of milliseconds to pause for. -1 for infinite pause.
     */
    public void setPauseDuration(long millisecondsPause) {
        setPause(System.currentTimeMillis() + millisecondsPause);
    }

    /**
     * Sets a pause that will end at the given time.
     *
     * @param unpauseTime The time (in milliseconds) at which the pausing should end.
     */
    public void setPause(long unpauseTime) {
        this.unpauseTime = unpauseTime;
    }

    /**
     * Checks to see if the pause is still in effect.
     *
     * @return True if the game should be paused, false otherwise.
     */
    private boolean checkPause() {
        boolean paused = System.currentTimeMillis() < unpauseTime || unpauseTime < 0;
        if (!paused) {
            lastTickTime = System.nanoTime(); // Update the last tick time now too.
        }

        return paused;
    }

    /**
     * Gets the time (in nanoseconds) at which the game will unpause. -1 for infinite.
     *
     * @return The unpause time, in nanoseconds.
     */
    public long getUnpauseTime() {
        return unpauseTime;
    }

    /**
     * Ends the pong game with a reason.
     */
    public void end(EndReason reason) {
        if (!ended) {
            ended = true;
            endReason = reason;
            callEvent(new PongEvent(PongEvent.EventType.GAME_ENDED));
        }
    }

    /**
     * Determines whether or not the pong game has ended.
     *
     * @return True if the game has ended, false otherwise.
     */
    public boolean isEnded() {
        return ended;
    }

    /**
     * Gets the end reason for this game.
     *
     * @return The end reason.
     */
    public EndReason getEndReason() {
        return endReason;
    }
}
