package filereaders;

import dk.brics.automaton.Automaton;
import implementations.filereaders.FSALoader;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class LoadAndStoreFSAs {
    @Test
    public void readFile() throws Exception {
        FSALoader fsaLoader = new FSALoader();
        Automaton fsa = fsaLoader.loadFromFile("src/test/java/filereaders/word_fsa_example.txt");
        assertEquals(false, fsa.run("wo"));
        assertEquals(true, fsa.run("world"));
        assertEquals(true, fsa.run("john.doe@mail.com"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void readFileWithError() throws Exception {
        FSALoader fsaLoader = new FSALoader();
        Automaton fsa = fsaLoader.loadFromFile("src/test/java/filereaders/word_fsa_error.txt");
    }
}
