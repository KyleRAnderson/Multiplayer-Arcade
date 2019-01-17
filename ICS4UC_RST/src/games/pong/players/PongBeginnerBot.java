package games.pong.players;

import java.util.function.BiConsumer;
import games.pong.Pong;
import games.pong.PongEvent;
import games.pong.pieces.Paddle;
import games.pong.pieces.Side;

/**
 * Class for representing and controlling a bot
 *
 * @author s405751 (Nicolas Hawrysh)
 * ICS4U RST
 */
public class PongBeginnerBot extends PongBot implements PongPlayer {
    private Side side;
	private int points;
	private double targetY;
	private Action oldAction;
	
	// The listener for when the action changes.
    private BiConsumer<PongPlayer, Action> actionListener;
    
	private Pong game;
    
	
	@Override
	public Side getSide() {
		return this.side;
	}
	@Override
	public void setSide(Side side) {
		this.side = side;
	}
	@Override
	public void setPoints(int points) {
		this.points = points;
		
	}
	@Override
	public int getPoints() {
		return points;
	}
	
	@Override
	public void setGame(Pong game) {
		this.game = game;
        this.game.setOnTick(this::runGoTo);
        this.game.addEventListener(this::pongEvent);
        setGoTo(this.game.getBoardHeight() / 2 + 5);
	}
	
	@Override
	public String getName() {
		return "Beginner Bot " + getSide();
	}
	
	@Override
	public boolean canBeScoredOn() {
		// bot can always be scored on
		return true;
	}

    /**
     * Sets a method to be called when the player's paddle's action should change.
     *
     * @param actionListener The listener to be called.
     */
    @Override
    public void setOnActionChanged(BiConsumer<PongPlayer, Action> actionListener) {
        this.actionListener = actionListener;
    }
	
	@Override
	protected void setGoTo(double y) {
		targetY = y;
    }
		
	@Override
	protected void pongEvent(PongEvent pongEvent) {
		switch (pongEvent.getType()) {
			// if the player scored, move paddle to center
        	case PLAYER_SCORED:
        		setGoTo(game.getBoardHeight() / 2);
        		break;
        	default:
        		// set paddle to move where to center of the ball is
        		setGoTo(game.getBall().getY() - game.getBall().getRadius());
        		break;
		}
	}
	@Override
	protected boolean hasTarget() {
		return targetY >= 0;
	}
	
	@Override
	protected void runGoTo() {
		// If we have a target, do logic to try and go to the target.
        if (hasTarget()) {
            final Paddle thisPaddle = game.getPaddle(this);
            final double paddleY = thisPaddle.getY(Side.CENTER);
            // If the paddle's y is larger than the target y by enough of a factor, move down.
            if (paddleY - targetY > thisPaddle.getHeight() / PADDLE_HEIGHT_DIVISOR) {
                setAction(Action.MOVE_DOWN);
            }
            // If the paddle's y is smaller than the target y by enough of a factor, move up.
            else if (paddleY - targetY < -thisPaddle.getHeight() / PADDLE_HEIGHT_DIVISOR) {
                setAction(Action.MOVE_UP);
            }
            // Otherwise just stop moving.
            else {
                setAction(Action.STOP);
            }
        }
		
	}
	
	
	/**
     * Called when the bot's action should change.
     *
     * @param newAction The new action that the player should take.
     */
    private void setAction(Action newAction) {
        if (actionListener != null) {
            actionListener.accept(this, newAction);
        }
    }

}
