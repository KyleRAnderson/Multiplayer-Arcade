package games.pong.ui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Class to play sfx using javafx media player
 *
 * @author s405751 (Nicolas Hawrysh)
 *
 * ICS4U
 */
public class SfxPongPlayer {
    private String strAudioFile;

    private MediaPlayer mpPlayer;

    /**
     * javafx media player has lag when you play sound for first time, so to fix it, this constructor will play 
     * an extremely small 0.03 second silent audio file.
     */
    public SfxPongPlayer() {
        strAudioFile = (getClass().getResource("/res/sfx/null.wav")).toString();
        setupMedia();
        play();
    }

    /**
     * Method to choose file
     */
    public void chooseFile(String audioFileName) {
        strAudioFile = (getClass().getResource("/res/pong/sfx/" + audioFileName)).toString();
        setupMedia();
    }

    /**
     * plays sfx in media player
     *
     */
    public void play() {
        mpPlayer.play();
    }

    /**
     * setups media player for the sfx
     */
    private void setupMedia() {
        Media mdMedia = new Media(strAudioFile);
        mpPlayer = new MediaPlayer(mdMedia);
    }

}
