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
 * Entr�e d'un lexique impl�mentant l'interface {@link SlpToolKit.NumericalLexiconIF
 * NumericalLexiconIF}.
 ******************************************************************************/
public class NumericalLexiconEntry extends LexiconEntry {
    /***************************************************************************
     * Constructeur vide (n�cessaire pour les acc�s au lexique)
     **************************************************************************/
    protected NumericalLexiconEntry() {}

    /***************************************************************************
     * Constructeur d�finissant uniquement la s�quence de nombre entier.
     *
     * @param sequence S�quence
     **************************************************************************/
    public NumericalLexiconEntry(long[] sequence)
    {
        this.setSequence(sequence);
    }

    /***************************************************************************
     * Constructeur d�finissant le champ fr�quence.
     *
     * @param frequency Fr�quence
     * @param sequence S�quence
     **************************************************************************/
    public NumericalLexiconEntry(int frequency, long[] sequence)
    {
        this(sequence);
        this.setFrequency(frequency);
    }

    /***************************************************************************
     * Constructeur d�finissant le champ probabilit�.
     *
     * @param probability Probabilit�
     * @param sequence S�quence
     **************************************************************************/
    public NumericalLexiconEntry(double probability, long[] sequence)
    {
        this(sequence);
        this.setProbability(probability);
    }

    /***************************************************************************
     * Constructeur d�finissant les champs fr�quence et probabilit�.
     *
     * @param frequency Fr�quence
     * @param probability Probabilit�
     * @param sequence S�quence
     **************************************************************************/
    public NumericalLexiconEntry(int frequency, double probability,
                                 long[] sequence)
    {
        this(probability, sequence);
        this.setFrequency(frequency);
    }

    /***************************************************************************
     * Constructeur d�finissant le champ tag.
     *
     * @param tag Tag
     * @param sequence S�quence
     **************************************************************************/
    public NumericalLexiconEntry(long tag, long[] sequence)
    {
        super.setPartOfSpeech(tag);
        super.setSequence(sequence);
    }

    /***************************************************************************
     * Constructeur d�finissant les champs fr�quence et tag.
     *
     * @param frequency Fr�quence
     * @param tag Tag
     * @param sequence S�quence
     **************************************************************************/
    public NumericalLexiconEntry(int frequency, long tag, long[] sequence)
    {
        this(tag, sequence);
        this.setFrequency(frequency);
    }

    /***************************************************************************
     * Constructeur d�finissant les champs probabilit� et tag.
     *
     * @param probability Probabilit�
     * @param tag Tag
     * @param sequence S�quence
     **************************************************************************/
    public NumericalLexiconEntry(double probability, long tag,
                                 long[] sequence)
    {
        this(tag, sequence);
        this.setProbability(probability);
    }

    /***************************************************************************
     * Constructeur complet.
     *
     * @param frequency Fr�quence
     * @param probability Probabilit�
     * @param tag Tag
     * @param sequence S�quence
     **************************************************************************/
    public NumericalLexiconEntry(int frequency, double probability,
                                 long tag, long[] sequence)
    {
        this(probability, tag, sequence);
        this.setFrequency(frequency);
    }

    /***************************************************************************
     * Retourne le champ s�quence.
     *
     * @return S�quence
     **************************************************************************/
    public long[] getSequence()
    {
        return super.getSequence();
    }

    /***************************************************************************
     * Retourne la valeur du champ tag. Une exception est lev�e si ce champ
     * n'a pas �t� d�fini.
     *
     * @return Valeur du tag
     **************************************************************************/
    public long getTag()
    {
        // Le champ tag correspond en fait au champ cat�gorie morpho-syntaxique
        if (super.partOfSpeechAvailable())
            return super.getPartOfSpeech();

        else
            throw new FieldNotDefinedException();
    }

    /***************************************************************************
     * Retourne si le champ tag a �t� d�fini ou non.
     *
     * @return Valeur bool�enne oui/non
     **************************************************************************/
    public boolean tagAvailable()
    {
        return this.partOfSpeechAvailable();
    }

    /**************************************************************************/
    public String toString()
    {
        long[] sequence = this.getSequence();
        String output = "({";

        for(int index = 0; index < sequence.length; index++)
        {
            if (index > 0)
                output += ",";

            output += sequence[index];
        }

        output += "}";

        if (this.tagAvailable())
            output += ", tag='" + this.getTag() + "'";

        if (this.probabilityAvailable())
            output += ", proba=" + this.getProbability();

        if (this.frequencyAvailable())
            output += ", freq=" + this.getFrequency();

        return output + ")";
    }
}