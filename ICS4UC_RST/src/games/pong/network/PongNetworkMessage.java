package games.pong.network;

import com.google.gson.Gson;
import games.pong.pieces.Paddle;
import games.pong.pieces.PongBall;

/**
 * @author Kyle Anderson
 */
public class PongNetworkMessage {
    private static final Gson gson = new Gson();


    // Time at which this data was calculated.
    private long nanoTimeSent;
    private Paddle leftPaddle, rightPaddle;
    private PongBall ball;
    // True if the ball hit the paddle and bounced off, false otherwise.
    private boolean ballHitPaddle;

    // True when the player's in game, false otherwise.
    private boolean isInGame;

    /**
     * Instantiates a new PongNetworkMessage object with the given time in nanoseconds.
     * @param nanoTime The time (in nanoseconds) that the frame was rendered.
     */
    public PongNetworkMessage(long nanoTime) {
        setNanoTimeSent(nanoTime);
    }

    /**
     * Gets the time (in nanosecond) at which this data was calculated and sent.
     * @return The time (in nanoseconds) at which the data was calculated (when frame was rendered).
     */
    public long getNanoTimeSent() {
        return nanoTimeSent;
    }

    /**
     * Sets the time (in nanosecond) at which this data was calculated and sent.
     * @param nanoTimeSent The time (in nanoseconds) at which the data was calculated (when frame was rendered).
     */
    public void setNanoTimeSent(long nanoTimeSent) {
        this.nanoTimeSent = nanoTimeSent;
    }

    /**
     * Gets the left paddle.
     * @return The left Paddle object.
     */
    public Paddle getLeftPaddle() {
        return leftPaddle;
    }

    /**
     * Sets the left paddle.
     * @param leftPaddle The left paddle object.
     */
    public void setLeftPaddle(Paddle leftPaddle) {
        this.leftPaddle = leftPaddle;
    }

    /**
     * Gets the right paddle.
     * @return The right paddle object.
     */
    public Paddle getRightPaddle() {
        return rightPaddle;
    }

    /**
     * Sets the right paddle.
     * @param rightPaddle The right paddle object.
     */
    public void setRightPaddle(Paddle rightPaddle) {
        this.rightPaddle = rightPaddle;
    }

    /**
     * Gets the PongBall.
     * @return The PongBall object.
     */
    public PongBall getBall() {
        return ball;
    }

    /**
     * Sets teh PongBall.
     * @param ball The PongBall object.
     */
    public void setBall(PongBall ball) {
        this.ball = ball;
    }

    /**
     * Determines if the ball hit the paddle. This is useful if on the local machine the
     * ball hits the paddle but the remote machine isn't up-to-date enough to determine this.
     * @return True if the ball hit the paddle and bounced off, false otherwise.
     */
    public boolean isBallHitPaddle() {
        return ballHitPaddle;
    }

    /**
     * Sets whether or not the ball just hit a paddle.
     * @param ballHitPaddle True if the ball just hit a paddle, false otherwise.
     */
    public void setBallHitPaddle(boolean ballHitPaddle) {
        this.ballHitPaddle = ballHitPaddle;
    }

    /**
     * Determines if the remote player is in game.
     * @return True if the player is in game, false otherwise.
     */
    public boolean isInGame() {
        return isInGame;
    }

    /**
     * Sets whether or not this player is in the game.
     * @param inGame True if in game, false otherwise.
     */
    public void setInGame(boolean inGame) {
        isInGame = inGame;
    }

    /**
     * Converts this PongNetworkMessage to Json string.
     * @return The json string representation of this object.
     */
    public String toJson() {
        return toJsonString(this);
    }

    /**
     * Converts json string to a PongNetworkMessage instance.
     * @param jsonString The json string to be parsed.
     * @return The PongNetworkMessage object.
     */
    public static PongNetworkMessage fromJsonString(String jsonString) {
        return gson.fromJson(jsonString, PongNetworkMessage.class);
    }

    /**
     * Converts a PongNetworkMessage to a json string.
     * @param message The PongNetworkMessage to be converted.
     * @return The json string.
     */
    public static String toJsonString(PongNetworkMessage message) {
        return gson.toJson(message);
    }
}
