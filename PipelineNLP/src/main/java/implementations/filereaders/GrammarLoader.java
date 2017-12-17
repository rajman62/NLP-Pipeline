package implementations.filereaders;

import implementations.syntacticutils.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GrammarLoader {
    private Pattern cfgExtension = Pattern.compile("^.*\\.cfg$");
    private Pattern ruleFormat = Pattern.compile("^\\s*(?<left>\\S+)\\s*-\\s*>\\s*(?<right>\\S+(\\s+\\S+)*)\\s*$");
    private Pattern whiteSpace = Pattern.compile("\\s+");
    private Pattern ignoreLineInTextFile = Pattern.compile("^((\\s*)|(#.*))$");


    public Grammar loadFromFile(String path) throws Exception {
        if (cfgExtension.matcher(path).matches()) {
            return cfgFileLoader(path);
        }

        throw new IOException("Unrecognized file format for: " + path);
    }

    private Grammar cfgFileLoader(String path) throws IOException {
        BufferedReader input = new BufferedReader(new FileReader(path));
        int lineNumber = 0;
        String line = input.readLine();
        Grammar out = new Grammar();

        while (line != null) {
            lineNumber += 1;

            if (ignoreLineInTextFile.matcher(line).matches()) {
                line = input.readLine();
                continue;
            }

            Matcher rule = ruleFormat.matcher(line);
            if (rule.matches()) {

                String[] parsedStringRules = whiteSpace.split(rule.group("right"));
                Sigma[] right = new Sigma[parsedStringRules.length];
                for (int i = 0; i < parsedStringRules.length; i++)
                    right[i] = RealNonTerminal.of(parsedStringRules[i]);

                NonTerminal left = RealNonTerminal.of(rule.group("left"));

                out.addRule(new Rule(left, right));

                line = input.readLine();
            } else {
                throw new IllegalArgumentException("In file " + path + " line " + lineNumber);
            }
        }
        input.close();
        return out;
    }
}
