package games.pong.players;

import games.player.NetworkPlayer;
import games.pong.EndReason;
import games.pong.Pong;
import games.pong.PongEvent;
import games.pong.network.PongNetworkMessage;
import games.pong.pieces.Paddle;
import games.pong.pieces.PongBall;
import games.pong.pieces.Side;
import network.party.network.NetworkMessage;

import java.util.function.BiConsumer;

/**
 * Represents a network player of the pong game.
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
public class PongNetworkPlayer extends NetworkPlayer implements PongPlayer {
    private Pong game;
    private Side side;
    private int score;
    // Name of the other host.
    private String hostName;

    /**
     * Set to true if the other player has entered the game, false otherwise.
     */
    private boolean otherPlayerBeganGame;

    @Override
    public void setOnActionChanged(BiConsumer<PongPlayer, Action> listener) {

    }

    @Override
    public Side getSide() {
        return side;
    }

    @Override
    public void setSide(Side side) {
        this.side = side;
    }

    @Override
    public void setPoints(int points) {
        this.score = points;
    }

    @Override
    public int getPoints() {
        return score;
    }

    @Override
    public void setGame(Pong game) {
        this.game = game;
        game.addEventListener(this::gameUpdated);

    }

    @Override
    public String getName() {
        return hostName;
    }

    /**
     * Receives network data from the other player.
     *
     * @param data The data received.
     */
    @Override
    public void receiveData(NetworkMessage data) {
        final PongNetworkMessage gameData = PongNetworkMessage.fromJsonString(data.getGameData());
        final long timeBetweenTickAndNetwork = Math.max(game.getLastTickTime() - gameData.getNanoTimeSent(), 0);

        hostName = data.getHostName();
        final PongEvent.EventType triggeringEvent = gameData.getTriggeringEvent();
        // If the other player has started, let's start too.
        if (gameData.isInGame() && triggeringEvent == PongEvent.EventType.GAME_BEGUN && !otherPlayerBeganGame) {
            game.begin();
            game.setPause(gameData.getUnpauseTime());
            otherPlayerBeganGame = true;
        } else if (triggeringEvent == PongEvent.EventType.GAME_READY) {
            game.begin();
            otherPlayerBeganGame = false;
        }
        // The other machine will let us know when their local player was scored on (meaning our local player scored).
        else if (triggeringEvent == PongEvent.EventType.PLAYER_SCORED &&
                gameData.getLocalPlayerScore() == getPoints() &&
                gameData.getUnpauseTime() != 0) {
            game.playerScored(game.getLocalPlayer(), gameData.getNetworkPlayerScore());
            game.setPause(gameData.getUnpauseTime());
        } else if (triggeringEvent == PongEvent.EventType.GAME_ENDED) {
            // If the remote player ended the game, we need to notify this player.
            game.end(EndReason.PLAYER_END);
        }

        // If the other client just hit the ball with the paddle, listen to them entirely.
        if (gameData.isBallHitPaddle()) {
            PongBall gameBall = game.getBall(), networkBall = gameData.getBall();
            gameBall.setX(networkBall.getX());
            gameBall.setY(networkBall.getY());
            gameBall.setVelocity(networkBall.getRisePerSecond(), networkBall.getRunPerSecond());
            gameBall.renderTick(timeBetweenTickAndNetwork);
            game.callBallCollided(game.getPaddle(this));
        }

        // Always trust the other player for the positioning of their paddle.
        Paddle gamePaddle = game.getPaddle(this), networkPaddle = gameData.getLocalPlayerPaddle();
        gamePaddle.setX(networkPaddle.getX());
        gamePaddle.setY(networkPaddle.getY());
        gamePaddle.setVelX(networkPaddle.getVelX());
        gamePaddle.setVelY(networkPaddle.getVelY());
        gamePaddle.renderTick(timeBetweenTickAndNetwork);
    }

    @Override
    public void hostDisconnecting() {
        game.end(EndReason.PLAYER_DISCONNECT);
    }


    /**
     * Called when something about the pong game is updated. This will be the sending method to the other client.
     *
     * @param changeEvent The change event.
     */
    private void gameUpdated(PongEvent changeEvent) {
        PongNetworkMessage message = new PongNetworkMessage(System.nanoTime());
        PongPlayer localPlayer = game.getLocalPlayer();

        message.setTriggeringEvent(changeEvent.getType());
        message.setLocalPlayerScore(localPlayer.getPoints());
        message.setNetworkPlayerScore(getPoints());
        message.setBall(game.getBall());
        message.setLocalPlayerPaddle(game.getPaddle(localPlayer));
        message.setInGame(true);

        // If the player just scored, send the unpause time in the message.
        switch (changeEvent.getType()) {
            case PLAYER_SCORED:
                /* Only do stuff if the player that was scored on was the local player
                (i.e the person who scored was not the local player). */
                if (!changeEvent.getPlayer().equals(localPlayer)) {
                    message.setUnpauseTime(game.getUnpauseTime());
                }
                break;
            case GAME_BEGUN:
                // Only notify the other player that the game has started it they didn't already start.
                if (!otherPlayerBeganGame) {
                    message.setUnpauseTime(game.getUnpauseTime());
                }
                break;
            default:
                break;
        }

        // If the local player just hit the paddle, set that up in the message.
        message.setBallHitPaddle(changeEvent.getType() == PongEvent.EventType.BALL_HIT_PADDLE &&
                changeEvent.getPaddle().getSide() == localPlayer.getSide());

        // Send the data as a last step.
        this.gameDataListener.accept(message.toJson());
    }
}
