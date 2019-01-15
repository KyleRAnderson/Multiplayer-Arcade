package games.pong.players;

import games.player.NetworkPlayer;
import games.pong.EndReason;
import games.pong.Pong;
import games.pong.PongEvent;
import games.pong.network.PongNetworkMessage;
import games.pong.pieces.Paddle;
import games.pong.pieces.PongBall;
import games.pong.pieces.Side;
import games.pong.ui.PongUI;
import network.party.network.NetworkMessage;

import java.util.Arrays;
import java.util.List;
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
    private boolean allowScoringOn;

    private static final PongEvent.EventType[] EVENT_FILTER = {
            PongEvent.EventType.BALL_HIT_PADDLE,
            PongEvent.EventType.GAME_BEGUN,
            PongEvent.EventType.GAME_ENDED,
            PongEvent.EventType.GAME_READY,
            PongEvent.EventType.PADDLE_MOVED_DOWN,
            PongEvent.EventType.PADDLE_MOVED_UP,
            PongEvent.EventType.PADDLE_STOPPED,
            PongEvent.EventType.PLAYER_SCORED
    };
    private final List<PongEvent.EventType> typeFilter;

    /**
     * Set to true if the other player has entered the game, false otherwise.
     */
    private boolean otherPlayerBeganGame;

    /**
     * Initializes a new pong network player.
     */
    public PongNetworkPlayer() {
        typeFilter = Arrays.asList(EVENT_FILTER);
    }

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

    @Override
    public boolean canBeScoredOn() {
        return allowScoringOn;
    }

    /**
     * Receives network data from the other player.
     *
     * @param data The data received.
     */
    @Override
    public void receiveData(NetworkMessage data) {
        final PongNetworkMessage gameData = PongNetworkMessage.fromJsonString(data.getGameData());
        final long timeBetweenTickAndNetwork = Math.max(getTimeStamp() - gameData.timestamp(), 0);

        hostName = data.getHostName();
        final PongEvent.EventType triggeringEvent = gameData.getTriggeringEvent();
        // If the other player has started, let's start too, but only if we didn't already start.
        if (gameData.isInGame() && triggeringEvent == PongEvent.EventType.GAME_BEGUN && !otherPlayerBeganGame
                && !game.hasBegun()) {
            game.begin();
            otherPlayerBeganGame = true;
        } else if (triggeringEvent == PongEvent.EventType.GAME_READY) {
            game.begin();
            otherPlayerBeganGame = false;
        }
        // The other machine will let us know when their local player was scored on (meaning our local player scored).
        else if (triggeringEvent == PongEvent.EventType.PLAYER_SCORED &&
                gameData.getLocalPlayerScore() == getPoints()) {
            allowScoringOn = true;
            game.playerScored(game.getLocalPlayer(), gameData.getNetworkPlayerScore());
            allowScoringOn = false;
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
        if (typeFilter.contains((changeEvent.getType()))) {
            PongNetworkMessage message = new PongNetworkMessage(getTimeStamp());
            PongPlayer localPlayer = game.getLocalPlayer();

            message.setTriggeringEvent(changeEvent.getType());
            message.setLocalPlayerScore(localPlayer.getPoints());
            message.setNetworkPlayerScore(getPoints());
            message.setBall(game.getBall());
            message.setLocalPlayerPaddle(game.getPaddle(localPlayer));
            message.setInGame(true);

            // If the local player just hit the paddle, set that up in the message.
            message.setBallHitPaddle(changeEvent.getType() == PongEvent.EventType.BALL_HIT_PADDLE &&
                    changeEvent.getPaddle().getSide() == localPlayer.getSide());

            final String sending = message.toJson();
            // Send the data as a last step.
            gameDataListener.accept(sending);
        }
    }

    /**
     * Gets the timestamp to be used in network calls.
     *
     * @return The timestamp to be used in network calls.
     */
    private static long getTimeStamp() {
        return System.currentTimeMillis() / 1000L;
    }
}
