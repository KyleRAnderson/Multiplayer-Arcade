package games.pong.ui;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

/**
 * Scoreboard for the pong game's UI.
 *
 * @author Kyle Anderson
 * ICS4U RST
 */
public class Scoreboard extends Group {
    // Text objects for the scores.
    private final Label leftScore;
    private final Label rightScore;

    /**
     * The distance the text parts of the scoreboard will be from the centre of the game.
     */
    private static final double PERCENTAGE_DIST_FROM_CENTRE = 0.05;

    /**
     * Instantiates a new Scoreboard object.
     */
    public Scoreboard() {
        leftScore = new Label("0");
        rightScore = new Label("0");

        getChildren().addAll(leftScore, rightScore);
    }

    /**
     * Calculates the positioning of the elements with the updated information about the screen dimensions.
     *
     * @param screenWidth  The width of the screen.
     * @param screenHeight The height of the screen.
     */
    public void calculate(final double screenWidth, final double screenHeight) {
        final double height = screenHeight * 0.1; // Universal height for both text boxes.

        leftScore.relocate(screenWidth / 2 - screenWidth * PERCENTAGE_DIST_FROM_CENTRE - leftScore.getWidth() * 0.65, height);
        rightScore.relocate(screenWidth / 2 + screenWidth * PERCENTAGE_DIST_FROM_CENTRE, height);
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
        leftScore.setTextFill(value);
        rightScore.setTextFill(value);
    }

    /**
     * Gets the text object for the left score.
     *
     * @return The left score text object.
     */
    public Label getLeftScoreText() {
        return leftScore;
    }

    /**
     * Gets the text object for the right score.
     *
     * @return The right score text object.
     */
    public Label getRightScoreText() {
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
    
    /**
     * sets size of text based on a scalefactor
     *
     * @param scale factor
     */
    public void setSize(final double scaleFactor) {
    	leftScore.resize(leftScore.getWidth() * scaleFactor, leftScore.getHeight() * scaleFactor);
    	rightScore.resize(rightScore.getWidth() * scaleFactor, rightScore.getHeight() * scaleFactor);
    }
}