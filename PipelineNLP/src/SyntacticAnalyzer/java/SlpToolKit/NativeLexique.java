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
 * Classe d'interface avec le module C lexique
 ******************************************************************************/
public class NativeLexique {
    /***************************************************************************
     * Appel de la fonction Suivant_Lexique du module lexique.
     *
     * @param native_lexicon Pointeur sur la structure C Lexique
     * @param native_retour Pointeur sur la structure C Retour_recherche_lex
     **************************************************************************/
    protected static native void lookForNext(long native_lexicon, long native_retour);

    /***************************************************************************
     * Retourne le nombre d'entr�es contenues dans le lexique natif.
     *
     * @param native_lexicon Pointeur sur la structure C Lexique
     * @return Nombre d'entr�es
     **************************************************************************/
    protected static native long getSize(long native_lexicon);

    /***************************************************************************
     * Appel de la fonction Exporte_Lexique du module lexique.
     *
     * @param native_lexicon Pointeur sur la structure C Lexique
     * @param filename Nom du fichier destination
     * @return Retourne vrai en cas de probl�me
     **************************************************************************/
    protected static native boolean exportToASCII(long native_lexicon, String filename);

    /***************************************************************************
     * Appel de la fonction Importe_Lexique du module lexique.
     *
     * @param native_lexicon Pointeur sur la structure C Lexique
     * @param filename Nom du fichier source
     * @return Retourne vrai en cas de probl�me
     **************************************************************************/
    protected static native boolean importFromASCII(long native_lexicon, String filename);

    /***************************************************************************
     * Appel de la fonction Read_Lexique du module lexique.
     *
     * @param native_lexicon Pointeur sur la structure C Lexique
     * @param filename Nom du fichier source
     * @return Retourne vrai en cas de probl�me
     **************************************************************************/
    protected static native boolean loadFromFile(long native_lexicon, String filename);

    /***************************************************************************
     * Appel de la fonction Write_Lexique du module lexique.
     *
     * @param native_lexicon Pointeur sur la structure C Lexique
     * @param filename Nom du fichier destination
     * @return Retourne vrai en cas de probl�me
     **************************************************************************/
    protected static native boolean saveFromFile(long native_lexicon, String filename);

    /***************************************************************************
     * Appel de la fonction Log_Proba du module lexique.
     *
     * @param native_lexicon Pointeur sur la structure C Lexique
     **************************************************************************/
    protected static native void normalize(long native_lexicon);

    /***************************************************************************
     * Cr�� une nouvelle structure Lexique.
     *
     * @return Pointeur sur la structure C Lexique cr��e
     **************************************************************************/
    protected static native long constructor();

    /***************************************************************************
     * Lib�re l'espace m�moire occup�e.
     *
     * @param native_lexicon Pointeur sur la structure C Lexique
     **************************************************************************/
    protected static native void destructor(long native_lexicon);

    /***************************************************************************
     * Retoure le pointeur sur la structure Arbre_lexico encapsul�e par la
     * structure Lexique.
     *
     * @param native_lexicon Pointeur sur la structure C Lexique
     * @return Pointeur
     **************************************************************************/
    protected static native long getArbreLexico(long native_lexicon);

    /***************************************************************************
     * Retourne si la structure Lexique d�fini le champ proba.
     *
     * @param native_lexicon Pointeur sur la structure C Lexique
     * @return Flag bool�en
     **************************************************************************/
    protected static native boolean probabilityDefined(long native_lexicon);

    /***************************************************************************
     * Retourne si la structure Lexique d�fini le champ freq.
     *
     * @param native_lexicon Pointeur sur la structure C Lexique
     * @return Flag bool�en
     **************************************************************************/
    protected static native boolean frequencyDefined(long native_lexicon);

    /***************************************************************************
     * Retourne si la structure Lexique d�fini le champ lemme.
     *
     * @param native_lexicon Pointeur sur la structure C Lexique
     * @return Flag bool�en
     **************************************************************************/
    protected static native boolean lemmeDefined(long native_lexicon);

    /***************************************************************************
     * Retourne si la structure Lexique d�fini le champ morpho.
     *
     * @param native_lexicon Pointeur sur la structure C Lexique
     * @return Flag bool�en
     **************************************************************************/
    protected static native boolean morphoDefined(long native_lexicon);

    /***************************************************************************
     * Appel de la fonction Accede_Lexique du module lexique.
     *
     * @param native_lexicon Pointeur sur la structure C Lexique
     * @param index Indice de l'entr�e d�sir�e
     * @return Pointeur sur la structure Retour_recherche_lex g�n�r�e
     **************************************************************************/
    private static native long internalGetFromIndex(long native_lexicon, long index);

