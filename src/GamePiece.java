import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
public class GamePiece {
    private Circle circle = new Circle(25);
    private final String player;
    private boolean clicked = false;
    private int index;

    public GamePiece(double offsetx, double offsety, String player, int index) {
        this.player= player;
        // Move the circle to the correct coordinates
        this.circle.setCenterX(offsetx+25);
        this.circle.setCenterY(offsety+25);
        this.index = index;

        if(player.equals("Human")) {
            this.circle.setFill(Color.WHITE);
            this.circle.setStroke(Color.BLACK);
        }
    }

    public boolean getClicked() {
        return this.clicked;
    }

    public Circle getPiece() {
        return this.circle;
    }
    public void setClicked(boolean value) {
        this.clicked = value;
    }
    public String getPlayer() { return this.player; }
    public int getIndex() { return index; }
}
