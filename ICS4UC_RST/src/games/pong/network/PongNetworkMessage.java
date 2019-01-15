package games.pong.network;

import com.google.gson.Gson;
import games.pong.PongEvent;
import games.pong.pieces.Paddle;
import games.pong.pieces.PongBall;

/**
 * Class for sending messages between pong games.
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
public class PongNetworkMessage {
    private static final Gson gson = new Gson();


    // Time at which this data was calculated.
    private long timestampSent;
    private Paddle localPlayerPaddle;
    private PongBall ball;
    // True if the ball hit the paddle and bounced off, false otherwise.
    private boolean ballHitPaddle;
    private PongEvent.EventType triggeringEvent;

    private int localPlayerScore, networkPlayerScore;

    // True when the player's in game, false otherwise.
    private boolean isInGame;

    /**
     * Instantiates a new PongNetworkMessage object with the given time in nanoseconds.
     *
     * @param millisTime The time (in milliseconds) that the frame was rendered.
     */
    public PongNetworkMessage(long millisTime) {
        setTimestampSent(millisTime);
    }

    /**
     * Gets the time (in milliseconds) at which this data was calculated and sent.
     *
     * @return The time (in milliseconds) at which the data was calculated (when frame was rendered).
     */
    public long timestamp() {
        return timestampSent;
    }

    /**
     * Sets the time (in nanosecond) at which this data was calculated and sent.
     *
     * @param timestampSent The time (in nanoseconds) at which the data was calculated (when frame was rendered).
     */
    public void setTimestampSent(long timestampSent) {
        this.timestampSent = timestampSent;
    }

    /**
     * Gets the left paddle of the player sending this data.
     *
     * @return The Paddle object.
     */
    public Paddle getLocalPlayerPaddle() {
        return localPlayerPaddle;
    }

    /**
     * Sets the paddle.
     *
     * @param localPlayerPaddle The player who is sending the data's paddle.
     */
    public void setLocalPlayerPaddle(Paddle localPlayerPaddle) {
        this.localPlayerPaddle = localPlayerPaddle;
    }

    /**
     * Gets the PongBall.
     *
     * @return The PongBall object.
     */
    public PongBall getBall() {
        return ball;
    }

    /**
     * Sets the PongBall.
     *
     * @param ball The PongBall object.
     */
    public void setBall(PongBall ball) {
        this.ball = ball;
    }

    /**
     * Determines if the ball hit the paddle. This is useful if on the local machine the
     * ball hits the paddle but the remote machine isn't up-to-date enough to determine this.
     *
     * @return True if the ball hit the paddle and bounced off, false otherwise.
     */
    public boolean isBallHitPaddle() {
        return ballHitPaddle;
    }

    /**
     * Sets whether or not the ball just hit a paddle.
     *
     * @param ballHitPaddle True if the ball just hit a paddle, false otherwise.
     */
    public void setBallHitPaddle(boolean ballHitPaddle) {
        this.ballHitPaddle = ballHitPaddle;
    }

    /**
     * Determines if the remote player is in game.
     *
     * @return True if the player is in game, false otherwise.
     */
    public boolean isInGame() {
        return isInGame;
    }

    /**
     * Sets whether or not this player is in the game.
     *
     * @param inGame True if in game, false otherwise.
     */
    public void setInGame(boolean inGame) {
        isInGame = inGame;
    }

    /**
     * Gets the event type that triggered this data send.
     *
     * @return The type of the event which triggered this data send.
     */
    public PongEvent.EventType getTriggeringEvent() {
        return triggeringEvent;
    }

    /**
     * Sets the type of the event which triggered this update.
     *
     * @param triggeringEvent The triggering event type.
     */
    public void setTriggeringEvent(PongEvent.EventType triggeringEvent) {
        this.triggeringEvent = triggeringEvent;
    }

    /**
     * Gets the sending machine's local player's score.
     *
     * @return The score of this machine's local player.
     */
    public int getLocalPlayerScore() {
        return localPlayerScore;
    }

    /**
     * Sets the score for the player.
     *
     * @param localPlayerScore The score to be set.
     */
    public void setLocalPlayerScore(int localPlayerScore) {
        this.localPlayerScore = localPlayerScore;
    }

    /**
     * Gets the score of the remote party according to the remote machine.
     *
     * @return The score of the remote party.
     */
    public int getNetworkPlayerScore() {
        return networkPlayerScore;
    }

    /**
     * Sets the score of the remote party according to the remote machine.
     *
     * @param networkPlayerScore The score of the remote party.
     */
    public void setNetworkPlayerScore(int networkPlayerScore) {
        this.networkPlayerScore = networkPlayerScore;
    }

    /**
     * Converts this PongNetworkMessage to Json string.
     *
     * @return The json string representation of this object.
     */
    public String toJson() {
        return toJsonString(this);
    }

    /**
     * Converts json string to a PongNetworkMessage instance.
     *
     * @param jsonString The json string to be parsed.
     * @return The PongNetworkMessage object.
     */
    public static PongNetworkMessage fromJsonString(String jsonString) {
        return gson.fromJson(jsonString, PongNetworkMessage.class);
    }

    /**
     * Converts a PongNetworkMessage to a json string.
     *
     * @param message The PongNetworkMessage to be converted.
     * @return The json string.
     */
    public static String toJsonString(PongNetworkMessage message) {
        return gson.toJson(message);
    }
}
