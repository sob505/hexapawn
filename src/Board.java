import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.List;

public class Board {
    private int numRows = 3;
    private final GameSquare[][] board = new GameSquare[numRows][numRows];
    public int[][] pieceLocation = new int[numRows][numRows];
    public GamePiece[][] pieces = new GamePiece[2][numRows];
    private final double squareMargin = 25.0;
    private GridPane gridPane = new GridPane();
    private int clickedPiece;
    private boolean firstClick = false;
    private Matchbox match;// = new Matchbox(this.getLocations(),numRows);
    private int[] numPieces = {3,3};
    private Number gameMode;
    private Number numRounds;
    private StackPane stack;

    Group arrows = new Group();
    private final double[] arrowPoints = new double[]{
            -5.0, 20.0,
             5.0, 20.0,
             5.0, -10.0,
             15.0, -10.0,
              0.0, -30.0,
            -15.0, -10.0,
            -5.0, -10.0
    };

    public GridPane makeBoard(Pane pane) {
        int startx = 100;
        int starty = 100;

        for(int i = 0; i < numRows; i++) {
            for(int j = 0; j < numRows; j++) {
                GameSquare square = new GameSquare(startx,starty,new int[]{j,i});
                square.getSquare().setFill(Color.WHITE);
                square.getSquare().setStroke(Color.BLACK);
                this.board[i][j] = square;
                gridPane.add(square.getSquare(),i,j);
                if(startx == 300) { // Only handles 3x3 board currently
                    startx = 100;
                    starty += 100;
                } else {
                    startx += 100;
                }

                // Add click event handler for squares
                addListener(square,pane);
            }
        }
        return gridPane;
    }

    /*
        Create 6 pieces on the board: 3 for HER and 3 for the human player.
     */
    public Group makePieces() {
        Group groupPieces = new Group();
        double humanCoordinate = 125.0;

        // Create the computer's and the player's pieces
        for(int i = 0; i < numRows; i++) {
            pieces[0][i] = new GamePiece(humanCoordinate,125.0,"HER");
            pieces[1][i] = new GamePiece(humanCoordinate,325.0,"Human");
            humanCoordinate += 100;
            addListener(pieces[1][i]);
            pieceLocation[0][i] = i+1; // Initialize locations for HER pieces
            pieceLocation[numRows-1][i] = i+4; // Initialize locations for Human pieces
            groupPieces.getChildren().addAll(pieces[0][i].getPiece(),pieces[1][i].getPiece());
        }
        match = new Matchbox(this.pieceLocation,numPieces[0]);

        return groupPieces;
    }

