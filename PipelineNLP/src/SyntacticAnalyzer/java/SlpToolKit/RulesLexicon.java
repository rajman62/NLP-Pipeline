package SyntacticAnalyzer.java.SlpToolKit;

import SyntacticAnalyzer.java.SlpToolKit.Exception.*;
import java.io.*;

/**
 * <p>Title: Projet de semestre</p>
 * <p>Description: Surcouche Java � la librairie SlpToolKit</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Ecole Polytechnique de Lausanne (EPFL) - D�partement d'intelligence artificielle (LIA)</p>
 * @author Antonin Mer�ay
 * @version 1.0
 */

/*******************************************************************************
 * Lexique de r�gles (impl�mentant l'interface {@link SyntacticAnalyzer.java.SlpToolKit.RulesLexiconIF RulesLexiconIF}).
 ******************************************************************************/
public class RulesLexicon implements RulesLexiconIF {
    /**
     * Pointeur sur la structure native Grammaire sous-jacente
     */
    protected long native_grammar;

    /**
     * Table d'acc�s lexicale retourn�e par la fonction getAM()
     */
    LexicalAccessTableIF associative_memory = null;

    /**
     * Flag indiquant si le lexique est d�j� normalis� ou non
     */
    boolean normalized = false;

    /**
     * Flag indiquant si le lexique de r�gles prend en compte les probabilit�s
     */
    boolean probability_defined;

    /**
     * Pr�fixe d'un constituant de r�gle lexicalis� (provenant d'un lexique)
     */
    private static String QUOTE_PREFIX = "\"";

    /**
     * Extension du fichier principal de sauvegarde d'une grammaire SlpToolKit
     */
    private static String GRAM_FILE_EXTENSION = ".slpgram";

    /**
     * S�parateur entre partie gauche et partie droite de r�gle
     */
    protected static String RULE_SEPARATOR = "->";

    /**
     * Pr�fixe d'un commentaire d'une exportation de grammaire SlpToolKit
     */
    private static String COMMENT_PREFIX = "#";

    /***************************************************************************
     * Constructeur.
     **************************************************************************/
    public RulesLexicon()
    {
        this.native_grammar = NativeGrammaire.constructor();
    }

    /***************************************************************************
     * Destructeur.
     **************************************************************************/
    protected void finalize()
    {
        NativeGrammaire.destructor(this.native_grammar);
    }

    /**************************************************************************/
    public LexicalAccessTableIF getAM()
    {
        // Si l'instance n'a pas �t� d�j� cr��e, le faire
        if (this.associative_memory == null)
            this.associative_memory = new Trie(NativeGrammaire.getNativeArbreLex(this.native_grammar), true);

        return this.associative_memory;
    }

    /**************************************************************************/
    public void normalize()
    {
        // Lever une exception si le lexique a d�j� �t� normalis�
        if (this.normalized)
            throw new InvalidMethodCallException(LEXICON_ALREADY_NORMALIZED_ERROR_MESSAGE);

        if (this.probability_defined == false)
            utils.printWarning("RulesLexicon.normalize() executed while 'probability' field is not defined");

        NativeLexique.normalize(NativeGrammaire.getNativeRulesLexicon(this.native_grammar));
        this.normalized = true;
    }
    /**************************************************************************/
    public boolean isNormalized()
    {
        return this.normalized;
    }

    /**************************************************************************/
    public long getSize()
    {
        return NativeGrammaire.getSize(this.native_grammar);
    }

    /**************************************************************************/
    public void exportToASCII(String filename) throws IOException
    {
        utils.checkParameterIsAssigned(filename);

        FileWriter writer = new FileWriter(filename);
        BufferedWriter buffered_writer = new BufferedWriter(writer);

        // Pour chaque entr�e du lexique de r�gles, �crire ...
        for(long entry_index = 0; entry_index < this.getSize(); entry_index++)
        {
            RulesLexiconEntry entry = (RulesLexiconEntry)this.getEntryFromIndex(entry_index);

            // Partie gauche -> ...
            buffered_writer.write(entry.getLeftPart() + " " + RULE_SEPARATOR);

            String[] right_part = entry.getRightPart();

            // .. Parties droites
            for(int part_index = 0; part_index < right_part.length; part_index++)
                buffered_writer.write(" " + right_part[part_index]);

            // Si la probabilit� est d�finie, l'�crire en commentaire
            if (entry.probabilityAvailable())
                buffered_writer.write("   (" + entry.getProbability() + ")");

            buffered_writer.newLine();
        }

        buffered_writer.flush();
        writer.close();
    }

