package configuration;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import implementations.conffile.ConfFile;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;


public class ParseYML {
    @Test
    public void parseSimpleYmlString() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        User user = mapper.readValue("name: user1\nage: 30", User.class);
        assertEquals("user1", user.name);
        assertEquals(30, user.age);
    }

    @Test
    public void parseSimpleYmlWithMissingStringValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        User user = mapper.readValue("age: 30", User.class);
        assertEquals(null, user.name);
        assertEquals(30, user.age);
    }

    @Test
    public void parseExampleConfFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ConfFile conf = mapper.readValue(
                new File("src/test/java/configuration/exampleconf.yml"),
                ConfFile.class);
        assertEquals("word/fsa", conf.lexicalConf.wordFSAPath);
        assertEquals("separator/fsa", conf.lexicalConf.separatorFSAPath);
        assertEquals("separator/eof", conf.lexicalConf.eosSeparatorFSAPath);
        assertEquals("morphological/analyzer", conf.lexicalConf.FomaBinPath);
        assertEquals("morphological/path", conf.lexicalConf.FomaConfPath);
        assertEquals("trees/path", conf.syntacticConf.syntacticTreesPath);
    }
}
