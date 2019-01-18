package games.pong.players;

import games.pong.PongEvent;
import games.pong.pieces.Paddle;
import games.pong.pieces.PongBall;
import games.pong.pieces.Side;

/**
 * Advanced bot which pre-calculates the ball's position in order to hit the ball
 * at that position.
 */
public class PongAdvancedBot extends PongBot {
    @Override
    public String getName() {
        return "Advanced Bot " + getSide();
    }


    /**
     * Called when there is an event in the pong game.
     *
     * @param pongEvent The event.
     */
    @Override
    protected void pongEvent(PongEvent pongEvent) {
        switch (pongEvent.getType()) {
            // if player scored, move paddle to center
            case PLAYER_SCORED:
                setGoTo(game.getBoardHeight() / 2);
                break;
            case BALL_HIT_PADDLE:
                if (pongEvent.getPaddle().equals(game.getPaddle(this))) {
                    setGoTo(game.getBoardHeight() / 2);
                } else {
                    setGoTo(calculateFutureBallHeight());
                }
                break;
            default:
                break;
        }
    }

    /**
     * Determines the future height of the ball in order to go to that position
     * and hit the ball.
     *
     * @return The future y position of the ball.
     */
    private double calculateFutureBallHeight() {
        final Paddle thisPaddle = game.getPaddle(this);
        final PongBall ball = game.getBall();
        final Side ballCollisionSide = getSide();
        final Side paddleCollisionSide = (getSide() == Side.LEFT) ? Side.RIGHT : Side.LEFT;
        /*
        The time to a collision is the distance divided by the velocity.
        We don't care if it's negative since this will help us later.
        */
        final double timeToCollisionNanos =
                (thisPaddle.getX(paddleCollisionSide) - ball.getX(ballCollisionSide)) / ball.getRunPerNanoSecond();

        // Ball's end y position.
        final double endY;
        // If the ball isn't moving vertically, its end vertical position should be its current vertical position.
        if (ball.getRisePerNanoSecond() == 0) {
            endY = ball.getY(Side.CENTER);
        } else {
            // Determine how far the ball actually travels if it went from the bottom to the top of the board.
            final double ballCycleDistance = game.getBoardHeight() - ball.getHeight();
            final double ballMaxCenterHeight = game.getBoardHeight() - ball.getRadius();
            final double ballMinCenterHeight = ball.getRadius();
            /*
            From the time for the ball to reach the correct x, we calculate the distance vertically that the ball will
            travel in that time.
            d = vt
            */
            final Side travellingFrom = (ball.getRisePerNanoSecond() < 0) ? Side.TOP : Side.BOTTOM; // Where we're going to say the ball started from, top or bottom.
            final double baseDistance = (travellingFrom == Side.TOP) ?
                    ballMaxCenterHeight - ball.getY(Side.CENTER) : ball.getY(Side.CENTER) - ballMinCenterHeight;
            // Total end distance (from top if travelling down, or from bottom if travelling up) the ball will have travelled.
            final double verticalDistance = Math.abs(ball.getRisePerNanoSecond()) * timeToCollisionNanos + baseDistance;

            /*
            Now we need to account for the rebounding of the ball.
            With the vertical distance calculated, we just need to find the number of bounces that it makes
            and then the remainder of that.
            */
            final double remainder = verticalDistance % ballCycleDistance; // Distance travelled after making bounces.
            final int numBounces = (int) Math.floor(verticalDistance / ballCycleDistance); // Number of bounces to be made.

            if (numBounces % 2 == 1) {
                // IF the original direction was from the top (so going down).
                if (travellingFrom == Side.TOP) {
                    endY = remainder + ballMinCenterHeight;
                } else {
                    endY = ballMaxCenterHeight - remainder;
                }

            } else {
                // If we were originally travelling down (from the top).
                if (travellingFrom == Side.TOP) {
                    endY = ballMaxCenterHeight - remainder;
                } else {
                    endY = remainder + ballMinCenterHeight;
                }
            }
        }

        return endY;
    }
}

