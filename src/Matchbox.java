/*
    The Matchbox class keeps track of all the potential moves and probabilities of those moves.
*/

public class Matchbox {
    private double[][] probability;
    private final boolean[][] moves;
    private double moveCnt = 0.0;

    public Matchbox(int[][] board, int numPieces) {
        moves = new boolean[numPieces][numPieces];

        for(int i = 1; i <= numPieces; i++) {
            int[] location = findLoc(i,board);
            checkMoves(i,location,board);
            calcProbability();
        }
    }

    private int[] findLoc(int pieceNum,int[][] board) {
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; i++) {
                if(board[i][j] == pieceNum) {
                    return new int[]{i,j};
                }
            }
        }
        return new int[]{-1,-1};
    }

    private void checkMoves(int index, int[] location, int[][] board) {
        // Check if the piece can move diagonally left
        moves[index][0] = (location[0] > 0 && board[location[0] - 1][location[1]] != 0);
        // Check if the piece can move forward
        moves[index][1] = (board[location[0]][location[1]+1] == 0);
        // Check if the piece can move diagonally right
        moves[index][2] = (location[0] < board.length - 1 && board[location[0] + 1][location[1]] != 0);
        countMoves();
    }

    private void countMoves() {
        for(int i = 0; i < moves.length; i++) {
            for(int j = 0; j < moves[i].length; j++) {
                if(moves[i][j]) { moveCnt++; }
            }
        }
    }

    private void calcProbability() {
        for(int i = 0; i < moves.length; i++) {
            for(int j = 0; j < moves[i].length; j++) {
                if(moves[i][j]) { probability[i][j] = 1 / moveCnt; }
                else { probability[i][j] = 0; }
            }
        }
    }

    /*
            Example board:
            1 2 3
            0 0 6
            4 5 0

            Example moves: diagonal left, forward, diagonal right
            row 1: 0, 1, 0
            row 2: 0, 1, 1
            row 3: 0, 0, 0
     */
}
