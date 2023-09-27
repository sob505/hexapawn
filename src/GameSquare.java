import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameSquare {
    private final Rectangle square;
    private final int[] index;

    public GameSquare(double x, double y,int[] index) {
        this.square = new Rectangle(x,y,100,100);
        this.square.setFill(Color.WHITE);
        this.square.setStroke(Color.BLACK);
        this.index = index;
    }

    Rectangle getSquare() {
        return this.square;
    }

    public int[] getIndex() { return this.index; }
}
