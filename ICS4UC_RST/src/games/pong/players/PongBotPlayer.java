package games.pong.players;

import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import games.player.Bot;
import games.player.PongKeyBinding;
import games.pong.Pong;
import games.pong.pieces.Paddle;
import games.pong.pieces.Side;
import javafx.scene.input.KeyCode;

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
	
	// The listener for when the action changes.
    private BiConsumer<PongPlayer, Action> actionListener;

    /**
     * Sets a method to be called when the player's paddle's action should change.
     *
     * @param actionListener The listener to be called.
     */
    @Override
    public void setOnActionChanged(BiConsumer<PongPlayer, Action> actionListener) {
        this.actionListener = actionListener;
    }

    /**
     * Called when the player's action should change.
     *
     * @param newAction The new action that the player should take.
     */
    private void actionChanged(Action newAction) {
        if (actionListener != null) {
            actionListener.accept(this, newAction);
        }
    }

    // Last action that was put through.
    private Action lastAction;
	
	@Override
	public void moveToBall(final int ballypos, final int paddleypos) {
		Action newAction = Action.STOP;

		if (paddleypos != ballypos) {

			if (ballypos > paddleypos) {

				newAction = Action.MOVE_UP;

			} else if (ballypos < paddleypos) {
				newAction = Action.MOVE_DOWN;
			}

		}
		if (newAction != lastAction) {
			actionChanged(newAction);
		}
		lastAction = newAction;
	}

}
