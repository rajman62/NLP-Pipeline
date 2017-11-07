package implementations.filereaders.conlluobjects;

import com.scalified.tree.TreeNode;

import java.util.*;

public class Sentence implements Iterable<Word> {
    private ArrayList<Word> wordList;
    private String sentence;
    private Integer sentenceLength = 0;
    private HashMap<Integer, Word> idToWordMapping;

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
