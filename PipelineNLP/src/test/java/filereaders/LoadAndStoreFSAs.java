package filereaders;

import dk.brics.automaton.Automaton;
import implementations.filereaders.FSALoader;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class LoadAndStoreFSAs {
    static final String TEST_PATH = "src/test/java/filereaders/";

    @Test
    public void readFile() throws Exception {
        FSALoader fsaLoader = new FSALoader();
        Automaton fsa = fsaLoader.loadFromFile(TEST_PATH + "word_fsa_example.txt");
        assertEquals(false, fsa.run("wo"));
        assertEquals(true, fsa.run("world"));
        assertEquals(true, fsa.run("john.doe@mail.com"));
        assertEquals(true, fsa.run(" t "));
    }

    @Test(expected = IllegalArgumentException.class)
    public void readFileWithError() throws Exception {
        FSALoader fsaLoader = new FSALoader();
        Automaton fsa = fsaLoader.loadFromFile(TEST_PATH + "word_fsa_error.txt");
    }

    @Test
    public void saveAndLoadFromSerFile() throws Exception {
        FSALoader fsaLoader = new FSALoader();
        Automaton fsa = fsaLoader.loadFromFile(TEST_PATH + "word_fsa_example.txt");
        fsaLoader.saveToFile(fsa, TEST_PATH + "word_fsa_example.ser");

        Automaton loadedFsa = fsaLoader.loadFromFile(TEST_PATH + "word_fsa_example.ser");
        assertEquals(false, loadedFsa.run("wo"));
        assertEquals(true, loadedFsa.run("world"));
        assertEquals(true, loadedFsa.run("john.doe@mail.com"));
        assertEquals(true, loadedFsa.run(" t "));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void saveFileWithError() throws Exception {
        FSALoader fsaLoader = new FSALoader();
        Automaton fsa = fsaLoader.loadFromFile(TEST_PATH + "word_fsa_example.txt");
        fsaLoader.saveToFile(fsa, "src/test/java/filereaders/word_fsa_save.txt");
    }
}
