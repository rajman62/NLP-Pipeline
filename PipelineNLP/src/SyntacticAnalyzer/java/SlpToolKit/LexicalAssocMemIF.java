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
 * Ensemble des m�thodes auxquelles doivent r�pondre une m�moire associative
 * lexicale. L'acc�s aux �l�ments de la m�moire peut se faire par la graphie
 * et par la clef.
 ******************************************************************************/
public interface LexicalAssocMemIF extends LexicalAccessTableIF {
    /***************************************************************************
     * Retourne la liste des cha�nes de caract�res (sous forme de String)
     * dont la clef est �gale � la valeur donn�e en param�tre. Une exception
     * est lev�e si l'un des caract�res des cha�nes retourn�es ne peut �tre
     * contenue dans une String (code ordinal sup�rieur � 255).
     *
     * @param key Clef recherch�e
     * @return Tableau des graphies trouv�es dans la m�moire
     **************************************************************************/
    String[] accessString(long key);

    /***************************************************************************
     * Retourne la liste des cha�nes de caract�res (sous forme de long[]) dont
     * la clef est �gale � la valeur donn�e en param�tre.
     *
     * @param key Clef recherch�e
     * @return Tableau des graphies trouv�es dans la m�moire
     **************************************************************************/
    long[][] access(long key);
}