/*
        Hexapawn
        Sachi Barnaby

        This class implements the Hexapawn game.

 */

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
    private Number gameMode;
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Game Board");

        // Create the board and pieces
        Pane board = gameBoard.makeBoard(canvas);
        Pane pieces = gameBoard.makePieces();

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
        stackPane.getChildren().addAll(board,pieces,cb);

        // Create a scene
        Scene scene = new Scene(stackPane, 800, 600);

        stage.setScene(scene);
        stage.show();

    }
}