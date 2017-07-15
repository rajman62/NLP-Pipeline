package SyntacticAnalyzer.java.SlpToolKit;

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
 * R�sultat d'une op�ration de recherche (par la graphie) sur une classe
 * impl�mentant l'interface {@link SyntacticAnalyzer.java.SlpToolKit.LexicalAccessTableIF LexicalAccessTableIF}.
 ******************************************************************************/
public class SearchResult {
    /**
     * Flag indiquant si la recherche a aboutie ou non.
     */
    protected boolean found;

    /**
     * Valeur de la clef correspondant � la s�quence recherch�e
     */
    protected long key;

    /**
     * Indice du dernier caract�re correspondant dans la s�quence recherch�e
     */
    protected int last_match_char_index;

    /**
     * Table d'acc�s dans laquelle a �t� effectu�e la recherche
     */
    protected LexicalAccessTableIF source;

    /**
     * Pointeur sur la structure C sous-jacente Retour_recherche_valeur_lexico
     */
    protected long native_data;

    /**
     * Message d'erreur affich� lors de l'appel de certaines m�thodes quand la
     * recherche n'a pas abouti
     */
    private static String SEARCH_FAILED_ERROR_MESSAGE = "search failed";

    /***************************************************************************
     * Constructeur d'un r�sultat de recherche.
     *
     * @param found Sp�cifie si la graphie recherch�e a �t� trouv�e ou non
     * @param key Clef de l'entr�e
     * @param last_match_char_index Dernier caract�re commun
     * @param source M�moire d'acc�s lexicale source associ�e
     * @param native_data Pointeur � la structure C native
     **************************************************************************/
    protected SearchResult(boolean found, long key, int last_match_char_index, LexicalAccessTableIF source, long native_data)
    {
        this.found = found;
        this.key = key;
        this.last_match_char_index = last_match_char_index;
        this.source = source;
        this.native_data = native_data;
    }

    /***************************************************************************
     * Indique si la graphie recherch�e a �t� trouv�e dans la table d'acc�s lexicale.
     *
     * @return Bool�en indiquant si oui ou non la pr�sence de la graphie recherch�e
     **************************************************************************/
    public boolean found()
    {
        return this.found;
    }

    /***************************************************************************
     * Retourne la valeur de la clef associ�e � l'entr�e trouv�e. L'appel de
     * cette m�thode l�ve une exception si la graphie n'a pas �t� trouv�e
     * (voir {@link #found() found()}).
     *
     * @return la clef associ�e � l'entr�e trouv�e
     **************************************************************************/
    public long getKey()
    {
        if (this.found)
            return this.key;

        else
            throw new InvalidMethodCallException(SEARCH_FAILED_ERROR_MESSAGE);
    }

    /***************************************************************************
     * Retourne la longueur de la plus longue sous-cha�ne commune � la graphie
     * recherch�e trouv�e dans la table d'acc�s.
     *
     * @return Longueur de la sous-chaine
     **************************************************************************/
    public int getLastMatchCharIndex()
    {
        return this.last_match_char_index;
    }

    /***************************************************************************
     * Passe au r�sultat de recherche suivant. Les champs de l'objet appelant
     * sont mis � jour pour refl�ter les informations de la prochaine entr�e de
     * la table d'acc�s lexicale dont la graphie correspond � celle recherch�e.
     * Cette m�thode peut �tre appel�e it�rativement jusqu'� ce que la m�thode
     * {@link #found() found()} retourne false, indiquant qu'aucune autre entr�e n'a
     * �t� trouv�e. Une exception est lev�e si cette m�thode est appel�e alors
     * que l'op�ration {@link #found() found()} retourne false.
     **************************************************************************/
    public void next()
    {
        if (this.found)
        {
            Trie trie = (Trie)this.source;

            NativeRetourRechercheValeurLexico.next(this.native_data, trie.native_data);

            this.found = NativeRetourRechercheValeurLexico.found(this.native_data);

            if (this.found)
                this.key = NativeRetourRechercheValeurLexico.getKey(this.native_data);

        }
        else
            throw new InvalidMethodCallException(SEARCH_FAILED_ERROR_MESSAGE);

    }

    /***************************************************************************
     * Destructeur.
     **************************************************************************/
    protected void finalize()
    {
        NativeRetourRechercheLex.destructor(this.native_data);
    }
}