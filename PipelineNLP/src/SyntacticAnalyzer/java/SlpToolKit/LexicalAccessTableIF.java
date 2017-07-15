package SyntacticAnalyzer.java.SlpToolKit;

import java.io.IOException;

/**
 * <p>Title: Projet de semestre</p>
 * <p>Description: Surcouche Java � la librairie SlpToolKit</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Ecole Polytechnique de Lausanne (EPFL) - D�partement d'intelligence artficielle (LIA)</p>
 * @author Antonin Mer�ay
 * @version 1.0
 */

/*******************************************************************************
 * Ensemble des m�thodes auxquelles doivent r�pondre une table d'acc�s
 * lexicale. Chaque entr�e de la table est caract�ris�e par une cha�ne de
 * caract�res et une clef qui lui est automatiquement associ�e. L'acc�s aux
 * �l�ments de la table se fait uniquement par la graphie.
 ******************************************************************************/
public interface LexicalAccessTableIF {
    /***************************************************************************
     * Ajoute une nouvelle cha�ne de caract�res (sous forme de String) � la
     * table d'acc�s. La valeur de la clef associ�e � la cha�ne ins�r�e est
     * retourn�e. Une exception est lev�e si le param�tre n'est pas assign�
     * (null).
     *
     * @param graphy Cha�ne de caract�re � ins�rer dans la table
     * @return Valeur de la clef associ�e � la cha�ne ins�r�e
     **************************************************************************/
    long insert(String graphy);

    /***************************************************************************
     * Ajoute une nouvelle cha�ne de caract�res (sous forme de long[]) � la
     * table d'acc�s. La valeur de la clef associ�e � la cha�ne ins�r�e est
     * retourn�e. Une exception est lev�e si le param�tre n'est pas assign�
     * (null) ou si la valeur ordinale d'un des caract�res de la cha�ne est
     * trop grande pour pouvoir �tre stock�e dans la table.
     *
     * @param sequence Cha�ne de caract�re � ins�rer dans la table
     * @return Valeur de la clef associ�e � la cha�ne ins�r�e
     **************************************************************************/
    long insert(long[] sequence);

    /***************************************************************************
     * Ex�cute la recherche dans la table d'une cha�ne de caract�res (sous forme
     * de String). Une nouvelle instance de SearchResult contenant le r�sultat
     * est retourn�e. Si le param�tre n'est pas assign� (null), une exception
     * est lev�e.
     *
     * @param element Graphie de la cha�ne recherch�e
     * @return R�sultat de le recherche
     **************************************************************************/
    SearchResult search(String element);

    /***************************************************************************
     * Ex�cute la recherche dans la table d'une cha�ne de caract�res (sous forme
     * de long[]). Une nouvelle instance de SearchResult contenant le r�sultat
     * est retourn�e. Si le param�tre n'est pas assign� (null) ou si la valeur
     * ordinale des caract�res de la cha�ne recherch�e est trop importante, une
     * exception est lev�e et la valeur null est retourn�e par la m�thode.
     *
     * @param sequence Graphie de la cha�ne recherch�e
     * @return R�sultat de le recherche
     **************************************************************************/
    SearchResult search(long[] sequence);

    /***************************************************************************
     * Retourne le nombre d'entr�es stock�es dans la table.
     *
     * @return Nombre d'entr�es stock�es de la table
     **************************************************************************/
    long getSize();

    /***************************************************************************
     * Retourne la cardinalit� des caract�res stock�s dans la table. La valeur
     * retourn�e correspond au nombre de bits n�cessaire au codage des
     * caract�res de l'alphabet.
     *
     * @return Cardinalit� des caract�res stock�s dans la table
     **************************************************************************/
    int getAlphabetCardinality();

    /***************************************************************************
     * Sauvegarde la table dans un fichier dont le nom (sans extension) est
     * sp�cifi� en param�tre. Le format du fichier cr�� d�pend de
     * l'impl�mentation concr�te de la table d'acc�s lexicale. Une exception
     * est lev�e en cas de probl�me d'�criture du fichier destination.
     *
     * @param filename Nom du fichier destination
     * @throws IOException Si probl�me d'�criture de fichier
     **************************************************************************/
    void saveToFile(String filename) throws IOException;

    /***************************************************************************
     * Charge la table � partir d'un fichier de sauvegarde dont le nom (sans extension)
     * est sp�cifi� en param�tre (voir {@link #saveToFile(String) saveToFile(String)}).
     * Une exception est lev�e en cas de probl�me de lecture du fichier source.
     *
     * @param filename Nom du fichier source
     * @throws IOException Si probl�me de lecture de fichier
     **************************************************************************/
    void loadFromFile(String filename) throws IOException;

    /***************************************************************************
     * Charge dans la table la liste des mots contenus dans le fichier dont le
     * nom est donn� en param�tre. Une exception est lev�e en cas de probl�me
     * de lecture du fichier source.
     *
     * @param filename Nom du fichier textuel source
     * @throws IOException Si probl�me de lecture de fichier
     **************************************************************************/
    void importContents(String filename) throws IOException;

    /***************************************************************************
     * Sauvegarde la liste des mots stock�s dans la table dans un fichier dont
     * le nom est sp�cifi� en param�tre. Une exception est lev�e en cas de
     * probl�me d'�criture du fichier destination.
     *
     * @param filename Nom du fichier textuel destination
     * @throws IOException Si probl�me d'�criture de fichier
     **************************************************************************/
    void exportContents(String filename) throws IOException;

    /***************************************************************************
     * Liste � l'�cran le contenu de la table. Chaque entr�e est affich�e sur
     * une ligne, la graphie �tant suivie de la clef lui �tant associ�e.
     **************************************************************************/
    void listContents();

    /***************************************************************************
     * Affecte au premier caract�re la position courante de parcours des mots
     * de la table.
     **************************************************************************/
    void goToRoot();

    /***************************************************************************
     * Avance d'un caract�re la position courante de parcours des mots de la
     * table. Ce caract�re correspond au dernier retourn� par la m�thode
     * {@link #getNextChar() getNextChar()}.
     **************************************************************************/
    void goToChar();

    /***************************************************************************
     * Retourne le prochain caract�re admissible � partir de la position
     * actuelle dans l'arbre. Cette m�thode est appel�e it�rativement et
     * retourne la valeur 0 lorsque plus aucun autre caract�re n'est trouv�.
     *
     * @return la valeur ordinale du caract�re admissible
     **************************************************************************/
    long getNextChar();

    /***************************************************************************
     * Retourne si oui ou non la position courante de parcours correspond � la
     * fin d'une cha�ne de caract�re contenue dans la table.
     *
     * @return Oui/non la position courante correspond � une mot contenu dans la table
     **************************************************************************/
    boolean endOfWord();
}