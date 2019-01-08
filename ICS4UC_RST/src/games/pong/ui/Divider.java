/**
 *
 */
package games.pong.ui;

import javafx.scene.Group;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * Dashed line Divider
 *
 * @author s405751 (Nicolas Hawrysh)
 * ICS4U RST
 */
public class Divider extends Group {
    // Number of squares that we want.
    private static final int NUMBER_OF_DASHES = 20;

    private final Rectangle[] arrSquares;

    /**
     * Constructs a new pong divider with the given fill color.
     * @param fill The fill color.
     */
    public Divider(Paint fill) {
        arrSquares = new Rectangle[NUMBER_OF_DASHES];

        // create new squares depending on constant and set fill to be color
        for (int intCounter = 0; intCounter < arrSquares.length; intCounter++) {
            arrSquares[intCounter] = new Rectangle();
            arrSquares[intCounter].setFill(fill);
        }

        getChildren().addAll(arrSquares);
    }


    /**
     * Calculates and fixes the positioning of all the squares making up the line.
     * @param screenWidth The width of screen.
     * @param screenHeight The height of screen.
     */
    public void calculate(final double screenWidth, final double screenHeight) {
        // calculate number of squares * 2 for the space in between
        final double dimensions = screenHeight / (double) (NUMBER_OF_DASHES * 2 - 1);
        // calculate the startX which is the center of the screen
        final double startX = screenWidth / 2 - dimensions / 2;
        int index = 0;

        // create squares to fill screenHeight
        for (double y = 0; y < screenHeight; y += dimensions * 2) {
            Rectangle rect = arrSquares[index++];
            rect.setX(startX);
            rect.setWidth(dimensions);
            rect.setY(y);
            rect.setHeight(dimensions);
        }
    }

}
