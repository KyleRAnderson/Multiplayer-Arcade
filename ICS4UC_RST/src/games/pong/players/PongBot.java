/**
 * 
 */
package games.pong.players;

import games.pong.PongEvent;

/**
 * @author s405751 (Nicolas Hawrysh)
 * 
 * ICS4U
 * 
 * Abstract class for pong bot
 */
public abstract class PongBot 
{
	protected static final int PADDLE_HEIGHT_DIVISOR = 10;
	
	/**
     * Determines the future height of the ball in order to go to that position
     * and hit the ball.
     *
     * @return The future y position of the ball.
     */
	protected abstract double calculateFutureBallHeight();
	
	/**
     * Sets a position that the bot shall attempt to reach.
     *
     * @param y The y position that the bot will reach.
     */
	protected abstract void setGoTo(final double y);
	
	/**
     * Called when a pong event changes.
     *
     * @param pongEvent The event.
     */
    protected abstract void pongEvent(PongEvent pongEvent);
    
    /**
     * Determines if this bot is trying to go to a target vertical position.
     *
     * @return True if the bot is trying to go to a position, false otherwise.
     */
    protected abstract boolean hasTarget();
    
    /**
     * Runs the bot's logic every tick.
     */
    protected abstract void runGoTo();

}
