package SyntacticAnalyzer.java.SlpToolKit;

import java.io.IOException;

/**
 * <p>Title: Projet de semestre</p>
 * <p>Description: Surcouche Java � la librairie SlpToolKit</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Ecole Polytechnique de Lausanne (EPFL) - D�partement d'intelligence artificielle (LIA)</p>
 * @author Antonin Mer�ay
 * @version 1.0
 */

/*******************************************************************************
 * Ensemble des m�thodes devant �tre impl�ment�es par un lexique g�n�rique.
 ******************************************************************************/
public interface LexiconIF {
    /**
     * Message d'erreur affich� si l'on tente de normaliser le lexique alors que
     * l'op�ration a d�j� �t� r�alis�e
     */
    static String LEXICON_ALREADY_NORMALIZED_ERROR_MESSAGE = "lexicon already normalized";

    /***************************************************************************
     * Retourne la m�moire associative lexicale encapsul�e dans le lexique.
     *
     * @return M�moire lexicale encapsul�e
     **************************************************************************/
    LexicalAccessTableIF getAM();

    /***************************************************************************
     * Sauvegarde le contenu du lexique dans un fichier au format ASCII. Une
     * exception est lev�e en cas de probl�me d'�criture du fichier.
     *
     * @param filename Nom du fichier destination
     * @throws IOException Si probl�me d'�criture de fichier
     **************************************************************************/
    void exportToASCII(String filename) throws IOException;

    /***************************************************************************
     * Charge le lexique avec le contenu d'un fichier ASCII dont le nom est
     * donn� en param�tre. Une exception est lev�e en cas de probl�me de lecture.
     *
     * @param filename Nom du fichier source
     * @throws IOException Si probl�me de lecture de fichier
     **************************************************************************/
    void importFromASCII(String filename) throws IOException;

    /***************************************************************************
     * Charge le lexique avec l'image m�moire contenue dans le fichier. Une
     * exception est lev�e en cas de probl�me de lecture.
     *
     * @param filename Nom du fichier source
     * @throws IOException Si probl�me de lecture de fichier
     **************************************************************************/
    void loadFromFile(String filename) throws IOException;

    /***************************************************************************
     * Sauvegarde l'image m�moire du lexique dans un fichier. Une exception est
     * lev�e en cas de probl�me d'�criture.
     *
     * @param filename Nom du fichier destination
     * @throws IOException Si probl�me d'�criture de fichier
     **************************************************************************/
    void saveToFile(String filename) throws IOException;

    /***************************************************************************
     * Normalise les probabilit�s du lexique.
     **************************************************************************/
    void normalize();

    /***************************************************************************
     * Retourne si les probabilit�s du lexique ont �t� normalis�es (
     * l'op�ration {@link #normalize() normalize()} a �t� ex�cut�e).
     *
     * @return Valeur bool�enne indique oui ou non
     **************************************************************************/
    boolean isNormalized();

    /***************************************************************************
     * Ajoute au lexique une nouvelle entr�e dont les informations sont sp�cifi�es
     * par l'entr�e en param�tre. La premi�re insertion permet de d�finir les champs pris
     * en compte. La m�thode retourne l'indice de l'entr�e dans le lexique
     * (incr�ment�e lors de chaque insertion). Une exception est lev�e si l'entr�e
     * ne d�finit pas tous les champs n�cessaires ou si l'entr�e � ins�rer existe
     * d�j� dans le lexique (doublon). Dans ces cas-l�, la valeur retourn�e par
     * la m�thode est -1.
     *
     * @param entry Nouvelle entr�e � ins�rer
     * @return Indice de l'insertion
     **************************************************************************/
    long insert(LexiconEntry entry);

    /***************************************************************************
     * Retourne si oui ou non le lexique contient une entr�e dont les informations
     * sont d�finies par l'entr�e en param�tre. Une exception est lev�e si les
     * informations stock�es dans l'entr�e sont incompl�tes.
     *
     * @param entry Entr�e dont il faut v�rifier la pr�sence
     * @return Valeur bool�enne oui/non
     **************************************************************************/
    boolean contains(LexiconEntry entry);

    /***************************************************************************
     * Retourne le nombre d'entr�es contenues dans le lexique.
     *
     * @return Nombre d'entr�es contenues
     **************************************************************************/
    long getSize();

    /***************************************************************************
     * Retourne les informations relatives � l'entr�e du lexique index�e par
     * le param�tre. Une exception est lev�e si la valeur de l'indice n'est
     * pas valide.
     *
     * @param index Indice de l'entr�e � extraire
     * @return Entr�e contenant les informations recherch�es
     **************************************************************************/
    LexiconEntry getEntryFromIndex(long index);

    /***************************************************************************
     * Recherche dans le lexique la premi�re entr�e correspondant au crit�re de
     * recherche sp�cifi� en param�tre.
     *
     * @param o Crit�re de recherche
     * @return Premi�re entr�e trouv�e
     **************************************************************************/
    LexiconEntry lookFor(Object o);

    /***************************************************************************
     * Recherche dans le lexique l'ensemble des entr�es correspondant au crit�re
     * de recherche sp�cifi� en param�tre. Retourne un tableau des entr�es
     * trouv�es.
     *
     * @param o Crit�re de recherche
     * @return Tableau des entr�es trouv�es
     **************************************************************************/
    LexiconEntry[] lookForAll(Object o);

    /***************************************************************************
     * Retourne le pointeur sur le lexique natif sous-jaccent
     *
     * @return Pointeur
     **************************************************************************/
    long getNativeLexicon();
}