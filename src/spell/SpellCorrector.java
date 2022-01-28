package spell;

import java.io.IOException;
import java.io.File;
import java.util.*;

public class SpellCorrector implements ISpellCorrector{
    //private HashMap<String, Integer> dictionary = null;
    private Trie dictionary = null;
    private HashSet<String> similarWords = null;
    private HashSet<String> editWords = null;
    private int currIteration = 0;
    public SpellCorrector(){
        //dictionary = new HashMap<>();
        dictionary = new Trie();
        similarWords = new HashSet<>();
        editWords = new HashSet<>();
    }

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        File file = new File(dictionaryFileName);
        Scanner scanner = new Scanner(file);
        int value = 0;

        while(scanner.hasNext()){
            String str = scanner.next();
            str = str.toLowerCase();
            dictionary.add(str);
        }
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        String closestWord = null;
        inputWord = inputWord.toLowerCase();
        similarWords.clear();
        editWords.clear();
        currIteration = 0;
        //if(dictionary.containsKey(inputWord)){
        INode finalNode = dictionary.find(inputWord);
        if(finalNode != null){
            if(finalNode.getValue() == 0){
                return null;
            }
            return inputWord;
        } else {
            findEditDistanceOne(inputWord);
        }
        if(similarWords.isEmpty()){
            currIteration++;
            findEditDistanceTwo();
        }
        if(!similarWords.isEmpty()){
            closestWord = findClosestWord();
        }
        return closestWord;
    }

    private HashSet<String> findMaxWords(){
        int maxVal = 0;
        HashSet<String> maxWords = new HashSet<>();
        for(String word : similarWords){
            //I already know that the word exists because it is in similarWords
            //if(dictionary.get(word) > maxVal){
            INode node = dictionary.find(word);
            int value = node.getValue();
            if(value > maxVal) {
                //maxVal = dictionary.get(word);
                maxVal = value;
                maxWords.clear();
                maxWords.add(word);
                //} else if (dictionary.get(word) == maxVal) {
            }else if (value == maxVal){
                maxWords.add(word);
            }
        }
       return maxWords;
    }

    private String findFirstAlphabetical(HashSet<String> maxWords){
        List<String> list = new ArrayList<String>(maxWords);
        Collections.sort(list);
        String wordToReturn = list.get(0);
        return wordToReturn;
    }

    private String findClosestWord(){
        HashSet<String> maxWords = findMaxWords();
        String wordToReturn = null;
        if(maxWords.size() == 1){
            Iterator<String> it = maxWords.iterator();
            wordToReturn = it.next();
        } else {
            wordToReturn = findFirstAlphabetical(maxWords);
        }
        return wordToReturn;
    }

    private void findEditDistanceTwo(){
        for(String editWord : editWords){
            findEditDistanceOne(editWord);
        }
    }

    private void findEditDistanceOne(String word){
        //Deletion
        findDeletionWords(word);
        //Transposition
        findTranspositionWords(word);
        //Alteration
        findAlterationWords(word);
        //Insertion
        findInsertionWords(word);
    }

    private void findAlterationWords(String word){
        for(int i = 0; i < word.length(); i++){
            for(int j = 'a'; j < 'a' + 26; j++){
                char ch = (char)j;
                String newWord = word.substring(0, i) + ch + word.substring(i + 1);
                if(currIteration == 0){
                    editWords.add(newWord);
                }
                if(checkIfInDict(newWord) && !word.equals(newWord)){
                    similarWords.add(newWord);
                }
            }
        }
    }

    private void findInsertionWords(String word){
        for(int i = 'a'; i < 'a' + 26; i++){
            for(int j = 0; j <= word.length(); j++){
                char ch = (char)i;
                String newWord = null;
                if(j != word.length()){
                    newWord = word.substring(0, j) + ch + word.substring(j);
                } else {
                    newWord = word + ch;
                }
                if(currIteration == 0){
                    editWords.add(newWord);
                }
                if(checkIfInDict(newWord) && !word.equals(newWord)){
                    similarWords.add(newWord);
                }
            }
        }
    }

    private void findTranspositionWords(String word){
        if(word.length() > 1){
            for(int i = 0; i < word.length() - 1; i++){
                String newWord = null;
                char firstTrans = word.charAt(i);
                char secondTrans = word.charAt(i + 1);
                String firstSub = word.substring(0, i);
                String secondSub = "";
                if((i + 2) < word.length()){
                    secondSub = word.substring(i + 2);
                }
                newWord = firstSub + secondTrans + firstTrans + secondSub;
                if(currIteration == 0){
                    editWords.add(newWord);
                }
                if(checkIfInDict(newWord) && !word.equals(newWord)){
                    similarWords.add(newWord);
                }
            }
        }
    }

    private void findDeletionWords(String word){
        if(word.length() > 1){
            for(int i = 0; i < word.length(); i++){
                String subOne = word.substring(0, i);
                String subTwo = word.substring(i + 1);
                String newWord = subOne + subTwo;
                if(currIteration == 0){
                    editWords.add(newWord);
                }
                if(checkIfInDict(newWord)){
                    similarWords.add(newWord);
                }
            }
        }
    }

    private boolean checkIfInDict(String word){
        boolean exists = false;
        if(dictionary.find(word) != null){
            exists = true;
        }
        return exists;
    }
}
