import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameSquare {
    private String playerPiece = "";
    private final Rectangle square;

    public GameSquare(double x, double y) {
        this.square = new Rectangle(x,y,100,100);
        this.square.setFill(Color.WHITE);
        this.square.setStroke(Color.BLACK);
    }

    Rectangle getSquare() {
        return this.square;
    }

    private void setPlayerPiece(String player) {
        this.playerPiece = player;
    }

    private String checkSquare() {
        return playerPiece;
    }
}
