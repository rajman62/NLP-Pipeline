package SyntacticAnalyzer.java.SlpToolKit.Exception;

/*******************************************************************************
 * Exception lev�e en cas de tentative d'acc�s � un champ non d�fini
 ******************************************************************************/
public class FieldNotDefinedException extends InvalidMethodCallException {

    public FieldNotDefinedException()
    {
        super("field not defined");
    }
}