    /**************************************************************************/
    public void importFromASCII(String filename) throws IOException
    {
        this.importFromASCII(filename, 0, 1);
    }

    /***************************************************************************
     * M�thode d'importation du lexique de r�gles compl�te.
     *
     * @param filename Nom du fichier textuel source contenant les r�gles
     * @param native_lexicon Pointeur sur la structure native C Lexique
     * @param initial_nt Nom terminal initial de la structure native C Grammaire
     * @throws IOException Si probl�me de lecture de fichier
     **************************************************************************/
    protected void importFromASCII(String filename, long native_lexicon, long initial_nt) throws IOException
    {
        utils.checkParameterIsAssigned(filename);

        if (NativeGrammaire.importeGrammaire(this.native_grammar, native_lexicon, filename, initial_nt, true))
            throw new CantReadFileException(filename);

        this.probability_defined = true;
    }

    /**************************************************************************/
    public void loadFromFile(String filename) throws IOException
    {
        utils.checkParameterIsAssigned(filename);

        if (NativeGrammaire.readGrammaire(this.native_grammar, filename + GRAM_FILE_EXTENSION, null))
            throw new CantReadFileException(filename);

        this.probability_defined = true;
    }

    /**************************************************************************/
    public void saveToFile(String filename) throws IOException
    {
        utils.checkParameterIsAssigned(filename);

        if (NativeGrammaire.writeGrammaire(this.native_grammar, filename, null))
            throw new CantWriteFileException(filename);
    }

    /**************************************************************************/
    public long insert(LexiconEntry p_entry)
    {
        utils.checkParameterIsAssigned(p_entry);

        RulesLexiconEntry entry = (RulesLexiconEntry)p_entry;

        // V�rifier qu'aucun membre de la partie droite ne soit lexicalis� (pas d'acc�s au lexique natif)
        String[] right_part_str = entry.getRightPart();
        for (int i = 0; i < right_part_str.length; i++)
            if (right_part_str[i].startsWith(QUOTE_PREFIX))
                throw new InvalidMethodCallException("lexicalized item insertion not allowed");

        // Si le lexique est vide, d�terminer par la 1�re entr�e si la probabilit� est d�finie
        if (this.getSize() == 0)
        {
            if (entry.probabilityAvailable())
                this.probability_defined = true;

            else
                this.probability_defined = false;
        }

        long left_part = this.getInternalCodeNT(entry.getLeftPart(), true);
        long[] right_part = this.getInternalRightPart(right_part_str, true);
        double probability;

        // Si la probabilit� est d�finie par le lexique, v�rifier sa pr�sence
        if (this.probability_defined)
        {
            if (entry.probabilityAvailable())
                probability = entry.getProbability();

            else
                throw new FieldRequiredException("probability");
        }
        else
        {
            if (entry.probabilityAvailable())
                LexiconEntry.printFieldNotManagedWarning("probability");

            probability = 1.0;
        }

        // Ex�cuter l'insertion native
        return (NativeGrammaire.ajouteRegle(this.native_grammar,
                                            left_part,
                                            right_part,
                                            probability)) - 1;
    }

    /***************************************************************************
     * Retourne le code interne d'un non-terminal donn� sous forme textuelle. Un
     * flag permet d'ordonner ou non l'insertion du non-terminal dans la
     * Grammaire native au cas o� ne serait pas encore pr�sent.
     *
     * @param nt Non-terminal sous forme textuelle
     * @param insertion Flag indiquant l'insertion en cas d'absence
     * @return Code interne du non-terminal (0 en cas d'erreur)
     **************************************************************************/
    private long getInternalCodeNT(String nt, boolean insertion)
    {
        if (insertion)
            return NativeGrammaire.ajouteNT(this.native_grammar, nt, 0);
        else
            return NativeGrammaire.convertNTchar(this.native_grammar, nt, 0);
    }

    /***************************************************************************
     * Retourne sous forme de codes internes la partie de droite de r�gle donn�e
     * sous forme textuelle. Un flag permet d'ordonner ou non l'insertion dans
     * la Grammaire native des non-terminaux qui n'y serait pas encore pr�sent.
     *
     * @param right_part Partie droite de r�gle sous forme textuelle
     * @param insertion Flag indiquant l'insertion en cas d'absence
     * @return Partie droite sous forme interne (null si conversion impossible)
     **************************************************************************/
    private long[] getInternalRightPart(String[] right_part, boolean insertion)
    {
        long[] output = new long[right_part.length];
        long buffer;

        for(int index = 0; index < output.length; index++)
        {
            buffer = this.getInternalCodeNT(right_part[index], insertion);

            // Retourner null si un non-terminal n'a pas �t� trouv�
            if (buffer == 0)
                return null;
            else
                output[index] = buffer;
        }

        return output;
    }

