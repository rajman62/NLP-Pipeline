package lexicalanalyzer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dk.brics.automaton.RunAutomaton;
import implementations.conffile.ConfFile;
import implementations.filereaders.FSALoader;
import implementations.lexicalutils.Arc;
import implementations.lexicalutils.ArcParsing;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class TestArcs {
    private String TEST_PATH = "src/test/java/lexicalanalyzer/";
    private RunAutomaton wordFSA;
    private RunAutomaton separatorFSA;
    private RunAutomaton invisibleCharacterPattern;
    private RunAutomaton eosSeparatorPattern;

    @Before
    public void init() throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ConfFile conf = mapper.readValue(new File(TEST_PATH + "conf.yml"), ConfFile.class);
        FSALoader fsaLoader = new FSALoader();
        wordFSA = new RunAutomaton(fsaLoader.loadFromFile(conf.lexicalConf.wordFSAPath));
        separatorFSA = new RunAutomaton(fsaLoader.loadFromFile(conf.lexicalConf.separatorFSAPath));
        invisibleCharacterPattern = new RunAutomaton(FSALoader.parseRegex(conf.lexicalConf.invisibleCharacterRegex));
        eosSeparatorPattern = new RunAutomaton(FSALoader.parseRegex(conf.lexicalConf.eosSeparatorRegex));
    }

    @Test
    public void testHashcodeAndEqual() {
        String input = "my input";
        Arc arc1 = new Arc(Pair.of(3, 7), input, Arc.Type.TOKEN);
        Arc arc2 = new Arc(Pair.of(3, 7), input, Arc.Type.TOKEN);
        Arc arc3 = new Arc(Pair.of(2, 2), input, Arc.Type.INVISIBLE_SEP);
        Set<Arc> set = new HashSet<>();
        set.add(arc1);
        set.add(arc2);
        assertEquals(arc1, arc2);
        assertEquals(1, set.size());
        set.add(arc3);
        assertNotEquals(arc2, arc3);
        assertEquals(2, set.size());
    }

    @Test
    public void testLoadingAndRunningArcsDiscovery() throws Exception {
        ArcParsing arcParsing = ArcParsing.parseArcs("Mr. Smith is processing data.", wordFSA, separatorFSA,
                invisibleCharacterPattern, eosSeparatorPattern);
        arcParsing.filterImpossibleTokenization();

        assertEquals(1, arcParsing.getArcsStartingAt(0).size());
        Arc arc = arcParsing.getArcsStartingAt(0).iterator().next();
        assertEquals("Mr.", arc.getString());
        assertEquals(Arc.Type.TOKEN, arc.getType());

        assertEquals(0, arcParsing.getArcsStartingAt(1).size());
        assertEquals(0, arcParsing.getArcsStartingAt(2).size());

        assertEquals(1, arcParsing.getArcsStartingAt(3).size());
        arc = arcParsing.getArcsStartingAt(3).iterator().next();
        assertEquals(" ", arc.getString());
        assertEquals(Arc.Type.INVISIBLE_SEP, arc.getType());

        // checks that filterImpossibleTokenization worked
        assertEquals(1, arcParsing.getArcsStartingAt(13).size());
        arc = arcParsing.getArcsStartingAt(13).iterator().next();
        assertEquals("processing", arc.getString());

        arcParsing = ArcParsing.parseArcs("M. Smith. Mr. Smith.", wordFSA, separatorFSA,
                invisibleCharacterPattern, eosSeparatorPattern);
        assertEquals(2, arcParsing.getArcsStartingAt(0).size());
    }
}
