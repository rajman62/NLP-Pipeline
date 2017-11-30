package nlpstack.communication;

import nlpstack.annotations.StringSegment;

public class ErrorLogger {
    public void lexicalError(String message, StringSegment segment) {
        printMessage(String.format("Lexical Error: %s", message));
    }

    public void syntacticError(String message, Chart chart) {
        printMessage(String.format("Syntactic Error: %s", message));
    }

    private void printMessage(String message) {
        System.err.println(message);
    }
}
