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

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Game Board");

        // Create the board and pieces
        Pane board = gameBoard(canvas);
        Pane pieces = makePieces();

        // Create a StackPane to overlay the Pane
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(board);
        stackPane.getChildren().add(pieces);

        // Create a scene
        Scene scene = new Scene(stackPane, 800, 600);

        stage.setScene(scene);
        stage.show();

    }

    private Pane gameBoard(Canvas canvas) {
        GameSquare r1 = new GameSquare(100,100);
        GameSquare r2 = new GameSquare(200,100);
        GameSquare r3 = new GameSquare(300,100);
        GameSquare r4 = new GameSquare(100,200);
        GameSquare r5 = new GameSquare(200,200);
        GameSquare r6 = new GameSquare(300,200);
        GameSquare r7 = new GameSquare(100,300);
        GameSquare r8 = new GameSquare(200,300);
        GameSquare r9 = new GameSquare(300,300);

        Pane pane = new Pane();
        pane.getChildren().addAll(canvas, r1.getSquare(), r2.getSquare(), r3.getSquare(), r4.getSquare(), r5.getSquare(),
                r6.getSquare(), r7.getSquare(), r8.getSquare(), r9.getSquare());
        return pane;
    }

    private Pane makePieces() {
        GamePiece HER1 = new GamePiece(125.0,125.0, "HER");
        HER1.addListener();
        GamePiece HER2 = new GamePiece(225.0,125.0, "HER");
        HER2.addListener();
        GamePiece HER3 = new GamePiece(325.0,125.0, "HER");
        HER3.addListener();
        GamePiece Human1 = new GamePiece(125.0,325.0, "Human");
        Human1.addListener();
        GamePiece Human2 = new GamePiece(225.0,325.0, "Human");
        Human2.addListener();
        GamePiece Human3 = new GamePiece(325.0,325.0, "Human");
        Human3.addListener();

        Pane pane = new Pane();
        pane.getChildren().addAll(HER1.getPiece(),HER2.getPiece(),HER3.getPiece(),
                Human1.getPiece(),Human2.getPiece(),Human3.getPiece());
        return pane;
    }
}