    public void addListener(ChoiceBox cb) {
        cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue ov, Number value, Number newValue) {
                gameMode = newValue; }
        }
        );
    }

    public void addListener(Slider slider) {
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                numRounds = newValue;
            }
        });
    }

    /*
        Create a listener for each piece where only one piece can be clicked at a time
     */
    public void addListener(GamePiece piece) {
        // Take action when a piece is clicked
        piece.getPiece().setOnMouseClicked(mouseEvent -> {
            // If a piece is already clicked, turn that piece white and make it unclicked
            if(firstClick) {
                pieces[1][clickedPiece-4].getPiece().setFill(Color.WHITE);
                pieces[1][clickedPiece-4].setClicked(false);
            }
            // Set up the newly clicked piece
            piece.setClicked(true);
            this.clickedPiece = findClickedPiece();
            this.firstClick = true;
            piece.getPiece().setFill(Color.YELLOW);
        });
    }

    // Find the index of the clicked piece - if no piece is clicked, return -1
    // Only human pieces can be clicked so the returned piece index is 4, 5, or 6
    private int findClickedPiece() {
        for(int i = 0; i < this.numPieces[1]; i++) {
            if(this.pieces[1][i].getClicked()) { return i+4; }
        }
        return -1;
    }

    private int[] findClickedPieceLoc(int pieceIndex) {
        for(int i = 0; i < numRows; i++) {
            for(int j = 0; j < numRows; j++) {
                if(pieceLocation[i][j] == pieceIndex) { return new int[]{i,j}; }
            }
        }
        return null;
    }
    /*
        Create listeners for each of the squares on the game board to move pieces if clicked
     */
    public void addListener(GameSquare gameSquare,Pane pane) {
        Rectangle square = gameSquare.getSquare();
        // Take action when a piece is clicked
        square.setOnMouseClicked(mouseEvent -> {
            // Only move a clicked piece if the move is valid
            if(this.clickedPiece != -1 && this.firstClick && this.isValidMove()) {
                this.firstClick = false;
                // Move the clicked piece to a new square
                movePiece(pieces[1][this.clickedPiece-4],square.getY(),square.getX());
                int[] clickedPieceLoc = findClickedPieceLoc(this.clickedPiece);
                pieceLocation[clickedPieceLoc[0]][clickedPieceLoc[1]] = 0;
                int[] newLoc = gameSquare.getIndex();
                pieceLocation[newLoc[0]][newLoc[1]] = this.clickedPiece;

                if(gameMode != null && gameMode.intValue() == 0) {
                    showMoves(pane); // Show the moves for HER
                    PauseTransition pause = new PauseTransition(Duration.seconds(1));
                    pause.play();
                    pause.setOnFinished(event ->
                            makeMove(pane)); // It's HER turn
                } else {
                    makeMove(pane);
                }
            }
        });
    }

    // Move the piece that is selected
    private void movePiece(GamePiece piece, double x, double y) {
        Polygon target = piece.getPiece();
        int offset = 50;

        if(piece.getPlayer().equals("Human")) {
            offset = 100;
            // Reset the clicked piece to unclicked
            piece.setClicked(false);
            target.setFill(Color.WHITE);
        }
        double[] coor = {target.getPoints().get(0),target.getPoints().get(1)};
        target.setTranslateX(x + 50 - squareMargin - coor[0]);
        target.setTranslateY(y + (offset - squareMargin) - coor[1]);
    }

    private void makeArrows() {
        this.arrows = new Group();
        int probabilityCnt = 0;
        boolean[][] moves = match.getMoves();
        for(int i = 0; i < moves.length; i++) {
            for(int j = 0; j < moves[i].length; j++) {
                Polygon target = this.pieces[0][i].getPiece();
                if (moves[i][j]) {
                    // Determine the angle depending on which type of move
                    int angle;
                    int offset;
                    Color color;
                    switch (j) {
                        case 0: // Diagonal left
                            angle = -135;
                            offset = -50;
                            color = Color.BLUE;
                            break;
                        case 2: // Diagonal right
                            angle = 135;
                            offset = 50;
                            color = Color.GREEN;
                            break;
                        default: // Down
                            angle = 180;
                            offset = 0;
                            color = Color.RED;
                    };
                    
                    Polygon arrow = new Polygon(arrowPoints);
                    arrow.setRotate(angle);
                    arrow.setTranslateX(target.getPoints().get(4)-25+offset);
                    arrow.setTranslateY(target.getPoints().get(3)+35);
                    //arrow.setStroke(color);
                    //arrow.setStrokeWidth(3.0);
                    arrow.setFill(color);

                    double probability = match.getSingleProbability()[probabilityCnt];
                    Label label = new Label(String.valueOf(probability));
                    label.setTranslateX(arrow.getTranslateX()+10);
                    label.setTranslateY(arrow.getTranslateY()+10);
                    label.setStyle("-fx-font-size: 20");
                    label.setStyle("-fx-font-weight: bold");
                    this.arrows.getChildren().addAll(new StackPane(arrow,label));
                }
            }
        }
    }

    private boolean isValidMove() {
        return true;
    }

    public int[][] getLocations() {
        return pieceLocation;
    }

    // Display arrows indicating all of HER potential moves
    private void showMoves(Pane pane) {
        pane.getChildren().remove(this.arrows);
        match.updateMatchbox(this.getLocations(),numPieces[0]);
        makeArrows();
        pane.getChildren().addAll(this.arrows);
    }
    private void makeMove(Pane pane) {
        // Choose a move at weighted random
        int random = (int) (Math.random() * 100);
        int move = match.getProbability().get(random);
        int[] moveLoc = new int[]{move/3,move%3};
        GamePiece pieceToMove = this.pieces[0][moveLoc[0]];

        // Move the corresponding piece
        //pieceToMove.getPiece().setFill(Color.PINK);
        movePiece(pieceToMove,this.board[moveLoc[0]][moveLoc[1]].getSquare().getY(),
                this.board[moveLoc[0]][moveLoc[1]].getSquare().getX());
        pane.getChildren().remove(this.arrows);
    }
}
