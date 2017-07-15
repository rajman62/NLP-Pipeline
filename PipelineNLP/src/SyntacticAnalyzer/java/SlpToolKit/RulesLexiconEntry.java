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
 * Entr�e d'un lexique impl�mentant l'interface {@link SyntacticAnalyzer.java.SlpToolKit.RulesLexiconIF
 * RulesLexiconIF}.
 ******************************************************************************/
public class RulesLexiconEntry extends LexiconEntry {
    /**
     * Partie gauche de la r�gle sous forme textuelle
     */
    private String left_part;

    /**
     * Partie droite de la r�gle sous forme textuelle
     */
    private String[] right_part;

    /**
     * Indique si l'entr�e recherch�e a �t� trouv�e (valide uniquement en cas de recherche)
     */
    private boolean found;

    /**
     * Grammaire native source (valide uniquement en cas de recherche)
     */
    private long native_grammar = 0;

    /**
     * Liste des indices des entr�es natives trouv�es (valide uniquement en cas de recherche)
     */
    private long[] index = null;

    /**
     * Valeur actuelle de l'indice dans le tableau index (valide uniquement en cas de recherche)
     */
    private int actual_index = 1;

    /***************************************************************************
     * Constructeur d�finissant uniquement la r�gle.
     *
     * @param left_part Partie gauche de la r�gle
     * @param right_part Partie droite de la r�gle
     **************************************************************************/
    public RulesLexiconEntry(String left_part, String[] right_part)
    {
        utils.checkParameterIsAssigned(left_part);
        utils.checkParameterIsAssigned(right_part);

        this.left_part = left_part;
        this.right_part = right_part;
    }

    /***************************************************************************
     * Constructeur d�finissant le champ probabilit�.
     *
     * @param probability Probabilit�
     * @param left_part Partie de gauche de la r�gle
     * @param right_part Partie de droite de la r�gle
     **************************************************************************/
    public RulesLexiconEntry(double probability, String left_part, String[] right_part)
    {
        this(left_part, right_part);

        super.setProbability(probability);
    }

    /***************************************************************************
     * Constructeur prot�g� destin� � la recherche
     *
     * @param left_part Partie gauche
     * @param right_part Partie droite
     * @param found Entr�e trouv�e oui/non
     * @param native_grammar Pointeur sur la structure Grammaire native source
     * @param index Liste des indices des entr�es recherch�es
     **************************************************************************/
    protected RulesLexiconEntry(String left_part, String[] right_part, boolean found, long native_grammar, long[] index)
    {
        this.left_part = left_part;
        this.right_part = right_part;
        this.found = found;
        this.native_grammar = native_grammar;
        this.index = index;
    }

    /***************************************************************************
     * Constructeur prot�g� destin� � la recherche avec probabilit�
     *
     * @param probability Probabilit�
     * @param left_part Partie gauche
     * @param right_part Partie droite
     * @param found Entr�e trouv�e oui/non
     * @param native_grammar Pointeur sur la structure Grammaire native source
     * @param indexs Liste des indices des entr�es recherch�es
     **************************************************************************/
    protected RulesLexiconEntry(double probability, String left_part, String[] right_part, boolean found, long native_grammar, long[] indexs)
    {
        this(left_part, right_part, found, native_grammar, indexs);

        super.setProbability(probability);
    }

    /***************************************************************************
     * Retourne la partie gauche de la r�gle.
     *
     * @return Cha�ne de caract�res contenant la partie gauche de la r�gle
     **************************************************************************/
    public String getLeftPart()
    {
        return this.left_part;
    }

    /***************************************************************************
     * Retourne la partie droite de la r�gle de l'appelant.
     *
     * @return Tableau des cha�nes de caract�res caract�risant la partie
     * droite de la r�gle
     **************************************************************************/
    public String[] getRightPart()
    {
        return this.right_part;
    }

    /**************************************************************************/
    public boolean found()
    {
        // Lever une exception si l'entr�e appelante ne provient pas d'une recherche
        if (this.native_grammar == 0)
            throw new InvalidMethodCallException(NOT_SEARCH_RESULT_ERROR_MESSAGE);

        else
            return this.found;
    }
    /**************************************************************************/
    public void next()
    {
        // Lever une exception si l'entr�e appelante ne provient pas d'une recherche
        if (this.native_grammar == 0)
            throw new InvalidMethodCallException(NOT_SEARCH_RESULT_ERROR_MESSAGE);

        // S'il n'y a plus d'entr�es correspondantes, l'appel de cette m�thode est interdit
        if (this.found)
        {
            // Extraire le champ probabilit� de la prochaine entr�e si elle existe
            if (this.actual_index < this.index.length)
            {
                if (this.probabilityAvailable())
                {
                    long index = this.index[this.actual_index++];
                    double value = NativeGrammaire.getProba(this.native_grammar, index);
                    this.setProbability(value);
                }
            }
            // Sinon, l'entr�e actuelle est la derni�re de la liste
            else
                this.found = false;
        }
        else
            throw new InvalidMethodCallException(NO_MORE_RESULT_ERROR_MESSAGE);
    }

    /**************************************************************************/
    public String toString()
    {
        String output = "('" + this.getLeftPart() + "' -> ";

        String[] right_part = this.getRightPart();

        for(int index = 0; index < right_part.length; index++)
            output += "'" + right_part[index] + "' ";

        if (this.probabilityAvailable())
            output += ", proba=" + this.getProbability();

        return output + ")";
    }
}
