import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class Board {
    private int numRows = 3;
    private final GameSquare[][] board = new GameSquare[numRows][numRows];
    public int[][] pieceLocation = new int[numRows][numRows];
    public GamePiece[][] pieces = new GamePiece[2][numRows];
    private final double squareMargin = 25.0;
    private GridPane gridPane = new GridPane();
    private int clickedPiece = -1;
    private boolean firstClick = false;

    public GridPane makeBoard(Pane pane) {
        int startx = 100;
        int starty = 100;

        for(int i = 0; i < numRows; i++) {
            for(int j = 0; j < numRows; j++) {
               // board[i][j] = new GameSquare(startx,starty);
                Rectangle square = new Rectangle(startx,starty,100,100);
                square.setFill(Color.WHITE);
                square.setStroke(Color.BLACK);
                gridPane.add(square,i,j);
                if(startx == 300) {
                    startx = 100;
                    starty += 100;
                } else {
                    startx += 100;
                }

                // Add click event handler for squares
               /* square.setOnMouseClicked(event -> {
                    square.setFill(Color.BLUE);
                }); */
                addListener(square,pane);//board[i][j]);
                //pane.getChildren().add(square);//this.board[i][j].getSquare());
            }
        }
        return gridPane;
    }

    /*
        Create 6 pieces on the board: 3 for HER and 3 for the human player.
     */
    public Group makePieces(Pane pane) {
        Group groupPieces = new Group();
        double humanCoordinate = 125.0;

        // Create the computer's and the player's pieces
        for(int i = 0; i < numRows; i++) {
            pieces[0][i] = new GamePiece(humanCoordinate,125.0,"HER");
            pieces[1][i] = new GamePiece(humanCoordinate,325.0,"Human");
            humanCoordinate += 100;
            addListener(pieces[1][i]);
            pieceLocation[numRows-1][i] = 1;
            groupPieces.getChildren().addAll(pieces[0][i].getPiece(),pieces[1][i].getPiece());
        }

        return groupPieces;
    }

    public void addListener(GamePiece piece) {
        // Take action when a piece is clicked
        piece.getPiece().setOnMouseClicked(mouseEvent -> {
            // If a piece is already clicked, turn that piece white and make it unclicked
            if(firstClick) {
                pieces[1][clickedPiece].getPiece().setFill(Color.WHITE);
                pieces[1][clickedPiece].setClicked(false);
            }
            // Set up the newly clicked piece
            piece.setClicked(true);
            this.clickedPiece = findClickedPiece();
            this.firstClick = true;
            piece.getPiece().setFill(Color.YELLOW);
            piece.setClicked(true);
        });
    }

    // Find the index of the clicked piece - if no piece is clicked, return -1
    private int findClickedPiece() {
        for(int i = 0; i < numRows; i++) {
            if(this.pieces[1][i].getClicked()) { return i; }
        }
        return -1;
    }

    public void addListener(Rectangle square,Pane pane) {
        // Take action when a piece is clicked
        square.setOnMouseClicked(mouseEvent -> {
            // Only move a clicked piece if the move is valid
            if(this.clickedPiece != -1 && this.firstClick && this.isValidMove()) {
                this.firstClick = false;
                // Move the clicked piece to a new square
                Polygon target = pieces[1][clickedPiece].getPiece();
                movePiece(target,square.getY(),square.getX());
            }
        });
    }

    // Move the piece that is selected
    private void movePiece(Polygon target, double x, double y) {
        double[] coor = {target.getPoints().get(0),target.getPoints().get(1)};
        target.setTranslateX(x + 50 - squareMargin - coor[0]);
        target.setTranslateY(y + (100 - squareMargin) - coor[1]);
        // Reset the clicked piece to unclicked
        pieces[1][clickedPiece].setClicked(false);
        pieces[1][clickedPiece].getPiece().setFill(Color.WHITE);
    }

    private boolean isValidMove() {
        return true;
    }
}
