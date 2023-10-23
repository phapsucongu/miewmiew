package snowflake.ooproject.commandline;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.*;

public class DictionaryManagement {
    private static final Trie trie = new Trie();
    public static Scanner sc = new Scanner(System.in);

    public static void readData() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("src\\main\\resources\\snowflake\\ooproject\\data.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts[0].charAt(0) >= 'a' && parts[0].charAt(0) <= 'z') {
                    char kyTuDau = parts[0].charAt(0);
                    String chuoiConLai = parts[0].substring(1);
                    parts[0] = Character.toUpperCase(kyTuDau) + chuoiConLai;
                }
                ArrayList<String> meaning = new ArrayList<>();
                meaning.add(parts[1]);
                trie.insert(parts[0], meaning,"nah","nah",meaning);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showAllData() {
        ArrayList<Node> res = new ArrayList<Node>();
        trie.recommendWord(trie.root, "", res);
        int count = 1;
        System.out.printf("%-5s", "No");
        System.out.printf("%-15s", "Word");
        System.out.printf("%-45s", "Meaning");
        System.out.println();
        for (Node word : res) {
            System.out.printf("%-5d", count++);
            System.out.printf("%-15s", word.word);
            System.out.printf("%-45s", trie.getMeaning(word.word).meaning.get(0));
            System.out.println();
        }
    }

    public static void main(String[] args) {
        readData();
        showAllData();
        while (true) {
            String word = sc.nextLine();
            if (word.charAt(0) >= 'a' && word.charAt(0) <= 'z') {
                char kyTuDau = word.charAt(0);
                String chuoiConLai = word.substring(1);
                word = Character.toUpperCase(kyTuDau) + chuoiConLai;
                //System.out.println(word);
            }
            if(trie.getMeaning(word) == null) {
                System.out.println("/not found/");
                continue;
            } else {
                System.out.println(trie.getMeaning(word).meaning.get(0));
            }
            List<Node> res = trie.searchPrefix(word);
            int MAX_RECOMMEND = 5;
            int count = 0;
            for (Node result : res) {
                System.out.println(result.word);
                count++;
                if (count >= MAX_RECOMMEND)
                    break;
            }
        }
    }
}