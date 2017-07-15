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
 * Classe d'interface avec la structure C Retour_recherche_valeur_lexico
 ******************************************************************************/
public class NativeRetourRechercheValeurLexico {
    /***************************************************************************
     * Lib�re l'espace m�moire occup�e par une structure Retour_recherche_valeur_lexico
     *
     * @param native_data Pointeur sur la structure C Retour_recherche_valeur_lexico
     **************************************************************************/
    public static native void destructor(long native_data);

    /***************************************************************************
     * Retourne la valeur du champ recherche.trouve.
     *
     * @param native_data Pointeur sur la structure C Retour_recherche_valeur_lexico
     * @return Valeur du champ recherche.trouve
     **************************************************************************/
    public static native boolean found(long native_data);

    /***************************************************************************
     * Retourne la valeur du champ recherche.indice_chaine
     *
     * @param native_data Pointeur sur la structure C Retour_recherche_valeur_lexico
     * @return Valeur du champ recherche.indice_chaine
     **************************************************************************/
    public static native int getLastMatchChar(long native_data);

    /***************************************************************************
     * Retourne la valeur du champ valeur.
     *
     * @param native_data Pointeur sur la structure C Retour_recherche_valeur_lexico
     * @return Valeur du champ valeur
     **************************************************************************/
    public static native long getKey(long native_data);

    /***************************************************************************
     * Appel de la fonction GetNext_Valeur_Lexico du module arbrelex.
     *
     * @param native_data Pointeur sur la structure C Retour_recherche_valeur_lexico
     * @param arbre_lex Pointeur sur la structure C Arbre_lexico associ�e
     **************************************************************************/
    public static native void next(long native_data, long arbre_lex);

    /***************************************************************************
     * Chargement de la libraire 'NativeRetourRechercheValeurLexico.dll'
     **************************************************************************/
    static
    {
        System.loadLibrary("NativeRetourRechercheValeurLexico");
    }
}