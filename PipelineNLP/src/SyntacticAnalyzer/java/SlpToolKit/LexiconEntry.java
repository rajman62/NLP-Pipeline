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
 * Entr�e g�n�rique de lexique (voir {@link SlpToolKit.LexiconIF LexiconIF}).
 ******************************************************************************/
public abstract class LexiconEntry {
    /**
     * Index de l'entr�e dans le lexique natif (utilis� pour extraire la
     * s�quence/graphie apr�s ex�cution de getEntryFromIndex).
     */
    private long index;

    /**
     * Indique si l'entr�e recherch�e a �t� trouv�e dans le lexique (champ
     * utilis� uniquement en cas de retour de recherche).
     */
    private boolean found;

    /**
     * Lexique d'o� provient l'entr�e (champ utilis� uniquement en cas de retour
     * de recherche).
     */
    private LexiconIF source = null;

    /**
     * Champ entier associ� � l'entr�e de lexique.
     */
    private long frequency;

    /**
     * Champ flottant associ� � l'entr�e de lexique.
     */
    private double probability;

    /**
     * Champ lemme (indice sur l'entr�e m�re).
     */
    private long lemme;

    /**
     * Graphie de l'entr�e (en cas de lexique natif d'octets).
     */
    private String graphy = null;

    /**
     * Graphie de l'entr�e (en cas de lexique de long).
     */
    private long[] sequence = null;

    /**
     * Champ d'indice de la cat�gorie morpho-syntaxique associ�e � l'entr�e du lexique.
     */
    private long part_of_speech_long;

    /**
     * Flag indiquant si le champ cat�gorie morpho-syntaxique est assign�.
     */
    private boolean part_of_speech_defined = false;

    /**
     * Flag indiquant si le champ lemme est assign�.
     */
    private boolean lemme_defined = false;

    /**
     * Flag indiquant si le champ fr�quence est assign�.
     */
    private boolean frequency_defined = false;

    /**
     * Flag indiquant si le champ probabilit� est assign�.
     */
    private boolean probability_defined = false;

    /**
     * Pointeur sur la structure de donn�e native Retour_recherche_lex sous-jacente
     * (ce champ n'est utilis� quand cas de retour de recherche).
     */
    private long native_data = 0;

    /**
     * Message d'erreur affich�e durant l'appel ill�gal d'une m�thode quand
     * l'entr�e appelante ne provient pas d'une op�ration de recherche
     */
    protected static String NOT_SEARCH_RESULT_ERROR_MESSAGE = "entry is not a search result";

    /**
     * Message d'erreur affich� durant l'appel ill�gal d'une m�thode quand plus
     * aucun r�sultat n'est disponible
     */
    protected static String NO_MORE_RESULT_ERROR_MESSAGE = "no more search result available";

    /***************************************************************************
     * D�finit la fr�quence associ�e � l'entr�e.
     *
     * @param frequency Valeur de la fr�quence
     **************************************************************************/
    protected void setFrequency(long frequency)
    {
        this.frequency_defined = true;
        this.frequency = frequency;
    }

    /***************************************************************************
     * D�finit la probabilit� associ�e � l'entr�e
     *
     * @param probability Valeur de la probabilit�
     **************************************************************************/
    protected void setProbability(double probability)
    {
        this.probability_defined = true;
        this.probability = probability;
    }

    /***************************************************************************
     * Retourne la fr�quence associ�e � l'entr�e. Une exception est lev�e si
     * ce champ n'a pas �t� d�fini.
     *
     * @return Valeur de la fr�quence
     **************************************************************************/
    public long getFrequency()
    {
        if (this.frequency_defined)
            return this.frequency;

        else
            throw new FieldNotDefinedException();
    }

    /***************************************************************************
     * Retourne si le champ fr�quence a �t� d�fini ou non.
     *
     * @return Valeur bool�enne oui/non
     **************************************************************************/
    public boolean frequencyAvailable()
    {
        return this.frequency_defined;
    }

    /***************************************************************************
     * Retourne la probabilit� associ�e � l'entr�e. Une exception est lev�e si
     * ce champ n'a pas �t� d�fini.
     *
     * @return Valeur de la probabilit�
     **************************************************************************/
    public double getProbability()
    {
        if (this.probability_defined)
            return this.probability;

        else
            throw new FieldNotDefinedException();
    }

    /***************************************************************************
     * Retourne si le champ probabilit� a �t� d�fini ou non.
     *
     * @return Valeur bool�enne oui/non
     **************************************************************************/
    public boolean probabilityAvailable()
    {
        return this.probability_defined;
    }

