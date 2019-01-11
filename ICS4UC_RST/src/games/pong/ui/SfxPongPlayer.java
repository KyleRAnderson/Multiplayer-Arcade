package games.pong.ui;

import javafx.scene.media.AudioClip;

/**
 * Class to play sfx using javafx media player
 *
 * @author s405751 (Nicolas Hawrysh)
 * <p>
 * ICS4U
 */
class SfxPongPlayer {
    private static AudioClip hitWall, scoredClip, hitPaddle;

    /**
     * Initializes the pong sound effects players.
     */
    static void init() {
        if (hitWall == null) {
            hitWall = new AudioClip(SfxPongPlayer.class.getResource("/res/pong/sfx/hitwall.wav").toString());
        }
        if (scoredClip == null) {
            scoredClip = new AudioClip(SfxPongPlayer.class.getResource("/res/pong/sfx/miss.wav").toString());
        }
        if (hitPaddle == null) {
            //noinspection SpellCheckingInspection
            hitPaddle = new AudioClip(SfxPongPlayer.class.getResource("/res/pong/sfx/hitpaddle.wav").toString());
        }
    }


    /**
     * Plays the scored sound (player missed the ball).
     */
    static void playScored() {
        scoredClip.play();
    }


    /**
     * Plays the paddle hit ball sound.
     */
    static void playHitWall() {
        hitWall.play();
    }

    /**
     * Plays the hit paddle sound.
     */
    static void playHitPaddle() {
        hitPaddle.play();
    }

}
