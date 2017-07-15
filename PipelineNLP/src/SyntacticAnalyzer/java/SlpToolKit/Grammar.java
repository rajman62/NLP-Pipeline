package SyntacticAnalyzer.java.SlpToolKit;

import SyntacticAnalyzer.java.SlpToolKit.Exception.InvalidMethodCallException;

import java.io.*;

/*******************************************************************************
 * Grammaire (pouvant �tre d�fini comme un ensemble de r�gles et un lexique de mots
 ******************************************************************************/
public class Grammar {
    /**
     * Lexique du vocabulaire
     */
    TextualLexicon lexicon;

    /**
     * Lexique des r�gles de la grammaire
     */
    RulesLexicon rules;

    /**
     * Code interne du non-terminal de plus haut niveau
     */
    private long initial_NT;

    /**
     * Extension du fichier stockant le vocabulaire
     */
    protected static String LEXICON_FILE_EXTENSION = ".word";

    /**
     * Extension du fichier stockant le lexique des r�gles grammaticales
     */
    protected static String GRAMMAR_FILE_EXTENSION = ".gram";

    /**
     * Code interne du premier non-terminal interfac� de mani�re textuelle par
     * le lexique de r�gles
     */
    protected static int FIRST_KNOWN_NT_CODE = 512;

    /***************************************************************************
     * Constructeur
     *
     * @param lexicon Lexique de vocabulaire associ�
     * @param initial_nt Non-terminal de plus haut niveau
     **************************************************************************/
    public Grammar(TextualLexicon lexicon, String initial_nt)
    {
        utils.checkParameterIsAssigned(lexicon);
        utils.checkParameterIsAssigned(initial_nt);

        // Lever une exception si le lexique fourni n'est pas valide
        if ((lexicon.definesProbability() == false) && (lexicon.getSize() > 0))
            throw new InvalidMethodCallException("TextualLexicon must define 'probability' field");

        this.lexicon = lexicon;
        this.rules = new RulesLexicon();

        // Ins�rer et d�finir le non-terminal de plus haut niveau dans la Grammaire native
        this.initial_NT = NativeGrammaire.ajouteNT(this.rules.native_grammar,
                                                     initial_nt,
                                                     this.lexicon.native_data);

        NativeGrammaire.setInitialNT(this.rules.native_grammar, this.initial_NT);
    }

    /***************************************************************************
     * Sauvegarde la grammaire dans un fichier dont le nom est donn� en
     * param�tre.
     *
     * @param filename Nom du fichier destination
     * @throws IOException Si probl�me d'�criture des fichiers
     **************************************************************************/
    public void saveGrammar(String filename) throws IOException
    {
        this.lexicon.saveToFile(filename + LEXICON_FILE_EXTENSION);
        this.rules.saveToFile(filename + GRAMMAR_FILE_EXTENSION);
    }

    /***************************************************************************
     * Charge la grammaire � partir d'un fichier dont le nom est sp�cifi� en
     * param�tre. Une exception est lev�e en cas de probl�me
     * (format non valide).
     *
     * @param filename Nom du fichier source
     * @throws IOException Si probl�me de lecture des fichiers
     **************************************************************************/
    public void loadGrammar(String filename) throws IOException
    {
        this.lexicon.loadFromFile(filename + LEXICON_FILE_EXTENSION);
        this.rules.loadFromFile(filename + GRAMMAR_FILE_EXTENSION);
    }

