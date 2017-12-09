package implementations.filereaders;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;

import java.io.*;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FSALoader {
    private Pattern textExtension = Pattern.compile("^.*\\.txt$");
    private Pattern serializedExtension = Pattern.compile("^.*\\.ser$");
    private Pattern ignoreLineInTextFile = Pattern.compile("^((\\s*)|(#.*))$");
    private Pattern extractRegex = Pattern.compile("^<(.*)>\\s*$");

    public Automaton loadFromFile(String path) throws Exception {
        if (textExtension.matcher(path).matches()) {
            return txtFileLoader(path);
        } else if (serializedExtension.matcher(path).matches()) {
            return Automaton.load(new FileInputStream(path));
        }

        throw new IOException("Unrecognized file format for: " + path);
    }

    public void saveToFile(Automaton fsa, String path) throws Exception {
        if (textExtension.matcher(path).matches()) {
            throw new UnsupportedOperationException(
                    "Cannot save automaton to txt file ("
                            + path +
                            "), an automaton can potentially store an infinite number of words " +
                            "and regexes are lost during the reduce operation.");
        } else if (serializedExtension.matcher(path).matches()) {
            fsa.store(new FileOutputStream(path));
        } else {
            throw new IOException("Unrecognized file format for: " + path);
        }
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
                Matcher reg = extractRegex.matcher(line);
                if (reg.matches())
                    regexList.add(new RegExp(parseSpecialCharacters(reg.group(1))));
                else
                    regexList.add(new RegExp(parseSpecialCharacters(line.trim())));
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

    private static String parseSpecialCharacters(String input) {
        String out = input.replace("\\n", "\n");
        out = out.replace("\\t", "\t");
        out = out.replace("\\\\", "\\");
        return out;
    }

    public static Automaton parseRegex(String regex) {
        return new RegExp(parseSpecialCharacters(regex)).toAutomaton();
    }
}
