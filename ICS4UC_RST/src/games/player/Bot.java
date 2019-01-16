package games.player;

/**
 * Bot player for pong
 *
 * @author s405751 (Nicolas Hawrysh)
 * ICS4U RST
 */

public abstract class Bot {
	
	/**
     * method for bot paddle to move to ball
     *
     * @param ball y position
     */
	public abstract void moveToBall(final int ballypos, final int paddleypos);

}