    /***************************************************************************
     * Destructeur explicite.
     **************************************************************************/
    protected void finalize()
    {
        // Lib�re la m�moire allou�e � la structure de donn�es native
        if (this.native_data != 0)
            NativeRetourRechercheLex.destructor(this.native_data);
    }

    /***************************************************************************
     * Retourne si l'entr�e appelante satisfait au crit�re de recherche.
     * Cette m�thode n'a de sens que dans le cas d'un retour de la m�thode
     * {@link SlpToolKit.LexiconIF#lookFor(Object) lookFor()}. Dans le cas
     * contraire, une exception est lev�e.
     *
     * @return Valeur bool�enne oui/non
     **************************************************************************/
    public boolean found()
    {
        if (this.source != null)
            return this.found;

        else
            throw new InvalidMethodCallException(NOT_SEARCH_RESULT_ERROR_MESSAGE);
    }

    /***************************************************************************
     * Passe � l'entr�e recherch�e suivante. Les champs de l'objet appelant sont
     * mis � jour pour refl�ter les informations de la prochaine entr�e du lexique
     * correspondant au crit�re de recherche. Cette m�thode n'a de sens que dans
     * le cas d'un retour de la m�thode {@link SlpToolKit.LexiconIF#lookFor(Object) lookFor()}
     * et peut �tre appel�e it�rativement jusqu'� ce que la m�thode {@link #found() found()}
     * retourne false. Une exception est lev�e si cette m�thode est appel�e alors
     * que l'op�ration {@link #found() found()} retourne false.
     **************************************************************************/
    public void next()
    {
        if (this.native_data == 0)
            throw new InvalidMethodCallException(NOT_SEARCH_RESULT_ERROR_MESSAGE);

        // Lever une exception si l'ex�cution de cette m�thode est inad�quate
        if (this.found)
        {
            // Extraire le lexique source
            LexiconIF lexicon = (LexiconIF)this.source;
            long native_lexicon = lexicon.getNativeLexicon();

            // Rechercher l'entr�e suivante correspondante aux crit�res de recherche
            NativeLexique.lookForNext(native_lexicon, this.native_data);

            // Mettre � jour les champs de l'entr�e
            this.found = NativeRetourRechercheLex.found(this.native_data);

            if (this.found)
            {
                this.frequency = NativeRetourRechercheLex.getFreq(this.native_data);
                this.probability = NativeRetourRechercheLex.getProba(this.native_data);
                this.part_of_speech_long = NativeRetourRechercheLex.getMorpho(this.native_data);
                this.lemme = NativeRetourRechercheLex.getLemme(this.native_data);
            }
        }
        else
            throw new InvalidMethodCallException(NO_MORE_RESULT_ERROR_MESSAGE);
    }

    /***************************************************************************
     * Assigne le champ lemme.
     *
     * @param lemme Valeur du lemme
     **************************************************************************/
    protected void setLemme(long lemme)
    {
        this.lemme = lemme;
        this.lemme_defined = true;
    }

     /**************************************************************************
     * Assigne le champ cat�gorie morpho-syntaxique.
     *
     * @param pos Valeur de la cat�gorie morpho-syntaxique
     **************************************************************************/
    protected void setPartOfSpeech(long pos)
    {
        this.part_of_speech_long = pos;
        this.part_of_speech_defined = true;
    }

     /**************************************************************************
     * Retourne si le champ cat�gorie morpho-syntaxique est assign� ou non
     *
     * @return Valeur bool�enne oui/non
     **************************************************************************/
    protected boolean partOfSpeechAvailable()
    {
        return this.part_of_speech_defined;
    }

    /***************************************************************************
     * Retourne la cat�gorie morpho-syntaxique associ�e � l'entr�e.
     * Une exception est lev�e si ce champ n'a pas �t� d�fini.
     *
     * @return Valeur de la cat�gorie morpho-syntaxique
     **************************************************************************/
    protected long getPartOfSpeech()
    {
        if (this.part_of_speech_defined)
            return this.part_of_speech_long;

        else
            throw new FieldNotDefinedException();
    }

    /***************************************************************************
     * Retoure si le champ lemme est assign� ou non.
     *
     * @return Valeur bool�enne oui/non
     **************************************************************************/
    protected boolean lemmeAvailable()
    {
        return this.lemme_defined;
    }

    /***************************************************************************
     * Retourne le lemme associ� � l'entr�e. Une exception est lev�e si ce champ
     * n'a pas �t� d�fini.
     *
     * @return Valeur du lemme
     **************************************************************************/
    protected long getLemme()
    {
        if (this.lemme_defined)
            return this.lemme;

        else
            throw new FieldNotDefinedException();
    }

