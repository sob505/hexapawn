import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Board {
    private int numRows = 3;
    public GameSquare[][] board = new GameSquare[numRows][numRows];
    public int[][] pieceLocation = new int[numRows][numRows];
    public GamePiece[][] pieces = new GamePiece[2][numRows];
    private double squareMargin = 25.0;

    public Pane makeBoard(Canvas canvas) {
        Pane pane = new Pane();
        pane.getChildren().addAll(canvas);
        int startx = 100;
        int starty = 100;

        for(int i = 0; i < numRows; i++) {
            for(int j = 0; j < numRows; j++) {
                this.board[i][j] = new GameSquare(startx,starty);
                if(startx == 300) {
                    startx = 100;
                    starty += 100;
                } else {
                    startx += 100;
                }
                addListener(this.board[i][j]);
                pane.getChildren().addAll(this.board[i][j].getSquare());
            }
        }
        return pane;
    }

    /*
        Create 6 pieces on the board: 3 for HER and 3 for the human player.
     */
    public Pane makePieces() {
        double humanCoordinate = 125.0;
        // Create the computer's and the player's pieces
        Pane pane = new Pane();
        for(int i = 0; i < numRows; i++) {
            pieces[0][i] = new GamePiece(humanCoordinate,125.0,"HER");
            pieces[1][i] = new GamePiece(humanCoordinate,325.0,"Human");
            humanCoordinate += 100;
            addListener(pieces[1][i]);
            pieceLocation[numRows-1][i] = 1;
            pane.getChildren().addAll(pieces[0][i].getPiece(),pieces[1][i].getPiece());
        }
        return pane;
    }

    public void addListener(GamePiece piece) {
        // Take action when a piece is clicked
        piece.getPiece().setOnMouseClicked(mouseEvent -> {
            int index = findClickedPiece();
            // If a piece is already clicked, turn that piece white
            if(index != -1) {
                pieces[1][index].getPiece().setFill(Color.WHITE);
                pieces[1][index].setClicked(false);
            }
            piece.getPiece().setFill(Color.YELLOW);
            piece.setClicked(true);
            // setCoordinates(mouseEvent.getX(), mouseEvent.getY());
        });
    }

    // Find the index of the clicked piece - if no piece is clicked, return -1
    private int findClickedPiece() {
        for(int i = 0; i < numRows; i++) {
            if(this.pieces[1][i].getClicked()) { return i; }
        }
        return -1;
    }

    public void addListener(GameSquare square) {
        // Take action when a piece is clicked
        square.getSquare().setOnMouseClicked(mouseEvent -> {
            square.getSquare().setFill(Color.BLUE);
            int movedPiece = findClickedPiece();
            movePiece(pieces[1][movedPiece],square.getSquare().getX() + squareMargin,square.getSquare().getY() + squareMargin);
        });
    }

    private void movePiece(GamePiece piece, double x, double y) {
        piece.setCoordinates(x,y);
    }

    private boolean isValidMove() {
        return true;
    }
}
