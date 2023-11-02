package snowflake.ooproject.commandline;

import java.util.*;
public class Node implements Comparable<Node> {
    private String meaning;
    private String pronunciation;
    private String  word;
    public Node() {
        meaning = null;
        pronunciation = null;
        word = null;
    }

    public Node(String word) {
        this.word = word;
    }
    public Node(String word,String pronunciation, String meaning) {
        this.meaning = meaning;
        this.pronunciation = pronunciation;
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public String getWord() {
        return word;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public int compareTo(Node o) {
        return this.word.compareTo(o.getWord());
    }
    public static void main(String[] args) {
        System.out.println("dm mahyh");
    }
}
