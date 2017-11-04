package fomawrapper;

import implementations.fomawrapper.FomaWrapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

public class TestFomaWrapper {
    static final String FOMA_BIN_PATH = "../Morphology/foma-0.9.18_win32/win32/foma.exe";
    static final String TEST_PATH = "src/test/java/fomawrapper/";

    @Test
    public void canApplyUp() throws IOException {
        FomaWrapper foma = new FomaWrapper(FOMA_BIN_PATH, TEST_PATH + "english.lexec");
        List<String> morph = foma.applyUp("process");
        assertEquals(2, morph.size());
        assertTrue(morph.contains("process+N"));
        assertTrue(morph.contains("process+V"));

        morph = foma.applyUp("process");
        assertEquals(2, morph.size());
        assertTrue(morph.contains("process+N"));
        assertTrue(morph.contains("process+V"));

        foma.close();
    }
}
