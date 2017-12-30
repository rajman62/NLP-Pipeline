package fomawrapper;

import implementations.fomawrapper.FomaWrapper;
import nlpstack.communication.ErrorLogger;
import org.apache.commons.lang3.SystemUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

public class TestFomaWrapper {
    private static String FOMA_BIN_PATH = "bin/foma";
    private static final String TEST_PATH = "src/test/java/fomawrapper/";

    @BeforeClass
    public static void getFomaBinPath() {
        if (SystemUtils.IS_OS_WINDOWS)
            FOMA_BIN_PATH = FOMA_BIN_PATH.concat(".exe");
    }

    @Test
    public void canApplyUpOnLexec() throws IOException {
        FomaWrapper foma = new FomaWrapper(FOMA_BIN_PATH, TEST_PATH + "english.lexc", new ErrorLogger());
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

    @Test
    public void canApplyUpOnFomaFiles() throws IOException {
        FomaWrapper foma = new FomaWrapper(FOMA_BIN_PATH, TEST_PATH + "english.foma", new ErrorLogger());
        List<String> morph = foma.applyUp("tries");
        assertEquals(2, morph.size());
        assertTrue(morph.contains("try+V+3P+Sg"));
        assertTrue(morph.contains("try+N+Pl"));

        morph = foma.applyUp("unknownword");
        assertEquals(0, morph.size());

        morph = foma.applyUp("panicking");
        assertEquals(1, morph.size());
        assertTrue(morph.contains("panic+V+PresPart"));

        foma.close();
    }
}
