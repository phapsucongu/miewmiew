package snowflake.ooproject.scrabble;

import java.util.*;

public class Gameplay {
    private HashMap<Character, Integer> tileBag;
    private List<Player> players;
    private int turn;
    private Scores scores;
    private Board gameBoard;

    public Gameplay() {
        this.initTileBag();
        players = new ArrayList<>();
        gameBoard = new Board();
    }

    public Gameplay(int numberOfPlayers) {
        this.initTileBag();
        players = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            players.add(new Player());
        }
        gameBoard = new Board();
        scores = new Scores(players);
    }

    private void initTileBag() {
        this.tileBag =  new HashMap<>();
        this.tileBag.put('A', 9);
        this.tileBag.put('B', 2);
        this.tileBag.put('C', 2);
        this.tileBag.put('D', 4);
        this.tileBag.put('E', 12);
        this.tileBag.put('F', 2);
        this.tileBag.put('G', 3);
        this.tileBag.put('H', 2);
        this.tileBag.put('I', 9);
        this.tileBag.put('J', 1);
        this.tileBag.put('K', 1);
        this.tileBag.put('L', 4);
        this.tileBag.put('M', 2);
        this.tileBag.put('N', 6);
        this.tileBag.put('O', 8);
        this.tileBag.put('P', 2);
        this.tileBag.put('Q', 1);
        this.tileBag.put('R', 6);
        this.tileBag.put('S', 4);
        this.tileBag.put('T', 6);
        this.tileBag.put('U', 4);
        this.tileBag.put('V', 2);
        this.tileBag.put('W', 2);
        this.tileBag.put('X', 1);
        this.tileBag.put('Y', 2);
        this.tileBag.put('Z', 1);
    }

    public boolean removeTileFromBag(char C) {
        if (this.tileBag.containsKey(C) && this.tileBag.get(C) > 0) {
            int newValue = this.tileBag.get(C) - 1;
            this.tileBag.put(C, newValue);
            return true;
        }
        return false;
    }

    public void addTileToBag(char C) {
        if (this.tileBag.containsKey(C)) {
            int newValue = this.tileBag.get(C) + 1;
            this.tileBag.put(C, newValue);
        }
    }

    public int totalNumberOfTilesInBag() {
        int total = 0;

        for (int i = 65; i <= 90; i++) {
            char C = (char) i;
            total += tileBag.get(C);
        }

        return total;

    }

    public Character getRandomLetterFromBag() {
        Random random = new Random();
        List<Character> letters = new ArrayList<>();
        for (Character c : tileBag.keySet()) {
            if (this.tileBag.get(c) != 0) letters.add(c);
        }
        Character randomLetter = letters.get(random.nextInt(letters.size()));
        this.tileBag.put(randomLetter, this.tileBag.get(randomLetter) - 1);
        return randomLetter;
    }

    public int switchTurn() {
        return turn = (turn + 1) % (players.size());
    }

    public void refreshTray(Player player, String word, int type) {
        Tile[] lettersTray = player.getLetters();

        if (type == 0) {
            for (Character character : word.toUpperCase().toCharArray()) {
                for (int i = 0; i < lettersTray.length; i++) {
                    if (lettersTray[i] == null) {
                        continue;
                    }
                    if(character.equals(lettersTray[i].getValue().charAt(0))) {
                        this.addTileToBag(lettersTray[i].getValue().charAt(0));
                        player.letters[i] = null;
                        break;
                    }
                }
            }
        } else if (type == 1) {
            for (int i = 0; i < lettersTray.length; i++) {
                this.addTileToBag(lettersTray[i].getValue().charAt(0));
                player.letters[i] = null;
            }
        }
    }


    public void refillTray(Player player) {
        Tile[] lettersTray = player.getLetters();
        for (int i = 0 ; i < lettersTray.length ; i++) {
            if (lettersTray[i] == null || lettersTray[i].getValue() == null) {
                lettersTray[i] = new Tile(String.valueOf(this.getRandomLetterFromBag()));
            }
        }
        player.setLetters(lettersTray);
    }

    public void update() {
        Player thePlayer = players.get(turn);

        gameBoard.draw();
        refillTray(thePlayer);
        thePlayer.drawTray();
        System.out.println(scores.getPlayerScore(thePlayer));

        Scanner scanner = new Scanner(System.in);
        String theWord = scanner.next();
        int startRow = scanner.nextInt();
        int startCol = scanner.nextInt();
        int dir = scanner.nextInt();
        Move theMove = new Move(theWord, dir, startRow, startCol);

        if (isMoveValid(thePlayer, theMove)) {
            gameBoard.placeWordOnBoard(theMove);
            refreshTray(thePlayer, theWord, 0);
            int score = Scores.computeMoveScore(theMove);
            this.scores.updatePlayerScore(thePlayer, score);
        }

        switchTurn();
        update();
    }

    public boolean isMoveValid(Player player, Move move) {
        String word = move.word.toUpperCase();
        int row = move.startRow;
        int col = move.startCol;
        int dir = move.direction;
        Tile[] tileCopy = player.getLetters();

        // DOES THE BOARD OVERFLOW
        if ((dir == Move.RIGHT && (col + word.length() > 14)) ||
                (dir == Move.DOWN && (row + word.length() > 14))) {
            System.out.println("isMoveValid err: : Board overflow. Invalid move!");
            return false;
        }

        // IS THE WORD VALID USING A DICT
        if (!Board.validateWord(word)) {
            System.out.println("isMoveValid err: This word doesn't exist.");
            return false;
        }

        // INTENDED WORD SHOULD BE THE LARGEST CONTIGUOUS STRING IN THAT DIRECTION
        if (dir == Move.RIGHT) {
            if ((col + word.length() + 1 <= 14
                    && gameBoard.getTileOnBoard(row, col + word.length() + 1) != null)
                    || (col - 1 >= 0 &&
                    gameBoard.getTileOnBoard(row, col - 1) != null)) {
                System.out.println("isMoveValid err: Incomplete input word (find better message)");
                return false;
            }
        } else if (dir == Move.DOWN) {
            if ((row + word.length() + 1 <= 14 &&
                    gameBoard.getTileOnBoard(row + word.length() + 1, col) != null)
                    || (row - 1 >= 0 &&
                    gameBoard.getTileOnBoard(row - 1, col) != null)) {
                System.out.println("Incomplete input word");
                return false;
            }
        }

        // DOES THE FIRST MOVE CROSS THE BOARD CENTER
        if (Move.totalMoves == 0) {
            if ((dir == Move.RIGHT && (col + word.length() < 7)) ||
                    (dir == Move.DOWN && (row + word.length() < 7)) ||
                    (row > 7 && col > 7)) {
                System.out.println("isMoveValid err: First move should touch the board center!");
                return false;
            }
        }

        // DOES THE SECOND (or greater) MOVE TOUCH ONE OF THE EXISTING TILES
        boolean tilesPresent = false;
        if (Move.totalMoves >= 1) {
            for (int i=0; i<word.length(); i++) {
                if (dir == Move.RIGHT) {
                    if (gameBoard.getTileOnBoard(row, col + i) != null
                            || (row - 1 >= 0
                            && gameBoard.getTileOnBoard(row - 1, col + i) != null)
                            || (row + 1 <= 14
                            && gameBoard.getTileOnBoard(row + 1, col + i) != null))    {   tilesPresent = true;    }
                } else if (dir == Move.DOWN) {
                    if (gameBoard.getTileOnBoard(row + i, col) != null
                            || (col - 1 >= 0
                            && gameBoard.getTileOnBoard(row + i, col - 1) != null)
                            || (col + 1 <= 14
                            && gameBoard.getTileOnBoard(row + i, col + 1) != null))    {   tilesPresent = true;    }
                }
            }
            if (!tilesPresent)  {
                System.out.println("isMoveValid err: New word has to touch an existing word.");
                return false;
            }
        }

        int countReplace = 0;
       // CAN THAT WORD BE CONSTRUCTED USING EXISTING PIECES AND PLAYER TILES
        for (int i = 0; i < word.length(); i++) {
            if (dir == Move.RIGHT) {
                if (gameBoard.getTileOnBoard(row, col + i) != null
                        && gameBoard.getTileOnBoard(row, col + i).getValue().equals(String.valueOf(word.charAt(i)))) {
                    //don't do anything, this is expected (unless it happens for all letters)
                } else if (gameBoard.getTileOnBoard(row, col + i) == null) {
                    //empty cell - user should have it
                    boolean doesContains = false;
                    for (Tile tile : tileCopy) {
                        if (tile.getValue().equals(String.valueOf(word.charAt(i)))) {
                           doesContains = true;
                           break;
                        }
                    }
                    if (!doesContains) {
                        System.out.println("isMoveValid err: You are missing the letter '" + word.charAt(i) + "'.");
                        return false;
                    }
                    countReplace++;
                } else {
                    //neither empty cell nor expected char
                    return false;
                }
            } else if (dir == Move.DOWN) {
                if (gameBoard.getTileOnBoard(row + i, col) != null
                        && gameBoard.getTileOnBoard(row + i, col).getValue().equals(String.valueOf(word.charAt(i)))) {
                    //expected for at most all-1 cases
                } else if (gameBoard.getTileOnBoard(row + i, col) == null) {
                    //empty cell - find tile with player
                    boolean doesContains = false;
                    for (Tile tile : tileCopy) {
                        if (tile.getValue().equals(String.valueOf(word.charAt(i)))) {
                            doesContains = true;
                            break;
                        }
                    }
                    if (!doesContains) {
                        System.out.println("isMoveValid err: You are missing the letter '" + word.charAt(i) + "'.");
                        return false;
                    }
                    countReplace++;
                } else {
                    //neither empty cell nor expected char
                    return false;
                }
            }
        }

        if (countReplace == 0) {
            System.out.println("isMoveValid err: The word already exists.");
            return false;
        }

        ArrayList<String> secList = this.getSecondaryWords(move);
        if (!this.validateSecondaryWords(secList)) {
            System.out.println("isMoveValid err: The other words you tried to create are invalid.");
            return false;
        }

        //sout
        for (String str : secList) {
            System.out.println(str);
        }

        move.isValid = true;
        Move.registerMove(move);
        move.secondaryWords = secList;

        return true;
    }

    private boolean validateSecondaryWords(ArrayList<String> list) {
        for (String str : list) {
            if (!Board.validateWord(str)) {
                System.out.println(str + " not valid secondary word.");
                return false;
            }
        }

        return true;
    }

    private ArrayList<String> getSecondaryWords(Move move) {
        ArrayList<String> secWords = new ArrayList<>();
        String word = move.word.toUpperCase();
        int row = move.startRow;
        int col = move.startCol;
        int dir = move.direction;

        for (int i = 0; i < word.length(); i++) {
            if (dir == Move.RIGHT) {
                if (gameBoard.getTileOnBoard(row, col + i) == null) {
                    //here the user is inserting a tile
                    if (gameBoard.getTileOnBoard(row - 1, col + i) != null ||
                            gameBoard.getTileOnBoard(row + 1, col + i) != null) {
                        System.out.println("calling construct word for: " + word.charAt(i));
                        secWords.add(this.constructWord(row, col + i, word.charAt(i), move));
                    }
                }
            } else if (dir == Move.DOWN) {
                if (gameBoard.getTileOnBoard(row + i, col) == null) {
                    if (gameBoard.getTileOnBoard(row + i, col - 1) != null ||
                            gameBoard.getTileOnBoard(row + i, col + 1) != null) {
                        System.out.println("calling construct word for: " + word.charAt(i));
                        secWords.add(this.constructWord(row + i, col, word.charAt(i), move));
                    }
                }
            }
        }

        return secWords;
    }

    private String constructWord(int row, int col, char C, Move move) {
        StringBuilder newWord = new StringBuilder();
        int start;
        int end;
        int i = 1;
        int j = 1;
        int dir = move.direction;
        C = Character.toUpperCase(C);

        if (dir == Move.DOWN) {
            while (col - j >= 0 && gameBoard.getTileOnBoard(row, col - j) != null) {
                j++;
            }
            start = col - j + 1;
            j = 1;

            while (col + j <= 14 && gameBoard.getTileOnBoard(row, col + j) != null) {
                j++;
            }
            end = col + j - 1;

            for (int newCol = start; newCol <= end; newCol++) {
                if (newCol == col) {    newWord.append(C);  }
                else {  newWord.append(gameBoard.getTileOnBoard(row, newCol).getValue());  }
            }
            return newWord.toString().trim();

        } else if (dir == Move.RIGHT) {
            while (row - i >= 0 && gameBoard.getTileOnBoard(row - i, col) != null) {
                i++;
            }
            start = row - i + 1;
            i = 1;

            while (row + i <= 14 && gameBoard.getTileOnBoard(row + i, col) != null) {
                i++;
            }
            end = row + i - 1;

            for (int newRow = start; newRow <= end; newRow++) {
                if (newRow == row) {    newWord.append(C);  }
                else {  newWord.append(gameBoard.getTileOnBoard(newRow, col).getValue());  }
            }
            return newWord.toString().trim();
        }

        return null;
    }
}