    /***************************************************************************
     * Retourne une nouvelle instance d'entr�e, copie de l'appelante.
     *
     * @return Nouvelle instance
     **************************************************************************/
    public Object clone()
    {
        try {
            LexiconEntry result = (LexiconEntry)this.getClass().newInstance();

            result.found = this.found;
            result.frequency = this.frequency;
            result.frequency_defined = this.frequency_defined;
            result.graphy = this.graphy;
            result.lemme = this.lemme;
            result.lemme_defined = this.lemme_defined;
            result.native_data = 0;
            result.part_of_speech_defined = this.part_of_speech_defined;
            result.part_of_speech_long = this.part_of_speech_long;
            result.probability = this.probability;
            result.probability_defined = this.probability_defined;
            result.sequence = this.sequence;
            result.source = this.source;

            return result;
        } catch (Exception e) {e.printStackTrace();};

        return null;
    }

    /***************************************************************************
     * Assigne le champ graphie.
     *
     * @param graphy Valeur du champ graphie
     **************************************************************************/
    protected void setGraphy(String graphy)
    {
        utils.checkParameterIsAssigned(graphy);
        this.graphy = graphy;
    }

    /***************************************************************************
     * Assigne le champ s�quence
     *
     * @param sequence Valeur du champ s�quence
     **************************************************************************/
    protected void setSequence(long[] sequence)
    {
        utils.checkParameterIsAssigned(sequence);
        this.sequence = sequence;
    }

    /***************************************************************************
     * Ins�re l'entr�e appelante dans un lexique SlpTookKit natif.
     *
     * @param native_lexicon Pointeur sur le lexique natif
     * @return Indice assign� de l'entr�e ins�r�e
     **************************************************************************/
    protected long insertIntoNativeLexicon(long native_lexicon)
    {
        final boolean FIRST_INSERT = (NativeLexique.getSize(native_lexicon) == 0);
        final boolean PROB_IN_LEX = NativeLexique.probabilityDefined(native_lexicon);
        final boolean FREQ_IN_LEX = NativeLexique.frequencyDefined(native_lexicon);
        final boolean POS_IN_LEX = NativeLexique.morphoDefined(native_lexicon);
        final boolean LEMME_IN_LEX = NativeLexique.lemmeDefined(native_lexicon);

        boolean probability_defined_copy = this.probability_defined;
        boolean frequency_defined_copy = this.frequency_defined;
        boolean part_of_speech_defined_copy = this.part_of_speech_defined;
        boolean lemme_defined_copy = this.lemme_defined;

        // S'il ne s'agit plu de la premi�re entr�e:
        // * Lever une exception si un champ d�fini dans le lexique n'est pas d�fini par l'entr�e ins�r�e
        // * Afficher un warning si un champ non d�fini dans le lexique est d�fini par l'entr�e ins�r�e
        if (!FIRST_INSERT)
        {
            if (PROB_IN_LEX && !probability_defined_copy)
                throw new FieldRequiredException("probability");

            if (!PROB_IN_LEX && probability_defined_copy)
            {
                printFieldNotManagedWarning("probability");
                probability_defined_copy = false;
            }

            if (FREQ_IN_LEX && !frequency_defined_copy)
                throw new FieldRequiredException("frequency");

            if (!FREQ_IN_LEX && frequency_defined_copy)
            {
                printFieldNotManagedWarning("frequency");
                frequency_defined_copy = false;
            }

            if (POS_IN_LEX && !part_of_speech_defined_copy)
                throw new FieldRequiredException("part of speech");

            if (!POS_IN_LEX && part_of_speech_defined_copy)
            {
                printFieldNotManagedWarning("part of speech");
                part_of_speech_defined_copy = false;
            }

            if (LEMME_IN_LEX && !lemme_defined_copy)
                throw new FieldRequiredException("lemme");

            if (!LEMME_IN_LEX && lemme_defined_copy)
            {
                printFieldNotManagedWarning("lemme");
                lemme_defined_copy = false;
            }
        }

        // Insertion dans le lexique natif
        return NativeLexique.insert(native_lexicon,
                                    this.graphy, this.sequence,
                                    probability_defined_copy, this.probability,
                                    frequency_defined_copy, this.frequency,
                                    lemme_defined_copy, this.lemme,
                                    part_of_speech_defined_copy, this.part_of_speech_long);
    }

