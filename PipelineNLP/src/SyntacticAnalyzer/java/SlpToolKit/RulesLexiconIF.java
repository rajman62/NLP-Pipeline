package SyntacticAnalyzer.java.SlpToolKit;

/**
 * <p>Title: Projet de semestre</p>
 * <p>Description: Surcouche Java � la librairie SlpToolKit</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Ecole Polytechnique de Lausanne (EPFL) - D�partement d'intelligence artificielle (LIA)</p>
 * @author Antonin Mer�ay
 * @version 1.0
 */

/*******************************************************************************
 * Ensemble des m�thodes devant �tre impl�ment�es par un lexique des r�gles.
 ******************************************************************************/
public interface RulesLexiconIF extends LexiconIF {
    /***************************************************************************
     * Recherche dans le lexique l'ensemble des entr�es dont la production
     * correspond � celle donn�e en param�tre. Retourne un tableau des entr�es trouv�es.
     *
     * @param entry Production de la r�gle recherch�e
     * @return Tableau des entr�es trouv�es
     **************************************************************************/
    RulesLexiconEntry[] lookForAll(RulesLexiconEntry entry);

    /***************************************************************************
     * Recherche dans le lexique la premi�re entr�e dont la production
     * correspond � celle donn�e en param�tre.
     *
     * @param entry Production de la r�gle recherch�e
     * @return Premi�re entr�e trouv�e
     **************************************************************************/
    RulesLexiconEntry lookFor(RulesLexiconEntry entry);
}