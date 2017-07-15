package SyntacticAnalyzer.java.SlpToolKit;

/**
 * <p>Title: Projet de semestre</p>
 * <p>Description: Surcouche Java � la librairie SlpToolKit</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Ecole Polytechnique de Lausanne (EPFL) - D�partement d'intelligence arttficielle (LIA)</p>
 * @author Antonin Mer�ay
 * @version 1.0
 */

import java.io.*;

/*******************************************************************************
 * Fonctions utilitaires utilis�es par les diff�rentes classes du package
 ******************************************************************************/
public class utils {
    /***************************************************************************
     * Convertit la cha�ne de caract�re pass�e en param�tre en un tableau de long
     *
     * @param input Cha�ne de caract�res � convertir
     * @return Tableau de long
     **************************************************************************/
    public static long[] stringToLongArray(String input)
    {
        long[] array_of_long = new long[input.length()];

        for(int i = 0; i < array_of_long.length; i++)
            array_of_long[i] = input.charAt(i);

        return array_of_long;
    }

    /***************************************************************************
     * Convertit le tableau de long pass� en param�tre en une cha�ne de
     * caract�res. Une exception est lev�e si la conversion est impossible.
     *
     * @param input Tableau de long � convertir
     * @return Cha�ne de caract�res
     **************************************************************************/
    public static String longArrayToString(long[] input)
    {
        char[] array_of_char = new char[input.length];

        for(int i = 0; i < array_of_char.length; i++)
        {
            if (input[i] < 256)
                array_of_char[i] = (char)input[i];

            else
                throw new RuntimeException("can't convert " + input[i] + " from long to char");
        }

        return new String(array_of_char);
    }

    /***************************************************************************
     * Leve une exception si le param�tre donn� en param�tre n'est pas assign�
     * (null).
     *
     * @param o Object dont l'assignation doit �tre v�rifi�e
     **************************************************************************/
    public static void checkParameterIsAssigned(Object o)
    {
        if (o == null)
            throw new NullPointerException();
    }

    /***************************************************************************
     * Affiche un message d'avertissement dans la console d�di�e.
     *
     * @param message Message � afficher
     **************************************************************************/
    public static void printWarning(String message)
    {
        System.err.println("WARNING: " + message);
    }
}