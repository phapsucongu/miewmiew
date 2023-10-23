package snowflake.ooproject.commandline;

import java.util.*;
public class Trie {

    public  final Node root;
    public Trie() {
        root = new Node();
    }

    // thêm từ
    public void insert(String word, ArrayList<String> meaning, String pronunciation, String type, ArrayList<String> examples) {
        Node cur_Node = root;
        for (char cur_char : word.toCharArray()) {
            if (!cur_Node.children.containsKey(cur_char)) {
                cur_Node.children.put(cur_char, new Node());
            }
            cur_Node = cur_Node.children.get(cur_char);
        }
        cur_Node.meaning = meaning;
        cur_Node.pronunciation = pronunciation;
        cur_Node.typeWord = type;
        cur_Node.examples = examples;
        cur_Node.word = word;
    }

    // lấy nghĩa của từ ( ko có thì in not found )
    public Node getMeaning(String word) {
        Node cur_Node = root;
        for (char cur_char : word.toCharArray()) {
            if (!cur_Node.children.containsKey(cur_char)) {
                return null;
            }
            cur_Node = cur_Node.children.get(cur_char);
        }
        if (cur_Node.meaning.isEmpty()) {
            return null;
        }
        return cur_Node;
    }
    public List<Node> searchPrefix(String prefix) {
        List<Node> res = new ArrayList<>();
        Node cur_Node = root;
        for (char cur_char : prefix.toCharArray()) {
            if (!cur_Node.children.containsKey(cur_char)) {
                return res;
            }
            cur_Node = cur_Node.children.get(cur_char);
        }
        recommendWord(cur_Node, prefix, res);
        return res;
    }

    // này là hàm đệ quy của cái trên bruh
    public void recommendWord(Node node, String prefix, List<Node> res) {
        if (node.meaning != null)
            res.add(node);
        for (char next_char : node.children.keySet()) {
            Node next_node = node.children.get(next_char);
            recommendWord(next_node, prefix + next_char, res);
        }
    }
    public static void main(String[] args) {
        Trie trie = new Trie();
        String word = "hello";
        ArrayList<String> meaning = new ArrayList<>();
        meaning.add("xin chao");
        String pronunciation = "hê lô";
        String type = "noun";
        ArrayList<String> examples = new ArrayList<>();
        examples.add("hello");
        trie.insert(word, meaning,pronunciation, type, examples);
        System.out.println(trie.getMeaning(word).meaning.get(0)+ " " + trie.getMeaning(word).pronunciation);

    }
}