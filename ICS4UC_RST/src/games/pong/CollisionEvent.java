package games.pong;

import games.pong.pieces.Paddle;
import games.pong.pieces.PongBall;

/**
 * Event for representing a collision between the pong ball and something else.
 * @author Kyle Anderson
 * ICS4U RST
 */
public class CollisionEvent {
    private PongBall ball;
    private Paddle paddle;
    private CollisionType type;

    /**
     * The type of collisions.
     */
    public enum CollisionType {
        PADDLE, TOP_WALL, BALL_TOP
    }

    /**
     * Constructs a new collision event based off of the ball.
     * @param ball The ball that was in the collision.
     * @param type The type of the collision.
     */
    public CollisionEvent(PongBall ball, CollisionType type) {
        setBall(ball);
        setType(type);
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

    public CollisionType getType() {
        return type;
    }

    public void setType(CollisionType type) {
        this.type = type;
    }
}
