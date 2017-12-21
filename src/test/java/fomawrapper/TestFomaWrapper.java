package fomawrapper;

import implementations.fomawrapper.FomaWrapper;
import nlpstack.communication.ErrorLogger;
import org.apache.commons.lang3.SystemUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

public class TestFomaWrapper {
    private static String FOMA_BIN_PATH = "bin/foma";
    private static final String TEST_PATH = "src/test/java/fomawrapper/";

    @Before
    public void getFomaBinPath() {
        if (SystemUtils.IS_OS_WINDOWS)
            FOMA_BIN_PATH = FOMA_BIN_PATH.concat(".exe");
    }

    @Test
    public void canApplyUp() throws IOException {
        FomaWrapper foma = new FomaWrapper(FOMA_BIN_PATH, TEST_PATH + "english.lexec", new ErrorLogger());
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