    /***************************************************************************
     * Charge la grammaire contenue sous forme textuelle lisible dans un
     * fichier dont le nom est donn� en param�tre. Une exception est lev�e en
     * cas de probl�me (format non valide).
     *
     * @param filename Nom du fichier source
     * @throws IOException Si probl�me de lecture du fichier
     **************************************************************************/
    public void importFromASCII(String filename) throws IOException
    {
        utils.checkParameterIsAssigned(filename);

        // Cr�er un fichier texte temporaire o� les NT's d�finis dans le lexique
        // sont remplac�s par leurs �quivalents num�rique
        File temp_file = new File(filename + ".tmp");
        temp_file.delete();

        FileReader reader = new FileReader(filename);
        BufferedReader buffered_reader = new BufferedReader(reader);

        FileWriter writer = new FileWriter(temp_file);
        BufferedWriter buffered_writer = new BufferedWriter(writer);

        List pos_list = this.lexicon.part_of_speech_list;

        for(String input = buffered_reader.readLine();
            input != null;
            input = buffered_reader.readLine())
        {
            String buffer = "";
            String output = "";

            for(int position = 0; position <= input.length(); position++)
            {
                char car = ' ';

                if (position < input.length())
                    car = input.charAt(position);

                if ((Character.isWhitespace(car)) || (position == input.length()))
                {
                    if ((pos_list.contains(buffer)) && (buffer.length() > 0))
                        buffer = ":" + pos_list.getIndexOf(buffer);

                    output += buffer;

                    if (position < input.length())
                        output += car;

                    buffer = "";
                }
                else
                    buffer += car;

            }

            buffered_writer.write(output);
            buffered_writer.newLine();
        }

        buffered_writer.close();
        writer.close();

        buffered_reader.close();
        reader.close();

        this.rules.importFromASCII(temp_file.getAbsolutePath(), this.lexicon.native_data, this.initial_NT);

        temp_file.delete();
    }

    /***************************************************************************
     * Sauvegarde la grammaire sous forme textuelle lisible dans un fichier
     * dont le nom est signal� en param�tre.
     *
     * @param filename Nom du fichier destination
     * @throws IOException Si probl�me d'�criture du fichier
     **************************************************************************/
    public void exportToASCII(String filename) throws IOException
    {
        utils.checkParameterIsAssigned(filename);

        FileWriter writer = new FileWriter(filename);
        BufferedWriter buffered_writer = new BufferedWriter(writer);

        // Ecrire toutes les r�gles de la grammaire dans le fichier destination
        for(long index = 0; index < this.getNumberOfRules(); index++)
        {
            RulesLexiconEntry entry = this.getRuleFromIndex(index);

            // Inscrire la partie gauche de la r�gle de grammaire
            buffered_writer.write(entry.getLeftPart() + " ");
            buffered_writer.write(RulesLexicon.RULE_SEPARATOR);

            // Inscrire la partie droite de la r�gle de grammaire
            String[] right_part = entry.getRightPart();
            for(int part_index = 0; part_index < right_part.length; part_index++)
                buffered_writer.write(" " + right_part[part_index]);

            // Inscrire la probabilit� de la r�gle entre parenth�ses
            buffered_writer.write("\t(" + entry.getProbability() + ")");

            buffered_writer.newLine();
        }

        buffered_writer.close();
        writer.close();
    }

    /***************************************************************************
     * Ajoute une r�gle syntaxique dont les informations sont stock�es dans
     * un objet RulesLexiconEntry (en param�tre) � la grammaire.
     *
     * @param entry R�gle syntaxique � ajouter � la grammaire
     **************************************************************************/
    public void addRule(RulesLexiconEntry entry)
    {
        utils.checkParameterIsAssigned(entry);

        // Extraction de la probabilit� (si elle n'est pas d�finie, prendre 1.0)
        double probability;
        if (entry.probabilityAvailable())
            probability = entry.getProbability();
        else
            probability = 1.0;

        // Extraire la repr�sentation interne de la r�gle
        String[] right_part_str = entry.getRightPart();
        long left_part = this.getCodeNT(entry.getLeftPart(), true);
        long[] right_part = new long[right_part_str.length];

        for(int index = 0; index < right_part.length; index++)
            right_part[index] = this.getCodeNT(right_part_str[index], true);

        // Ajouter la r�gle � la grammaire native
        NativeGrammaire.ajouteRegle(this.rules.native_grammar,
                                    left_part,
                                    right_part,
                                    probability);
    }

    /***************************************************************************
     * Retourne le nombre de r�gles syntaxiques contenues dans la grammaire.
     *
     * @return Nombre de r�gles
     **************************************************************************/
    public long getNumberOfRules()
    {
        return this.rules.getSize();
    }

