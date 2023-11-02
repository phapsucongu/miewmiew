package snowflake.ooproject.commandline;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static snowflake.ooproject.commandline.DictionaryApp.dict;
import static snowflake.ooproject.commandline.DictionaryApp.recentWord;

public class DictionaryManagement {
    public static Scanner sc = new Scanner(System.in);
    public static final Node NOT_FOUND = new Node("","","/Not found/");

    public static String readFile(String path, Charset encoding) throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
    public static void readData() {
        try {
            String content = readFile("src\\main\\resources\\snowflake\\ooproject\\anhviet109K.txt", Charset.defaultCharset());
            String[] words = content.split("@");
            for (String word: words) {
                String[] result = word.split("\r?\n", 2);
                if (result.length >1) {
                    String wordTarget1;
                    String wordSound1 ;
                    if(result[0].contains("/")) {
                        String firstmeaning = result[0].substring(0, result[0].indexOf("/"));
                        String lastSoundMeaning = result[0].substring(result[0].indexOf("/"));
                        wordTarget1 = firstmeaning;
                        wordSound1 = lastSoundMeaning;
                    }
                    else
                    {
                        wordTarget1 = result[0];
                        wordSound1 = "";
                    }
                    String wordExplain1 = result[1];
                    dict.add( new Node(wordTarget1.trim(), wordSound1.trim(), wordExplain1.trim()));
                    //System.out.println(wordSound1.trim());
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    public static String formatWord(String word)
    {
        String ans = word.replaceAll("\\s+", " ").trim();
        ans = ans.toLowerCase();
        return ans;
    }

    public static void showAllData() {
        int count = 0;
        System.out.printf("%-5s", "No");
        System.out.printf("%-15s", "Word");
        System.out.printf("%-45s", "Meaning");
        System.out.println();
        for (Node word : dict) {
            if(word.getWord().charAt(0) >= 'a' && word.getWord().charAt(0) <= 'z') {
                count++;
                String[] meaning = word.getMeaning().split("\r?\n", 3);
                String choosenMeaning = meaning[0];
                if (meaning[0].charAt(0) == '*')
                    choosenMeaning = meaning[1];
                //System.out.printf("%-10d| %-20s| %s\n", count, word.getWord(), choosenMeaning);
                System.out.println(count + " " + word.getWord() + " " + choosenMeaning);
                if (count == 10) break;
            }
        }
    }

    public static void showRecentWord() {
        int count = 0;
        for (String word : recentWord) {
            count++;
            System.out.println(count + " " + word );
            if (count == 10) break;
        }
    }
    public static Node search(String word)
    {
        word = formatWord(word);
        Node w1 = new Node(word);
        Node w2 = new Node(word + "a");
        TreeSet<Node> choose = (TreeSet<Node>) dict.subSet(w1,w2);
        Iterator<Node> it = choose.iterator();
        if (it.hasNext())
        {
            Node s = it.next();
            if(s.getWord().equals(word))
                return s ;
        }
        return NOT_FOUND;
    }

    public static List<String> searchPrefix(String word)
    {
        word = formatWord(word);
        Node w1 = new Node(word + "a");
        Node w2 = new Node(word + "zzzzzzzzz");
        TreeSet<Node> choose = (TreeSet<Node>) dict.subSet(w1,w2);
        ArrayList<String> ans = new ArrayList<>();
        for (Node w : choose) {
            ans.add(w.getWord());
        }
        return ans;
    }

    public static String addWord(String word, String pronunciation, String meaning) {
        word = formatWord(word);
        pronunciation = formatWord(pronunciation);
        meaning = formatWord(meaning);
        if(word.isEmpty())
            return "Please enter a word";
        if (dict.contains(new Node(word)))
            return "This word is already existed";
        dict.add(new Node(word, pronunciation, meaning));
        return "Add successfully !";
    }

    public static String deleteWord(String word) {
        word = formatWord(word);
        if(word.isEmpty())
            return "Please enter a word";
        if(!dict.contains(new Node(word)))
            return "This word is not already existed";
        dict.remove(search(word));
        recentWord.remove(search(word).getWord());
        return "Remove successfully !";
    }

    public static String editWord(String word, String meaning) {
        word = formatWord(word);
        meaning = formatWord(meaning);
        if(word.isEmpty())
            return "Please enter a word";
        if(!dict.contains(new Node(word)))
            return "This word is not already existed";
        Node cur = search(word);
        dict.remove(search(word));
        dict.add(new Node(word, cur.getPronunciation(), meaning));
        return "Edit successfully !";
    }

    private static int editDistance(String a, String b)
    {
        int n = a.length();
        int m = b.length();
        int[][] f = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) f[i][0] = i;
        for (int i = 0; i <= m; i++) f[0][i] = i;
        for (int i = 1; i <= n; i++)
            for (int j = 1; j <= m; j++) {
                f[i][j] = Math.min(f[i - 1][j], f[i][j - 1]) + 1;
                if (a.charAt(i - 1) == b.charAt(j - 1)) f[i][j] = Math.min(f[i][j], f[i - 1][j - 1]);
                else f[i][j] = Math.min(f[i][j], f[i - 1][j - 1] + 1);
            }
        return f[n][m];
    }
    public static String autoCorrect(String word)
    {
        word = formatWord(word);
        if (!searchPrefix(word).isEmpty()) {
            return "Did you mean " + searchPrefix(word).get(0) + " ?";
        }
        if(word.isEmpty())
            return "Please enter a word";
        if(dict.contains(new Node(word)))
            return "This word is already existed";
        for (Node w : dict)
        {
            if( w.getWord().length() - word.length() >= 1 && w.getWord().length() - word.length() <= -1)
                continue;
            int d = editDistance(word, w.getWord());
            if (d==1)
                return "Did you mean " + w.getWord() + " ?";
        }
        return "Did you mean " + word + " ?";
    }
    public static void run() {
        readData();
        showAllData();
        while (true) {
            String word = sc.nextLine();
            recentWord.add(formatWord(word));
            System.out.println(search(word).getPronunciation());
            System.out.println(search(word).getMeaning());
            int count = 0;
            for (String s : searchPrefix(word)) {
                System.out.println(s);
                count++;
                if (count == 10) break;
            }
            if (search(word) == NOT_FOUND)
                System.out.println(autoCorrect(word));
            showRecentWord();
        }
    }
    public static void main(String[] args) {
        run();
    }
}