package snowflake.ooproject.scrabble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Scores {
    private HashMap<Player, Integer> playerScores;
    private static HashMap<Character, Integer> tileScore;

    public Scores(List<Player> players) {
        this.playerScores = new HashMap<>();
        for (Player p : players) {
            playerScores.put(p, 0);
        }
        this.initTileScore();
    }

    private void initTileScore() {
        Scores.tileScore = new HashMap<>();
        Scores.tileScore.put('A', 1);
        Scores.tileScore.put('B', 3);
        Scores.tileScore.put('C', 3);
        Scores.tileScore.put('D', 2);
        Scores.tileScore.put('E', 1);
        Scores.tileScore.put('F', 4);
        Scores.tileScore.put('G', 2);
        Scores.tileScore.put('H', 4);
        Scores.tileScore.put('I', 1);
        Scores.tileScore.put('J', 8);
        Scores.tileScore.put('K', 5);
        Scores.tileScore.put('L', 1);
        Scores.tileScore.put('M', 3);
        Scores.tileScore.put('N', 1);
        Scores.tileScore.put('O', 1);
        Scores.tileScore.put('P', 3);
        Scores.tileScore.put('Q', 10);
        Scores.tileScore.put('R', 1);
        Scores.tileScore.put('S', 1);
        Scores.tileScore.put('T', 1);
        Scores.tileScore.put('U', 1);
        Scores.tileScore.put('V', 4);
        Scores.tileScore.put('W', 4);
        Scores.tileScore.put('X', 8);
        Scores.tileScore.put('Y', 4);
        Scores.tileScore.put('Z', 10);
    }

    private int getTileScore(Character C) {
        if (Scores.tileScore.containsKey(C)) {
            return Scores.tileScore.get(C);
        }
        return -1;
    }

    public void updatePlayerScore(Player player, int score) {
        this.playerScores.put(player, this.playerScores.get(player) + score);
    }

    public int getPlayerScore(Player player) {
        return playerScores.getOrDefault(player, -1);
    }

    public static int computeMoveScore(Move move) {
        String word = move.word;
        int dir = move.direction;
        int row = move.startRow;
        int col = move.startCol;

        int totalScore = 0;
        int tempWordScore = 0;
        int wordMultiplier = 1;
        int tileMultiplier = 1;
        int secondaryScore;

        for (int i = 0; i < word.length(); i++) {
            if (dir == Move.RIGHT) {

                String boardRef = String.valueOf(row) + String.valueOf(col + i);
                if (Objects.equals(Board.getBoardScoreForTile(boardRef), "2W")) {    wordMultiplier *= 2; }
                if (Objects.equals(Board.getBoardScoreForTile(boardRef), "3W")) {    wordMultiplier *= 3; }
                if (Objects.equals(Board.getBoardScoreForTile(boardRef), "2L")) {    tileMultiplier = 2; }
                if (Objects.equals(Board.getBoardScoreForTile(boardRef), "3L")) {    tileMultiplier = 3; }

                tempWordScore += (Scores.tileScore.get(Character.toUpperCase(word.charAt(i)))) *
                        tileMultiplier;
                tileMultiplier = 1;

            } else if (dir == Move.DOWN) {

                String boardRef = String.valueOf(row + i) + String.valueOf(col);
                if (Objects.equals(Board.getBoardScoreForTile(boardRef), "2W")) {    wordMultiplier *= 2; }
                if (Objects.equals(Board.getBoardScoreForTile(boardRef), "3W")) {    wordMultiplier *= 3; }
                if (Objects.equals(Board.getBoardScoreForTile(boardRef), "2L")) {    tileMultiplier = 2; }
                if (Objects.equals(Board.getBoardScoreForTile(boardRef), "3L")) {    tileMultiplier = 3; }

                tempWordScore += (Scores.tileScore.get(Character.toUpperCase(word.charAt(i)))) *
                        tileMultiplier;
                tileMultiplier = 1;

            }
        }

        totalScore += tempWordScore * wordMultiplier;
        secondaryScore = Scores.computeSecondaryWordScore(move);

        return totalScore + secondaryScore;
    }

    private static int computeSecondaryWordScore(Move move) {
        ArrayList<String> list = move.secondaryWords;
        int tempScore = 0;

        for (String str : list) {
            for (int i = 0; i < str.length(); i++) {
                tempScore += Scores.tileScore.get(Character.toUpperCase(str.charAt(i)));
            }
        }

        return tempScore;
    }
}
