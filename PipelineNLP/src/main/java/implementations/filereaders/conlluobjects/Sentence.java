package implementations.filereaders.conlluobjects;

import com.scalified.tree.TreeNode;
import com.scalified.tree.multinode.ArrayMultiTreeNode;
import com.scalified.tree.multinode.LinkedMultiTreeNode;

import java.util.*;

public class Sentence implements Iterable<Word> {
    private ArrayList<Word> wordList;
    private String sentence;
    private Integer sentenceLength = 0;
    private HashMap<Integer, Word> idToWordMapping;
    private LinkedMultiTreeNode<Word> parseTree;

    public Sentence(List<Word> words, String sentence) {
        this.sentence = sentence;
        wordList = sanitize(words);

        idToWordMapping = new HashMap<>();

        for (Word w : wordList) {
            if (w.id != null) {
                sentenceLength += 1;
                idToWordMapping.put(w.id, w);
            }
        }

        parseTree = generateTree();

        if (sentenceLength != parseTree.size()) {
            throw new IllegalArgumentException("Incomplete parsing tree, some nodes are missing.");
        }
    }

    /**
     * Sanitize takes care of mutlitoken fields (ids described as a range or float). It fills in word.id and word.multiTokenHead.
     * In the case of a range id, word.id becomes word.idRange.getMinimum(). All words included in that range have
     * their word.id = null and their word.multiTokenHead set.
     * In the case of a float id, word.id = null and the multiTokenHead takes the decimal value
     * @param input the list of words of the sentence
     * @return the list of words with id and multiTokenHead filled in properly
     */
    private ArrayList<Word> sanitize(List<Word> input) {
        ArrayList<Word> words = new ArrayList<>(input);
        int i = 0;
        while (i < words.size()) {
            if (words.get(i).idInt != null) {
                words.get(i).id = words.get(i).idInt;
                i += 1;
            }

            else if (words.get(i).idFloat != null) {
                words.get(i).multiTokenHead = words.get(i).idFloat.getLeft();
                i += 1;
            }

            else if (words.get(i).idRange != null) {
                words.get(i).id = words.get(i).idRange.getMinimum();
                Word multiToken = words.get(i);
                i += 1;

                while (i < words.size() && words.get(i).idInt != null && multiToken.idRange.contains(words.get(i).idInt)) {
                    words.get(i).multiTokenHead = multiToken.id;
                    i += 1;
                }

                if (!multiToken.idRange.getMaximum().equals(words.get(i-1).idInt))
                    throw new IllegalArgumentException("MultiToken word defined without full list of tokens following");
            }
        }

        return words;
    }

    private LinkedMultiTreeNode<Word> generateTree() {
        HashMap<Integer, LinkedMultiTreeNode<Word>> integerToNode = new HashMap<>((int)(((float)length()/0.75f))+1);
        for (Word w : this) {
            integerToNode.put(w.id, new LinkedMultiTreeNode<Word>(w));
        }

        LinkedMultiTreeNode<Word> root = null;

        // we need to traverse the list in order, otherwise words might appear in the wrong order in the final tree
        for (Word w : wordList) {
            if (w.id == null || w.head == null)
                continue;

            if (w.head.equals(0)) {
                root = integerToNode.get(w.id);
                continue;
            }

            integerToNode.get(w.head).add(integerToNode.get(w.id));
        }

        if (root == null)
            throw new IllegalArgumentException("Word with head = 0 is missing");

        return root;
    }

    private String generateSentence() {
        return null;
    }

    public TreeNode<Word> getTree(){
        return parseTree;
    }

    public int length() {
        return sentenceLength;
    }

    public Word getWord(int pos) {
        return idToWordMapping.get(pos);
    }

    public List<Word> getMultiTokenWordList() {
        return wordList;
    }

    @Override
    public Iterator<Word> iterator() {
        return idToWordMapping.values().iterator();
    }
}
