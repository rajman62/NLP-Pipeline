package implementations.filereaders;

import org.leibnizcenter.cfg.grammar.Grammar;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.regex.Pattern;


public class GrammarLoader {
    private Pattern cfgExtension = Pattern.compile("^.*\\.cfg$");


    public Grammar<String> loadFromFile(String path) throws Exception {
        if (cfgExtension.matcher(path).matches()) {
            return Grammar.parse(Paths.get(path), Charset.forName("UTF-8"));
        }

        throw new IOException("Unrecognized file format for: " + path);
    }
}
