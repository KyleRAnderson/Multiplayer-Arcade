package games.pong.players;

import games.pong.PongEvent;
import games.pong.pieces.Side;

/**
 * Class for representing and controlling a beginner bot
 *
 * @author s405751 (Nicolas Hawrysh)
 * ICS4U RST
 */
public class PongBeginnerBot extends PongBot {

    @Override
    public String getName() {
        return "Beginner Bot " + getSide();
    }

    @Override
    protected void pongEvent(PongEvent pongEvent) {
        // if the player scored, move paddle to center
        if (pongEvent.getType() == PongEvent.EventType.PLAYER_SCORED) {
            setGoTo(game.getBoardHeight() / 2);
        } else {// set paddle to move where to center of the ball is
            setGoTo(game.getBall().getY() - game.getBall().getRadius());
        }
    }

    @Override
    protected boolean hasTarget() {
        return targetY >= 0;
    }


    @Override
    protected void runGoTo() {
        setGoTo(game.getBall().getY(Side.CENTER));
        super.runGoTo();
    }
}
