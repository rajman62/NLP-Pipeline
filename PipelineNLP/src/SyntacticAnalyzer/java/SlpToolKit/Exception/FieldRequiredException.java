package SyntacticAnalyzer.java.SlpToolKit.Exception;

/*******************************************************************************
 * Exception lev�e en cas o� un champ requis n'a pas �t� sp�cifi�
 ******************************************************************************/
public class FieldRequiredException extends InvalidMethodCallException {

    public FieldRequiredException(String field_name)
    {
        super("'" + field_name + "' field is required");
    }
}