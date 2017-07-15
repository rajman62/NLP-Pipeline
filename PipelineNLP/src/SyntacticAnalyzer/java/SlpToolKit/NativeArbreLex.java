package SyntacticAnalyzer.java.SlpToolKit;

/**
 * <p>Title: Projet de semestre</p>
 * <p>Description: Surcouche Java � la librairie SlpToolKit</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Ecole Polytechnique de Lausanne (EPFL) - D�partement d'intelligence arttficielle (LIA)</p>
 * @author Antonin Mer�ay
 * @version 1.0
 */

/*******************************************************************************
 * Classe d'interface avec le module C arbrelex
 ******************************************************************************/
public class NativeArbreLex {
    /***************************************************************************
     * Constructeur d'une structeur Arbre_lexico.
     *
     * @return Pointeur sur la structure C Arbre_lexico cr��e
     **************************************************************************/
    public static native long constructor();

    /***************************************************************************
     * Destructeur d'une structure Arbre_lexico.
     *
     * @param native_arbre_lex Pointeur sur la structure C Arbre_lexico
     **************************************************************************/
    public static native void destructor(long native_arbre_lex);

    /***************************************************************************
     * Appel de la fonction Insere_Lexico_Ulong du module arbrelex.
     *
     * @param native_arbre_lex Pointeur sur la structure C Arbre_lexico
     * @param key Clef
     * @param sequence S�quence
     * @return Code d'inversion de l'entr�e ins�r�e
     **************************************************************************/
    public static native long insert(long native_arbre_lex, long key, long[] sequence);

    /***************************************************************************
     * Appel de la fonction Insere_Lexico du module arbrelex.
     *
     * @param native_arbre_lex Pointeur sur la structure C Arbre_lexico
     * @param key Clef
     * @param sequence S�quence
     * @return Code d'inversion de l'entr�e ins�r�e
     **************************************************************************/
    public static native long insert(long native_arbre_lex, long key, String sequence);

    /***************************************************************************
     * Retourne le nombre d'entr�e.
     *
     * @param native_arbre_lex Pointeur sur la structure C Arbre_lexico
     * @return Taille
     **************************************************************************/
    public static native long getSize(long native_arbre_lex);

    /***************************************************************************
     * Appel de la fonction Write_Arbre_Lexico du module arbrelex.
     *
     * @param native_arbre_lex Pointeur sur la structure C Arbre_lexico
     * @param filename Nom du fichier destination
     * @return Retourne vrai en cas d'erreur
     **************************************************************************/
    public static native boolean saveToFile(long native_arbre_lex, String filename);

    /***************************************************************************
     * Appel de la fonction Read_Arbre_Lexico du module arbrelex.
     *
     * @param native_arbre_lex Pointeur sur la structure C Arbre_lexico
     * @param filename Nom du fichier source
     * @return Retourne vrai en cas d'erreur
     **************************************************************************/
    public static native boolean loadFromFile(long native_arbre_lex, String filename);

    /***************************************************************************
     * Appel de la fonction Importe_Arbre_Lexico du module arbrelex.
     *
     * @param native_arbre_lex Pointeur sur la structure C Arbre_lexico
     * @param filename Nom du fichier source
     * @return Retourne vrai en cas d'erreur
     **************************************************************************/
    public static native boolean importContents(long native_arbre_lex, String filename);

    /***************************************************************************
     * Appel de la fonction Exporte_Arbre_Lexico du module arbrelex.
     *
     * @param native_arbre_lex Pointeur sur la structure C Arbre_lexico
     * @param filename Nom du fichier destination
     * @return Retourne vrai en cas d'erreur
     **************************************************************************/
    public static native boolean exportContents(long native_arbre_lex, String filename);

    /***************************************************************************
     * Appel de la fonction Liste_Lexico du module arbrelex.
     *
     * @param native_arbre_lex Pointeur sur la structure C Arbre_lexico
     **************************************************************************/
    public static native void listContents(long native_arbre_lex);

    /***************************************************************************
     * Retourne si la position de parcours de l'arbre sp�cifi�e correspond � la
     * fin d'une s�quence stock�e.
     *
     * @param native_arbre_lex Pointeur sur la structure C Arbre_lexico
     * @param position Indice de la position de parcours
     * @return Flag bool�en
     **************************************************************************/
    public static native boolean endOfWord(long native_arbre_lex, long position);

