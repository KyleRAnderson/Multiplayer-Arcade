package games.pong.players;

import java.util.function.BiConsumer;

import games.player.Bot;
import games.pong.Pong;
import games.pong.pieces.Side;

/**
 * Class for representing and controlling a bot
 *
 * @author s405751 (Nicolas Hawrysh)
 * ICS4U RST
 */
public class PongBotPlayer extends Bot implements PongPlayer {
    private Side side;
    private String playerName;
	private int points;
    
	@Override
	public void setOnActionChanged(BiConsumer<PongPlayer, Action> listener) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Side getSide() {
		return this.getSide();
	}
	@Override
	public void setSide(Side side) {
		this.side = side;
		
		playerName = "Computer";
		
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
	}
	
	@Override
	public String getName() {
		return playerName;
	}
	@Override
	public boolean canBeScoredOn() {
		// bot can always be scored on
		return true;
	}
	
	@Override
	public void moveToBall(final int ypos) {
		
	}

}