    /***************************************************************************
     * V�rifie la pr�sence l'une entr�e similaire � celle appelante dans le
     * lexique SlpToolKit natif donn� en param�tre.
     *
     * @param native_lexicon Pointeur sur le lexique natif
     * @return Oui/non une entr�e similaire est contenue dans le lexique natif
     **************************************************************************/
    protected boolean containedIntoNativeLexicon(long native_lexicon)
    {
        final boolean PROB_IN_LEX = NativeLexique.probabilityDefined(native_lexicon);
        final boolean FREQ_IN_LEX = NativeLexique.frequencyDefined(native_lexicon);
        final boolean POS_IN_LEX = NativeLexique.morphoDefined(native_lexicon);
        final boolean LEMME_IN_LEX = NativeLexique.lemmeDefined(native_lexicon);

        boolean probability_defined_copy = this.probability_defined;
        boolean frequency_defined_copy = this.frequency_defined;
        boolean part_of_speech_defined_copy = this.part_of_speech_defined;
        boolean lemme_defined_copy = this.lemme_defined;

        // Afficher un warning si un champ non d�fini dans le lexique natif est sp�cifi� par l'entr�e
        if (!PROB_IN_LEX && probability_defined_copy)
        {
            this.printFieldNotManagedWarning("probability");
            probability_defined_copy = false;
        }

        if (!FREQ_IN_LEX && frequency_defined_copy)
        {
            this.printFieldNotManagedWarning("frequency");
            frequency_defined_copy = false;
        }

        if (!POS_IN_LEX && part_of_speech_defined_copy)
        {
            this.printFieldNotManagedWarning("part of speech");
            part_of_speech_defined_copy = false;
        }

        if (!LEMME_IN_LEX && lemme_defined_copy)
        {
            this.printFieldNotManagedWarning("lemme");
            lemme_defined_copy = false;
        }

        // Test dans le lexique natif
        return NativeLexique.internalContains(native_lexicon,
                                              this.graphy, this.sequence,
                                              probability_defined_copy, this.probability,
                                              frequency_defined_copy, this.frequency,
                                              lemme_defined_copy, this.lemme,
                                              part_of_speech_defined_copy, this.part_of_speech_long);
    }

    /***************************************************************************
     * Retourne le lexique source. Une exception est lev�e si ce champ n'est pas
     * d�fini.
     *
     * @return Lexique source
     **************************************************************************/
    protected LexiconIF getSource()
    {
        if (this.source == null)
            throw new FieldNotDefinedException();

        return this.source;
    }

    /***************************************************************************
     * Retourne la s�quence associ�e de l'entr�e.
     *
     * @return S�quence
     **************************************************************************/
    protected long[] getSequence()
    {
        // Extraire la s�quence dans le lexique natif source si n�cessaire
        if (this.sequence == null)
            this.sequence = NativeLexique.extractSequence(this.source.getNativeLexicon(), this.index);

        return this.sequence;
    }

    /***************************************************************************
     * Retourne la graphie associ�e � l'entr�e.
     *
     * @return Graphie
     **************************************************************************/
    protected String getString()
    {
        // Extraire la graphie dans le lexique natif source si n�cessaire
        if (this.graphy == null)
            this.graphy = NativeLexique.extractGraphy(this.source.getNativeLexicon(), this.index);

        return this.graphy;
    }

    /***************************************************************************
     * Assigne les champs de l'entr�e appelante selon les param�tres fournis.
     *
     * @param native_lexicon Pointeur sur le lexique natif source
     * @param native_retour Pointeur sur la structure Retour_recherche_lex sous-jacente source
     * @param source Lexique source
     * @param index Indice de l'entr�e dans le lexique natif
     * @param got_native Sp�cifie si l'entr�e conserve une r�f�rence sur la structure Retour_recherche_lex
     **************************************************************************/
    protected void set(long native_lexicon, long native_retour, LexiconIF source, long index, boolean got_native)
    {
        this.source = source;
        this.index = index;
        this.found = NativeRetourRechercheLex.found(native_retour);

        // Assigner la probabilit� - optionnelle
        if (NativeLexique.probabilityDefined(native_lexicon))
            this.setProbability(NativeRetourRechercheLex.getProba(native_retour));

        // Assigner la fr�quence - optionnelle
        if (NativeLexique.frequencyDefined(native_lexicon))
            this.setFrequency(NativeRetourRechercheLex.getFreq(native_retour));

        // Assigner le lemme - optionnelle
        if (NativeLexique.lemmeDefined(native_lexicon))
            this.setLemme(NativeRetourRechercheLex.getLemme(native_retour));

        // Assigner la cat�gorie morpho-syntaxique - optionnelle
        if (NativeLexique.morphoDefined(native_lexicon))
            this.setPartOfSpeech(NativeRetourRechercheLex.getMorpho(native_retour));

        if (got_native)
            this.native_data = native_retour;
    }

    /***************************************************************************
     * Affichage d'un message d'avertissement informant la non gestion d'un champ
     *
     * @param field Champ non g�r�
     **************************************************************************/
    protected static void printFieldNotManagedWarning(String field)
    {
        utils.printWarning("'" + field + "' field not managed");
    }

}