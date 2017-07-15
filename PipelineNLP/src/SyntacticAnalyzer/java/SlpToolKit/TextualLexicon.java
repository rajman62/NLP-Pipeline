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
import java.io.*;

/*******************************************************************************
 * Lexique de donn�es textuelles (impl�mentant l'interface {@link SyntacticAnalyzer.java.SlpToolKit.TextualLexiconIF TextualLexiconIF}).
 ******************************************************************************/
public class TextualLexicon implements TextualLexiconIF {
    /**
     * Pointeur sur la structure de donn�e C contenant le lexique natif sous-jaccent
     */
    protected long native_data = 0;

    /**
     * Flag indiquant si le lexique a d�j� �t� normalis� ou non
     */
    protected boolean normalized = false;

    /**
     * Instance d'arbre lexical retourn� par la fonction getAM()
     */
    protected Trie associative_memory = null;

    /**
     * Liste cha�n�e contenant les correspondances textuelles des cat�gories morpho-syntaxiques
     */
    protected List part_of_speech_list = null;

    /**
     * Classe des entr�es g�n�r�es par un lexique textuel (TextualLexiconEntry)
     */
    private static Class ENTRY_CLASS = null;

    /**
     * Extension du fichier contenant les CMS's sous forme textuelle
     */
    private static String POS_LIST_FILE_EXTENSION = ".map";

    /**
     * Extension du fichier contenant la liste des graphies en cas d'exportation (natif)
     */
    private static String TREE_FILE_EXTENSION = ".tree";

    /**
     * Extension du fichier contenant les CMS's sous forme num�rique interne (natif)
     */
    private static String MORPHO_FILE_EXTENSION = ".morph";

    /**
     * Extension du fichier temporaire
     */
    private static String TEMP_FILE_EXTENSION = ".tmp";

    /**
     * Extension du fichier de d�finition (natif)
     */
    private static String LEXICAL_FILE_EXTENSION = ".slplex";

    /**
     * Pr�fixe des commentaires dans les fichiers natifs
     */
    private static String COMMENT_PREFIX = "#";

    /***************************************************************************
     * Constructeur.
     **************************************************************************/
    public TextualLexicon()
    {
        this.native_data = NativeLexique.constructor();
        this.part_of_speech_list = new List();

        // Ins�re une CMS vide pour que la num�rotation commence � 1 !!!
        this.part_of_speech_list.append("");
    }

    /***************************************************************************
     * Destructeur.
     **************************************************************************/
    protected void finalize()
    {
        NativeLexique.destructor(this.native_data);
    }

    /**************************************************************************/
    public LexicalAccessTableIF getAM()
    {
        if (this.associative_memory == null)
        {
            long native_trie = NativeLexique.getArbreLexico(this.native_data);
            this.associative_memory = new Trie(native_trie, true);
        }

        return this.associative_memory;
    }

    /**************************************************************************/
    public void exportToASCII(String filename) throws IOException
    {
        utils.checkParameterIsAssigned(filename);

        if (NativeLexique.exportToASCII(this.native_data, filename))
            throw new CantWriteFileException(filename);

        // La version textuelle des cat�gories morpho-syntaxiques ne sont pas sauvegard�es
    }

