package nlpstack.annotations;

import org.apache.commons.lang3.tuple.Pair;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringSegment {
    private String sentence;
    private Pair<String, String> word;

    private StringSegment(String sentence, Pair<String, String> word) {
        this.sentence = sentence;
        this.word = word;
    }

    public static StringSegment fromString(String sentence) {
        return new StringSegment(sentence, null);
    }

    public static StringSegment fromAnnotatedWord(Pair<String, String> word) {
        return new StringSegment(null, word);
    }

    /**
     * Indicates of this segment has an annotation: a pair (string, tag)
     */
    public boolean isAnnotated() {
        return sentence == null;
    }

    /**
     * If is annotated is true, this is just null
     */
    public String getNoneAnnotatedString() {
        return sentence;
    }

    /**
     * If isAnnotated is false, this will return null
     */
    public Pair<String, String> getAnnotation() {
        return word;
    }

    /**
     * If isAnnotated is false, a null pointer exception will be raised
     */
    public String getAnnotatedString() {
        return word.getLeft();
    }

    /**
     * If isAnnotated is false, a null pointer exception will be raised
     */
    public String getAnnotatedTag() {
        return word.getRight();
    }
}
