/*
    The Matchbox class keeps track of all the potential moves and probabilities of those moves.
*/

import javafx.scene.Group;
import javafx.scene.shape.Polygon;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Matchbox {
    private ArrayList<Integer> probability;
    private double[] singleProbability;
    private final boolean[][] moves;
    private int moveCnt = 0;
    private int[][] board;
    private final ArrayList<int[][]> matchboxes = new ArrayList<>();
    private final ArrayList<int[]> punishedMoves = new ArrayList<>();
    public Matchbox(int numPieces) {
        this.moves = new boolean[numPieces][numPieces];
        this.probability = new ArrayList<Integer>();
    }

    public Matchbox(int[][] board, int numPieces,String player) {
        this.board = board;
        this.moves = new boolean[numPieces][numPieces];
        this.probability = new ArrayList<Integer>();

        if(player.equals("HER")) {
            for (int i = 1; i <= numPieces; i++) {
                int[] location = findLoc(i);
                checkMoves(i - 1, location, "HER");
            }
        } else {
            for (int i = 4; i <= 3+numPieces; i++) {
                int[] location = findLoc(i);
                checkMoves(i - 4, location, "Human");
            }
        }
        countMoves();
        calcProbability();
    }

    private int[] findLoc(int pieceNum) {
        for(int i = 0; i < this.board.length; i++) {
            for(int j = 0; j < this.board[i].length; j++) {
                if(board[i][j] == pieceNum) {
                    return new int[]{i,j};
                }
            }
        }
        return new int[]{-1,-1};
    }

    private void checkMoves(int index, int[] location, String player) {
        // If a piece is missing, make the whole row of moves false
        if(location[0] < 0) {
            Arrays.fill(moves[index], false);
            return;
        }
        if(player.equals("HER")) { // HER pieces can only move downwards
            // Check if the piece can move diagonally left
            moves[index][0] = (location[1] > 0 && this.board[location[0] + 1][location[1] - 1] > 3);
            // Check if the piece can move forward
            moves[index][1] = (this.board[location[0] + 1][location[1]] == 0);
            // Check if the piece can move diagonally right
            moves[index][2] = (location[1] < this.board.length - 1 && this.board[location[0] + 1][location[1] + 1] > 3);
        } else { // Human pieces move upwards
            // Check if the piece can move diagonally left
            moves[index][0] = (location[1] > 0 && this.board[location[0] - 1][location[1] - 1] != 0
                    && this.board[location[0] - 1][location[1] - 1] < 4);
            // Check if the piece can move forward
            moves[index][1] = (this.board[location[0] - 1][location[1]] == 0);
            // Check if the piece can move diagonally right
            moves[index][2] = (location[1] < this.board.length - 1 && this.board[location[0] - 1][location[1] + 1] != 0
                    && this.board[location[0] - 1][location[1] + 1] < 4);
        }
    }


    private void countMoves() {
        this.moveCnt = 0;
        for(int i = 0; i < moves.length; i++) {
            for(int j = 0; j < moves[i].length; j++) {
                if(moves[i][j]) { this.moveCnt++; }
            }
        }
       // if(moveCnt == 0) { this.result = "Human Wins"; }
    }

    private void calcProbability() {
        this.probability = new ArrayList<Integer>();
        this.singleProbability = new double[moveCnt];
        int probabilityCnt = 0;
        for(int i = 0; i < this.moves.length; i++) {
            for(int j = 0; j < this.moves[i].length; j++) {
                if(this.moves[i][j]) {
                    int repeat = (int) (((double) 1 / this.moveCnt) * 100);
                    this.singleProbability[probabilityCnt] = repeat;
                    for(int k = 0; k < repeat; k++) {
                        this.probability.add((i * 3) + j);//((double) 1 / moveCnt) * 100;
                    }
                }
            }
        }
    }

    public void updateMatchbox(int[][] board, int numPieces, String player) {
        this.board = board;
        if(player.equals("HER")) {
            for (int i = 1; i <= numPieces; i++) {
                int[] location = findLoc(i);
                checkMoves(i - 1, location, "HER");
            }
        } else {
            for (int i = 4; i <= 3+numPieces; i++) {
                int[] location = findLoc(i);
                checkMoves(i - 4, location, "Human");
            }
        }

        removeMatches(this.matchboxes,this.punishedMoves);
        countMoves();
        calcProbability();
    }

    private void removeMatches(ArrayList<int[][]> matchboxes, ArrayList<int[]> lastMoves) {
        for(int i = 0; i < matchboxes.size(); i++) {
            if (Arrays.deepEquals(matchboxes.get(i), this.board)) {
                this.moves[lastMoves.get(i)[0]][lastMoves.get(i)[1]] = false;
            }
        }
    }

    public void punish(int[] lastMove,int[][] lastPosition) {
        this.punishedMoves.add(copy(lastMove));
        this.matchboxes.add(copy(lastPosition));
    }

    public static int[][] copy(int[][] original) {
        int[][] temp = new int[original.length][original[0].length];
        for(int i = 0; i < original.length; i++) {
            for(int j = 0; j < original[i].length; j++) {
                temp[i][j] = original[i][j];
            }
        }
        return temp;
    }

    public static int[] copy(int[] original) {
        int[] temp = new int[original.length];
        for(int i = 0; i < original.length; i++) {
            temp[i] = original[i];
        }
        return temp;
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