    /**************************************************************************/
    public void importFromASCII(String filename) throws IOException
    {
        utils.checkParameterIsAssigned(filename);

        File tree_file = new File(filename + TREE_FILE_EXTENSION);
        File morph_file = new File(filename + MORPHO_FILE_EXTENSION);
        File temp_file = new File(filename + TEMP_FILE_EXTENSION);

        // Effacer le fichier temporaire pr�c�dent au cas o�
        temp_file.delete();
        morph_file.delete();

        // Renommer le fichier .tree en .tmp pour l'op�ration
        if (tree_file.exists())
            tree_file.renameTo(temp_file);

        // Ouvrir le fichier temporaire en lecture
        FileReader reader = new FileReader(temp_file);
        BufferedReader buffered_reader = new BufferedReader(reader);

        // Cr�er un nouveau fichier .tree en �criture
        FileWriter tree_writer = new FileWriter(tree_file);
        BufferedWriter buffered_tree_writer = new BufferedWriter(tree_writer);

        // Cr�er un nouveau fichier .morpho en �criture
        FileWriter morph_writer = new FileWriter(morph_file);
        BufferedWriter buffered_morph_writer = new BufferedWriter(morph_writer);

        // La num�rotation des CMS's commencent � 1
        this.part_of_speech_list.clear();
        this.part_of_speech_list.append("");

        // Lire chaque ligne du fichier temporaire (anciennement .tree)
        for(String line = buffered_reader.readLine(); line != null; line = buffered_reader.readLine())
        {
            int index = line.lastIndexOf(":");

            // Si la ligne est un commentaire ou ne comporte pas de CMS
            if ((line.startsWith(COMMENT_PREFIX)) || (index == -1))
            {
                buffered_tree_writer.write(line);
                buffered_tree_writer.newLine();
            }
            else
            {
                // Sinon, extraire la graphie et la CMS
                String graphy = line.substring(0, index);
                String part_of_speech = line.substring(index + 1, line.length());

                this.part_of_speech_list.safeAppend(part_of_speech);

                // Ecrire graphie et CMS dans leurs fichiers respectifs
                buffered_morph_writer.write(this.part_of_speech_list.getIndexOf(part_of_speech) + "");
                buffered_morph_writer.newLine();

                buffered_tree_writer.write(graphy);
                buffered_tree_writer.newLine();
            }
        }

        // Fermer les fichiers
        buffered_tree_writer.close();
        buffered_morph_writer.close();
        buffered_reader.close();
        tree_writer.close();
        morph_writer.close();
        reader.close();

        // Effacer le fichier .morph s'il est vide
        if (morph_file.length() == 0)
            morph_file.delete();

        // Effectuer l'importation native
        NativeLexique.importFromASCII(this.native_data, filename + LEXICAL_FILE_EXTENSION);

        // Effacer les fichiers temporaires
        morph_file.delete();
        tree_file.delete();
        temp_file.renameTo(tree_file);
    }

    /**************************************************************************/
    public void loadFromFile(String filename) throws IOException
    {
        utils.checkParameterIsAssigned(filename);

        // Chargement du lexique natif
        if (NativeLexique.loadFromFile(this.native_data, filename))
            throw new CantReadFileException(filename);

        // Charger la liste des CMS � partir du fichier texte ad-hoc
        FileReader pos_reader = new FileReader(filename + POS_LIST_FILE_EXTENSION);
        BufferedReader buffered_reader = new BufferedReader(pos_reader);

        // La num�rotation des CMS dans la liste doit commenc�e � 1
        this.part_of_speech_list.clear();
        this.part_of_speech_list.append("");

        for(;;)
        {
            String part_of_speech = buffered_reader.readLine();

            if (part_of_speech == null)
                break;
            else
                this.part_of_speech_list.append(part_of_speech);
        }

        buffered_reader.close();
        pos_reader.close();
    }

    /**************************************************************************/
    public void saveToFile(String filename) throws IOException
    {
        utils.checkParameterIsAssigned(filename);

        // Sauvegarde du lexique natif
        if (NativeLexique.saveFromFile(this.native_data, filename))
            throw new CantWriteFileException(filename);

        // Ecrit la liste des CMS textuelles dans un fichier texte
        FileWriter pos_writer = new FileWriter(filename + POS_LIST_FILE_EXTENSION);
        BufferedWriter buffered_writer = new BufferedWriter(pos_writer);

        for(long index = 1; index < this.part_of_speech_list.getSize(); index++)
        {
            if (index > 1)
                buffered_writer.newLine();

            String part_of_speech = (String)this.part_of_speech_list.getFromIndex(index);

            buffered_writer.write(part_of_speech);
        }

        buffered_writer.close();
        pos_writer.close();
    }

