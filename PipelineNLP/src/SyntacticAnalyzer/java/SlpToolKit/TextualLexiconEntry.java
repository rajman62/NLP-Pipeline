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
 * Entr�e d'un lexique impl�mentant l'interface {@link SlpToolKit.TextualLexiconIF
 * TextualLexiconIF}.
 ******************************************************************************/
public class TextualLexiconEntry extends LexiconEntry {
    /**
     * Cat�gorie morpho-syntaxique de l'entr�e sous forme textuelle
     */
    private String part_of_speech_str = null;

    /***************************************************************************
     * Constructeur vide (n�cessaire pour les acc�s au lexique)
     **************************************************************************/
    protected TextualLexiconEntry() {}

    /***************************************************************************
     * Constructeur d�finissant la graphie uniquement.
     *
     * @param graphy Graphie
     **************************************************************************/
    public TextualLexiconEntry(String graphy)
    {
        setGraphy(graphy);
    }

    /***************************************************************************
     * Constructeur d�finissant le champ fr�quence.
     *
     * @param graphy Graphie
     * @param frequency Fr�quence
     **************************************************************************/
    public TextualLexiconEntry(String graphy, int frequency)
    {
        this(graphy);

        setFrequency(frequency);
    }

    /***************************************************************************
     * Constructeur d�finissant le champ probabilit�.
     *
     * @param graphy Graphie
     * @param probability Probabilit�
     **************************************************************************/
    public TextualLexiconEntry(String graphy, double probability)
    {
        this(graphy);

        setProbability(probability);
    }

    /***************************************************************************
     * Constructeur d�finissant les champs probabilit� et fr�quence.
     *
     * @param graphy Graphie
     * @param probability Probabilit�
     * @param frequency Fr�quence
     **************************************************************************/
    public TextualLexiconEntry(String graphy, double probability, int frequency)
    {
        this(graphy);

        setFrequency(frequency);
        setProbability(probability);
    }

    /***************************************************************************
     * Constructeur d�finissant la cat�gorie morpho-syntaxique.
     *
     * @param graphy Graphie
     * @param part_of_speech Cat�gorie morpho-syntaxique
     **************************************************************************/
    public TextualLexiconEntry(String graphy, String part_of_speech)
    {
        this(graphy);

        this.setPartOfSpeech(part_of_speech);
    }

    /***************************************************************************
     * Constructeur d�finissant les champs cat�gorie morpho-syntaxique et fr�quence.
     *
     * @param graphy Graphie
     * @param part_of_speech Cat�gorie morpho-syntaxique
     * @param frequency Fr�quence
     **************************************************************************/
    public TextualLexiconEntry(String graphy, String part_of_speech, int frequency)
    {
        this(graphy, frequency);

        this.setPartOfSpeech(part_of_speech);
    }

    /***************************************************************************
     * Constructeur d�finissant les champs cat�gorie morpho-syntaxique et probabilit�.
     *
     * @param graphy Graphie
     * @param part_of_speech Cat�gorie morpho-syntaxique
     * @param probability Probabilit�
     **************************************************************************/
    public TextualLexiconEntry(String graphy, String part_of_speech, double probability)
    {
        this(graphy, probability);

        this.setPartOfSpeech(part_of_speech);
    }

    /***************************************************************************
     * Constructeur d�finissant les champs cat�gorie morpho-syntaxique, probabilit�
     * et fr�quence.
     *
     * @param graphy Graphie
     * @param part_of_speech Cat�gorie morpho-syntaxique
     * @param probability Probabilit�
     * @param frequency Fr�quence
     **************************************************************************/
    public TextualLexiconEntry(String graphy, String part_of_speech, double probability, int frequency)
    {
        this(graphy, probability, frequency);

        this.setPartOfSpeech(part_of_speech);
    }

    /***************************************************************************
     * Constructeur d�finissant le champ lemme.
     *
     * @param lemme Lemme
     * @param graphy Graphie
     **************************************************************************/
    public TextualLexiconEntry(long lemme, String graphy)
    {
        this(graphy);

        setLemme(lemme);
    }

    /***************************************************************************
     * Constructeur d�finissant les champs lemme et fr�quence.
     *
     * @param lemme Lemme
     * @param graphy Graphie
     * @param frequency Fr�quence
     **************************************************************************/
    public TextualLexiconEntry(long lemme, String graphy, int frequency)
    {
        this(lemme, graphy);

        setFrequency(frequency);
    }

    /***************************************************************************
     * Constructeur d�finissant les champs lemme et probabilit�.
     *
     * @param lemme Lemme
     * @param graphy Graphie
     * @param probability Probabilit�
     **************************************************************************/
    public TextualLexiconEntry(long lemme, String graphy, double probability)
    {
        this(lemme, graphy);

        setProbability(probability);
    }

    /***************************************************************************
     * Constructeur d�finissant les champs lemme, probabilit� et fr�quence.
     *
     * @param lemme Lemme
     * @param graphy Graphie
     * @param probability Probabilit�
     * @param frequency Fr�quence
     **************************************************************************/
    public TextualLexiconEntry(long lemme, String graphy, double probability, int frequency)
    {
        this(lemme, graphy, probability);

        setFrequency(frequency);
    }

