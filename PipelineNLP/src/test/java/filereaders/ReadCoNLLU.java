package filereaders;

import implementations.filereaders.ConlluReader;
import implementations.filereaders.conlluobjects.ConlluIterator;
import implementations.filereaders.conlluobjects.Sentence;
import implementations.filereaders.conlluobjects.Word;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;

public class ReadCoNLLU {
    static final String TEST_PATH = "src/test/java/filereaders/";

    @Test
    public void readConllu() throws FileNotFoundException {
        Iterator<Sentence> sentences = new ConlluIterator(TEST_PATH + "conllu_example.conllu");
        sentences.hasNext();
        Sentence sent1 = sentences.next();
        assertEquals("Al", sent1.getWord(1).form);
        assertEquals("PROPN", sent1.getWord(1).upostag);
        assertEquals("forces", sent1.getWord(6).form);
        assertEquals("force", sent1.getWord(6).lemma);
        assertEquals("NOUN", sent1.getWord(6).upostag);
        assertEquals("NNS", sent1.getWord(6).xpostag);

        assertEquals(29, sent1.length());

        Sentence sent2 = sentences.next();
        assertEquals(18, sent2.length());
        assertEquals("will", sent2.getWord(8).form);
    }

    @Test
    public void parseLine() {
        Word testWord = Word.parse("28\tborder\tborder\tNOUN\tNN\tNumber=Sing\t21\tnmod\t_\tSpaceAfter=No");
        assertEquals("border", testWord.form);
        assertEquals((Integer)21, testWord.head);
        assertEquals((Integer)28, testWord.idInt);
        assertEquals(null, testWord.idRange);
        assertEquals(null, testWord.idFloat);

        // Case with id range
        testWord = Word.parse("1-3\tforces\tforce\tNOUN\tNNS\tNumber=Plur\t7\tnsubj\t_\t_");
        assertEquals(Range.between(1, 3), testWord.idRange);
        assertEquals(null, testWord.idInt);
        assertEquals(null, testWord.idFloat);
        assertEquals("forces", testWord.form);
        assertEquals("force", testWord.lemma);
        assertEquals("NOUN", testWord.upostag);
        assertEquals("NNS", testWord.xpostag);
        assertEquals("Number=Plur", testWord.feats);
        assertEquals((Integer)7, testWord.head);
        assertEquals("nsubj", testWord.deprel);
        assertEquals("_", testWord.deps);
        assertEquals("_", testWord.misc);

        // case with decimal id
        testWord = Word.parse("8.1\treported\treport\tVERB\tVBN\tTense=Past|VerbForm=Part|Voice=Pass\t_\t_\t5:conj\t_");
        assertEquals(null, testWord.idRange);
        assertEquals(null, testWord.idInt);
        assertEquals(Pair.of(8, 1), testWord.idFloat);
        assertEquals(null, testWord.head);
    }

    @Test
    public void getStats() {
        ConlluReader reader = new ConlluReader(TEST_PATH + "conllu_example.conllu");
        reader.calculateStats();

        assertEquals(2, reader.getNbSentences());
        assertEquals(40, reader.getNbForms());
        assertEquals(40, reader.getNbLemmas());
        assertEquals(10, reader.getUPostTags().size());
        assertEquals(18, reader.getXPostTags().size());
    }

    @Test
    public void multiTokenTest() {
        ConlluReader reader = new ConlluReader(TEST_PATH + "conllu_multitoken.conllu");
        reader.calculateStats();

        assertEquals(2, reader.getNbSentences());
        assertEquals(17, reader.getNbForms());

        Iterator<Sentence> sentences = reader.iterator();
        Sentence sent1 = sentences.next();
        Sentence sent2 = sentences.next();

        assertEquals(13, sent1.length());
        assertEquals(5, sent2.length());

        assertEquals((Integer) 8, sent1.getMultiTokenWordList().get(8).multiTokenHead);
        assertEquals((Integer) 8, sent1.getMultiTokenWordList().get(7).id);
        assertEquals((Integer) 8, sent1.getWord(8).id);
        assertEquals(null, sent1.getMultiTokenWordList().get(8).id);
        assertEquals((Integer) 2, sent2.getMultiTokenWordList().get(1).id);
        assertEquals((Integer) 2, sent2.getWord(2).id);
        assertEquals(null, sent2.getMultiTokenWordList().get(2).id);
        assertEquals(null, sent2.getMultiTokenWordList().get(3).id);
        assertEquals((Integer) 2, sent2.getMultiTokenWordList().get(2).multiTokenHead);
        assertEquals((Integer) 2, sent2.getMultiTokenWordList().get(3).multiTokenHead);
    }
}