    /**************************************************************************/
    public void normalize()
    {
        if (this.normalized == false)
        {
            NativeLexique.normalize(this.native_data);
            this.normalized = true;
        }
        else
            throw new InvalidMethodCallException(LEXICON_ALREADY_NORMALIZED_ERROR_MESSAGE);
    }

    /**************************************************************************/
    public boolean isNormalized()
    {
        return this.normalized;
    }

    /**************************************************************************/
    public long insert(LexiconEntry p_entry)
    {
        utils.checkParameterIsAssigned(p_entry);

        TextualLexiconEntry entry = (TextualLexiconEntry)p_entry;

        entry.getGraphy(); // Extraire la graphie

        // Si le champ cat�gorie morpho-syntaxique est d�fini ...
        if (entry.PoSAvailable())
        {
            // Ajouter la nouvelle cat�gorie � la liste si elle n'existe pas d�j�
            entry.setPartOfSpeech(this.part_of_speech_list.safeAppend(entry.getPoS()));
        }

        // Insertion dans le lexique natif
        return entry.insertIntoNativeLexicon(this.native_data);
    }

    /**************************************************************************/
    public boolean contains(LexiconEntry p_entry)
    {
        utils.checkParameterIsAssigned(p_entry);

        TextualLexiconEntry entry = (TextualLexiconEntry)p_entry;

        // Si le champ cat�gorie morpho-syntaxique est d�fini ...
        if (entry.PoSAvailable())
        {
            String pos_string = entry.getPoS();

            // Extraire l'indice correspondant � sa graphie
            if (this.part_of_speech_list.contains(pos_string))
                entry.setPartOfSpeech(this.part_of_speech_list.getIndexOf(pos_string));

            // Si elle n'est pas trouv�e, l'entr�e recherch�e n'existe donc pas
            else
                return false;
        }

        // Test dans le lexique natif
        return entry.containedIntoNativeLexicon(this.native_data);
    }

    /**************************************************************************/
    public long getSize()
    {
        return NativeLexique.getSize(this.native_data);
    }

    /**************************************************************************/
    public LexiconEntry getEntryFromIndex(long index)
    {
        if ((index < 0) || (index >= this.getSize()))
            throw new IndexOutOfBoundsException();

        return NativeLexique.getFromIndex(this.native_data, this, index, ENTRY_CLASS);
    }

    /**************************************************************************/
    public LexiconEntry lookFor(Object o)
    {
        return this.lookFor((String)o);
    }

    /**************************************************************************/
    public LexiconEntry[] lookForAll(Object o)
    {
        return this.lookForAll((String)o);
    }

    /**************************************************************************/
    public TextualLexiconEntry[] lookForAll(String seq)
    {
        utils.checkParameterIsAssigned(seq);

        LexiconEntry[] temp_result = NativeLexique.lookForAll(this.native_data, seq, this, ENTRY_CLASS);
        TextualLexiconEntry[] result = new TextualLexiconEntry[temp_result.length];

        for(int index = 0; index < result.length; index++)
            result[index] = (TextualLexiconEntry)temp_result[index];

        return result;
    }

    /**************************************************************************/
    public TextualLexiconEntry lookFor(String seq)
    {
        utils.checkParameterIsAssigned(seq);

        return (TextualLexiconEntry)NativeLexique.lookFor(this.native_data, seq, this, ENTRY_CLASS);
    }

    /**************************************************************************/
    public long getNativeLexicon()
    {
        return this.native_data;
    }

    /***************************************************************************
     * Retourne si le lexique natif sous-jacent d�fini le champ probabilit�
     *
     * @return Flag bool�en
     **************************************************************************/
    protected boolean definesProbability()
    {
        return NativeLexique.probabilityDefined(this.native_data);
    }

    /**************************************************************************/
    static
    {
        // D�finit la classe des entr�es de lexique instanci�es
        try {
            ENTRY_CLASS = Class.forName("SyntacticAnalyzer.java.SlpToolKit.TextualLexiconEntry");
        } catch (Exception e) {e.printStackTrace();};
    }
}