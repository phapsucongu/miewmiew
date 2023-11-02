package snowflake.ooproject.scrabble;

import java.util.*;

public class Tile implements Comparable<Tile> {
    private String value;

    public Tile() {

    }

    @Override
    public int compareTo(Tile o) {
        return value.compareTo(o.getValue());
    }

    public Tile(String value) {
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
