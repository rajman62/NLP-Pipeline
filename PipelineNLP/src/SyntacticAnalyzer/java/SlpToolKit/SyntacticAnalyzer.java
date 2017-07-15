package SyntacticAnalyzer.java.SlpToolKit;

import SyntacticAnalyzer.java.SlpToolKit.Exception.MethodNotImplementedException;

/*******************************************************************************
 * Outil d'analyse syntaxique
 ******************************************************************************/
public class SyntacticAnalyzer{
    /**
     * Grammaire utilis�e pour effectuer les op�rations d'analyse
     */
    protected Grammar grammar;

    /***************************************************************************
     * Constructeur d'un analyseur syntaxique. Les analyses effectu�es par
     * cette instance seront effectu�es � partir de la grammaire sp�cifi�e
     * en param�tre.
     *
     * @param grammar Grammaire source
     **************************************************************************/
    public SyntacticAnalyzer(Grammar grammar) {
        utils.checkParameterIsAssigned(grammar);

        this.grammar = grammar;
    }

    /***************************************************************************
     * Effectue une analyse syntaxique de la phrase donn�e en param�tre
     * (la grammaire utilis�e est celle sp�cifi�e au constructeur).
     * La m�thode retourne le r�sultat de l'analyse sous forme d'une nouvelle
     * instance de PChart. Equivalent de la fonction Analyse_Syntaxique du
     * module cyk de SlpToolKit.
     *
     * @param sentence Phrase � analyser
     * @return R�sultat de l'analyse sous forme d'une nouvelle instance de PChart
     **************************************************************************/
    public PChart analyze(String sentence)
    {
        utils.checkParameterIsAssigned(sentence);

        long native_grammaire = this.grammar.rules.native_grammar;
        long native_lexique = this.grammar.lexicon.native_data;

        // Effectue l'analyse syntaxique native
        long retour_lexicalise = NativeCyk.analyseSyntaxique(sentence,
                                                             native_lexique,
                                                             native_grammaire);

        // Cr�ation de la PChart � partir des informations natives collect�es
        return new PChart(this.grammar, sentence, retour_lexicalise);
    }

    /***************************************************************************
     * Effectue une lexicalisation de la phrase donn�e en param�tre
     * (string input) en se basant sur la grammaire (donn� au constructeur) et
     * le lexique qui lui est associ�. Le r�sultat est retourn� sous forme
     * d'une nouvelle instance de PChart. Equivalent de la fonction Lexicalise
     * du module cyk de SlpToolKit.
     *
     * @param sentence Phrase � lexicaliser
     * @return R�sultat de la lexicalisation sous forme d'une nouvelle instance
     * de PChart
     **************************************************************************/
    public PChart lexicalize(String sentence)
    {
        utils.checkParameterIsAssigned(sentence);

        throw new MethodNotImplementedException();

/*        long retour_lexicalise = NativeCyk.lexicalise(sentence,
                                                      this.grammar.lexicon.native_data,
                                                      this.grammar.rules.native_grammar,
                                                      0);

        return null; */
    }

    /***************************************************************************
     * Effectue une tokenisation de la phrase donn�e en param�tre (string input)
     * sans v�rification lexicale (sans correction orthographique).
     * Le r�sultat est retourn� sous forme d'une nouvelle instance de PChart.
     * Equivalent de la fonction Decoupe_Simple du module cyk de SlpToolKit.
     *
     * @param sentence Phrase � tokeniser
     * @return R�sultat de la tokenisation sous forme d'une nouvelle instance de
     * PChart
     **************************************************************************/
    public PChart tokenize(String sentence)
    {
        utils.checkParameterIsAssigned(sentence);

        throw new MethodNotImplementedException();

/*        long retour_lexicalise = NativeCyk.decoupeSimple(sentence,
                                                         this.grammar.lexicon.native_data,
                                                         this.grammar.rules.native_grammar);

        return null; */
    }
}