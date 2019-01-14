package games.pong;

import games.pong.pieces.Paddle;
import games.pong.pieces.PongBall;
import games.pong.players.PongPlayer;

/**
 * Event for representing a collision between the pong ball and something else.
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
public class PongEvent {
    private PongBall ball;
    private Paddle paddle;
    private EventType type;
    private PongPlayer player;

    /**
     * The type of collisions.
     */
    public enum EventType {
        BALL_HIT_PADDLE,
        BALL_HIT_TOP_WALL,
        BALL_HIT_BOTTOM_WALL,
        PADDLE_MOVED_UP,
        PADDLE_MOVED_DOWN,
        PADDLE_STOPPED,
        PLAYER_SCORED,
        GAME_BEGUN,
        GAME_ENDED,
        GAME_READY;
    }

    /**
     * Constructs a new collision event based off of the ball.
     *
     * @param type The type of the collision.
     */
    public PongEvent(EventType type) {
        setType(type);
    }

    /**
     * Instantiates a new PongEvent with the given ball and event type.
     *
     * @param ball The ball.
     * @param type The type of event.
     */
    public PongEvent(PongBall ball, EventType type) {
        this(type);
        setBall(ball);
    }

    public PongBall getBall() {
        return ball;
    }

    public void setBall(PongBall ball) {
        this.ball = ball;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public PongPlayer getPlayer() {
        return player;
    }

    public void setPlayer(PongPlayer player) {
        this.player = player;
    }
}
