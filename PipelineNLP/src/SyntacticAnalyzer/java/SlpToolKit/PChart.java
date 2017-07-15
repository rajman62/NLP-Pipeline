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

import SyntacticAnalyzer.java.SlpToolKit.Exception.InvalidMethodCallException;

/*******************************************************************************
 * Structure de donn�es accueillant une table d'analyse syntaxique CYK
 ******************************************************************************/
public class PChart {
    /**
     * Grammaire source de l'analyse
     */
    protected Grammar grammar;

    /**
     * Pointeur sur la structure C Infos_phrase sous-jacente
     */
    protected long native_infos_phrase;

    /**
     * Pointeur sur la structure C Retour_lexicalise sous-jacente
     */
    protected long native_retour_lexicalise;

    /**
     * NON IMPLEMENT�
     */
    protected ChartElement[][] elements;

    /**
     * Liste L1 native utilis�e pour le parcours it�ratif des interpr�tations
     */
    protected long native_liste_L1;

    /**
     * Parsing_Output natif utilis� pour le parcours it�ratif des interpr�tations
     */
    protected long native_parsing_output;

    /**
     * Flag indiquant si le parcours it�ratif des interpr�tations est termin� ou non
     */
    protected boolean interpretation_available;

    /**
     * Pr�fixe des CMS (num�rique) dans les interpr�tations textuelles
     */
    protected static final String ANALYSE_POS_PREFIX = "[ :";

    /***************************************************************************
     * Constructeur
     *
     * @param grammar Grammaire source
     * @param sentence Phrase analys�e
     * @param native_retour_lexicalise Structure C Retour_lexicalise sous-jacente
     **************************************************************************/
    protected PChart(Grammar grammar, String sentence, long native_retour_lexicalise)
    {
        long native_decoupage = NativeCyk.RetourLexicalise.getDecoupage(native_retour_lexicalise);

        // Cr�er une structure C Infos_phrase
        this.native_infos_phrase = NativeCyk.InfosPhrase.constructor(grammar.rules.native_grammar,
                                                                     grammar.lexicon.native_data,
                                                                     sentence,
                                                                     native_decoupage);

        this.grammar = grammar;
        this.native_retour_lexicalise = native_retour_lexicalise;
        this.native_liste_L1 = NativeCyk.Liste_L1.constructor();
        this.native_parsing_output = NativeCyk.ParsingOutput.constructor();

        this.goToFirstInterpretation();
    }

    /***************************************************************************
     * Destructeur.
     **************************************************************************/
    protected void finalize()
    {
        NativeCyk.InfosPhrase.destructor(this.native_infos_phrase);
        NativeCyk.RetourLexicalise.destructor(this.native_retour_lexicalise);
        NativeCyk.Liste_L1.destructor(this.native_liste_L1);
        NativeCyk.ParsingOutput.destructor(this.native_parsing_output);
    }

    /***************************************************************************
     * Retourne la taille de la PChart, soit son nombre de colonne
     * (�gal � son nombre de ligne).
     *
     * @return Taille
     **************************************************************************/
    public int getSize()
    {
        return NativeCyk.TableCYK.getSize(this.getTableCyk());
    }

    /***************************************************************************
     * Retourne l'interpr�tation la plus probable de la phrase analys�e.
     * Sp�cifier le param�tre (bool only_P) � vrai n'implique alors que les
     * analyses ayant comme racine le symbole de plus haut niveau de la
     * grammaire.
     *
     * @param only_P Analyses de P uniquement oui/non
     * @return Interpr�tation la plus probable
     **************************************************************************/
    public Interpretation getMostProbableInterpretation(boolean only_P)
    {
        // Ex�cute la fonction d'extraction native
        long native_parsing_output = NativeCyk.extraitPlusProbable(this.getTableCyk(),
                                                                   this.native_infos_phrase,
                                                                   this.getSize(),
                                                                   1,
                                                                   only_P);

        // Extrait les informations de l'interpr�tation
        double probability = NativeCyk.getProbability();
        String analyse = NativeCyk.ParsingOutput.getAnalyse(native_parsing_output);
        long[] rules_list = NativeCyk.ParsingOutput.getRulesList(native_parsing_output);

        // Lib�re l'espace m�moire occup�e par la structure native Parsing_Output
        NativeCyk.ParsingOutput.destructor(native_parsing_output);

        // Instancie la nouvelle interpr�tation
        return new Interpretation(probability,
                                  this.processRulesList(rules_list),
                                  this.processAnalyse(analyse));
    }

