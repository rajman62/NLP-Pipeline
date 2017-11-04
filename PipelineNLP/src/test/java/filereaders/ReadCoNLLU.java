package filereaders;

import implementations.filereaders.ConlluReader;
import implementations.filereaders.conlluobjects.Sentence;
import implementations.filereaders.conlluobjects.Word;
import org.apache.commons.lang3.Range;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;

public class ReadCoNLLU {
    static final String TEST_PATH = "src/test/java/filereaders/";

    @Test
    public void readConllu() throws FileNotFoundException {
        Iterator<Sentence> sentences = new ConlluReader(TEST_PATH + "conllu_example.conllu");
        sentences.hasNext();
        Sentence sent1 = sentences.next();
        assertEquals("Al", sent1.getWord(0).form);
        assertEquals("PROPN", sent1.getWord(0).upostag);
        assertEquals("forces", sent1.getWord(5).form);
        assertEquals("force", sent1.getWord(5).lemma);
        assertEquals("NOUN", sent1.getWord(5).upostag);
        assertEquals("NNS", sent1.getWord(5).xpostag);

        assertEquals(29, sent1.length());

        Sentence sent2 = sentences.next();
        assertEquals(18, sent2.length());
        assertEquals("will", sent2.getWord(7).form);
    }

    @Test
    public void parseLine() {
        Word testWord = Word.parse("28\tborder\tborder\tNOUN\tNN\tNumber=Sing\t21\tnmod\t_\tSpaceAfter=No");
        assertEquals("border", testWord.form);
        assertEquals((Integer)21, testWord.head);

        testWord = Word.parse("1-3\tforces\tforce\tNOUN\tNNS\tNumber=Plur\t7\tnsubj\t_\t_");
        assertEquals(Range.between(1, 3), testWord.id);
        assertEquals("forces", testWord.form);
        assertEquals("force", testWord.lemma);
        assertEquals("NOUN", testWord.upostag);
        assertEquals("NNS", testWord.xpostag);
        assertEquals("Number=Plur", testWord.feats);
        assertEquals((Integer)7, testWord.head);
        assertEquals("nsubj", testWord.deprel);
        assertEquals("_", testWord.deps);
        assertEquals("_", testWord.misc);
    }
}
