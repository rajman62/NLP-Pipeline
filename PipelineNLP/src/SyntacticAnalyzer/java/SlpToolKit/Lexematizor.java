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

/*******************************************************************************
 * Outil de lex�matisation. Un lex�matiseur permet d'extraire chaque lex�mme
 * formant une phrase donn�e en se basant sur une m�moire contenant un ensemble
 * de mots possibles (classe {@link SyntacticAnalyzer.java.SlpToolKit.LexicalAccessTableIF LexicalAccessTableIF})
 ******************************************************************************/
public class Lexematizor {
    /**
     * Valeur par d�faut du pr�fixe ajout� devant les lex�mes non reconnus
     */
    protected final String DEFAULT_HEAD = "HEAD";

    /**
     * Pr�fixe ajout� devant les lex�mes non reconnus
     */
    protected String head = DEFAULT_HEAD;

    /**
     * Pointeur sur la structure C Arbre_lexico sous-jacente
     */
    protected long arbre_lex = 0;

    /***************************************************************************
     * Constructeur d'un lex�matiseur. La liste des mots reconnus est sp�cifi�e
     * par une table d'acc�s lexicale.
     *
     * @param am Table d'acc�s lexicale utilis�e par le lex�misateur
     * @param head Cha�ne d'ent�te utilis� lors de lex�me inconnu
     **************************************************************************/
    public Lexematizor(LexicalAccessTableIF am, String head)
    {
        utils.checkParameterIsAssigned(am);

        if (head != null)
            this.head = head;

        try {
            if (am.getClass() ==  Class.forName("SlpToolKit.Trie"))
            {
                Trie trie = (Trie)am;
                this.arbre_lex = trie.native_data;
            }
            else
                throw new InvalidMethodCallException("First parameter must be an instance of 'Trie' class");

        }catch (Exception e) {};
    }

   /****************************************************************************
    * Effectue une lex�matisation de la phrase donn�e en param�tre. Retourne un
    * d�coupage en lex�mes de la phrase en utilisant les informations sp�cifi�es
    * au constructeur.
    *
    * @param input Phrase � lex�matiser
    * @return D�coupage en lex�mes de la phrase source
    ***************************************************************************/
    public String[] lexematize(String input)
    {
        utils.checkParameterIsAssigned(input);

        return this.internalLexematize(input);
    }

    /***************************************************************************
     * M�thode de lex�matisation interne.
     *
     * @param input Phrase � lex�matiser
     * @return D�coupage en lex�mes de la phrase source
     **************************************************************************/
    private native String[] internalLexematize(String input);

    /***************************************************************************
     * Chargement de la libraire 'Lexematizor.dll'
     **************************************************************************/
    static
    {
        System.loadLibrary("Lexematizor");
    }
}