package SyntacticAnalyzer.java.SlpToolKit;

/**
 *
 * <p>Title: Projet de semestre</p>
 * <p>Description: Surcouche Java � la librairie SlpToolKit</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Ecole Polytechnique de Lausanne (EPFL) - D�partement d'intelligence arttficielle (LIA)</p>
 * @author Antonin Mer�ay
 * @version 1.0
 */

/*******************************************************************************
 * Interpr�tation syntaxique
 ******************************************************************************/
public class Interpretation {
    /**
     * Probabilit� de l'interpr�tation
     */
    double probability;

    /**
     * Liste de r�gles
     */
    long[] rules_list;

    /**
     * Analyse textuelle sous forme crochet�e
     */
    String analyse;

    /***************************************************************************
     * Constructeur.
     *
     * @param probability Probabilit�
     * @param rules_list Liste de r�gles
     * @param analyse Analyse textuelle crochet�e
     **************************************************************************/
    protected Interpretation(double probability, long[] rules_list, String analyse)
    {
        this.probability = probability;
        this.rules_list = rules_list;
        this.analyse = analyse;
    }

    /***************************************************************************
     * Retourne la liste ordonn�e des r�gles (de haut en bas, de gauche �
     * droite) constituant la d�rivation consid�r�e. Le tableau retourn� est
     * constitu� des indices des r�gles de la grammaire utilis�e.
     *
     * @return Liste ordonn�e des r�gles constituant la d�rivation
     **************************************************************************/
    public long[] getRulesList()
    {
        return this.rules_list;
    }

    /***************************************************************************
     * Retourne la d�rivation consid�r�e sous forme textuelle crochet�e
     * (utilisant les caract�res �[� et �]�).
     *
     * @return Cha�ne de caract�res contenant la d�rivation sous forme crochet�e
     **************************************************************************/
    public String getBracketedInterpretation()
    {
        return this.analyse;
    }

    /***************************************************************************
     * Retourne la probabilit� de l'interpr�tation.
     *
     * @return Probabilit�
     **************************************************************************/
    public double getProbability()
    {
        return this.probability;
    }
}