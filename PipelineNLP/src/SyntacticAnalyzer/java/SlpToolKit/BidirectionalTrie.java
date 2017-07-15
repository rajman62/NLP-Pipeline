package SyntacticAnalyzer.java.SlpToolKit;

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
 * Impl�mentation concr�te d'une {@link SyntacticAnalyzer.java.SlpToolKit.LexicalAssocMemIF
 * m�moire associative lexical} gr�ce aux arbres.
 ******************************************************************************/
public class BidirectionalTrie extends Trie implements LexicalAssocMemIF {
    /**
     * Liste cha�n�e contenant les codes d'inversion associ�es aux entr�es.
     */
    protected List inv_code_list = null;

    /**
     * Liste cha�n�e contenant les clefs associ�es aux entr�es.
     */
    protected List key_code_list = null;

    /**
     * Extention utilis�e pour la sauvegarde/lecture de fichiers de la liste des
     * clefs.
     */
    protected static final String KEY_LIST_EXTENSION = ".klist";

    /**
     * Extension utilis�e pour la sauvegarde/lecture de fichiers de la liste des
     * codes d'inversion.
     */
    protected static final String INV_LIST_EXTENSION = ".ilist";

    /***************************************************************************
     * Constructeur d'un arbre lexical bidirectionnel. Le param�tre permet de
     * sp�cifi� si les mots contenu dans la m�moire sont stock�s en long[]
     * (lex_of_long vrai) ou en String (lex_of_long_faux).
     *
     * @param lex_of_long Oui/non les mots sont stock�s en m�moire au format long[]
     **************************************************************************/
    public BidirectionalTrie(boolean lex_of_long)
    {
        super(lex_of_long);
        this.inv_code_list = new List();
        this.key_code_list = new List();
    }

    /***************************************************************************
     * Ajoute une nouvelle entr�e � la table, soit la clef et une graphie sp�cifi�e
     * au format String. Une exception est lev�e si le param�tre graphie n'est
     * pas assign� (null) et si cette entr�e est d�j� contenue dans la m�moire.
     *
     * @param key Valeur de la clef de l'entr�e � ins�rer
     * @param graphy Cha�ne de caract�res de l'entr�e � ins�rer
     **************************************************************************/
    public void insert(long key, String graphy)
    {
        utils.checkParameterIsAssigned(graphy);

        long inversion_code;

        // Utiliser la fonction d'insertion selon de type d'arbre
        if (this.lex_of_long)
            inversion_code = NativeArbreLex.insert(this.native_data, key, utils.stringToLongArray(graphy));

        else
            inversion_code = NativeArbreLex.insert(this.native_data, key, graphy);

        // Ajouter la nouvelle entr�e dans les listes cha�n�es
        this.inv_code_list.append(new Long(inversion_code));
        this.key_code_list.append(new Long(key));

        this.key = key + 1;
    }

    /***************************************************************************
     * Ajoute une nouvelle entr�e � la table, soit la clef et une graphie sp�cifi�e
     * au format long[]. Une exception est lev�e si la graphie n'est pas assign�e
     * (null), si la graphie ne peut pas �tre ins�r�e ou si l'entr�e est d�j�
     * contenue dans la m�moire.
     *
     * @param key Valeur de la clef de l'entr�e � ins�rer
     * @param sequence Cha�ne de caract�res de l'entr�e � ins�rer
     **************************************************************************/
    public void insert(long key, long[] sequence)
    {
        utils.checkParameterIsAssigned(sequence);

        long inversion_code;

        // Utiliser la fonction d'insertion selon de type d'arbre
        if (this.lex_of_long)
            inversion_code = NativeArbreLex.insert(this.native_data, key, sequence);

        else
            inversion_code = NativeArbreLex.insert(this.native_data, key, utils.longArrayToString(sequence));

        // Ajouter la nouvelle entr�e dans les listes cha�n�es
        this.inv_code_list.append(new Long(inversion_code));
        this.key_code_list.append(new Long(key));

        this.key = key + 1;
    }

    /**************************************************************************/
    public String[] accessString(long searched_key)
    {
        long[][] buffer = this.access(searched_key);

        // Effectuer la conversion long[][] -> String[]
        String[] result = new String[buffer.length];
        for(int index = 0; index < result.length; index++)
            result[index] = utils.longArrayToString(buffer[index]);

        return result;
    }

