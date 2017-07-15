package SyntacticAnalyzer.java.SlpToolKit.Exception;

/*******************************************************************************
 * Exception lev�e en cas d'appel d'une m�thode pas encore impl�ment�e
 ******************************************************************************/
public class MethodNotImplementedException extends InvalidMethodCallException {

    public MethodNotImplementedException()
    {
        super("method not implemented");
    }
}