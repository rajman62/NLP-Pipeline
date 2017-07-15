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
 * Classe d'interface avec le module C cyk
 ******************************************************************************/
public class NativeCyk {
    /***************************************************************************
     * Appel de la fonction Analyse_Syntaxique du module cyk.
     *
     * @param sentence Phrase � analyser
     * @param native_lexicon Pointeur sur la structure C Lexique
     * @param native_grammar Pointeur sur la structure C Grammaire
     * @return Pointeur sur la structure C Retour_lexicalise g�n�r�e
     **************************************************************************/
    public static native long analyseSyntaxique(String sentence, long native_lexicon, long native_grammar);

    /***************************************************************************
     * Appel de la fonction Lexicalise du module cyk.
     *
     * @param sentence Phrase � lexicaliser
     * @param native_lexicon Pointeur sur la structure C Lexique
     * @param native_grammar Pointeur sur la structure C Grammaire
     * @param cost ?
     * @return Pointeur sur la structure C Retour_lexicalise g�n�r�e
     **************************************************************************/
    public static native long lexicalise(String sentence, long native_lexicon, long native_grammar, double cost);

    /***************************************************************************
     * Appel de la fonction Decoupe_Simple du module cyk.
     *
     * @param sentence Phrase � d�couper
     * @param native_lexicon Pointeur sur la structure C Lexique
     * @param native_grammar Pointeur sur la structure C Grammaire
     * @return Pointeur sur la structure C Retour_lexicalise g�n�r�e
     **************************************************************************/
    public static native long decoupeSimple(String sentence, long native_lexicon, long native_grammar);

    /***************************************************************************
     * Appel de la fonction Extrait_Plus_Probable du module cyk.
     *
     * @param native_table_cyk Pointeur sur la structure C Table_CYK
     * @param native_infos_phrase Pointeur sur la structure C Infos_phrase
     * @param i Indice de la ligne de la table CYK � consid�rer
     * @param j Indice de la colonne de la table CYK � consid�rer
     * @param only_p Indique si l'on consid�re uniquement les d�rivations � partir du NT de plus haut niveau
     * @return Pointeur sur la structure C Parsing_Output g�n�r�e
     **************************************************************************/
    public static native long extraitPlusProbable(long native_table_cyk,
                                                  long native_infos_phrase,
                                                  int i,
                                                  int j,
                                                  boolean only_p);// retourne Parsing_Output*

    /***************************************************************************
     * Appel de la fonction Parcours_Cyk_Iteratif du module cyk.
     *
     * @param native_table_cyk Pointeur sur la structure C Table_CYK
     * @param native_infos_phrase Pointeur sur la structure C Infos_phrase
     * @param i Indice de la ligne de la table CYK � consid�rer
     * @param j Indice de la colonne de la table CYK � consid�rer
     * @param native_parsing_output Pointeur sur la structure C Parsing_Output
     * @param native_liste_l1 Pointeur sur la structure C Liste_L1
     * @param only_p Indique si l'on consid�re uniquement les d�rivations � partir du NT de plus haut niveau
     * @return Indique s'il existe encore des d�rivations
     **************************************************************************/
    public static native boolean parcoursCykIteratif(long native_table_cyk,
                                                     long native_infos_phrase,
                                                     int i,
                                                     int j,
                                                     long native_parsing_output,
                                                     long native_liste_l1,
                                                     boolean only_p);

    /***************************************************************************
     * Appel de la fonction RaZ_Parcours_Cyk_Iteratif du module cyk.
     *
     * @param native_table_cyk Pointeur sur la structure C Table_CYK
     * @param native_liste_l1 Pointeur sur la structure C Liste_L1
     **************************************************************************/
    public static native void razParcoursCykIteratif(long native_table_cyk,
                                                     long native_liste_l1);

    /***************************************************************************
     * Retourne la probabilit� de la derni�re d�rivation extraite.
     *
     * @return Probabilit�
     **************************************************************************/
    public static native double getProbability();

    /***************************************************************************
     * Classe des fonctions d'interface propres � la structure C Retour_lexicalise
     **************************************************************************/
    static public class RetourLexicalise {
        /***********************************************************************
         * Lib�re la m�moire occup�e par une structure C Retour_Lexicalise.
         *
         * @param native_retour_lexicalise Pointeur sur la structure � lib�rer
         **********************************************************************/
        public static native void destructor(long native_retour_lexicalise);

        /***********************************************************************
         * Retourne le pointeur sur la structure Table_CYK encapsul�e.
         *
         * @param native_retour_lexicalise Pointeur sur le structure C Retour_lexicalise
         * @return Pointeur sur la structure Table_CYK
         **********************************************************************/
        public static native long getTableCYK(long native_retour_lexicalise);

        /***********************************************************************
         * Retourne le pointeur sur la structure Type_decoupage encapsul�e.
         *
         * @param native_retour_lexicalise Pointeur sur la structure C Retour_lexicalise
         * @return Pointeur sur la structure C Type_decoupage
         **********************************************************************/
        public static native long getDecoupage(long native_retour_lexicalise);
    }

