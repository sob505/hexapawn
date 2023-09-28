/*
        Hexapawn
        Sachi Barnaby

        This class implements the Hexapawn game.

 */

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.application.Application;

public class Hexapawn extends Application {
    private final Canvas canvas = new Canvas(800,500);
    private final GraphicsContext gc = canvas.getGraphicsContext2D();
    private final Board gameBoard = new Board();
    private Number gameMode;
    private static Pane pane = new Pane();

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Game Board");

        // Create the board and pieces
        GridPane board = gameBoard.makeBoard(pane);
        Group pieces = gameBoard.makePieces();
        pane.getChildren().addAll(canvas,board,pieces);
        board.setTranslateX(100);
        board.setTranslateY(100);

        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(
                "Slow Mode", "Fast Mode", "Auto Mode")
        );
        cb.setTranslateX(100);
        cb.setTranslateY(-100);

        cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue ov,Number value, Number newValue) { gameMode = newValue; }
            }
        );

        // Create a StackPane to overlay the Pane
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(pane,cb);

        // Create a scene
        Scene scene = new Scene(stackPane, 800, 600);

        stage.setScene(scene);
        stage.show();

    }
}