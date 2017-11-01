package implementations.filereaders;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class FSALoader {
    private Pattern textExtension = Pattern.compile("^.*\\.txt$");
    private Pattern serializedExtension = Pattern.compile("^.*\\.ser$");
    private Pattern ignoreLineInTextFile = Pattern.compile("^((\\s*)|(#.*))$");


    public Automaton loadFromFile(String path) throws Exception {
        if (textExtension.matcher(path).matches()) {
            return txtFileLoader(path);
        } else if (serializedExtension.matcher(path).matches()) {
            return Automaton.load(new FileInputStream(path));
        }

        throw new IOException("Unrecognized file format for:" + path);
    }

    private Automaton txtFileLoader(String path) throws Exception {
        BufferedReader input = new BufferedReader(new FileReader(path));
        LinkedList<RegExp> regexList = new LinkedList<>();
        int lineNumber = 0;

        while (true) {
            String line = input.readLine();
            lineNumber += 1;

            if (line == null)
                break;
            if (ignoreLineInTextFile.matcher(line).matches())
                continue;

            try {
                regexList.add(new RegExp(line));
            } catch (IllegalArgumentException exp) {
                throw new IllegalArgumentException("In file " + path + " line " + lineNumber + ": " + exp.getMessage());
            }
        }

        Automaton out = regexList.pop().toAutomaton();
        for (RegExp reg : regexList) {
            out = out.union(reg.toAutomaton());
        }

        return out;
    }
}
