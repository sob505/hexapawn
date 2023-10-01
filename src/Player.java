public class Player {
    private int numPieces = 3;
    private int numWins = 0;
    private String name;
    private int[] lastMove;

    public Player(String name) {
        this.name = name;
    }

    public void resetPlayer() {
        this.numPieces = 3;
    }

    public int getNumPieces() { return this.numPieces; }
    public int getNumWins() { return this.numWins; }
    public void setNumPieces(int pieces) { this.numPieces = pieces; }
    public void lostPiece() { this.numPieces--; }
    public void won() { this.numWins++; }
    public String getName() { return this.name; }
    public void setLastMove(int[] move) { this.lastMove = move; }
    public int[] getLastMove() { return this.lastMove; }
    public void setNumWins() { this.numWins = 0; }
}