    /***************************************************************************
     * Appel de la fonction Recherche_Lexique du module lexique.
     *
     * @param native_lexicon Pointeur sur la structure C Lexique
     * @param sequence Graphie recherch�e
     * @return Pointeur sur la structure Retour_recherche_lex g�n�r�e
     **************************************************************************/
    private static native long internalLookFor(long native_lexicon, String sequence);

    /***************************************************************************
     * Appel de la fonction Recherche_Lexique_Ulong du module lexique.
     *
     * @param native_lexicon Pointeur sur la structure C Lexique
     * @param sequence S�quence recherch�e
     * @return Pointeur sur la structure Retour_recherche_lex g�n�r�e
     **************************************************************************/
    private static native long internalLookFor(long native_lexicon, long[] sequence);

    /***************************************************************************
     * Appel de la fonction Get_Graphie du module lexique.
     *
     * @param native_lexicon Pointeur sur la structure C Lexique
     * @param index Indice de la graphie
     * @return Graphie
     **************************************************************************/
    protected static native String extractGraphy(long native_lexicon, long index);

    /***************************************************************************
     * Appel de la fonction Acces_Code_Inversion_BdD_Ulong du module lexique.
     *
     * @param native_lexicon Pointeur sur la structure C Lexique
     * @param index Indice de la s�quence
     * @return S�quence
     **************************************************************************/
    protected static native long[] extractSequence(long native_lexicon, long index);

    /***************************************************************************
     * Appel des fonctions Insere_Lexique & Insere_Lexique_Ulong du module lexique.
     *
     * @param native_lexicon Pointeur sur la structure C Lexique
     * @param graphy Graphie ins�r�e
     * @param sequence S�quence ins�r�e
     * @param proba_def Flag indiquant si la probabilit� est d�finie
     * @param proba Probabilit�
     * @param freq_def Flag indiquant si la fr�quence est d�finie
     * @param freq Fr�quence
     * @param lemme_def Flag indiquant si le lemme est d�fini
     * @param lemme Lemme
     * @param morpho_def Flag indiquant si la cat�gorie morpho-syntaxique est d�finie
     * @param morpho Cat�gorie morpho-syntaxique
     * @return Indice de l'entr�e ins�r�e
     **************************************************************************/
    protected static native long insert(long native_lexicon,
                                      String graphy, long[] sequence,
                                      boolean proba_def, double proba,
                                      boolean freq_def, long freq,
                                      boolean lemme_def, long lemme,
                                      boolean morpho_def, long morpho);

    /***************************************************************************
     * Appel des fonctions Dans_Lexique & Dans_Lexique_Ulong du module lexique.
     *
     * @param native_lexicon Pointeur sur la structure C Lexique
     * @param graphy Graphie recherch�e
     * @param sequence S�quence recherch�e
     * @param proba_def Flag indiquant si la probabilit� est d�finie
     * @param proba Probabilit�
     * @param freq_def Flag indiquant si la fr�quence est d�finie
     * @param freq Fr�quence
     * @param lemme_def Flag indiquant si le lemme est d�fini
     * @param lemme Lemme
     * @param morpho_def Flag indiquant si la cat�gorie morpho-syntaxique est d�finie
     * @param morpho Cat�gorie morpho-syntaxique
     * @return Flag bool�en
     **************************************************************************/
    protected static native boolean internalContains(long native_lexicon,
                                  String graphy, long[] sequence,
                                  boolean proba_def, double proba,
                                  boolean freq_def, long freq,
                                  boolean lemme_def, long lemme,
                                  boolean morpho_def, long morpho);

    /***************************************************************************
     * Retourne une nouvelle instance de LexiconEntry contenant les informations
     * de l'entr�e r�f�renc�e par son indice dans le lexique.
     *
     * @param native_lexicon Pointeur sur la structure C Lexique
     * @param source Lexique source
     * @param index Indice de l'entr�e
     * @param entry_class Classe de l'instance cr��e
     * @return Entr�e
     **************************************************************************/
    protected static LexiconEntry getFromIndex(long native_lexicon, LexiconIF source, long index, Class entry_class)
    {
        try {
            long retour = internalGetFromIndex(native_lexicon, index);

            LexiconEntry output = (LexiconEntry)entry_class.newInstance();

            output.set(native_lexicon, retour, source, index, false);

            NativeRetourRechercheLex.destructor(retour);

            return output;
        } catch (Exception e) {e.printStackTrace();};

        return null;
    }