    /***************************************************************************
     * Retourne la prochaine interpr�tation. Cette m�thode est appel�e de
     * mani�re it�rative et retourne la valeur null quand il n'y a plus
     * d'interpr�tations (il est possible de recommencer le processus en
     * ex�cutant goToFirstInterpretation). Le param�tre (bool only_P) permet
     * ou non de ne prendre en compte que les analyses ayant comme racine le
     * symbole de plus haut niveau de la grammaire.
     *
     * @param only_P Analyses de P uniquement oui/non
     * @return Prochaine interpr�tation
     **************************************************************************/
    public Interpretation getNextInterpretation(boolean only_P)
    {
        // L'ex�cution de cette m�thode est ill�gale s'il n'y a plus d'interpr�tations
        if (this.interpretation_available == false)
            throw new InvalidMethodCallException("no more interpretation available");

        // Ex�cute la fonction d'extraction native
        this.interpretation_available = NativeCyk.parcoursCykIteratif(this.getTableCyk(),
                                                                      this.native_infos_phrase,
                                                                      this.getSize(),
                                                                      1,
                                                                      this.native_parsing_output,
                                                                      this.native_liste_L1,
                                                                      only_P);

        // Extrait les informations de l'interpr�tation
        double probability = NativeCyk.getProbability();
        String analyse = NativeCyk.ParsingOutput.getAnalyse(this.native_parsing_output);
        long[] rules_list = NativeCyk.ParsingOutput.getRulesList(this.native_parsing_output);

        // Instancie la nouvelle interpr�tation
        return new Interpretation(probability,
                                  this.processRulesList(rules_list),
                                  this.processAnalyse(analyse));
    }

    /***************************************************************************
     * Retourne � la toute premi�re interpr�tation contenue dans l'instance
     * appelante (� utiliser en conjonction � la m�thode getNextInterpretation).
     **************************************************************************/
    public void goToFirstInterpretation()
    {
        this.interpretation_available = true;
        long native_table_cyk = this.getTableCyk();

        if (native_table_cyk != 0)
            NativeCyk.razParcoursCykIteratif(native_table_cyk, this.native_liste_L1);
    }

    /***************************************************************************
     * Informe si le parcours it�ratif par getNextInterpretation offre encore
     * une interpr�tation.
     *
     * @return Flag bool�en
     **************************************************************************/
    public boolean interpretationAvailable()
    {
        return  this.interpretation_available;
    }

    /***************************************************************************
     * Retourne le pointeur du la structure C Table_CYK sous-jacente.
     *
     * @return Pointeur
     **************************************************************************/
    private long getTableCyk()
    {
        return NativeCyk.RetourLexicalise.getTableCYK(this.native_retour_lexicalise);
    }

    /***************************************************************************
     * Traite une liste de r�gles formant une interpr�tation du format interne
     * C au format de surface Java.
     *
     * @param input Liste de r�gles au format C
     * @return Liste de r�gles au format Java
     **************************************************************************/
    private static long[] processRulesList(long[] input)
    {
        // Conserve uniquement les valeurs positives de la liste extraite
        List temp_rules_list = new List();
        for(int index = 0; index < input.length; index++)
        {
            long value = input[index];
            if (value > 0)
                temp_rules_list.append(new Long(value-1)); // La num�rotation commence � 1 en interne
        }

        // Conversion List(Long) -> long[]
        long[] output = new long[(int)temp_rules_list.getSize()];

        for(int index = 0; index < output.length; index++)
        {
            Object value = temp_rules_list.getFromIndex((int)index);
            output[index] = ((Long)(value)).longValue();
        }

        return output;
    }

