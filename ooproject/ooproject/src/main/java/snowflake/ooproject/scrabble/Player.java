package snowflake.ooproject.scrabble;

public class Player {
    private String name;
    private int id;
    Tile[] letters;

    public Player() {
        this.letters = new Tile[7];
    }

    public Player(String name, int id, Tile[] letters) {
        this.name = name;
        this.id = id;
        this.letters = letters;
    }

    public void drawTray() {
        for (Tile letter : letters) {
            if (letter == null) {
                System.out.println("null");
            }
            else {
                System.out.println(letter.getValue());
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Tile[] getLetters() {
        return letters;
    }

    public void setLetters(Tile[] letters) {
        this.letters = letters;
    }
}