    /**************************************************************************/
    public boolean contains(LexiconEntry p_entry)
    {
        long[] index = this.getInternalIndexFromRule((RulesLexiconEntry)p_entry);

        if (index == null)
            return false;

        else if (index.length == 0)
            return false;

        else
            return true;
    }

    /***************************************************************************
     * Retourne la liste des indices internes d'une Grammaire C correspondant �
     * entr�e donn�e en param�tre.
     *
     * @param entry Entr�e recherch�e
     * @return Liste des indices internes
     **************************************************************************/
    private long[] getInternalIndexFromRule(RulesLexiconEntry entry)
    {
        utils.checkParameterIsAssigned(entry);

        long left_part = this.getInternalCodeNT(entry.getLeftPart(), false);
        long[] right_part = this.getInternalRightPart(entry.getRightPart(), false);

        // Retourner null si un non-terminal n'a pas �t� trouv�
        if ((left_part == 0) || (right_part == null))
            return null;

        // Ex�cution de la recherche native
        long index[] = NativeGrammaire.regleVersNumero(this.native_grammar,
                                                       left_part,
                                                       right_part);

        return index;
    }

    /**************************************************************************/
    public LexiconEntry getEntryFromIndex(long p_index)
    {
        if ((p_index < 0) || (p_index >= this.getSize()))
            throw new IndexOutOfBoundsException();

        // La num�rotation dans une grammaire native commence � 1
        p_index++;

        // Extraire les champs de l'entr�e index�e
        long left_part_long = NativeGrammaire.getLeftPart(this.native_grammar, p_index);
        long[] right_part_long = NativeGrammaire.getRightPart(this.native_grammar, p_index);
        double probability = NativeGrammaire.getProba(this.native_grammar, p_index);

        // Convertir la r�gle dans son format textuel
        String left_part_str = NativeGrammaire.convertNT(this.native_grammar, left_part_long, 0);
        String[] right_part_str = new String[right_part_long.length];

        for(int index = 0; index < right_part_str.length; index++)
            right_part_str[index] = NativeGrammaire.convertNT(this.native_grammar, right_part_long[index], 0);

        // Choisir le constructeur selon si la probabilit� est d�finie ou non
        if (this.probability_defined)
            return new RulesLexiconEntry(probability, left_part_str, right_part_str);
        else
            return new RulesLexiconEntry(left_part_str, right_part_str);
    }

    /**************************************************************************/
    public LexiconEntry[] lookForAll(Object o)
    {
        return this.lookForAll((RulesLexiconEntry)o);
    }

    /**************************************************************************/
    public LexiconEntry lookFor(Object o)
    {
        return this.lookFor((RulesLexiconEntry)o);
    }

    /**************************************************************************/
    public RulesLexiconEntry[] lookForAll(RulesLexiconEntry entry)
    {
        long[] index = this.getInternalIndexFromRule(entry);

        if (index == null)
            return new RulesLexiconEntry[0];

        // Cr�er et retourner le tableau d'entr�es
        RulesLexiconEntry[] output = new RulesLexiconEntry[index.length];

        for(int i = 0; i < output.length; i++)
            output[i] = (RulesLexiconEntry)this.getEntryFromIndex(index[i] - 1);

        return output;
    }

    /**************************************************************************/
    public RulesLexiconEntry lookFor(RulesLexiconEntry entry)
    {
        long[] index = this.getInternalIndexFromRule(entry);

        if (index == null)
            return new RulesLexiconEntry(null, null, false, 0, null);

        // Si au moins une entr�e correspondante a �t� trouv�e ...
        if (index.length > 0)
        {
            // Ex�cuter le constructeur ad�quat (probabilit� ou non)
            if (this.probability_defined)
            {
                double probability = NativeGrammaire.getProba(this.native_grammar, index[0]);
                return new RulesLexiconEntry(probability,
                                             entry.getLeftPart(),
                                             entry.getRightPart(),
                                             true,
                                             this.native_grammar,
                                             index);
            }
            else
                return new RulesLexiconEntry(entry.getLeftPart(),
                                             entry.getRightPart(),
                                             true,
                                             this.native_grammar,
                                             index);
        }
        else
            return new RulesLexiconEntry(null, null, false, this.native_grammar, null);

    }

    /**************************************************************************/
    public long getNativeLexicon()
    {
        return NativeGrammaire.getNativeRulesLexicon(this.native_grammar);
    }
}