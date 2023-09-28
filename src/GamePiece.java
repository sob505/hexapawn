import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
public class GamePiece {
    private Double[] coordinates;
    private Polygon triangle = new Polygon();
    private final String player;
    private boolean clicked = false;

    public GamePiece(double offsetx, double offsety, String player) {
        this.player= player;
        // Change triangle shape depending on whose piece it is
        resetCoordinates();
        // Move the triangle to the correct coordinates
        offset(offsetx,offsety);
        this.triangle.getPoints().addAll(coordinates);

        if(player.equals("Human")) {
            triangle.setFill(Color.WHITE);
            triangle.setStroke(Color.BLACK);
        }
    }

    private void resetCoordinates() {
        if(this.player.equals("HER")) {
            this.coordinates = new Double[]{
                    0.0,0.0,
                    25.0,50.0,
                    50.0,0.0,
            };
        } else {
            this.coordinates = new Double[] {
                    0.0, 50.0,  // Left
                    25.0, 0.0,  // Top
                    50.0, 50.0, // Right
            };
        }
    }

    public void offset(double x,double y) {
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
        resetCoordinates();
        //double[] dist = {this.coordinates[0] - x, this.coordinates[1] - y};
        offset(x,y);//dist[0], dist[1]);
        this.triangle = new Polygon();
        this.triangle.getPoints().addAll(this.coordinates);
    }

    public void setClicked(boolean value) {
        this.clicked = value;
    }
    public String getPlayer() { return this.player; }
}
