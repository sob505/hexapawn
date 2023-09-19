import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
public class GamePiece {
    private final Double[] coordinates;
    private final Polygon triangle = new Polygon();
    private String player;
    private boolean clicked = false;

    public GamePiece(double offsetx, double offsety, String player) {
        // Change triangle shape depending on whose piece it is
        if(player.equals("Human")) {
            this.coordinates = new Double[]{
                    0.0, 50.0,
                    25.0, 0.0,
                    50.0, 50.0,
            };
        } else {
            this.coordinates = new Double[] {
                    0.0,0.0,
                    25.0,50.0,
                    50.0,0.0,
            };
        }

        // Move the triangle to the correct coordinates
        offset(offsetx,offsety);
        this.triangle.getPoints().addAll(coordinates);

        if(player.equals("Human")) {
            triangle.setFill(Color.WHITE);
            triangle.setStroke(Color.BLACK);
        }

        this.player= player;
    }

    private void offset(double x,double y) {
        for(int i = 0; i < coordinates.length; i++) {
            if(i % 2 == 0) {
                this.coordinates[i] += x;
            } else {
                this.coordinates[i] += y;
            }
        }
    }

    public boolean getClicked() {
        return this.clicked;
    }

    public Polygon getPiece() {
        return triangle;
    }

    public void setCoordinates(double x, double y) {
        double[] dist = {this.coordinates[0] - x, this.coordinates[1] - y};
        offset(dist[0], dist[1]);
    }

    public void setClicked(boolean value) {
        this.clicked = value;
    }
}
