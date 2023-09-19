import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Board {
    public GameSquare[] board = new GameSquare[9];
    public int[] pieceLocation = new int[9];
    public GamePiece[] pieces = new GamePiece[9];
    private double squareMargin = 25.0;

    public Pane makeBoard(Canvas canvas) {
        Pane pane = new Pane();
        pane.getChildren().addAll(canvas);
        int startx = 100;
        int starty = 100;

        for(int i = 0; i < this.board.length; i++) {
            GameSquare temp = new GameSquare(startx,starty);
            this.board[i] = temp;
            if(startx == 300) {
                startx = 100;
                starty += 100;
            } else {
                startx += 100;
            }
            addListener(this.board[i]);
            pane.getChildren().addAll(this.board[i].getSquare());
        }
        return pane;
    }

    /*
        Create 6 pieces on the board: 3 for HER and 3 for the human player.
     */
    public Pane makePieces() {
        // Create the computer's pieces
        GamePiece HER1 = new GamePiece(125.0,125.0, "HER");
        GamePiece HER2 = new GamePiece(225.0,125.0, "HER");
        GamePiece HER3 = new GamePiece(325.0,125.0, "HER");

        // Create the player's pieces
        double humanCoordinate = 125.0;
        for(int i = 0; i < pieces.length; i++) {
            pieces[i] = new GamePiece(humanCoordinate,325.0,"Human");
            humanCoordinate += 100;
            addListener(pieces[i]);
        }

        Pane pane = new Pane();
        pane.getChildren().addAll(HER1.getPiece(),HER2.getPiece(),HER3.getPiece(),
                pieces[0].getPiece(),pieces[1].getPiece(),pieces[2].getPiece());
        return pane;
    }

    public void addListener(GamePiece piece) {
        // Take action when a piece is clicked
        piece.getPiece().setOnMouseClicked(mouseEvent -> {
            int index = findClickedPiece();
            // If a piece is already clicked, turn that piece white
            if(index != -1) {
                pieces[index].getPiece().setFill(Color.WHITE);
                pieces[index].setClicked(false);
            }
            piece.getPiece().setFill(Color.YELLOW);
            piece.setClicked(true);
            // setCoordinates(mouseEvent.getX(), mouseEvent.getY());
        });
    }

    // Find the index of the clicked piece - if no piece is clicked, return -1
    private int findClickedPiece() {
        for(int i = 0; i < this.pieces.length; i++) {
            if(this.pieces[i].getClicked()) { return i; }
        }
        return -1;
    }

    public void addListener(GameSquare square) {
        // Take action when a piece is clicked
        square.getSquare().setOnMouseClicked(mouseEvent -> {
            square.getSquare().setFill(Color.BLUE);
            int movedPiece = findClickedPiece();
            movePiece(pieces[movedPiece],square.getSquare().getX() + squareMargin,square.getSquare().getY() + squareMargin);
        });
    }

    private void movePiece(GamePiece piece, double x, double y) {
        piece.setCoordinates(x,y);
    }

    private boolean isValidMove() {
        return true;
    }
}
