/**
 *
 */
package games.pong.players;

import games.pong.Pong;
import games.pong.PongEvent;
import games.pong.pieces.Paddle;
import games.pong.pieces.Side;

import java.util.function.BiConsumer;

/**
 * @author s405751 (Nicolas Hawrysh)
 *
 * ICS4U
 *
 * Abstract class for pong bot
 */
public abstract class PongBot implements PongPlayer {
    protected static final int PADDLE_HEIGHT_DIVISOR = 10;
    /**
     * Target location of the bot.
     */
    protected double targetY;

    // Side that the player's on.
    protected Side side;
    // Points for hte player.
    protected int points;

    // Old action that the player performed.
    protected Action oldAction;

    /**
     * Listener for when this player's action changes.
     */
    protected BiConsumer<PongPlayer, Action> actionListener;

    /**
     * The game that this bot is in.
     */
    protected Pong game;

    /**
     * Sets a position that the bot shall attempt to reach.
     *
     * @param y The y position that the bot will reach.
     */
    protected void setGoTo(final double y) {
        targetY = y;
    }

    /**
     * Called when a pong event changes.
     *
     * @param pongEvent The event.
     */
    protected abstract void pongEvent(PongEvent pongEvent);

    /**
     * Runs the bot's logic every tick.
     */
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
     * Sets a new action to be done by the paddle.
     *
     * @param newAction The new action.
     */
    protected void setAction(Action newAction) {
        if (newAction != oldAction && actionListener != null) {
            actionListener.accept(this, newAction);
        }
        oldAction = newAction;
    }

    @Override
    public void setGame(Pong game) {
        this.game = game;
        this.game.setOnTick(this::runGoTo);
        this.game.addEventListener(this::pongEvent);
        setGoTo(this.game.getBoardHeight() / 2 + 5);
    }

    /**
     * Determines if this bot has a target vertical height to reach.
     * @return True if there is a target, false otherwise.
     */
    protected boolean hasTarget() {
        return targetY >= 0;
    }

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
    public boolean canBeScoredOn() {
        return true;
    }

    @Override
    public void setOnActionChanged(BiConsumer<PongPlayer, Action> listener) {
        actionListener = listener;
    }

}
