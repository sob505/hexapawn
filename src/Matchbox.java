/*
    The Matchbox class keeps track of all the potential moves and probabilities of those moves.
*/

import javafx.scene.Group;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;

public class Matchbox {
    private ArrayList<Integer> probability;
    private double[] singleProbability;
    private final boolean[][] moves;
    private int moveCnt = 0;
    private String result;

    public Matchbox(int[][] board, int numPieces) {
        this.moves = new boolean[numPieces][numPieces];
        this.probability = new ArrayList<Integer>();

        for(int i = 1; i <= numPieces; i++) {
            int[] location = findLoc(i,board);
            checkMoves(i-1,location,board,"HER");
        }
        countMoves();
        calcProbability();
    }

    private int[] findLoc(int pieceNum,int[][] board) {
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                if(board[i][j] == pieceNum) {
                    return new int[]{i,j};
                }
            }
        }
        return new int[]{-1,-1};
    }

    private void checkMoves(int index, int[] location, int[][] board, String player) {
        if(player.equals("HER")) { // HER pieces can only move downwards
            // If a piece has reached the bottom of the board, HER wins
            if(location[0] == board.length) { this.result = "HER Wins"; }
            else {
                // Check if the piece can move diagonally left
                moves[index][0] = (location[1] > 0 && board[location[0] + 1][location[1] - 1] != 0);
                // Check if the piece can move forward
                moves[index][1] = (board[location[0] + 1][location[1]] == 0);
                // Check if the piece can move diagonally right
                moves[index][2] = (location[1] < board.length - 1 && board[location[0] + 1][location[1] + 1] != 0);
            }
        } else { // Human pieces move upwards
            // If a piece has reached the top of the board, Human wins
            if(location[0] == 0) { this.result = "Human Wins"; }
            else {// Check if the piece can move diagonally left
                moves[index][0] = (location[1] > 0 && board[location[0] + 1][location[1] + 1] != 0);
                // Check if the piece can move forward
                moves[index][1] = (board[location[0] - 1][location[1]] == 0);
                // Check if the piece can move diagonally right
                moves[index][2] = (location[1] < board.length - 1 && board[location[0] - 1][location[1] + 1] != 0);
            }
        }
    }

    private void countMoves() {
        for(int i = 0; i < moves.length; i++) {
            for(int j = 0; j < moves[i].length; j++) {
                if(moves[i][j]) { moveCnt++; }
            }
        }
        if(moveCnt == 0) { this.result = "Human Wins"; }
    }

    private void calcProbability() {
        this.singleProbability = new double[moveCnt];
        int probabilityCnt = 0;
        for(int i = 0; i < moves.length; i++) {
            for(int j = 0; j < moves[i].length; j++) {
                if(moves[i][j]) {
                    int repeat = (int) (((double) 1 / moveCnt) * 100);
                    this.singleProbability[probabilityCnt] = repeat;
                    for(int k = 0; k < repeat; k++) {
                        this.probability.add((i * 3) + j);//((double) 1 / moveCnt) * 100;
                    }
                }
            }
        }
    }

    public void updateMatchbox(int[][] board, int numPieces) {
        for(int i = 1; i <= numPieces; i++) {
            int[] location = findLoc(i,board);
            checkMoves(i-1,location,board,"HER");
            calcProbability();
        }
    }

    public boolean[][] getMoves() { return this.moves; }
    public int getMoveCnt() { return this.moveCnt; }
    public ArrayList<Integer> getProbability() { return this.probability; }
    public double[] getSingleProbability() { return this.singleProbability; }

    /*
            Example board:
            1 2 3
            0 0 6
            4 5 0

            Example moves: diagonal left, forward, diagonal right
            row 1: 0, 1, 0 // moves for piece 1
            row 2: 0, 1, 1 // moves for piece 2
            row 3: 0, 0, 0 // moves for piece 3

            Example probability:
            0.0 0.3 0.0
            0.0 0.3 0.3
            0.0 0.0 0.0
     */
}
