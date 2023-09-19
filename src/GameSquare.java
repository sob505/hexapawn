import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameSquare {
    private String playerPiece = "";
    private final Rectangle square;
    private boolean clicked = false;

    public GameSquare(double x, double y) {
        this.square = new Rectangle(x,y,100,100);
        square.setFill(null);
        square.setStroke(Color.BLACK);
    }

    Rectangle getSquare() {
        return square;
    }

    private void setPlayerPiece(String player) {
        this.playerPiece = player;
    }

    private String checkSquare() {
        return playerPiece;
    }

    public boolean getClicked() { return this.clicked; }
}
