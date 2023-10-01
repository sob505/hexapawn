import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class Board {
    private final int numRows = 3;
    private final GameSquare[][] board = new GameSquare[numRows][numRows];
    public int[][] pieceLocation = new int[numRows][numRows];
    public GamePiece[][] pieces = new GamePiece[2][numRows];
    private final GridPane gridPane = new GridPane();
    private GamePiece clickedPiece;
    private boolean firstClick = false;
    private Matchbox HERMatch;
    private Matchbox humanMatch;
    private final int[] numPieces = {3,3};
    private Number gameMode;
    private int numRounds = 1;
    private int roundCnt = 0;
    private Pane pane;
    private Button btn;
    private String playerTurn = "Human";
    private Player human = new Player("Human");
    private Player HER = new Player("HER");
    private Group groupPieces;
    private Text HERWins;
    private Text humanWins;
    private int[][] lastPosition;
    private String endGame = null;
    private int[] lastMove;
    private Polygon[] arrowList;
    Group arrows = new Group();
    private boolean keepMemory = false;
    private final double[] arrowPoints = new double[]{
            -5.0, 20.0,
             5.0, 20.0,
             5.0, -10.0,
             15.0, -10.0,
              0.0, -30.0,
            -15.0, -10.0,
            -5.0, -10.0
    };
    private Polygon arrow;

    public Board(Pane pane, Number num) {
        this.makeBoard(pane);
        this.makePieces();
        showWins();
        this.gameMode = num;
    }

    public void makeBoard(Pane pane) {
        this.pane = pane;
        int startx = 100;
        int starty = 100;

        for(int i = 0; i < numRows; i++) {
            for(int j = 0; j < numRows; j++) {
                GameSquare square = new GameSquare(startx,starty,new int[]{i,j});
                square.getSquare().setFill(Color.WHITE);
                square.getSquare().setStroke(Color.BLACK);
                this.board[i][j] = square;
                gridPane.add(square.getSquare(),j,i);
                if(startx == 300) { // Only handles 3x3 board currently
                    startx = 100;
                    starty += 100;
                } else {
                    startx += 100;
                }

                // Add click event handler for squares
                addListener(square);
            }
        }
    }

    public void showWins() {
        this.pane.getChildren().removeAll(HERWins,humanWins);
        this.HERWins = new Text("HER Win Count: " + String.valueOf(HER.getNumWins()));
        this.HERWins.setX(600);
        this.HERWins.setY(100);
        this.humanWins = new Text("Human Win Count: " + String.valueOf(human.getNumWins()));
        this.humanWins.setX(600);
        this.humanWins.setY(120);
        this.pane.getChildren().addAll(HERWins,humanWins);
    }

    /*
        Create 6 pieces on the board: 3 for HER and 3 for the human player.
     */
    public void makePieces() {
        this.groupPieces = new Group();
        double humanCoordinate = 125.0;

        // Create the computer's and the player's pieces
        for(int i = 0; i < numRows; i++) {
            pieces[0][i] = new GamePiece(humanCoordinate+2,125.0,"HER",i+1);
            pieces[1][i] = new GamePiece(humanCoordinate+2,327.0,"Human",i+4);
            humanCoordinate += 100;
            addListener(pieces[1][i]);
            pieceLocation[0][i] = i+1; // Initialize locations for HER pieces
            pieceLocation[1][i] = 0;
            pieceLocation[numRows-1][i] = i+4; // Initialize locations for Human pieces
            this.groupPieces.getChildren().addAll(pieces[0][i].getPiece(),pieces[1][i].getPiece());
        }
        if(!keepMemory) {
            this.HERMatch = new Matchbox(numPieces[0]);
            this.humanMatch = new Matchbox(this.pieceLocation, numPieces[1], "Human");
        }
    }

    public void addListener(ChoiceBox cb) {
        cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                gameMode = newValue;
                if(gameMode.intValue() == 2) {
                    autoMode();
                }
            }
        });
    }

    // Run Auto Mode
    private void autoMode() {
        this.HER.setNumWins();;
        this.human.setNumWins();
        this.roundCnt = 0;
        for(int i = 0; i < this.numRounds; i++) {
            while(this.endGame == null) {
                move(human,HER,humanMatch,HERMatch);
                move(HER,human, HERMatch,humanMatch);
            }
            this.roundCnt++;
        }
    }

    private void move(Player player, Player opponent, Matchbox playerMatchbox, Matchbox opponentMatchbox) {
        makeMove(this.pane,player,playerMatchbox);
        checkWins();
        if(this.endGame == null) {
            opponentMatchbox.updateMatchbox(this.getLocations(), opponent.getNumPieces(), opponent.getName());
            checkMoveCnt();
        }
    }

    public void addListener(Slider slider) {
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                numRounds = newValue.intValue();
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
                clickedPiece.getPiece().setFill(Color.WHITE);
                clickedPiece.setClicked(false);
            }
            // Set up the newly clicked piece
            piece.setClicked(true);
            this.clickedPiece = findClickedPiece();
            this.firstClick = true;
            piece.getPiece().setFill(Color.YELLOW);
        });
    }

    // Find the clicked piece - if no piece is clicked, return null
    private GamePiece findClickedPiece() {
        for(int i = 0; i < this.pieces.length; i++) {
            for(int j = 0; j < this.pieces[i].length; j++) {
                if (this.pieces[i][j] != null && this.pieces[i][j].getClicked()) {
                    return this.pieces[i][j];
                }
            }
        }
        return null;
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
    public void addListener(GameSquare gameSquare) {
        Rectangle square = gameSquare.getSquare();
        // Take action when a piece is clicked
        square.setOnMouseClicked(mouseEvent -> {
            // Only move a clicked piece if the move is valid
            if(this.endGame == null && this.clickedPiece != null && this.firstClick && this.isValidMove(this.clickedPiece,gameSquare)) {
                this.playerTurn = "HER";
                this.firstClick = false;
                // Move the clicked piece to a new square
                movePiece(this.clickedPiece, square.getX(), square.getY(), gameSquare.getIndex());
                checkWins();
                if (this.endGame == null && this.playerTurn.equals("HER")) {
                    this.HERMatch.updateMatchbox(this.getLocations(), HER.getNumPieces(),"HER");
                    checkMoveCnt();

                    // For Slow mode, show the matchbox and let the user choose a move or random one
                    if (this.endGame == null) {
                        if (gameMode != null && gameMode.intValue() == 0) {
                            showMoves(pane); // Show the moves for HER
                            this.btn = new Button("Random Move");
                            this.btn.setTranslateX(205);
                            this.btn.setTranslateY(75);
                            this.btn.setOnAction((ActionEvent event) -> {
                                makeMove(pane,HER,HERMatch);
                                afterHERMove();
                                pane.getChildren().remove(btn);
                            });
                            // It's HER turn
                            pane.getChildren().add(btn);
                        } else{ // Fast Mode
                            makeMove(pane,HER,HERMatch);
                        }
                        afterHERMove();
                    }
                }
            }
        });
    }

    private void afterHERMove() {
        checkWins();
        this.humanMatch.updateMatchbox(this.getLocations(), this.numPieces[1], "Human");
        checkMoveCnt();
    }

    // Move the piece that is selected
    private void movePiece(GamePiece piece, double x, double y, int[] squareIndex) {
        Circle target = piece.getPiece();
        // If there is already a piece in that square, find and remove it
        int index = pieceLocation[squareIndex[0]][squareIndex[1]];
        if(index != 0) {
            GamePiece removed = findPiece(index);
            //if(removed.getPlayer().equals("HER")) { this.pieces[0][removed.getIndex()-1] = null; }
            //else { this.pieces[0][removed.getIndex()-4] = null;}
            removed.getPiece().setStroke(null);
            removed.getPiece().setFill(null);
            this.pane.getChildren().remove(removed);
            this.groupPieces.getChildren().remove(removed);
            if(removed.getPlayer().equals("HER")) { HER.lostPiece(); }
            else { human.lostPiece(); }
        }

        if(piece.getPlayer().equals("Human")) {
            // Reset the clicked piece to unclicked
            piece.setClicked(false);
            target.setFill(Color.WHITE);
        }
        target.setCenterX(x+50);
        target.setCenterY(y+50);
        movePieceLoc(piece,squareIndex);
    }

    private void checkWins() {
        if(this.endGame != null) { return; }
        // Either player wins if their pawn makes it to the opponent's starting position
        for(int j = 0; j < this.pieceLocation.length; j++) {
            if(this.pieceLocation[0][j] > 3) {
                endGame(this.human);
                return;
            } else if(this.pieceLocation[numRows-1][j] != 0 && this.pieceLocation[numRows-1][j] < 3) {
                endGame(this.HER);
                return;
            }
        }
        // If a team has no pawns left, they lose
        if(this.human.getNumPieces() == 0) { endGame(this.HER); }
        else if(this.HER.getNumPieces() == 0) { endGame(this.human); }
    }
    private void checkMoveCnt() {
        if(this.endGame != null) { return; }
        // If a team has no moves left, they lose
        if(this.humanMatch.getMoveCnt() == 0) { endGame(this.HER); }
        else if(this.HERMatch.getMoveCnt() == 0) { endGame(this.human); }
    }

    private void endGame(Player player) {
        this.endGame = "endGame";
        // Print who won
        Text text = new Text(player.getName() + " won!");
        text.setX(215);
        text.setY(60);
        player.won(); // Increment that player's win count
        showWins();
        this.playerTurn = "Human";

        // If HER loses, punish it
        if(player.getName().equals("Human")) {
            HERMatch.punish(this.lastMove,this.lastPosition);
        } else {
            humanMatch.punish(this.lastMove,this.lastPosition);
        }

        // Continue playing if more rounds are left in auto mode
        if(this.gameMode.intValue() == 2) {
            if (roundCnt < numRounds - 1) {
                resetBoard();
                this.pane.getChildren().removeAll(text);
                this.endGame = null;
                this.roundCnt++;
                return;
            }
        }
        // Create a reset button to play a new round
        Button reset = new Button("Play again?");
        reset.setTranslateX(215);
        reset.setTranslateY(70);
        this.pane.getChildren().addAll(text, reset);
        reset.setOnAction((ActionEvent event) -> {
            // Reset the board
            resetBoard();
            this.pane.getChildren().removeAll(text,reset);
            this.endGame = null;
            if(this.gameMode.intValue() == 2) { autoMode(); }
        });
    }

    private void resetBoard() {
        this.keepMemory = true;
        this.groupPieces.getChildren().clear();
        makePieces();
        this.firstClick = false;
        this.human.resetPlayer();
        this.HER.resetPlayer();
        this.humanMatch.updateMatchbox(this.getLocations(),human.getNumPieces(),"Human");
        this.HERMatch.updateMatchbox(this.getLocations(),HER.getNumPieces(),"HER");
        this.pane.getChildren().add(this.groupPieces);
    }

    // Find a piece in pieces based on its index
    private GamePiece findPiece(int index) {
        for(int i = 0; i < this.pieces.length; i++) {
            for(int j = 0; j < this.pieces[i].length; j++) {
                if(this.pieces[i][j].getIndex() == index) { return this.pieces[i][j]; }
            }
        }
        return null;
    }

    private void movePieceLoc(GamePiece piece,int[] squareIndex) {
        int[] pieceLoc = findClickedPieceLoc(piece.getIndex());
        assert pieceLoc != null;
        pieceLocation[pieceLoc[0]][pieceLoc[1]] = 0;
        pieceLocation[squareIndex[0]][squareIndex[1]] = piece.getIndex();
    }

    private void makeArrows() {
        this.arrows = new Group();
        this.arrowList = new Polygon[HERMatch.getMoveCnt()];
        int arrowCnt = 0;
        //this.arrowsList = new ArrayList<>();
        int probabilityCnt = 0;
        boolean[][] moves = HERMatch.getMoves();
        for(int i = 0; i < moves.length; i++) {
            for(int j = 0; j < moves[i].length; j++) {
                Circle target = this.pieces[0][i].getPiece();
                if (moves[i][j]) {
                    // Determine the angle depending on which type of move
                    int angle;
                    int offset;
                    Color color;
                    switch (j) {
                        case 0: // Diagonal left
                            angle = -135;
                            offset = -60;
                            color = Color.BLUE;
                            break;
                        case 2: // Diagonal right
                            angle = 135;
                            offset = 30;
                            color = Color.GREEN;
                            break;
                        default: // Down
                            angle = 180;
                            offset = -15;
                            color = Color.RED;
                    };

                    Polygon arr = new Polygon(arrowPoints);
                    arr.setRotate(angle);
                    arr.setTranslateX(target.getCenterX()+offset);
                    arr.setTranslateY(target.getCenterY()+25);
                    arr.setFill(color);
                    this.arrowList[arrowCnt] = arr;

                    double probability = HERMatch.getSingleProbability()[probabilityCnt];
                    Label label = new Label(String.valueOf(probability));
                    label.setTranslateX(arr.getTranslateX());
                    label.setTranslateY(arr.getTranslateY()+30);
                    label.setStyle("-fx-font-size: 12");
                    label.setStyle("-fx-font-weight: bold");
                    addListener(this.arrowList[arrowCnt],pieces[0][i]);
                    this.arrows.getChildren().addAll(new StackPane(this.arrowList[arrowCnt++],label));
                }
            }
        }
    }

    private void addListener(Polygon arrow,GamePiece pieceToMove) {
        arrow.setOnMouseClicked(mouseEvent -> {
            // Store the last board position for HER
            this.lastPosition = Matchbox.copy(this.pieceLocation);

            int[] pieceLoc = findClickedPieceLoc(pieceToMove.getIndex());
            assert pieceLoc != null;
            GameSquare targetSquare;
            int[] position = new int[2];
            position[0] = pieceToMove.getIndex() - 1;
            if(arrow.getRotate() == -135) {
                position[1] = 0;
                targetSquare = this.board[pieceLoc[0]+1][pieceLoc[1]-1];
            } else if(arrow.getRotate() == 135) {
                position[1] = 2;
                targetSquare = this.board[pieceLoc[0]+1][pieceLoc[1]+1];
            } else {
                position[1] = 1;
                targetSquare = this.board[pieceLoc[0]+1][pieceLoc[1]];
            }
            this.lastMove = position;
            movePiece(pieceToMove,targetSquare.getSquare().getX(),
                    targetSquare.getSquare().getY(),targetSquare.getIndex());
            this.pane.getChildren().removeAll(this.btn,this.arrows);
            afterHERMove();
        });
    }

    // Check if the Human player is making a valid move (assume it's Human)
    private boolean isValidMove(GamePiece piece, GameSquare square) {
        int[] pieceLoc = findClickedPieceLoc(piece.getIndex());
        // If the Human is trying to move backwards or sideways, it's invalid
        if(pieceLoc == null) { return false; }
        if(square.getIndex()[0] >= pieceLoc[0]) { return false;}
        // Based on the piece index, check if the move is valid
        int[] indexDifference = new int[]{pieceLoc[0]-square.getIndex()[0],pieceLoc[1]-square.getIndex()[1]};
        // Trying to go diagonal left
        if(indexDifference[0] == 1 && indexDifference[1] == 1) { return this.humanMatch.getMoves()[piece.getIndex()-4][0]; }
        // Trying to go diagonal right
        else if(indexDifference[0] == 1 && indexDifference[1] == -1) { return this.humanMatch.getMoves()[piece.getIndex()-4][2]; }
        // Trying to go up
        else if(indexDifference[0] == 1 && indexDifference[1] == 0) { return this.humanMatch.getMoves()[piece.getIndex()-4][1]; }
        else { return false; }
    }

    public int[][] getLocations() {
        return pieceLocation;
    }

    // Display arrows indicating all of HER potential moves
    private void showMoves(Pane pane) {
        pane.getChildren().remove(this.arrows);
        makeArrows();
        pane.getChildren().addAll(this.arrows);
    }
    private void makeMove(Pane pane, Player player, Matchbox matchbox) {
        if(this.endGame != null) { return; }
        this.lastPosition = Matchbox.copy(this.pieceLocation);
        // Choose a move at weighted random
        int random = (int) (Math.random() * 99);
        int move = matchbox.getProbability().get(random);
        int[] moveLoc = new int[]{move/3,move%3};
        this.lastMove = moveLoc;
        int playerIndex;
        if(player.getName().equals("HER")) { playerIndex = 0; }
        else {playerIndex = 1;}
        GamePiece pieceToMove = this.pieces[playerIndex][moveLoc[0]];

        // Move the corresponding piece
        int[] boardIndex = new int[2];
        int[] pieceIndex = findClickedPieceLoc(pieceToMove.getIndex());
        if(pieceIndex != null) {
            if (pieceToMove.getPlayer().equals("HER")) {
                boardIndex[0] = pieceIndex[0] + 1;
            } else {
                boardIndex[0] = pieceIndex[0] - 1;
            }
            if (moveLoc[1] == 0) {
                boardIndex[1] = pieceIndex[1] - 1;
            } else if (moveLoc[1] == 1) {
                boardIndex[1] = pieceIndex[1];
            } else if (moveLoc[1] == 2) {
                boardIndex[1] = pieceIndex[1] + 1;
            } else {
                boardIndex[1] = -1;
            }

            GameSquare square = this.board[boardIndex[0]][boardIndex[1]];
            movePiece(pieceToMove,square.getSquare().getX(),
                    square.getSquare().getY(),square.getIndex());
            pane.getChildren().removeAll(this.arrows,this.btn);
        }
    }

    public Group getGroupPieces() { return this.groupPieces; }
    public GridPane getGridPane() { return this.gridPane; }
}
