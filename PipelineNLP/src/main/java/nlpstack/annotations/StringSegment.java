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

    public boolean isAnnotated() {
        return sentence == null;
    }

    public String getString() {
        return sentence;
    }

    public Pair<String, String> getAnnotation(){
        return word;
    }
}
