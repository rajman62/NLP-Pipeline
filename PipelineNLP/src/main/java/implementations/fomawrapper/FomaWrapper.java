package implementations.fomawrapper;

import nlpstack.communication.ErrorLogger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FomaWrapper {
    private BufferedWriter stdinFoma;
    private BufferedReader stderrFoma;
    private BufferedReader stdoutFoma;
    private Process fomaProcess;
    private ErrorLogger errorLogger;

    private Pattern commandLineRegex = Pattern.compile("foma\\[\\d+]: ");
    private Pattern warningLineRegex = Pattern.compile("\\**Warning:.*\n");
    private Pattern applyUpLineRegex = Pattern.compile("apply up> ");

    private char[] buffer;
    private int bufferLength = 1024;
    private int bufferElementCount;

    public FomaWrapper(String binPath, String lexecPath, ErrorLogger errorLogger) throws IOException {
        fomaProcess = Runtime.getRuntime().exec(binPath);
        stdinFoma = new BufferedWriter(new OutputStreamWriter(fomaProcess.getOutputStream()));
        stderrFoma = new BufferedReader(new InputStreamReader(fomaProcess.getErrorStream()));
        stdoutFoma = new BufferedReader(new InputStreamReader(fomaProcess.getInputStream()));
        this.errorLogger = errorLogger;

        buffer = new char[bufferLength];
        bufferElementCount = 0;

        waitForInputLine(commandLineRegex);
        flushInputBuffer();
        initUp(lexecPath);
    }

    public List<String> applyUp(String word) throws IOException {
        stdinFoma.write(word + "\n");
        stdinFoma.flush();
        ArrayList<String> out = new ArrayList<>();
        for (Matcher match : getAll(
                Pattern.compile(String.format("(%s.*)\n", Pattern.quote(word))),
                applyUpLineRegex,
                Pattern.compile(Pattern.quote(word).concat("\n")))) {
            if (!match.group(1).equals("???"))
                out.add(match.group(1));
        }
        flushInputBuffer();
        return out;
    }

    public void close() {
        fomaProcess.destroy();
    }

    /**
     * Load lexec file
     * @param lexecPath path to the lexec file
     * @throws IOException if communication error with foma
     */
    private void initUp(String lexecPath) throws IOException {
        stdinFoma.write("read lexc " + lexecPath + "\n");
        stdinFoma.flush();
        waitForInputLine(commandLineRegex);
        flushInputBuffer();
        stdinFoma.write("up\n");
        stdinFoma.flush();
        waitForInputLine(applyUpLineRegex);
        flushInputBuffer();
    }

    /**
     * Waits until pattern has been matched on the output of fome
     * @param pattern the pattern that indicates when we can stop waiting
     * @return the matched object of pattern
     * @throws IOException if communication error with foma
     */
    private Matcher waitForInputLine(Pattern pattern) throws IOException {
        while (true) {
            String line = getNextLine();

            warningLineHandling(line);

            Matcher inputPatternMatcher = pattern.matcher(line);
            if (inputPatternMatcher.matches())
                return inputPatternMatcher;
        }
    }

    /**
     * Gets all the matchers that match pattern until 'until' is found
     * @param pattern pattern to be matched
     * @param until indicates when we can stop looking for pattern
     * @param ignore indicates the lines that should just be ignored (until has priority over ignore)
     * @return the list of matcher that pattern matched
     * @throws IOException if communication error with foma
     */
    private ArrayList<Matcher> getAll(Pattern pattern, Pattern until, Pattern ignore) throws IOException {
        ArrayList<Matcher> out = new ArrayList<>();
        while (true) {
            String line = getNextLine();

            warningLineHandling(line);

            Matcher untilPatternMatcher = until.matcher(line);
            if (untilPatternMatcher.matches())
                return out;

            Matcher inputPatternMatcher = pattern.matcher(line);
            if (inputPatternMatcher.matches() && !ignore.matcher(line).matches()) {
                out.add(inputPatternMatcher);
            }
        }
    }

    /**
     * Custom function that reads from stdin. Like readline, it returns stdout of foma line by line.
     * The advantage of using this over readline(), is that this function returns the new data even if the input
     * isn't a full line. If the output of stdout isn't a full line, the same data will be returned by this function
     * until there is a full line, unless flushInputBuffer is called.
     * @return The next line of stdin (with an ending \n if it is a full line)
     * @throws IOException if communication error with foma
     */
    private String getNextLine() throws IOException {
        int readLength = 0;
        int readOffset = 0;
        if (bufferElementCount == 0 || stdoutFoma.ready()) {
            readLength = stdoutFoma.read(buffer, bufferElementCount, bufferLength - bufferElementCount);
        }

        if (readLength == -1) {
            errorLogger.lexicalError("End of Stream returned by foma", null);
            throw new IOException("End of Stream returned by foma");
        }

        bufferElementCount += readLength;

        while (readOffset < bufferElementCount && buffer[readOffset] != '\n') {
            readOffset += 1;
        }

        String out;

        if (buffer[readOffset] == '\n') {
            out = new String(buffer, 0, readOffset + 1);
            bufferElementCount -= (readOffset + 1);
            for (int i = 0; i < bufferElementCount; i++)
                buffer[i] = buffer[readOffset + 1 + i];
        } else
            out = new String(buffer, 0, readOffset);

        return out;
    }

    /**
     * Removes all the data from the input buffer
     * @throws IOException if communication error with foma
     */
    private void flushInputBuffer() throws IOException {
        bufferElementCount = 0;
    }

    private void warningLineHandling(String line) {
        Matcher warningMatcher = warningLineRegex.matcher(line);
        if (warningMatcher.matches())
            errorLogger.lexicalError(String.format("Warning in foma: %s", line), null);
    }
}