    /***************************************************************************
     * Constructeur d�finissant les champs lemme et cat�gorie morpho-syntaxique
     *
     * @param lemme Lemme
     * @param graphy Graphie
     * @param part_of_speech Cat�gorie morpho-syntaxique
     **************************************************************************/
    public TextualLexiconEntry(long lemme, String graphy, String part_of_speech)
    {
        this(lemme, graphy);

        this.setPartOfSpeech(part_of_speech);
    }

    /***************************************************************************
     * Constructeur d�finissant les champs lemme, cat�gorie morpho-syntaxique et
     * fr�quence.
     *
     * @param lemme Lemme
     * @param graphy Graphie
     * @param part_of_speech Cat�gorie morpho-syntaxique
     * @param frequency Fr�quence
     **************************************************************************/
    public TextualLexiconEntry(long lemme, String graphy, String part_of_speech, int frequency)
    {
        this(lemme, graphy, part_of_speech);

        setFrequency(frequency);
    }

    /***************************************************************************
     * Constructeur d�finissant les champs lemme, cat�gorie morpho-syntaxique et
     * probabilit�.
     *
     * @param lemme Lemme
     * @param graphy Graphie
     * @param part_of_speech Cat�gorie morpho-syntaxique
     * @param probability Probabilit�
     **************************************************************************/
    public TextualLexiconEntry(long lemme, String graphy, String part_of_speech, double probability)
    {
        this(lemme, graphy, part_of_speech);

        setProbability(probability);
    }

    /***************************************************************************
     * Constructeur complet.
     *
     * @param lemme Lemme
     * @param graphy Graphie
     * @param part_of_speech Cat�gorie morpho-syntaxique
     * @param probability Probabilit�
     * @param frequency Fr�quence
     **************************************************************************/
    public TextualLexiconEntry(long lemme, String graphy, String part_of_speech, double probability, int frequency)
    {
        this(lemme, graphy, part_of_speech, probability);

        setFrequency(frequency);
    }

    /***************************************************************************
     * Assigne la cat�gorie morpho-syntaxique sous forme textuelle.
     *
     * @param part_of_speech Cat�gorie morpho-syntaxique
     **************************************************************************/
    private void setPartOfSpeech(String part_of_speech)
    {
        utils.checkParameterIsAssigned(part_of_speech);

        this.setPartOfSpeech(0);
        this.part_of_speech_str = part_of_speech;
    }

    /***************************************************************************
     * Retourne la valeur du champ graphie de l'entr�e.
     *
     * @return Cha�ne de caract�re de la graphie
     **************************************************************************/
    public String getGraphy()
    {
        return super.getString();
    }

    /***************************************************************************
     * Retourne la valeur du champ cat�gorie morpho-syntaxique de l'entr�e. Une
     * exception est lev�e si ce champ n'a pas �t� d�fini.
     *
     * @return Chaine de caract�re de la cat�gorie morpho-syntaxique
     **************************************************************************/
    public String getPoS()
    {
        if (this.PoSAvailable())
        {
            // Si n�cessaire extraire la valeur du champ dans le lexique source
            if (this.part_of_speech_str == null)
            {
                // Extraire le lexique source
                TextualLexicon lexicon = (TextualLexicon)this.getSource();
                List list = lexicon.part_of_speech_list;

                // Extraire la cha�ne dans la liste associ�e au lexique
                this.part_of_speech_str = (String)list.getFromIndex(super.getPartOfSpeech());
            }

            return this.part_of_speech_str;
        }

        else
            throw new FieldNotDefinedException();
    }

    /***************************************************************************
     * Retourne si le champ cat�gorie morpho-syntaxique a �t� d�fini ou non
     *
     * @return Valeur bool�enne oui/non
     **************************************************************************/
    public boolean PoSAvailable()
    {
        return super.partOfSpeechAvailable();
    }

    /***************************************************************************
     * Retourne si le champ cat�gorie lemme a �t� d�fini ou non
     *
     * @return Valeur bool�enne oui/non
     **************************************************************************/
    public boolean lemmeAvailable()
    {
        return super.lemmeAvailable();
    }

    /***************************************************************************
     * Retourne le lemme de l'objet appelant. Une exception est lev�e si le
     * champ lemme n'est pas d�fini.
     *
     * @return Entr�e contenant les informations sur le lemme de l'appelant
     **************************************************************************/
    public TextualLexiconEntry getLemmeEntry()
    {
        return (TextualLexiconEntry)this.getSource().getEntryFromIndex(this.getLemme());
    }

    /**************************************************************************/
    public String toString()
    {
        String output = "('" + this.getGraphy() + "'";

        if (this.partOfSpeechAvailable())
            output += ", pos='" + this.getPoS() + "'";

        if (this.lemmeAvailable())
            output += ", lemme=" + this.getLemme();

        if (this.probabilityAvailable())
            output += ", proba=" + this.getProbability();

        if (this.frequencyAvailable())
            output += ", freq=" + this.getFrequency();

        return output + ")";
    }
}