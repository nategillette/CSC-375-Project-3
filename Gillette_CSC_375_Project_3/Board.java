package PegSolitaire;


import java.util.ArrayList;

public class Board {

    //32 pegs, 33 spaces
    public final int size = 7;
    int[][] board = {
            {0, 0, 1, 1, 1, 0, 0},
            {0, 0, 1, 1, 1, 0, 0},
            {1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 2, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1},
            {0, 0, 1, 1, 1, 0, 0},
            {0, 0, 1, 1, 1, 0, 0}
    };

    private static final int FULL = 1;
    private static final int EMPTY = 2;

    /**
     * Determines if the given move from x,y to newX,newY is legitimate and works.
     * Called before a jump.
     */

    public boolean isValidMove(int x, int y, int newX, int newY) {
        return 0 <= x && x < board.length
                && 0 <= y && y < board[x].length
                && 0 <= newX && newX < board.length
                && 0 <= newY && newY < board[newX].length
                && board[newX][newY] == EMPTY
                && board[(x + newX) / 2][(y + newY) / 2] == FULL
                && board[x][y] == FULL;
    }

    /**
     * Method modifies board array according to the coordinates given. Returns false if the move is invalid.
     * *Changes array value*
     */

    public boolean jump(int x, int y, int direction) {
        int newX = getNewX(x, direction);
        int newY = getNewY(y, direction);

        if (isValidMove(x, y, newX, newY)) {
            board[newX][newY] = FULL;
            board[x][y] = EMPTY;
            board[(x + newX) / 2][(y + newY) / 2] = EMPTY;
            return true;
        }

        return false;
    }

    /**
     * Returns the number of remaining moves on the board.
     */

    public ArrayList<Move> getRemainingMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                for (int direction = 0; direction < this.size; direction++) {
                    int newX = getNewX(x, direction);
                    int newY = getNewY(y, direction);
                    if (isValidMove(x, y, newX, newY)) {
                        moves.add(new Move(x, y, direction));
                    }
                }
            }
        }
        return moves;
    }

    /**
     * Determines a new coordinate for a X directin move.
     */

    private int getNewX(int x, int direction) {
        int newX = x;
        switch (direction) {
            case 0:
                newX += 2;
                break;
            case 2:
                newX -= 2;
        }
        return newX;
    }

    /**
     * Determines a new coordinate for a Y directin move.
     */

    private int getNewY(int y, int direction) {
        int newY = y;

        switch (direction) {
            case 1:
                newY -= 2;
                break;
            case 3:
                newY += 2;
        }

        return newY;
    }

    /**
     * Method copies the board from the source object to the destination.
     * Use this always to create a copy of a board.
     */

    public void copy(Board source, Board target) {
        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                target.board[x][y] = source.board[x][y];
            }
        }
    }

    /**
     * Returns the number of remaining pegs on the board.
     */

    public int getRemainingPegs() {
        int count = 0;
        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                if (isOccupied(x, y)) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Returns if the board location is occupied or empty.
     */

    public boolean isOccupied(int x, int y) {
        return board[x][y] == FULL;
    }

    /**
     * Returns if the board location is a barrier or is playable.
     */

    public boolean isBarrier(int x, int y) {
        return board[x][y] == 0;
    }

    /**
     * Returns a string representation of the board as well as its remaining moves
     */

    @Override
    public String toString() {
        String ret = "remaining: " + this.getRemainingPegs() + "\n";
        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                ret = ret + board[x][y];
            }
            ret = ret + "\n";
        }
        return ret;
    }

}

