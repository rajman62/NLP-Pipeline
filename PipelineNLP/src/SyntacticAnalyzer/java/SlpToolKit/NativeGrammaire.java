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
 * Classe d'interface avec le module Grammaire
 ******************************************************************************/
public class NativeGrammaire {
    /***************************************************************************
     * Appel de la fonction Ajoute_NT du module Grammaire.
     *
     * @param native_grammar Pointeur sur la structure C Grammaire
     * @param nt Non-terminal � ajouter
     * @param native_lexicon Pointeur sur la structure C Lexique associ�e
     * @return Code interne du non-terminal indroduit
     **************************************************************************/
    public static native long ajouteNT(long native_grammar, String nt, long native_lexicon);

    /***************************************************************************
     * Appel de la fonction Ajoute_Regle du module Grammaire.
     *
     * @param native_grammar Pointeur sur la structure C Grammaire
     * @param left_part Partie gauche de la r�gle
     * @param right_part Partie droite de la r�gle
     * @param proba Probabilit� de la r�gle
     * @return Num�ro de la r�gle introduite
     **************************************************************************/
    public static native long ajouteRegle(long native_grammar, long left_part, long[] right_part, double proba);

    /***************************************************************************
     * Cr�er une nouvelle structure C Grammaire.
     *
     * @return Pointeur sur la structure C Grammaire g�n�r�e
     **************************************************************************/
    public static native long constructor();

    /***************************************************************************
     * Lib�re l'espace m�moire occup�e par une structure C Grammaire.
     *
     * @param native_grammar Pointeur sur la structure C Grammaire
     **************************************************************************/
    public static native void destructor(long native_grammar);

    /***************************************************************************
     * Appel de la fonction Write_Grammaire du module Grammaire.
     *
     * @param native_grammar Pointeur sur la structure C Grammaire
     * @param base_filename Nom du fichier destination
     * @param lexique_filename Nom du fichier du lexique associ�
     * @return Retourne vrai en cas de probl�me
     **************************************************************************/
    public static native boolean writeGrammaire(long native_grammar, String base_filename, String lexique_filename);

    /***************************************************************************
     * Appel de la fonction Read_Grammaire du module Grammaire.
     *
     * @param native_grammar Pointeur sur la structure C Grammaire
     * @param base_filename Nom du fichier source
     * @param lexique_filename Nom du fichier du lexique associ�
     * @return Retourne vrai en cas de probl�me
     **************************************************************************/
    public static native boolean readGrammaire(long native_grammar, String base_filename, String lexique_filename);

    /***************************************************************************
     * Appel de la fonction Importe_Grammaire du module Grammaire.
     *
     * @param native_grammar Pointeur sur la structure C Grammaire
     * @param native_lexicon Pointeur sur la structure C Lexique associ�e
     * @param filename Nom du fichier source
     * @param initial Code du non-terminal initial
     * @param probabilistic Indique si la grammaire lue est probabiliste
     * @return Retourne vrai en cas de probl�me
     **************************************************************************/
    public static native boolean importeGrammaire(long native_grammar, long native_lexicon, String filename, long initial, boolean probabilistic);

    /***************************************************************************
     * Appel de la fonction Convert_NT du module Grammaire.
     *
     * @param native_grammar Pointeur sur la structure C Grammaire
     * @param nt Non-terminal � convertir
     * @param native_lexicon Pointeur sur la structure C Lexique associ�e
     * @return Forme de surface du non-terminal
     **************************************************************************/
    public static native String convertNT(long native_grammar, long nt, long native_lexicon);

    /***************************************************************************
     * Appel de la fonction Convert_NT_char  du module Grammaire.
     *
     * @param native_grammar Pointeur sur la structure C Grammaire
     * @param nt Non-terminal � convertir
     * @param native_lexicon Pointeur sur la structure C Lexique associ�e
     * @return Forme interne du non-terminal
     **************************************************************************/
    public static native long convertNTchar(long native_grammar, String nt, long native_lexicon);

    /***************************************************************************
     * Appel de la fonction Exporte_Grammaire du module Grammaire.
     *
     * @param native_grammar Pointeur sur la structure C Grammaire
     * @param native_lexicon Pointeur sur la structure C Lexique associ�e
     * @param filename Nom du fichier destination
     * @return Retourne vrai en cas de probl�me
     **************************************************************************/
    public static native boolean exporteGrammaire(long native_grammar, long native_lexicon, String filename);

    /***************************************************************************
     * Appel de la fonction Regle_vers_Numero du module Grammaire.
     *
     * @param native_grammar Pointeur sur la structure C Grammaire
     * @param left_part Partie gauche de la r�gle
     * @param right_part Partie droite de la r�gle
     * @return Liste des indices de r�gles
     **************************************************************************/
    public static native long[] regleVersNumero(long native_grammar, long left_part, long[] right_part);

    /***************************************************************************
     * Appel de la fonction Numero_vers_Regle du module Grammaire.
     *
     * @param native_grammar Pointeur sur la structure C Grammaire
     * @param index Indice de la r�gle
     * @return Partie gauche de la r�gle
     **************************************************************************/
    public static native long getLeftPart(long native_grammar, long index);

    /***************************************************************************
     * Appel de la fonction Numero_vers_Regle du module Grammaire.
     *
     * @param native_grammar Pointeur sur la structure C Grammaire
     * @param index Indice de la r�gle
     * @return Partie droite de la r�gle
     **************************************************************************/
    public static native long[] getRightPart(long native_grammar, long index);

    /***************************************************************************
     * Appel de la fonction Numero_vers_Regle du module Grammaire.
     *
     * @param native_grammar Pointeur sur la structure C Grammaire
     * @param index Indice de la r�gle
     * @return Probabilit� de la r�gle
     **************************************************************************/
    public static native double getProba(long native_grammar, long index);

    /***************************************************************************
     * Retourne le nombre de r�gles contenues.
     *
     * @param native_grammar Pointeur sur la structure C Grammaire
     * @return Nombre de r�gle
     **************************************************************************/
    public static native long getSize(long native_grammar);

    /***************************************************************************
     * Retourne le pointeur sur la structure C Lexique encapsul�e par la Grammaire.
     *
     * @param native_grammar Pointeur sur la structure C Grammaire
     * @return Pointeur
     **************************************************************************/
    public static native long getNativeRulesLexicon(long native_grammar);

    /***************************************************************************
     * Retourne le pointeur sur la structure C Arbre_lexico encapsul�e par la
     * structure Lexique encapsul�e par la structure Grammaire.
     *
     * @param native_grammar Pointeur sur la structure C Grammaire
     * @return Pointeur
     **************************************************************************/
    public static native long getNativeArbreLex(long native_grammar);

    /***************************************************************************
     * D�finit la valeur du champ valeur_initiale.
     *
     * @param native_grammar Pointeur sur la structure C Grammaire
     * @param initial_nt Valeur du champ valeur_initiale
     **************************************************************************/
    public static native void setInitialNT(long native_grammar, long initial_nt);

    /***************************************************************************
     * Chargement de la libraire 'NativeGrammaire.dll'
     **************************************************************************/
    static
    {
        System.loadLibrary("NativeGrammaire");
    }
}