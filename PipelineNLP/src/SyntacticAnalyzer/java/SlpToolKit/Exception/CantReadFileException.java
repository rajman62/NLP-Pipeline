package SyntacticAnalyzer.java.SlpToolKit.Exception;

/*******************************************************************************
 * Exception lev�e si un probl�me survient lors de la lecture d'un fichier
 ******************************************************************************/
public class CantReadFileException extends java.io.IOException {

    public CantReadFileException(String filename)
    {
        super("can't read file '" + filename + "'");
    }
}