package implementations.filereaders.conlluobjects;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ConlluIterator implements Iterator<Sentence> {
    private BufferedReader file;
    private String filePath;
    private Integer lineNumber;

    private Pattern ignoreLine = Pattern.compile("(#.*)|(\\s*)");
    private Pattern beginningOfSentence = Pattern.compile("1.*");
    private Pattern optionalSentenceString = Pattern.compile("# text = (.*)");
    private Pattern endOfSentence = Pattern.compile("\\s*");

    private Sentence nextSentence;

    public ConlluIterator(String path) throws FileNotFoundException {
        file = new BufferedReader(new FileReader(path));
        filePath = path;
        lineNumber = 0;
        nextSentence = null;
    }

    private Sentence getNextSentence() throws IOException {
        String line = file.readLine();
        lineNumber += 1;
        String sentenceString = null;
        List<Word> wordList = new LinkedList<>();

        if (line == null) {
            return null;
        }

        while (!beginningOfSentence.matcher(line).matches()) {

            if (!ignoreLine.matcher(line).matches())
                throw new IllegalArgumentException(
                        "Unparsable line in " + filePath + " on line " + lineNumber.toString() + ": " + line);

            Matcher optionalSentenceString = this.optionalSentenceString.matcher(line);
            if (optionalSentenceString.matches())
                sentenceString = optionalSentenceString.group(1);

            line = file.readLine();
            lineNumber += 1;

            if (line == null)
                return null;
        }

        while (line != null && !endOfSentence.matcher(line).matches()) {
            try {
                wordList.add(Word.parse(line));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                        "Unparsable line in " + filePath + " on line " + lineNumber.toString() + ": " + line);
            }
            line = file.readLine();
            lineNumber += 1;
        }

        try {
            return new Sentence(wordList, sentenceString);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Unparsable sentence in " + filePath + " on line " + lineNumber.toString() + ": " + e.getMessage());
        }
    }

    @Override
    public boolean hasNext() {
        if (nextSentence == null)
            try {
                nextSentence = getNextSentence();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }

        return nextSentence != null;
    }

    @Override
    public Sentence next() {
        if (nextSentence != null) {
            Sentence out = nextSentence;
            nextSentence = null;
            return out;
        }

        try {
            return getNextSentence();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }
}
