/**
 * 
 */
package games.pong.ui;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * Dashed line Divider
 *
 * @author s405751 (Nicolas Hawrysh)
 * ICS4U RST
 */
public class Divider extends Group {
	private ArrayList<Line> line;
	
	public void create(int startX, int startY, int end) {
		for (int intCounter = 0; intCounter < end; intCounter++) {
		    line.add(new Line(startX, startY, intCounter, end));
		    line.get(intCounter).setStroke(Color.WHITE);
		}
	}
	
	public void draw() {
		for (int intCounter = 0; intCounter < line.size(); intCounter++) {
			getChildren().addAll(line);
		}
	}

}
