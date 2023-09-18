import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
public class GamePiece {
    private Double[] startingCoordinates;

    private Polygon triangle = new Polygon();
    private String player;
    private int clickCount = 0;


    public void addListener() {
        // On mouse click, code within here will be run.
        this.triangle.setOnMouseClicked(mouseEvent -> {
            // Read from another property to toggle
            this.triangle.setFill(Color.RED);
            this.clickCount = 1;
        });
    }

    public GamePiece(double offsetx, double offsety, String player) {
        // Change triangle shape depending on whose piece it is
        if(player.equals("Human")) {
            this.startingCoordinates = new Double[]{
                    0.0, 50.0,
                    25.0, 0.0,
                    50.0, 50.0,
            };
        } else {
            this.startingCoordinates = new Double[] {
                    0.0,0.0,
                    25.0,50.0,
                    50.0,0.0,
            };
        }

        // Move the triangle to the correct coordinates
        offset(offsetx,offsety);
        this.triangle.getPoints().addAll(startingCoordinates);

        if(player.equals("Human")) {
            triangle.setFill(Color.WHITE);
            triangle.setStroke(Color.BLACK);
        }

        this.player= player;
    }

    private void offset(double x,double y) {
        for(int i = 0; i < startingCoordinates.length; i++) {
            if(i % 2 == 0) {
                this.startingCoordinates[i] += x;
            } else {
                this.startingCoordinates[i] += y;
            }
        }
    }

    public Polygon getPiece() {
        return triangle;
    }
}
