package configuration;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import implementations.conffile.ConfFile;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

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
        assertEquals("morphological/analyzer", conf.lexicalConf.EMORBinPath);
        assertEquals("morphological/path", conf.lexicalConf.EMORConfPath);
        assertEquals("trees/path", conf.syntacticConf.syntacticTreesPath);
    }
}
