package SyntacticAnalyzer.java.SlpToolKit;

import java.util.Vector;

/*******************************************************************************
 * Liste d'objets fourni pour SlpToolKit
 ******************************************************************************/
public class List {
    /**
     * Liste Java sous-jacente
     */
    private Vector list;

    /***************************************************************************
     * Constructeur
     **************************************************************************/
    public List()
    {
        this.list = new Vector();
    }

    /***************************************************************************
     * Retourne le nombre d'�l�ments contenu dans la liste
     *
     * @return Taille de la liste
     **************************************************************************/
    public long getSize()
    {
        return this.list.size();
    }

    /***************************************************************************
     * Retourne l'�l�ment stock� � un indice donn�.
     *
     * @param index Indice dans la liste
     * @return Objet stock� � l'indice sp�cifi�
     **************************************************************************/
    public Object getFromIndex(long index)
    {
        return this.list.get((int)index);
    }

    /***************************************************************************
     * Ajoute un objet � la fin de la liste
     *
     * @param o Objet � ins�rer
     **************************************************************************/
    public void append(Object o)
    {
        this.list.add(o);
    }

    /***************************************************************************
     * Retourne si un objet donn� est contenu dans la liste
     *
     * @param o Objet dont la pr�sence dans la liste doit �tre test�e
     * @return Indicateur de pr�sence vrai/faux
     **************************************************************************/
    public boolean contains(Object o)
    {
        return this.list.contains(o);
    }

    /***************************************************************************
     * Retourne l'indice dans la liste d'un objet donn�
     *
     * @param o Objet dont il faut trouver l'indice
     * @return Indice dans la liste
     **************************************************************************/
    public long getIndexOf(Object o)
    {
        return this.list.indexOf(o);
    }

    /***************************************************************************
     * Efface le contenu de la liste
     **************************************************************************/
    public void clear()
    {
        this.list.clear();
    }

    /***************************************************************************
     * Dans le cas o� il n'est pas d�j� pr�sent dans la liste, ajoute un objet
     * en queue. Retourne l'indice dans la liste de l'objet, qu'il ait d� �tre
     * ins�r� ou non.
     *
     * @param o Objet � ins�rer
     * @return Indice de l'objet dans la liste
     **************************************************************************/
    public long safeAppend(Object o)
    {
        if (this.list.contains(o) == false)
            this.list.add(o);

        return this.list.indexOf(o);
    }
}