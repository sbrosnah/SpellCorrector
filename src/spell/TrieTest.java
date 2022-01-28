package spell;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

public class TrieTest {
    Trie myTrie = null;
    Trie otherTrie = null;
    String FileName = null;
    private TrieTest(){
        myTrie = new Trie();
        otherTrie = new Trie();
    }
    public static void main(String[] args) throws IOException {
        String FileName = args[0];
        TrieTest test = new TrieTest();
        test.loadWords(FileName);
        test.testEquals();

    }

    private void loadWords(String FileName) throws IOException {
        this.FileName = FileName;
        File file = new File(FileName);
        Scanner scanner = new Scanner(file);
        while(scanner.hasNext()){
            String str = scanner.next();
            str = str.toLowerCase();
            add(str);
        }
    }

    private void add(String str){
        myTrie.add(str);
        otherTrie.add(str);
    }

    private void testEquals() {
        myTrie.add("car");
        otherTrie.add("car");
        otherTrie.add("car");
        System.out.println(otherTrie.equals(myTrie));
        System.out.println(myTrie.equals(otherTrie));
    }

}
