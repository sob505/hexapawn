import javafx.scene.Group;
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
    private int clickedPiece;
    private boolean firstClick = false;
    private Matchbox match;// = new Matchbox(this.getLocations(),numRows);
    private int[] numPieces = {3,3};

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
                Polygon target = pieces[1][this.clickedPiece-4].getPiece();
                movePiece(target,square.getY(),square.getX());
                int[] clickedPieceLoc = findClickedPieceLoc(this.clickedPiece);
                pieceLocation[clickedPieceLoc[0]][clickedPieceLoc[1]] = 0;
                int[] newLoc = gameSquare.getIndex();
                pieceLocation[newLoc[0]][newLoc[1]] = this.clickedPiece;

                makeMove(pane); // It's HER turn
            }
        });
    }

    // Move the piece that is selected
    private void movePiece(Polygon target, double x, double y) {
        double[] coor = {target.getPoints().get(0),target.getPoints().get(1)};
        target.setTranslateX(x + 50 - squareMargin - coor[0]);
        target.setTranslateY(y + (100 - squareMargin) - coor[1]);
        // Reset the clicked piece to unclicked
        pieces[1][clickedPiece-4].setClicked(false);
        pieces[1][clickedPiece-4].getPiece().setFill(Color.WHITE);
    }

    private void makeArrows() {
        this.arrows = new Group();
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
                    this.arrows.getChildren().add(arrow);
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

    private void makeMove(Pane pane) {
        pane.getChildren().remove(this.arrows);
        match.updateMatchbox(this.getLocations(),numPieces[0]);
        makeArrows();
        pane.getChildren().addAll(this.arrows);

        // Move a HER piece at random


    }
}
