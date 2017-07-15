package SyntacticAnalyzer.java.SlpToolKit.Exception;

/*******************************************************************************
 * Exception lev�e si un probl�me survient lors de l'�criture d'un fichier
 ******************************************************************************/
public class CantWriteFileException extends java.io.IOException {

    public CantWriteFileException(String filename)
    {
        super("can't write file '" + filename + "'");
    }
}