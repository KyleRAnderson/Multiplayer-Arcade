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
    private static AudioClip hitClip, scoredClip;

    static void init() {
        if (hitClip == null) {
            hitClip = new AudioClip(SfxPongPlayer.class.getResource("/res/pong/sfx/hit.wav").toString());
        }
        if (scoredClip == null) {
            scoredClip = new AudioClip(SfxPongPlayer.class.getResource("/res/pong/sfx/miss.wav").toString());
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
    static void playHit() {
        hitClip.play();
    }

}