    /***************************************************************************
     * Classe des fonctions d'interface propres � la structure C Table_CYK
     **************************************************************************/
    static public class TableCYK {
        /***********************************************************************
         * M�THODE NON IMPLEMENT�E!
         *
         * @param native_table_cyk -
         * @param native_grammaire -
         * @param nt -
         * @param i -
         * @param j -
         * @return -
         **********************************************************************/
        public static native boolean caseContientNT(long native_table_cyk, long native_grammaire, long nt, int i, int j);

        /***********************************************************************
         * M�THODE NON IMPLEMENT�E!
         *
         * @param native_table_cyk -
         * @param native_grammaire -
         * @param nt -
         * @param i -
         * @param j -
         * @return -
         **********************************************************************/
        public static native boolean caseContientP(long native_table_cyk, long native_grammaire, long nt, int i, int j);

        /***********************************************************************
         * Lib�re l'espace m�moire occup�e par une structure C Table_CYK.
         *
         * @param native_table_cyk Pointeur sur la structure C
         **********************************************************************/
        public static native void destructor(long native_table_cyk);

        /***********************************************************************
         * Retourne le nombre de ligne (colonne) de la table CYK.
         *
         * @param native_table_cyk Pointeur sur la structure C
         * @return Nombre de ligne (colonne)
         **********************************************************************/
        public static native int getSize(long native_table_cyk);

        /***********************************************************************
         * M�THODE NON IMPLEMENT�E!
         *
         * @param native_table_cyk -
         * @param native_infos_phrase -
         * @param i -
         * @param j -
         * @param only_P -
         * @return -
         **********************************************************************/
        public static native long getNumberOfInterpretations(long native_table_cyk, long native_infos_phrase, int i, int j, boolean only_P); // NEW
    }

    /***************************************************************************
     * Classe des fonctions d'interface propres � la structure C Infos_phrase
     **************************************************************************/
    static public class InfosPhrase {
        /***********************************************************************
         * Cr�e une nouvelle structure C Infos_phrase.
         *
         * @param native_grammaire Pointeur sur la structure C Grammaire
         * @param native_lexique Pointeur sur la structure C Lexique
         * @param sentence Phrase
         * @param native_decoupage Pointeur sur la structure Type_decoupage
         * @return Pointeur sur la structure C Infos_phrase cr��e
         **********************************************************************/
        public static native long constructor(long native_grammaire, long native_lexique, String sentence, long native_decoupage); // MODIFIED

        /***********************************************************************
         * Lib�re l'espace m�moire occup�e par une structure C Infos_phrase.
         *
         * @param native_infos_phrase Pointeur sur la structure C Infos_phrase
         **********************************************************************/
        public static native void destructor(long native_infos_phrase);
    }

    /***************************************************************************
     * Classe d'interface avec le module C parsing_output
     **************************************************************************/
    static public class ParsingOutput {
        /***********************************************************************
         * Cr�e une nouvelle structure C Parsing_Output.
         *
         * @return Pointeur sur la structure C Parsing_Output cr��e
         **********************************************************************/
        public static native long constructor();

        /***********************************************************************
         * Retourne la valeur du champ analyse d'une structure C Parsing_Output.
         *
         * @param native_parsing_output Pointeur sur la structure C Parsing_Output
         * @return Analyse
         **********************************************************************/
        public static native String getAnalyse(long native_parsing_output);

        /***********************************************************************
         * Retourne la valeur du champ regles d'une structure C Parsing_Output.
         *
         * @param native_parsing_output Pointeur sur la structure C Parsing_Output
         * @return Liste de r�gles
         **********************************************************************/
        public static native long[] getRulesList(long native_parsing_output);

        /***********************************************************************
         * Lib�re l'espace m�moire occup�e par une structure C Parsing_Output.
         *
         * @param native_parsing_output Pointeur sur la structure C Parsing_Output
         **********************************************************************/
        public static native void destructor(long native_parsing_output);
    }

    /***************************************************************************
     * Classe des fonctions d'interface avec la structure C Liste_L1
     **************************************************************************/
    static public class Liste_L1 {
        /***********************************************************************
         * Cr�e une nouvelle structure C Liste_L1.
         *
         * @return Pointeur sur la structure C Liste_L1 g�n�r�e
         **********************************************************************/
        public static native long constructor();

        /***********************************************************************
         * Lib�re l'espace m�moire occup�e par une structure C Liste_L1.
         *
         * @param native_liste_l1 Pointeur sur la structure C Liste_L1
         **********************************************************************/
        public static native void destructor(long native_liste_l1);
    }

    /***************************************************************************
     * Chargement de la libraire 'NativeCyk.dll'
     **************************************************************************/
    static
    {
        System.loadLibrary("NativeCyk");
    }
}