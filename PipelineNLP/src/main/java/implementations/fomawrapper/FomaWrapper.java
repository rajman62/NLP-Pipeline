package implementations.fomawrapper;

import java.io.*;
import java.nio.CharBuffer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FomaWrapper {
    private BufferedWriter stdinFoma;
    private BufferedReader stderrFoma;
    private BufferedReader stdoutFoma;
    private Process fomaProcess;

    private Pattern commandLineRegex = Pattern.compile("foma\\[\\d+]:.*");
    private Pattern warningLineRegex = Pattern.compile("\\**Warning:.*");
    private Pattern applyUpLineRegex = Pattern.compile("apply up>.*");
    private Pattern applyUpOutputRegex = Pattern.compile("^(.*)$");

    private char[] buffer;
    private int bufferLength = 1024;
    private int bufferElementCount;
    private int readOffset;

    public FomaWrapper(String binPath, String lexecPath) throws IOException {
        fomaProcess = Runtime.getRuntime().exec(binPath);
        stdinFoma = new BufferedWriter(new OutputStreamWriter(fomaProcess.getOutputStream()));
        stderrFoma = new BufferedReader(new InputStreamReader(fomaProcess.getErrorStream()));
        stdoutFoma = new BufferedReader(new InputStreamReader(fomaProcess.getInputStream()));

        buffer = new char[bufferLength];
        readOffset = 0;
        bufferElementCount = 0;

        waitForInputLine(commandLineRegex);
        initUp(lexecPath);
    }

    public List<String> applyUp(String word) throws IOException {
        stdinFoma.write(word + "\n");
        stdinFoma.flush();
        ArrayList<String> out = new ArrayList<>();
        for (Matcher match : getAll(applyUpOutputRegex, applyUpLineRegex)) {
            if (!match.group(1).equals("???"))
                out.add(match.group(1));
        }
        return out;
    }

    public void close() {
        fomaProcess.destroy();
    }

    private void initUp(String lexecPath) throws IOException {
        stdinFoma.write("read lexc " + lexecPath + "\n");
        stdinFoma.flush();
        waitForInputLine(commandLineRegex);
        stdinFoma.write("up\n");
        stdinFoma.flush();
        waitForInputLine(applyUpLineRegex);
    }

    private Matcher waitForInputLine(Pattern pattern) throws IOException {
        while (true) {
            String line = getNextLine();

            warningLineHandling(line);

            Matcher inputPatternMatcher = pattern.matcher(line);
            if (inputPatternMatcher.matches())
                return inputPatternMatcher;
        }
    }

    private ArrayList<Matcher> getAll(Pattern pattern, Pattern until) throws IOException {
        ArrayList<Matcher> out = new ArrayList<>();
        while (true) {
            String line = getNextLine();

            warningLineHandling(line);

            Matcher untilPatternMatcher = until.matcher(line);
            if (untilPatternMatcher.matches())
                return out;

            Matcher inputPatternMatcher = pattern.matcher(line);
            if (inputPatternMatcher.matches()) {
                out.add(inputPatternMatcher);
            }
        }
    }

    private String getNextLine() throws IOException {
        int readLength = 0;
        if (bufferElementCount == 0 || (bufferElementCount > 0 && stdoutFoma.ready())) {
            readLength = stdoutFoma.read(buffer, bufferElementCount, bufferLength - bufferElementCount);
        }

        bufferElementCount += readLength;

        while (readOffset < bufferElementCount && buffer[readOffset] != '\n') {
            readOffset += 1;
        }

        String out = new String(buffer, 0, readOffset);

        if (buffer[readOffset] == '\n')
            bufferElementCount -= readOffset + 1;
        else
            bufferElementCount -= readOffset;

        for (int i = 0; i < bufferElementCount; i++)
            buffer[i] = buffer[readOffset + 1 + i];

        readOffset = 0;
        return out;
    }

    private void warningLineHandling(String line) {
        Matcher warningMatcher = warningLineRegex.matcher(line);
        if (warningMatcher.matches())
            //TODO: use a logger here
            System.out.println(line);
    }
}
