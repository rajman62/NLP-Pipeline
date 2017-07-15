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
 * Classe d'interface avec la structure Retour_recherche_lex
 ******************************************************************************/
public class NativeRetourRechercheLex {
    /***************************************************************************
     * Retourne la valeur du champ proba.
     *
     * @param native_data Pointeur sur la structure C Retour_recherche_lex
     * @return Valeur du champ proba
     **************************************************************************/
    public static native double getProba(long native_data);

    /***************************************************************************
     * Retourne la valeur du champ freq.
     *
     * @param native_data Pointeur sur la structure C Retour_recherche_lex
     * @return Valeur du champ freq
     **************************************************************************/
    public static native long getFreq(long native_data);

    /***************************************************************************
     * Retourne la valeur du champ lemme.
     *
     * @param native_data Pointeur sur la structure C Retour_recherche_lex
     * @return Valeur du champ lemme
     **************************************************************************/
    public static native long getLemme(long native_data);

    /***************************************************************************
     * Retourne la valeur du champ morpho.
     *
     * @param native_data Pointeur sur la structure C Retour_recherche_lex
     * @return Valeur du champ morpho
     **************************************************************************/
    public static native long getMorpho(long native_data);

    /***************************************************************************
     * Lib�re l'espace m�moire occup� par une structure Retour_recherche_lex.
     *
     * @param native_data Pointeur sur la structure C Retour_recherche_lex
     **************************************************************************/
    public static native void destructor(long native_data);

    /***************************************************************************
     * Retourne la valeur du champ trouve.
     *
     * @param native_data Pointeur sur la structure C Retour_recherche_lex
     * @return Valeur du champ trouve
     **************************************************************************/
    public static native boolean found(long native_data);

    /***************************************************************************
     * Chargement de la libraire 'NativeRetourRechercheLex.dll'
     **************************************************************************/
    static
    {
        System.loadLibrary("NativeRetourRechercheLex");
    }
}