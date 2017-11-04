package implementations.filereaders.conlluobjects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Sentence implements Iterable<Word> {
    private ArrayList<Word> wordList;
    private String sentence;

    public Sentence(List<Word> words, String sentence) {
        this.sentence = sentence;
        wordList = new ArrayList<>(words);
    }

    public int length() {
        return wordList.size();
    }

    public Word getWord(int pos) {
        return wordList.get(pos);
    }

    @Override
    public Iterator<Word> iterator() {
        return wordList.iterator();
    }
}
