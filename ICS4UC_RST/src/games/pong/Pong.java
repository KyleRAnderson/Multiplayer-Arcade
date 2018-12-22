package games.pong;

import games.Game;
import games.Score;
import games.pong.players.PongKeyboardPlayer;
import games.pong.players.PongPlayer;
import javafx.scene.Parent;
import javafx.scene.image.Image;

/**
 * @author Kyle Anderson
 * ICS4U RST
 */
public class Pong extends Game {


    public Pong(PongKeyboardPlayer localPlayer, PongPlayer player2) {

    }

    @Override
    public void start() {

    }

    @Override
    public boolean isInProgress() {
        return false;
    }

    @Override
    public void end() {

    }

    @Override
    public Score getScore() {
        return null;
    }

    @Override
    public Image getCoverArt() {
        return null;
    }

    @Override
    public Parent getWindow() {
        return null;
    }
}