    /***************************************************************************
     * Appel de la fonction Decode_Char du module arbrelex.
     *
     * @param native_arbre_lex Pointeur sur la structure C Arbre_lexico
     * @param position Indice de la position de parcours
     * @return Caract�re retourn�e
     **************************************************************************/
    public static native long decodeChar(long native_arbre_lex, long position);

    /***************************************************************************
     * Appel de la fonction Cherche_Fils du module arbrelex.
     *
     * @param native_arbre_lex Pointeur sur la structure C Arbre_lexico
     * @param father_position Indice de la position de parcours p�re
     * @param son_position Indice de la derni�re position fils visit�e
     * @return Indice de la prochaine position fils
     **************************************************************************/
    public static native long getNextSon(long native_arbre_lex, long father_position, long son_position);

    /***************************************************************************
     * Appel de la fonction GetFirst_Valeur_Lexico du module arbrelex.
     *
     * @param native_arbre_lex Pointeur sur la structure C Arbre_lexico
     * @param sequence S�quence recherch�e
     * @return Pointeur sur la structure C Retour_recherche_valeur_lexico g�n�r�e
     **************************************************************************/
    private static native long internalSearch(long native_arbre_lex, String sequence);

    /***************************************************************************
     * Appel de la fonction GetFirst_Valeur_Lexico_Ulong du module arbrelex.
     *
     * @param native_arbre_lex Pointeur sur la structure C Arbre_lexico
     * @param sequence S�quence recherch�e
     * @return Pointeur sur la structure C Retour_recherche_valeur_lexico g�n�r�e
     **************************************************************************/
    private static native long internalSearch(long native_arbre_lex, long[] sequence);

    /***************************************************************************
     * Appel de la fonction Acces_Code_Inversion_BdD du module arbrelex.
     *
     * @param native_arbre_lex Pointeur sur la structure C Arbre_lexico
     * @param inversion_code Code d'inversion
     * @return Graphie
     **************************************************************************/
    public static native String accessString(long native_arbre_lex, long inversion_code);

    /***************************************************************************
     * Appel de la fonction Acces_Code_Inversion_BdD_Ulong du module arbrelex.
     *
     * @param native_arbre_lex Pointeur sur la structure C Arbre_lexico
     * @param inversion_code Code d'inversion
     * @return S�quence
     **************************************************************************/
    public static native long[] accessSequence(long native_arbre_lex, long inversion_code);

    /***************************************************************************
     * Effectue la recherche d'une cha�ne de caract�re dans une structure Arbre_lexico.
     *
     * @param native_arbre_lex Pointeur sur la structure C Arbre_lexico
     * @param sequence Cha�ne de caract�re recherch�e
     * @param source Table d'acc�s source
     * @return R�sultat de la recherche
     **************************************************************************/
    public static SearchResult search(long native_arbre_lex, String sequence, LexicalAccessTableIF source)
    {
        long retour_recherche_arbre_lexico = internalSearch(native_arbre_lex, sequence);

        long key = NativeRetourRechercheValeurLexico.getKey(retour_recherche_arbre_lexico);
        boolean found = NativeRetourRechercheValeurLexico.found(retour_recherche_arbre_lexico);
        int last_match_char = NativeRetourRechercheValeurLexico.getLastMatchChar(retour_recherche_arbre_lexico);

        return new SearchResult(found, key, last_match_char, source, retour_recherche_arbre_lexico);
    }

    /***************************************************************************
     * Effectue la recherche d'une cha�ne de long dans une structure Arbre_lexico.
     *
     * @param native_arbre_lex Pointeur sur la structure C Arbre_lexico
     * @param sequence Cha�ne de long recherch�e
     * @param source Table d'acc�s source
     * @return R�sultat de la recherche
     **************************************************************************/
    public static SearchResult search(long native_arbre_lex, long[] sequence, LexicalAccessTableIF source)
    {
        long retour_recherche_arbre_lexico = internalSearch(native_arbre_lex, sequence);

        long key = NativeRetourRechercheValeurLexico.getKey(retour_recherche_arbre_lexico);
        boolean found = NativeRetourRechercheValeurLexico.found(retour_recherche_arbre_lexico);
        int last_match_char = NativeRetourRechercheValeurLexico.getLastMatchChar(retour_recherche_arbre_lexico);

        return new SearchResult(found, key, last_match_char, source, retour_recherche_arbre_lexico);
    }

    /***************************************************************************
     * Chargement de la libraire 'NativeArbreLex.dll'
     **************************************************************************/
    static
    {
        System.loadLibrary("NativeArbreLex");
    }
}