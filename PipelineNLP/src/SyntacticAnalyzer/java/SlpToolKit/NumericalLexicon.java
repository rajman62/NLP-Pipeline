package SyntacticAnalyzer.java.SlpToolKit;


import java.io.IOException;
import SyntacticAnalyzer.java.SlpToolKit.Exception.*;

/**
 * <p>Title: Projet de semestre</p>
 * <p>Description: Surcouche Java � la librairie SlpToolKit</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Ecole Polytechnique de Lausanne (EPFL) - D�partement d'intelligence artificielle (LIA)</p>
 * @author Antonin Mer�ay
 * @version 1.0
 */

/*******************************************************************************
 * Lexique de s�quences d'entiers (impl�mentant l'interface {@link SlpToolKit.NumericalLexiconIF NumericalLexiconIF}).
 ******************************************************************************/
public class NumericalLexicon implements NumericalLexiconIF {
    /**
     * Pointeur sur la structure de donn�e C contenant le lexique natif sous-jaccent
     */
    protected long native_data = 0;

    /**
     * Flag indiquant si le lexique a d�j� �t� normalis� ou non
     */
    protected boolean normalized = false;

    /**
     * Instance d'arbre lexical retourn� par la fonction getAM()
     */
    protected Trie associative_memory = null;

    /**
     * Classe des entr�es g�n�r�es par un lexique num�rique (NumericalLexiconEntry)
     */
    private static Class ENTRY_CLASS = null;

    /**
     * Extension du fichier de d�finition (natif)
     */
    private static String LEXICAL_FILE_EXTENSION = ".slplex";

    /***************************************************************************
     * Constructeur.
     **************************************************************************/
    public NumericalLexicon() {
        this.native_data = NativeLexique.constructor();
    }

    /***************************************************************************
     * Destructeur explicite.
     **************************************************************************/
    protected void finalize()
    {
        NativeLexique.destructor(this.native_data);
    }

    /**************************************************************************/
    public LexicalAccessTableIF getAM()
    {
        if (this.associative_memory == null)
        {
            long native_trie = NativeLexique.getArbreLexico(this.native_data);
            this.associative_memory = new Trie(native_trie, true);
        }

        return this.associative_memory;
    }

    /**************************************************************************/
    public void exportToASCII(String filename) throws IOException
    {
        utils.checkParameterIsAssigned(filename);

        if (NativeLexique.exportToASCII(this.native_data, filename))
            throw new CantWriteFileException(filename);
    }

    /**************************************************************************/
    public void importFromASCII(String filename) throws IOException
    {
        utils.checkParameterIsAssigned(filename);

        filename += LEXICAL_FILE_EXTENSION;

        if (NativeLexique.importFromASCII(this.native_data, filename))
            throw new CantReadFileException(filename);
    }

    /**************************************************************************/
    public void loadFromFile(String filename) throws IOException
    {
        utils.checkParameterIsAssigned(filename);

        if (NativeLexique.loadFromFile(this.native_data, filename))
            throw new CantReadFileException(filename);
    }

    /**************************************************************************/
    public void saveToFile(String filename) throws IOException
    {
        utils.checkParameterIsAssigned(filename);

        if (NativeLexique.saveFromFile(this.native_data, filename))
            throw new CantWriteFileException(filename);
    }

    /**************************************************************************/
    public void normalize()
    {
        if (this.normalized == false)
        {
            NativeLexique.normalize(this.native_data);
            this.normalized = true;
        }
        else
            throw new InvalidMethodCallException(LEXICON_ALREADY_NORMALIZED_ERROR_MESSAGE);
    }

    /**************************************************************************/
    public long getSize()
    {
        return NativeLexique.getSize(this.native_data);
    }

    /**************************************************************************/
    public NumericalLexiconEntry[] lookForAll(long[] seq)
    {
        utils.checkParameterIsAssigned(seq);

        LexiconEntry[] temp_result = NativeLexique.lookForAll(this.native_data, seq, this, ENTRY_CLASS);
        NumericalLexiconEntry[] result = new NumericalLexiconEntry[temp_result.length];

        for(int index = 0; index < result.length; index++)
            result[index] = (NumericalLexiconEntry)temp_result[index];

        return result;
    }

    /**************************************************************************/
    public NumericalLexiconEntry lookFor(long[] seq)
    {
        utils.checkParameterIsAssigned(seq);

        return (NumericalLexiconEntry)NativeLexique.lookFor(this.native_data, seq, this, ENTRY_CLASS);
    }

    /**************************************************************************/
    public LexiconEntry getEntryFromIndex(long index)
    {
        if ((index < 0) || (index >= this.getSize()))
            throw new IndexOutOfBoundsException();

        return NativeLexique.getFromIndex(this.native_data, this, index, ENTRY_CLASS);
    }

    /**************************************************************************/
    public boolean isNormalized()
    {
        return this.normalized;
    }

    /**************************************************************************/
    public long insert(LexiconEntry p_entry)
    {
        utils.checkParameterIsAssigned(p_entry);

        NumericalLexiconEntry entry = (NumericalLexiconEntry)p_entry;

        // Extraire la s�quence le cas o�
        entry.getSequence();

        return entry.insertIntoNativeLexicon(this.native_data);
    }

    /**************************************************************************/
    public boolean contains(LexiconEntry entry)
    {
        utils.checkParameterIsAssigned(entry);

        return entry.containedIntoNativeLexicon(this.native_data);
    }

    /**************************************************************************/
    public LexiconEntry[] lookForAll(Object o)
    {
        return (LexiconEntry[])this.lookForAll((long[])o);
    }

    /**************************************************************************/
    public LexiconEntry lookFor(Object o)
    {
        return (LexiconEntry)this.lookFor((long[])o);
    }

    /**************************************************************************/
    public long getNativeLexicon()
    {
        return this.native_data;
    }

    /**************************************************************************/
    static
    {
        // D�finit la classe des entr�es de lexique instanci�es
        try {
            ENTRY_CLASS = Class.forName("SlpToolKit.NumericalLexiconEntry");
        } catch (Exception e) {e.printStackTrace();};
    }
}