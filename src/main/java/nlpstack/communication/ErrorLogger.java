package nlpstack.communication;

import nlpstack.Main;
import nlpstack.annotations.StringSegment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorLogger {
    private Logger logger = LoggerFactory.getLogger(Main.class);

    public void lexicalError(String message, StringSegment segment) {
        printMessage(String.format("Lexical Error: %s", message));
    }

    public void syntacticError(String message, Chart chart) {
        printMessage(String.format("Syntactic Error: %s", message));
    }

    private void printMessage(String message) {
        logger.error(message);
    }
}
