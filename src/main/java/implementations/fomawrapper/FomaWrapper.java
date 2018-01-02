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
    private Pattern warningLineRegex = Pattern.compile("(\\**Warning:)|(Error).*\n");
    private Pattern applyUpLineRegex = Pattern.compile("apply up> \n?");
    private Pattern extractLineRegex = Pattern.compile("^(.*)\n$");

    private Pattern lexecFilePattern = Pattern.compile("^.*\\.lexe?c$");
    private Pattern fomaFilePattern = Pattern.compile("^.*\\.foma$");

    private char[] buffer;
    private int bufferLength = 1024;
    private int bufferElementCount;

    /**
     * object that read and writes to a foma terminal. It is only meant for reading and executing foma and lexec files.
     * @param binPath path to the foma executable
     * @param confPath path to a lexec of foma file.
     * @param errorLogger error logger to use
     * @throws IOException if communication with foma fails
     */
    public FomaWrapper(String binPath, String confPath, ErrorLogger errorLogger) throws IOException {
        fomaProcess = Runtime.getRuntime().exec(binPath);
        stdinFoma = new BufferedWriter(new OutputStreamWriter(fomaProcess.getOutputStream()));
        stderrFoma = new BufferedReader(new InputStreamReader(fomaProcess.getErrorStream()));
        stdoutFoma = new BufferedReader(new InputStreamReader(fomaProcess.getInputStream()));
        this.errorLogger = errorLogger;

        buffer = new char[bufferLength];
        bufferElementCount = 0;

        waitForInputLine(commandLineRegex);
        flushInputBuffer();
        if (lexecFilePattern.matcher(confPath).matches())
            initLexec(confPath);
        else if (fomaFilePattern.matcher(confPath).matches())
            initFoma(confPath);
        else
            throw new IllegalArgumentException(String.format("%s is neither a .lexec or .foma file", confPath));
        up();
    }

    public List<String> applyUp(String word) throws IOException {
        stdinFoma.write(word + "\n");
        stdinFoma.flush();
        ArrayList<String> out = new ArrayList<>();
        for (Matcher match : getAll(
                // Pattern.compile(String.format("(%s.*)\n", Pattern.quote(word)), Pattern.CASE_INSENSITIVE),
                extractLineRegex,
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
    private void initLexec(String lexecPath) throws IOException {
        stdinFoma.write("read lexc " + lexecPath + "\n");
        stdinFoma.flush();
        waitForInputLine(commandLineRegex);
        flushInputBuffer();
    }

    /**
     * Load foma file
     * @param fomaPath path to the foma file
     * @throws IOException if communication error with foma
     *
     */
    private void initFoma(String fomaPath) throws IOException {
        stdinFoma.write(String.format("source %s\n", fomaPath));
        stdinFoma.flush();
        waitForInputLine(commandLineRegex);
        flushInputBuffer();
    }

    /**
     * execute up command in foma terminal
     * @throws IOException if communication error with foma
     */
    private void up() throws IOException {
        stdinFoma.write("up\n");
        stdinFoma.flush();
        waitForInputLine(applyUpLineRegex);
        flushInputBuffer();
    }

    /**
     * Waits until pattern has been matched on the output of foma
     * @param pattern the pattern that indicates when we can stop waiting
     * @return the matched object of pattern
     * @throws IOException if communication error with foma
     */
    private Matcher waitForInputLine(Pattern pattern) throws IOException {
        while (true) {
            String line = getNextLine();

            warningAndErrorLineHandling(line);

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

            warningAndErrorLineHandling(line);

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
     */
    private void flushInputBuffer() {
        bufferElementCount = 0;
    }

    private void warningAndErrorLineHandling(String line) {
        Matcher warningMatcher = warningLineRegex.matcher(line);
        if (warningMatcher.matches())
            errorLogger.lexicalError(String.format("Warning in foma: %s", line), null);
    }
}
