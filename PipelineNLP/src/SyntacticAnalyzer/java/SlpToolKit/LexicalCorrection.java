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
 * Solution d'une correction lexicale (r�sultant de l'appel de la m�thode
 * {@link SlpToolKit.LexicalCorrector#correctWord(String) LexicalCorrector.correctWord(String)}).
 ******************************************************************************/
public class LexicalCorrection {

    /**
     * Poids de la correction
     */
    protected double cost;

    /**
     * Constituants de la correction
     */
    protected LexicalCorrectionPart[] parts;


    /***************************************************************************
     * Constructeur.
     *
     * @param cost Poids
     * @param parts Constituants
     **************************************************************************/
    protected LexicalCorrection(double cost, LexicalCorrectionPart[] parts) {
        this.cost = cost;
        this.parts = parts;
    }

    /***************************************************************************
     * Retourne la valeur du co�t en terme de distance lexicographique entre
     * la solution et le mot � corriger.
     *
     * @return Co�t (distance lexicographique)
     **************************************************************************/
    public double getCost()
    {
        return cost;
    }

    /***************************************************************************
     * Retourne une liste ordonn�e de parties de solution d�signant chacune une
     * entr�e de la m�moire associative lexicale. La notion de liste est ici
     * n�cessaire car une solution peut �tre constitu�e de plusieurs mots.
     *
     * @return Tableau des parties constituant la solution
     **************************************************************************/
    public LexicalCorrectionPart[] getParts()
    {
        return parts;
    }
}