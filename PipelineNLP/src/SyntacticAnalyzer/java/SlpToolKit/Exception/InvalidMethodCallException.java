package SyntacticAnalyzer.java.SlpToolKit.Exception;

/*******************************************************************************
 * Exception lev�e en cas d'appel d'une m�thode non autoris�e
 ******************************************************************************/
public class InvalidMethodCallException extends RuntimeException {

    public InvalidMethodCallException(String reason)
    {
        super(reason);
    }
}