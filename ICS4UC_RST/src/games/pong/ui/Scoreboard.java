package games.pong.ui;

import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Scoreboard for the pong game's UI.
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
public class Scoreboard extends HBox {
    // Text objects for the scores.
    private final Text leftScore;
    private final Text rightScore;

    /**
     * Instantiates a new Scoreboard object.
     */
    public Scoreboard() {
        leftScore = new Text();
        rightScore = new Text();

        getChildren().addAll(leftScore, rightScore);
    }

    /**
     * Sets the font to be used in the scoreboard application.
     *
     * @param value The font to be used.
     */
    public void setFont(Font value) {
        leftScore.setFont(value);
        rightScore.setFont(value);
    }

    /**
     * Sets the fill for the scoreboard text.
     *
     * @param value The fill color for the text.
     */
    public void setFontFill(Paint value) {
        leftScore.setFill(value);
        rightScore.setFill(value);
    }

    /**
     * Gets the text object for the left score.
     *
     * @return The left score text object.
     */
    public Text getLeftScoreText() {
        return leftScore;
    }

    /**
     * Gets the text object for the right score.
     *
     * @return The right score text object.
     */
    public Text getRightScoreText() {
        return rightScore;
    }

    /**
     * Sets the score for the left player.
     *
     * @param score The score for the player on the left.
     */
    public void setLeftScore(final int score) {
        leftScore.setText(String.valueOf(score));
    }

    /**
     * Sets the score for the right player.
     *
     * @param score The score for the player on the right.
     */
    public void setRightScore(final int score) {
        rightScore.setText(String.valueOf(score));
    }
}
