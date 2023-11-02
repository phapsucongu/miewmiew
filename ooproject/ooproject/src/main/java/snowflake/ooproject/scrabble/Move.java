package snowflake.ooproject.scrabble;

import java.util.ArrayList;

public class Move {
    public static final int RIGHT = 0;
    public static final int DOWN = 1;

    String word;
    int direction;
    int startRow;
    int startCol;
    boolean isValid;
    ArrayList<String> secondaryWords;

    static int totalMoves = 0;

    public Move(String word, int direction, int startRow, int startColumn) {
        this.word = word;
        this.direction = direction;
        this.startRow = startRow;
        this.startCol = startColumn;
        this.isValid = false;
    }

    public static void registerMove(Move move) {
        if (move.isValid) {
            totalMoves++;
        }
    }
}
