package SyntacticAnalyzer.java.SlpToolKit;

/**
 * <p>Title: Projet de semestre</p>
 * <p>Description: Surcouche Java � la librairie SlpToolKit</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Ecole Polytechnique de Lausanne (EPFL) - D�partement d'intelligence artificielle (LIA)</p>
 * @author Antonin Mer�ay
 * @version 1.0
 */

import SyntacticAnalyzer.java.SlpToolKit.Exception.*;
import java.lang.Math.*;

/*******************************************************************************
 * Outil de correction lexicale.
 ******************************************************************************/
public class LexicalCorrector {
    /**
     * Code ordinal du mode de correction standard
     */
    protected final int STANDARD_MODE = 0;

    /**
     * Code ordinal du mode de correction pond�r�
     */
    protected final int WEIGHTED_MODE = 1;

    /**
     * Code ordinal du mode de correction s�par�
     */
    protected final int SEPARATED_MODE = 2;

    /**
     * Poids par d�faut d'une correction
     */
    protected final double DEFAULT_WEIGHT = 1.0;

    /**
     * Pointeur sur la structure C Arbre_lexico sous-jacente
     */
    protected long arbre_lex = 0;

    /**
     * Distance maximale de correction
     */
    protected double max_distance = 1.0;

    /**
     * Mode de correction
     */
    protected int correction_mode = STANDARD_MODE;

    /**
     * Poids d'une accentuation/d�saccentuation
     */
    protected double accentuation_weight = DEFAULT_WEIGHT;

    /**
     * Poids de l'insertion/suppression d'un espace
     */
    protected double space_weight = DEFAULT_WEIGHT;

    /**
     * Poids du passage minuscule <-> majuscule
     */
    protected double maj_min_weight = DEFAULT_WEIGHT;

    /**
     * Flag sp�cifiant si toutes les solutions doivent �tre retourn�es
     */
    protected boolean return_all_solutions = true;

    /**
     * Nombre de solutions � retourner
     */
    protected int solutions_to_return = 10;

    /***************************************************************************
     * Constructeur d'un correcteur lexicale. La table d'acc�s lexical contenant
     * la liste des mots corrects est sp�cifi�e en param�tre (dans l'�tat
     * actuel seul les instances de {@link SyntacticAnalyzer.java.SlpToolKit.Trie Trie} sont accept�es).
     *
     * @param am Table d'acc�s lexicale des mots consid�r�s comme corrects
     **************************************************************************/
    public LexicalCorrector(LexicalAccessTableIF am) {
        try {
            if (am.getClass() ==  Class.forName("SlpToolKit.Trie"))
            {
                Trie trie = (Trie)am;
                this.arbre_lex = trie.native_data;
            }
            else
                throw new InvalidMethodCallException("Parameter must be an instance of 'Trie' class");

        }catch (Exception e) {};

    }

    /***************************************************************************
     * D�finit la distance lexicographique maximale autoris�e entre un mot �
     * corriger et une solution candidate.
     *
     * @param distance distance lexicographique maximale
     **************************************************************************/
    public void setMaxDistance(double distance)
    {
        this.max_distance = Math.abs(distance);
    }

    /***************************************************************************
     * Enclenche le mode de correction standard. Seules les op�rations
     * d'insertion, de suppression et de substitution sont utilis�es et elles
     * ont toutes un co�t unitaire.
     **************************************************************************/
    public void setStandardCorrectionMode()
    {
        this.correction_mode = STANDARD_MODE;
    }

    /***************************************************************************
     * Enclenche le mode de correction pond�r�. Equivalent au mode standard
     * augment� des op�rations de r�accentuation, passage en minuscule/majuscule
     * et insertion/suppression d'un caract�re blanc. Les co�ts des op�rations
     * de r�accentuation, de passage en minuscule/majuscule et
     * d'insertion/suppression d'un caract�re blanc sont donn�s en param�tres.
     *
     * @param a Co�t de l'op�ration de r�accentuation
     * @param m Co�t de passage minuscule/majuscule
     * @param s Co�t d'insertion/suppression d'un caract�re blanc
     **************************************************************************/
    public void setWeightedCorrectionMode(double a, double m, double s)
    {
        this.correction_mode = WEIGHTED_MODE;
        this.accentuation_weight = Math.abs(a);
        this.maj_min_weight = Math.abs(m);
        this.space_weight = Math.abs(s);
    }

    /***************************************************************************
     * Enclenche le mode de correction s�par�. Equivalent au mode pond�r� o�
     * l'insertion/suppression d'un caract�re blanc peut intervenir entre
     * plusieurs mots.
     *
     * @param a Co�t de l'op�ration de r�accentuation
     * @param m Co�t de passage minuscule/majuscule
     * @param s Co�t d'insertion/suppression d'un caract�re blanc
     **************************************************************************/
    public void setSeparatedCorrectionMode(double a, double m, double s)
    {
        this.correction_mode = SEPARATED_MODE;
        this.accentuation_weight = Math.abs(a);
        this.maj_min_weight = Math.abs(m);
        this.space_weight = Math.abs(s);
    }

    /***************************************************************************
     * Effectue une correction lexicale du mot sp�cifi� en param�tre.
     * Retourne un tableau de solutions candidates.
     *
     * @param input Mot � corriger
     * @return Tableau des solutions candidates
     **************************************************************************/
    public native LexicalCorrection[] correctWord(String input);

    /***************************************************************************
     * Sp�cifie le nombre maximum de solutions retourn�es lors de corrections.
     * L'appel de la m�thode {@link #correctWord(String) correctWord} retourne
     * au maximum le nombre sp�cifi� de solutions, class�es par ordre croissant
     * de distance lexicographique.
     *
     * @param input Nombre de solutions maximum d�sir�es
     **************************************************************************/
    public void setMaxSolutionNumber(int input)
    {
        this.return_all_solutions = false;
        this.solutions_to_return = Math.abs(input);
    }

    /***************************************************************************
     * Indique de de retourner toutes les solutions possibles lors de corrections.
     * L'appel de la m�thode {@link #correctWord(String) correctWord} retourne
     * toutes solutions possibles jusqu'� la distance lexicographique maximale
     * donn�e.
     **************************************************************************/
    public void returnAllSolutions()
    {
        this.return_all_solutions = true;
    }

    /***************************************************************************
     * Chargement de la libraire 'LexicalCorrector.dll'
     **************************************************************************/
    static
    {
        System.loadLibrary("LexicalCorrector");
    }
}