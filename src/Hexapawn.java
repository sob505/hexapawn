/*
        Hexapawn
        Sachi Barnaby

        This class implements the Hexapawn game.

 */

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.application.Application;

public class Hexapawn extends Application {
    private final Canvas canvas = new Canvas(800,500);
    private final GraphicsContext gc = canvas.getGraphicsContext2D();
    public static void main(String[] args) {
        launch(args);
    }
    private final Board gameBoard = new Board();
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Game Board");

        // Create the board and pieces
        Pane board = gameBoard.makeBoard(canvas);
        Pane pieces = gameBoard.makePieces();

        // Create a StackPane to overlay the Pane
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(board);
        stackPane.getChildren().add(pieces);

        // Create a scene
        Scene scene = new Scene(stackPane, 800, 600);

        stage.setScene(scene);
        stage.show();

    }
}