    /**************************************************************************/
    public long[][] access(long searched_key)
    {
        List result_list = new List();

        // Pour toutes les entr�es contenues dans l'arbre ...
        for(int index = 0; index < this.key_code_list.getSize(); index++)
        {
            // Extraire la clef et le code d'inversion
            long key = ((Long)key_code_list.getFromIndex(index)).longValue();
            long inversion_code = ((Long)inv_code_list.getFromIndex(index)).longValue();
            long[] sequence = null;

            // Si la clef de l'entr�e correspond � celle recherch�e ...
            if (key == searched_key)
            {
                // Extraire la s�quence correspondante
                if (this.lex_of_long)
                    sequence = NativeArbreLex.accessSequence(this.native_data, inversion_code);

                else
                {
                    String string = NativeArbreLex.accessString(this.native_data, inversion_code);
                    sequence = utils.stringToLongArray(string);
                }

                // Ajouter la s�quence � la liste des r�sultats
                result_list.append(sequence);
            }
        }

        // Effectuer la conversion List de long[] -> long[][]
        long[][] result = new long[(int)result_list.getSize()][];
        for(int index = 0; index < result.length; index++)
        {
            long[] element = (long[])result_list.getFromIndex(index);
            result[index] = element;
        }

        return result;
    }

    /**************************************************************************/
    public void saveToFile(String filename) throws IOException
    {
        super.saveToFile(filename);

        // Sauvegarde la liste des clefs et des codes d'inversion dans des fichiers annexes
        this.saveLongList(this.inv_code_list, filename + INV_LIST_EXTENSION);
        this.saveLongList(this.key_code_list, filename + KEY_LIST_EXTENSION);
    }

    /**************************************************************************/
    public void loadFromFile(String filename) throws IOException
    {
        super.loadFromFile(filename);

        // Charge la liste des clefs et des codes d'inversion dans des fichiers annexes
        this.loadLongList(this.inv_code_list, filename + INV_LIST_EXTENSION);
        this.loadLongList(this.key_code_list, filename + KEY_LIST_EXTENSION);
    }

    /**************************************************************************/
    public void importContents(String filename) throws IOException
    {
        super.importContents(filename);

        // Charger la liste des codes d'inversion � partir du fichier annexe
        this.loadLongList(this.inv_code_list, filename + INV_LIST_EXTENSION);

        // La liste des clefs associ�es aux entr�es est simplement la suite enti�re de 0 � N
        this.key_code_list.clear();
        for(int index = 0; index < this.inv_code_list.getSize(); index++)
            this.key_code_list.append(new Long(index));
    }

    /**************************************************************************/
    public void exportContents(String filename) throws IOException
    {
        super.exportContents(filename);

        // Sauvegarde la liste des codes d'inversion dans un fichier annexe
        this.saveLongList(this.inv_code_list, filename + INV_LIST_EXTENSION);
    }

    /***************************************************************************
     * Sauvegarde la liste de Long dans un fichier texte (une entr�e par ligne).
     *
     * @param input Liste de Long
     * @param filename Nom du fichier texte destination
     * @throws IOException Si probl�me d'�criture de fichier
     **************************************************************************/
    private static void saveLongList(List input, String filename) throws IOException
    {
        FileWriter writer = new FileWriter(filename);
        BufferedWriter buffered_writer = new BufferedWriter(writer);

        for(int index = 0; index < input.getSize(); index++)
        {
            if (index > 0)
                buffered_writer.newLine();

            String value = ((Long)input.getFromIndex(index)).toString();
            buffered_writer.write(value);
        }

        buffered_writer.close();
        writer.close();
    }

    /***************************************************************************
     * Charge une liste de Long avec le contenu d'un fichier texte (une entr�e
     * par ligne).
     *
     * @param output Liste de Long
     * @param filename Nom du fichier texte source
     * @throws IOException Si probl�me de lecture de fichier
     **************************************************************************/
    private static void loadLongList(List output, String filename) throws IOException
    {
        FileReader reader = new FileReader(filename);
        BufferedReader buffered_reader = new BufferedReader(reader);

        output.clear();

        for(String line = buffered_reader.readLine(); line != null; line = buffered_reader.readLine())
            output.append(new Long(line));

        buffered_reader.close();
        reader.close();
    }
}