    /***************************************************************************
     * Retourne les informations concernant la r�gle syntaxique index�e par
     * le param�tre dans un objet de la classe RulesLexiconEntry.
     * Une exception est lev�e en cas d'erreur (index non valide).
     *
     * @param index Index de la r�gle syntaxique d�sir�e
     * @return Nouvelle instance de r�gle contenant les informations
     **************************************************************************/
    public RulesLexiconEntry getRuleFromIndex(long index)
    {
        if ((index < 0) || (index >= this.getNumberOfRules()))
            throw new IndexOutOfBoundsException();

        // La num�rotation interne d'une Grammaire C commence � 1.
        index++;

        // Extrait la valeur des diff�rents champs
        long native_grammar = this.rules.native_grammar;
        long left_part = NativeGrammaire.getLeftPart(native_grammar, index);
        long[] right_part = NativeGrammaire.getRightPart(native_grammar, index);
        double probability = NativeGrammaire.getProba(native_grammar, index);

        // Obtenir la forme textuelle de la r�gle
        String left_part_str = this.getStringNT(left_part);
        String[] right_part_str = new String[right_part.length];

        for(int i = 0; i < right_part_str.length; i++)
            right_part_str[i] = this.getStringNT(right_part[i]);

        return new RulesLexiconEntry(probability, left_part_str, right_part_str);
    }

    /***************************************************************************
     * Recherche les r�gles syntaxiques dont le membre de gauche et le membre
     * de droite sont sp�cifi�s dans l'objet en param�tre. Retourne la liste
     * des index correspondants.
     *
     * @param entry R�gle contenant les informations recherch�es
     * @return Liste des index correspondants
     **************************************************************************/
    public long[] getIndexFromRule(RulesLexiconEntry entry)
    {
        utils.checkParameterIsAssigned(entry);

        if (entry.probabilityAvailable())
            utils.printWarning("Grammar.getIndexFromRule ignores 'probability' field");

        // Extraire la repr�sentation interne de la partie gauche
        long left_part = this.getCodeNT(entry.getLeftPart(), false);

        if (left_part == 0)
            return new long[0];

        // Extraire la repr�sentation interne de la partie droite
        String[] right_part_str = entry.getRightPart();
        long[] right_part = new long[right_part_str.length];

        for(int i = 0; i < right_part.length; i++)
        {
            long buffer = this.getCodeNT(right_part_str[i], false);

            if (buffer == 0)
                return new long[0];
            else
                right_part[i] = buffer;
        }

        // Ex�cuter la fonction native pour rechercher les indices internes
        long native_grammar = this.rules.native_grammar;
        long[] index = NativeGrammaire.regleVersNumero(native_grammar, left_part, right_part);

        // La num�rotation interne commence � 1 (et pas � 0)
        for(int i = 0; i < index.length; i++)
            index[i]--;

        return index;
    }

    /***************************************************************************
     * Retourne sous sa forme textuelle le non-terminal sp�cifi� en param�tre.
     *
     * @param nt Non-terminal sous sa forme interne num�rique
     * @return Non-terminal sous sa forme externe textuelle
     **************************************************************************/
    private String getStringNT(long nt)
    {
        List pos_list = this.lexicon.part_of_speech_list;

        if ((nt < FIRST_KNOWN_NT_CODE) || (nt < pos_list.getSize()))
            return (String)this.lexicon.part_of_speech_list.getFromIndex(nt);

        else
        {
            String result = NativeGrammaire.convertNT(this.rules.native_grammar,
                                                      nt,
                                                      this.lexicon.native_data);

            if (result == null)
                return ":" + nt;
            else
                return result;
        }
    }

    /***************************************************************************
     * Retourne sous sa forme interne un non-terminal sp�cifi� de mani�re
     * textuelle. Un flag permet de d�terminer si le NT doit �tre ajout� � la
     * Grammaire native s'il n'est pas pr�sent.
     *
     * @param nt Non-terminal
     * @param insertion Flag autorisant l'insertion en cas d'absence
     * @return Code interne du non-terminal
     **************************************************************************/
    private long getCodeNT(String nt, boolean insertion)
    {
        long native_grammar = this.rules.native_grammar;
        long native_lexicon = this.lexicon.native_data;
        List pos_list = this.lexicon.part_of_speech_list;

        if (pos_list.contains(nt))
            return pos_list.getIndexOf(nt);
        else if (insertion)
            return NativeGrammaire.ajouteNT(native_grammar, nt, native_lexicon);
        else
            return NativeGrammaire.convertNTchar(native_grammar, nt, native_lexicon);
    }
}