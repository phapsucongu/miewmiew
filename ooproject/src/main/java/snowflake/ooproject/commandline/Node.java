package snowflake.ooproject.commandline;

import java.util.*;
public class Node {
    HashMap<Character, Node> children;
    ArrayList<String> meaning = new ArrayList<String>();
    String pronunciation;
    String typeWord;
    String  word;
    ArrayList<String> examples = new ArrayList<String>();
    public Node() {
        children = new HashMap<>();
        meaning = null;
        pronunciation = null;
        typeWord = null;
        examples = null;
        word = null;
    }

    public static void main(String[] args) {
        System.out.println("dm mahyh");
    }
}