    /***************************************************************************
     * Recherche de la premi�re entr�e dont la s�quence est donn�e en param�tre.
     *
     * @param native_lexicon Pointeur sur la structure C Lexique
     * @param sequence S�quence
     * @param lex_of_long Sp�cifie si le lexique natif est constitu� de s�quences de long ou de char
     * @param source Lexique Java source
     * @param entry_class Classe de l'entr�e de lexique instanci�e
     * @return R�sultat de la recherche
     **************************************************************************/
    private static LexiconEntry lookFor(long native_lexicon, Object sequence, boolean lex_of_long, LexiconIF source, Class entry_class)
    {
        try {
            long retour = 0;

            if (lex_of_long)
                retour = internalLookFor(native_lexicon, (long[])sequence);
            else
                retour = internalLookFor(native_lexicon, (String)sequence);

            LexiconEntry output = (LexiconEntry)entry_class.newInstance();

            if (lex_of_long)
                output.setSequence((long[])sequence);
            else
                output.setGraphy((String)sequence);

            output.set(native_lexicon, retour, source, 0, true);

            return output;
        } catch (Exception e) {e.printStackTrace();};

        return null;
    }

    /***************************************************************************
     * Recherche de la premi�re entr�e dont la s�quence de long correspond � la
     * valeur donn�e en param�tre.
     *
     * @param native_lexicon Pointeur sur la structure C Lexique
     * @param sequence S�quence de long
     * @param source Lexique Java source
     * @param entry_class Classe de l'entr�e de lexique instanci�e
     * @return R�sultat de la recherche
     **************************************************************************/
    protected static LexiconEntry lookFor(long native_lexicon, long[] sequence, LexiconIF source, Class entry_class)
    {
        return lookFor(native_lexicon, sequence, true, source, entry_class);
    }

    /***************************************************************************
     * Recherche de la premi�re entr�e dont la graphie correspond � la valeur
     * donn�e en param�tre.
     *
     * @param native_lexicon Pointeur sur la structure C Lexique
     * @param graphy Graphie
     * @param source Lexique Java source
     * @param entry_class Classe de l'entr�e de lexique instanci�e
     * @return R�sultat de la recherche
     **************************************************************************/
    protected static LexiconEntry lookFor(long native_lexicon, String graphy, LexiconIF source, Class entry_class)
    {
        return lookFor(native_lexicon, graphy, false, source, entry_class);
    }

    /***************************************************************************
     * Recherche de toutes les entr�es dont la s�quence correspond � la valeur
     * donn�e en param�tre.
     *
     * @param native_lexicon Pointeur sur la structure C Lexique
     * @param sequence S�quence recherch�e
     * @param lex_of_long Sp�cifie si le lexique natif est constitu� de s�quences de long ou de char
     * @param source Lexique Java source
     * @param entry_class Classe des entr�es de lexique instanci�es
     * @return R�sultat de la recherche
     **************************************************************************/
    private static LexiconEntry[] lookForAll(long native_lexicon, Object sequence, boolean lex_of_long, LexiconIF source, Class entry_class)
    {
        try {
            List entry_list = new List();
            LexiconEntry temp_entry = lookFor(native_lexicon, sequence, lex_of_long, source, entry_class);

            for(;temp_entry.found(); temp_entry.next())
            {
                LexiconEntry new_entry = (LexiconEntry)temp_entry.clone();

                entry_list.append(new_entry);
            }

            LexiconEntry[] result = new LexiconEntry[(int)entry_list.getSize()];

            for(int index = 0; index < result.length; index++)
                result[index] = (LexiconEntry)entry_list.getFromIndex(index);

            return result;
        } catch (Exception e) {e.printStackTrace();};

        return null;
    }

    /***************************************************************************
     * Recherche de toutes les entr�es dont la s�quence de long correspond � la
     * valeur donn�e en param�tre.
     *
     * @param native_lexicon Pointeur sur la structure C Lexique
     * @param sequence S�quence de long recherch�e
     * @param source Lexique Java source
     * @param entry_class Classe des entr�es de lexique instanci�es
     * @return R�sultat de la recherche
     **************************************************************************/
    protected static LexiconEntry[] lookForAll(long native_lexicon, long[] sequence, LexiconIF source, Class entry_class)
    {
        return lookForAll(native_lexicon, sequence, true, source, entry_class);
    }

    /***************************************************************************
     * Recherche de toutes les entr�es dont la graphie correspond � la valeur
     * donn�e en param�tre.
     *
     * @param native_lexicon Pointeur sur la structure C Lexique
     * @param graphy Graphie recherch�e
     * @param source Lexique Java source
     * @param entry_class Classe des entr�es de lexique instanci�es
     * @return R�sultat de la recherche
     **************************************************************************/
    protected static LexiconEntry[] lookForAll(long native_lexicon, String graphy, LexiconIF source, Class entry_class)
    {
        return lookForAll(native_lexicon, graphy, false, source, entry_class);
    }

    /***************************************************************************
     * Chargement de la libraire 'NativeLexique.dll'
     **************************************************************************/
    static
    {
        System.loadLibrary("NativeLexique");
    }
}