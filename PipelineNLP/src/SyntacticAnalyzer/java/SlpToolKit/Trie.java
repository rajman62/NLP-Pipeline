package SyntacticAnalyzer.java.SlpToolKit;


import SyntacticAnalyzer.java.SlpToolKit.Exception.*;
import java.io.IOException;

/**
 * <p>Title: Projet de semestre</p>
 * <p>Description: Surcouche Java � la librairie SlpToolKit</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Ecole Polytechnique de Lausanne (EPFL) - D�partement d'intelligence artficielle (LIA)</p>
 * @author Antonin Mer�ay
 * @version 1.0
 */

/*******************************************************************************
 * Impl�mentation concr�te d'une {@link SlpToolKit.LexicalAccessTableIF
 * table d'acc�s lexicale} gr�ce aux arbres.
 ******************************************************************************/
public class Trie implements LexicalAccessTableIF {
    /**
     * Pointeur sur la structure native C Arbre_lexico sous-jacente
     */
    protected long native_data = 0;

    /**
     * Indice du noeud courant dans le parcours de l'arbre
     */
    protected long position_courante = 0;

    /**
     * Indice du noeud suivant dans le parcours de l'arbre
     */
    protected long position_suivante = 0;

    /**
     * Flag indiquant si l'arbre contient des s�quences de type octet (false) ou
     * des s�quences de type long (true)
     */
    protected boolean lex_of_long;

    /**
     * Valeur de clef utilis�e par d�faut lors de la prochaine insertion
     */
    protected long key = 0;


    /**
     * Extension utilis�e pour la sauvegarde/lecture � partir d'un fichier
     */
    protected static final String FILE_EXTENSION = ".tree";

    /***************************************************************************
     * Constructeur d'un arbre lexical. Le param�tre sp�cifie si les graphies
     * contenues dans la m�moire sont stock�es au format long[] (lex_of_long vrai)
     * ou au format String (lex_of_long faux).
     *
     * @param lex_of_long Oui/non les mots sont stock�s en m�moire au format long[]
     **************************************************************************/
    public Trie(boolean lex_of_long) {
        this.lex_of_long = lex_of_long;
        this.native_data = NativeArbreLex.constructor();
    }

    /***************************************************************************
     * Constructeur utilis� par la fonction LexiconIF.getAM(). Ce constructeur
     * ne peut �tre utilis� en dehors du package.
     *
     * @param native_data Pointeur sur la structure C sous-jacente
     * @param lex_of_long Type d'arbre lexical
     **************************************************************************/
    protected Trie(long native_data, boolean lex_of_long)
    {
        this.native_data = native_data;
        this.lex_of_long = lex_of_long;
    }

    /***************************************************************************
     * Destructeur.
     **************************************************************************/
    protected void finalize()
    {
        NativeArbreLex.destructor(this.native_data);
    }

    /***************************************************************************
     * Insertion d'une nouvelle entr�e dans l'arbre lexical
     * Ajoute � la table une nouvelle entr�e caract�ris�e par une cha�ne de
     * caract�res (sous forme de String) et une clef.
     *
     * @param key Clef
     * @param graphy Graphie
     **************************************************************************/
    public void insert(long key, String graphy)
    {
        utils.checkParameterIsAssigned(graphy);

        // Utiliser la fonction d'insertion selon le type d'arbre
        if (this.lex_of_long)
            NativeArbreLex.insert(this.native_data, key, utils.stringToLongArray(graphy));

        else
            NativeArbreLex.insert(this.native_data, key, graphy);

        this.key = key + 1;
    }

    /***************************************************************************
     * Ajoute � la table une nouvelle entr�e caract�ris�e par une s�quence de
     * long et une clef.
     *
     * @param key Clef
     * @param sequence S�quence de long
     **************************************************************************/
    public void insert(long key, long[] sequence)
    {
        utils.checkParameterIsAssigned(sequence);

        // Utiliser la fonction d'insertion selon le type d'arbre
        if (this.lex_of_long)
            NativeArbreLex.insert(this.native_data, key, sequence);

        else
            NativeArbreLex.insert(this.native_data, key, utils.longArrayToString(sequence));

        this.key = key + 1;
    }