    /***************************************************************************
     * Remplace dans une intepr�tation sous forme textuelle crochet�e les CMS
     * de la forme num�rique � la forme textuelle.
     *
     * @param input Interpr�tation � traiter
     * @return Interpr�tation r�sultante
     **************************************************************************/
    private String processAnalyse(String input)
    {
        String output = "";
        int position = 0;
        int start, finish;
        List pos_list = this.grammar.lexicon.part_of_speech_list;

        // Tant qu'une version num�rique d'une CMS est trouv�e ...
        for(;;position = finish)
        {
            // Extraire la position de la cha�ne de la prochaine CMS num�rique
            start = input.indexOf(ANALYSE_POS_PREFIX, position);
            if (start == -1)
                break;

            start += ANALYSE_POS_PREFIX.length();

            // Extraire la sous-cha�ne de la valeur num�rique enti�re
            String numeric_pos = "";

            finish = start;
            for(;;finish++)
            {
                char c = input.charAt(finish);

                if (Character.isDigit(c) == false)
                    break;
                else
                    numeric_pos = numeric_pos + c;
            }

            int pos_index = Integer.parseInt(numeric_pos);

            // Si la version textuelle de la CMS est connue, proc�der � la substitution
            if (pos_index < pos_list.getSize())
            {
                String pos_str = (String)pos_list.getFromIndex(pos_index);

                output += input.substring(position, start - 1) + pos_str;
            }

            // Sinon, ne pas modifier la cha�ne destination
            else
                output += input.substring(position, finish);

        }

        return output + input.substring(position, input.length());
    }

    /***************************************************************************
     * Classe imbriqu�e ChartElement
     **************************************************************************/
    public class ChartElement {
        /***********************************************************************
         * Constructeur.
         **********************************************************************/
        public ChartElement()
        {
        }

        /***********************************************************************
         * Retourne si oui ou non la case contient le non terminal d�sign� en
         * param�tre (par une cha�ne de caract�res).
         *
         * @param NT Cha�ne de caract�res du non terminal recherch�
         * @return Valeur bool�enne oui/non de la pr�sence du non terminal
         **********************************************************************/
        public boolean containsNT(String NT)
        {
            return false;
        }

        /***********************************************************************
         * Retourne si oui ou non la case contient le symbole g�n�rateur P
         * (non terminal de plus haut niveau).
         *
         * @return Valeur bool�enne oui/non de la pr�sence de P
         **********************************************************************/
        public boolean containsP()
        {
            return false;
        }

        /***********************************************************************
         * Retourne le mot de la phrase analys�e contenu dans la case.
         * La valeur null est retourn�e si la case ne contient aucun mot.
         *
         * @return Cha�ne de caract�re du mot contenu
         **********************************************************************/
        public String getWord()
        {
            return null;
        }

        /***********************************************************************
         * Retourne l'interpr�tation la plus probable de la case appelante. Le
         * param�tre (bool only_P) permet ou non de ne prendre en compte que les
         * analyses ayant pour racine le symbole de plus haut niveau de la
         * grammaire.
         *
         * @param only_P Analyses de P uniquement oui/non
         * @return Interpr�tation la plus probable
         **********************************************************************/
        public Interpretation getMostProbableInterpretation(boolean only_P)
        {
            return null;
        }

        /***********************************************************************
         * Retourne la prochaine interpr�tation de la case appelante. Le param�tre
         * permet ou non de ne consid�rer que les analyses poss�dant comme racine
         * le symbole de plus haut niveau de la grammaire.
         *
         * @param only_P Analyses de P uniquement oui/non
         * @return Prochaine interpr�tation
         **********************************************************************/
        public Interpretation getNextInterpretation(boolean only_P)
        {
            return null;
        }

        /***********************************************************************
         * Retourne le nombre d'interpr�tations existantes pour la case appelante.
         *
         * @return Nombre d'interpr�tations
         **********************************************************************/
        public long getNumberOfInterpretations()
        {
            return 0;
        }
    }
}