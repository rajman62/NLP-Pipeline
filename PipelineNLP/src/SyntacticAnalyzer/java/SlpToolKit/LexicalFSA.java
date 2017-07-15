package SyntacticAnalyzer.java.SlpToolKit;

/**
 * <p>Title: Projet de semestre</p>
 * <p>Description: Surcouche Java � la librairie SlpToolKit</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Ecole Polytechnique de Lausanne (EPFL) - D�partement d'intelligence artificielle (LIA)</p>
 * @author Antonin Mer�ay
 * @version 1.0
 */

/*******************************************************************************
 * Impl�mentation concr�te d'une {@link SlpToolKit.LexicalAssocMemIF
 * m�moire associative lexical} gr�ce aux automates d'�tats finis.
 ******************************************************************************/
public class LexicalFSA implements LexicalAssocMemIF {
    private long automate_lex;
    private boolean lex_of_long;

    /***************************************************************************
     * Constructeur.
     *
     * @param lex_of_long Sp�cifie le type des s�quences stock�es
     **************************************************************************/
    public LexicalFSA(boolean lex_of_long) {
        this.lex_of_long = lex_of_long;

        constructor(lex_of_long);
    }

    /***************************************************************************
     * Destructeur.
     **************************************************************************/
    protected native void finalize();

    /***************************************************************************
     * Constructeur natif.
     *
     * @param lex_of_long Sp�cifie le type des s�quences stock�es
     **************************************************************************/
    private native void constructor(boolean lex_of_long);
    public native long insert(String element);
    public native long insert(long[] element);
    public native SearchResult search(String element);
    public native SearchResult search(long[] element);
    public native long getSize();
    public native int getAlphabetCardinality();
    public native void saveToFile(String filename);
    public native void loadFromFile(String filename);
    public native void importContents(String filename);
    public native void exportContents(String filename);
    public native void listContents();
    public native void goToRoot();
    public native void goToChar();
    public native long getNextChar();
    public native boolean endOfWord();
    public native String[] accessString(long key);
    public native long[][] access(long key);

    /***************************************************************************
     * Chargement de la libraire 'LexicalFSA.dll'
     **************************************************************************/
    static
    {
        System.loadLibrary("LexicalFSA");
    }
}