    /**************************************************************************/
    public SearchResult search(long[] sequence)
    {
        utils.checkParameterIsAssigned(sequence);

        // Utiliser la fonction de recherche selon de type d'arbre
        if (this.lex_of_long)
            return NativeArbreLex.search(this.native_data, sequence, this);

        else
            return NativeArbreLex.search(this.native_data, utils.longArrayToString(sequence), this);
    }

    /**************************************************************************/
    public SearchResult search(String graphy)
    {
        utils.checkParameterIsAssigned(graphy);

        // Utiliser la fonction de recherche selon de type d'arbre
        if (this.lex_of_long)
            return NativeArbreLex.search(this.native_data, utils.stringToLongArray(graphy), this);

        else
            return NativeArbreLex.search(this.native_data, graphy, this);
    }

    /**************************************************************************/
    public long getSize()
    {
        return NativeArbreLex.getSize(this.native_data);
    }

    /**************************************************************************/
    public void saveToFile(String filename) throws IOException
    {
        utils.checkParameterIsAssigned(filename);

        if (NativeArbreLex.saveToFile(this.native_data, filename + FILE_EXTENSION))
            throw new CantWriteFileException(filename);
    }

    /**************************************************************************/
    public void loadFromFile(String filename) throws IOException
    {
        utils.checkParameterIsAssigned(filename);

        if (NativeArbreLex.loadFromFile(this.native_data, filename + FILE_EXTENSION))
            throw new CantReadFileException(filename);
    }

    /**************************************************************************/
    public void importContents(String filename) throws IOException
    {
        utils.checkParameterIsAssigned(filename);

        if (NativeArbreLex.importContents(this.native_data, filename))
            throw new CantReadFileException(filename);
    }

    /**************************************************************************/
    public void exportContents(String filename) throws IOException
    {
        utils.checkParameterIsAssigned(filename);

        if (NativeArbreLex.exportContents(this.native_data, filename))
            throw new CantWriteFileException(filename);
    }

    /**************************************************************************/
    public void listContents()
    {
        NativeArbreLex.listContents(this.native_data);
    }

    /**************************************************************************/
    public long getNextChar()
    {
        // Rechercher l'indice du prochain noeud fils
        this.position_suivante = NativeArbreLex.getNextSon(this.native_data,
                                                           this.position_courante,
                                                           this.position_suivante);

        if (this.position_suivante == 0)
            return 0;

        else
            // Si ce noeud fils est trouv�, d�coder le caract�re associ�
            return NativeArbreLex.decodeChar(this.native_data, this.position_suivante);
    }

    /**************************************************************************/
    public boolean endOfWord()
    {
        return NativeArbreLex.endOfWord(this.native_data, this.position_courante);
    }

    /**************************************************************************/
    public void goToRoot()
    {
        // Retourner au noeud racine
        this.position_suivante = 0;
        this.position_courante = 0;
    }

    /**************************************************************************/
    public void goToChar()
    {
        // Mettre � jour la position de parcours courante dans l'arbre
        this.position_courante = this.position_suivante;
    }

    /**************************************************************************/
    public int getAlphabetCardinality()
    {
        if (this.lex_of_long)
            return 32; // Type long cod� sur 32 bits

        else
            return 8; // Type caract�re cod� sur 8 bits
    }

    /**************************************************************************/
    public long insert(String graphy)
    {
        final long KEY = this.key;

        insert(KEY, graphy);

        return KEY;
    }

    /**************************************************************************/
    public long insert(long[] sequence)
    {
        final long KEY = this.key;

        insert(KEY, sequence);

        return KEY;
    }
}