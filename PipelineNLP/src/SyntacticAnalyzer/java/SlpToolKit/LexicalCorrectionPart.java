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
 * Partie de correction constituant une instance {@link SlpToolKit.LexicalCorrection LexicalCorrection}.
 ******************************************************************************/
public class LexicalCorrectionPart {

    /**
     * Indice de la position de la partie de correction dans la chaine de
     * caract�res � corriger
     */
    protected int string_position;

    /**
     * Graphie de la partie de correction
     */
    protected String graphy;

    /***************************************************************************
     * Constructeur
     * @param string_position Position dans la cha�ne de caract�res originale
     * @param graphy Graphie de la partie de correction
     **************************************************************************/
    protected LexicalCorrectionPart(int string_position, String graphy) {
        this.string_position = string_position;
        this.graphy = graphy;
    }

    /***************************************************************************
     * Retourne la position dans la cha�ne de caract�re de la partie de
     * correction.
     *
     * @return Position
     **************************************************************************/
    public int getStringPosition()
    {
        return string_position;
    }

    /***************************************************************************
     * Retourne la graphie de la partie de correction.
     *
     * @return Graphie
     **************************************************************************/
    public String getGraphy()
    {
        return graphy;
    }
}