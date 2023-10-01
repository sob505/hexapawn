/*
        Hexapawn
        Sachi Barnaby

        This class implements the Hexapawn game. Run this class to start the game.
        In Fast Mode, you can click the white pieces and then a square to move them (if that is a valid move).
        Keep playing until one side wins. Restart the game by pressing the button.
        In Slow Mode, make your move and then either click an arrow or press the "Random Move" button for HER to make
        its move. Repeat until one side wins. Restart in the same way.

 */

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.application.Application;

public class Hexapawn extends Application {
    private final Canvas canvas = new Canvas(800,500);
    private final GraphicsContext gc = canvas.getGraphicsContext2D();
    private static Pane pane = new Pane();

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Game Board");

        // Create the board and pieces
        Board gameBoard = new Board(pane, 1);
        GridPane grid = gameBoard.getGridPane();
        Group pieces = gameBoard.getGroupPieces();
        pane.getChildren().addAll(canvas,grid,pieces);
        grid.setTranslateX(100);
        grid.setTranslateY(100);

        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(
                "Slow Mode", "Fast Mode", "Auto Mode")
        );
        cb.setValue("Fast Mode");
        cb.setTranslateX(100);
        cb.setTranslateY(-100);
        gameBoard.addListener(cb);

        Slider slider = new Slider(0.0,20.0,1.0);
        slider.setValue(1.0);
        slider.setPrefWidth(200);
        slider.setMaxWidth(200);
        slider.setTranslateX(150);
        slider.setTranslateY(-50);
        slider.setShowTickLabels(true);
        gameBoard.addListener(slider);

        Label label = new Label("Number of Rounds");
        label.setTranslateX(100);
        label.setTranslateY(-70);

        // Create a StackPane to overlay the Pane
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(pane,cb,slider,label);

        // Create a scene
        Scene scene = new Scene(stackPane, 800, 600);

        stage.setScene(scene);
        stage.show();

    }
}