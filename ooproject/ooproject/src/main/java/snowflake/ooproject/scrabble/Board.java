package snowflake.ooproject.scrabble;

import snowflake.ooproject.commandline.Node;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import static snowflake.ooproject.commandline.DictionaryApp.dict;
import static snowflake.ooproject.commandline.DictionaryManagement.*;

public class Board {
    private Tile[][] board;
    private static HashMap<String, String> boardScores;

    public Board() {
        initBoard();
        initBoardScores();
    }

    public void draw() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (this.board[i][j] == null) {
                    System.out.print("*");
                } else {
                    System.out.print(this.board[i][j].getValue());
                }
            }
            System.out.println();
        }
    }

    private void initBoard() {
        this.board = new Tile[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                this.board[i][j] = null;
            }
        }
    }

    private void initBoardScores() {
        Board.boardScores = new HashMap<>();
        //TRIPLE WORD
        boardScores.put("00", "3W");
        boardScores.put("70", "3W");
        boardScores.put("07", "3W");
        boardScores.put("014", "3W");
        boardScores.put("140", "3W");
        boardScores.put("147", "3W");
        boardScores.put("714", "3W");
        boardScores.put("1414", "3W");


        //DOUBLE WORD
        boardScores.put("11", "2W");
        boardScores.put("22", "2W");
        boardScores.put("33", "2W");
        boardScores.put("44", "2W");
        boardScores.put("113", "2W");
        boardScores.put("212", "2W");
        boardScores.put("311", "2W");
        boardScores.put("410", "2W");
        boardScores.put("131", "2W");
        boardScores.put("122", "2W");
        boardScores.put("104", "2W");
        boardScores.put("1010", "2W");
        boardScores.put("1111", "2W");
        boardScores.put("1212", "2W");
        boardScores.put("1313", "2W");

        //TRIPLE LETTER
        boardScores.put("51", "3L");
        boardScores.put("91", "3L");
        boardScores.put("15", "3L");
        boardScores.put("55", "3L");
        boardScores.put("95", "3L");
        boardScores.put("135", "3L");
        boardScores.put("19", "3L");
        boardScores.put("59", "3L");
        boardScores.put("99", "3L");
        boardScores.put("139", "3L");
        boardScores.put("513", "3L");
        boardScores.put("913", "3L");

        //DOUBLE LETTER
        boardScores.put("30", "2L");
        boardScores.put("110", "2L");
        boardScores.put("62", "2L");
        boardScores.put("82", "2L");
        boardScores.put("03", "2L");
        boardScores.put("73", "2L");
        boardScores.put("143", "2L");
        boardScores.put("26", "2L");
        boardScores.put("66", "2L");
        boardScores.put("86", "2L");
        boardScores.put("126", "2L");
        boardScores.put("37", "2L");
        boardScores.put("117", "2L");
        boardScores.put("28", "2L");
        boardScores.put("68", "2L");
        boardScores.put("88", "2L");
        boardScores.put("128", "2L");
        boardScores.put("011", "2L");
        boardScores.put("711", "2L");
        boardScores.put("1411", "2L");
        boardScores.put("612", "2L");
        boardScores.put("812", "2L");
        boardScores.put("314", "2L");
        boardScores.put("1114", "2L");
    }

    public static String getBoardScoreForTile(String ref) {
        if (boardScores.containsKey(ref)) {
            return boardScores.get(ref);
        } else {
            return null;
        }
    }

    public Tile getTileOnBoard(int row, int col) {
        if (row < 0 || row > 14 || col < 0 || col > 14) {
            return null;
        }
        return this.board[row][col];
    }

    public static boolean validateWord(String word) {
        word = formatWord(word);
        Node w1 = new Node(word);
        Node w2 = new Node(word + "a");
        TreeSet<Node> choose = (TreeSet<Node>) dict.subSet(w1,w2);
        Iterator<Node> it = choose.iterator();
        if (it.hasNext())
        {
            Node s = it.next();
            if(s.getWord().equals(word))
                return true;
        }
        return false;
    }

    public void placeWordOnBoard(Move move) {
        if (move.isValid) {
            for (int i = 0; i < move.word.length(); i++) {
                if (move.direction == Move.RIGHT) {
                    this.board[move.startRow][move.startCol+i]
                            = new Tile(String.valueOf(move.word.toUpperCase().charAt(i)));
                } else if (move.direction == Move.DOWN) {
                    this.board[move.startRow+i][move.startCol]
                            = new Tile(String.valueOf(move.word.toUpperCase().charAt(i)));
                }
            }
        }
    }

}
