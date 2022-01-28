package spell;

import java.util.ArrayList;
import java.util.Locale;

public class Trie implements ITrie{
    Node root = null;
    int nodeCount = 0;
    int wordCount = 0;

    public Trie() {
        root = new Node();
        nodeCount = 1;
        wordCount = 0;
    }

    @Override
    public void add(String word) {
        word = word.toLowerCase();
        int startPos = 'a';
        INode currNode = root;
        char currChar = 'a';
        int charPos = 0;
        if(word == ""){
            root.count++;
        }
        for (int i = 0; i < word.length(); i++) {
            currChar = word.charAt(i);
            charPos = (int) currChar - startPos;
            INode[] children = currNode.getChildren();
            if(children[charPos] == null){
                nodeCount += 1;
                children[charPos] = new Node();
            }
            if (i == word.length() - 1) {
                if(children[charPos].getValue() == 0){
                    wordCount++;
                }
                children[charPos].incrementValue();
            } else {
                currNode = children[charPos];
            }
        }
    }

    @Override
    public INode find(String word) {
        word = word.toLowerCase();
        int startPos = 'a';
        INode currNode = root;
        if(word == ""){
            return root;
        }
        for (int i = 0; i < word.length(); i++) {
            char currChar = word.charAt(i);
            int charPos = (int) currChar - startPos;
            INode[] children = currNode.getChildren();
            if(children[charPos] == null){
                return null;
            }
            if (i == word.length() - 1 && children[charPos].getValue() > 0) {
                return children[charPos];
            }
            currNode = children[charPos];
        }
        return null;
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    @Override
    public String toString() {
        StringBuilder currWord = new StringBuilder();
        StringBuilder outPut = new StringBuilder();

        toString_Helper(root, currWord, outPut);

        return outPut.toString();
    }

    private void toString_Helper(Node n, StringBuilder currWord, StringBuilder output) {

        if (n.count > 0) {
            output.append(currWord.toString() + "\n");
        }

        for (int i = 0; i < n.children.length; i++){

            Node child = n.children[i];

            if (child != null) {

                char childChar = (char)('a' + i);

                currWord.append(childChar);

                toString_Helper(child, currWord, output);

                currWord.deleteCharAt(currWord.length() - 1);
            }
        }

    }

    @Override
    public int hashCode() {
        //Calculate the index of the first non-null child of root node and use it in the multiplication.
        INode[] rootChildren = root.getChildren();
        int index = 0;
        for (int i = 0; i < rootChildren.length; i++) {
            if(rootChildren[i] != null){
                index = i;
            }
        }
        return (nodeCount * wordCount * index);
    }

    @Override public boolean equals(Object obj) {
        if(this == null && obj == null){
            return true;
        }else if(obj == null){
            return false;
        } else if (obj == this){
            return true;
        } else if (obj.getClass() != Trie.class){
            return false;
        }

        Trie other = (Trie)obj;

        //is this.wordCount == other.wordCount?
        //is this.nodeCount == other.nodeCount?
        if(this.wordCount != other.wordCount){
            return false;
        } else if (this.nodeCount != other.nodeCount){
            return false;
        } else if(this.root == other.root){
            //Compare the root nodes to see if they're identical
            return true;
        }
        return equals_Helper(this.root, other.root);
    }

    private boolean equals_Helper(INode thisNode, INode otherNode) {
        //TODO: TRAVERSE BOTH TREES AT ONCE UNTIL WE FIND THAT ONE IS DIFFERENT.
        //Calling toString would be too expensive computationally.
        Integer[] locations = new Integer[26];
        //does thisNode.count == otherNode.count
        if(thisNode.getValue() != otherNode.getValue()){
            return false;
        }
        //Check if the children arrays have children in the same locations (letters)?
        INode[] thisChildren = thisNode.getChildren();
        INode[] otherChildren = otherNode.getChildren();
        int currIndex = 0;
        for (int i = 0; i < thisChildren.length; ++i) {
            if(thisChildren[i] != null && otherChildren[i] != null){
                locations[currIndex] = i;
                currIndex++;
            } else if (thisChildren[i] == null && otherChildren[i] != null) {
                return false;
            } else if (thisChildren[i] != null && otherChildren[i] == null) {
                return false;
            }
        }
        //If all of that is true, then you recurse
        boolean equals = true;
        for (int i = 0; i < locations.length; i++) {
            if(locations[i] == null){
                break;
            }
            INode nextThisNode = thisChildren[locations[i]];
            INode nextOtherNode = otherChildren[locations[i]];
            equals = equals_Helper(nextThisNode, nextOtherNode);
            if (equals == false) {
                break;
            }
        }
        return equals;
    }


}
