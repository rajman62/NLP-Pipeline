package LexicalAnalyzer.FSA;

import java.util.ArrayList;


public class NbitsStrings {
    int[] arrA;
    ArrayList<ArrayList<Integer>> arrBits;

    public NbitsStrings(int n) {
        arrA = new int[n];
        arrBits = new ArrayList<ArrayList<Integer>>();
    }

    public ArrayList<ArrayList<Integer>> nBits(int n) {
        if (n <= 0) {
            ArrayList<Integer> list = new ArrayList<Integer>();
            for (int i=0;i<arrA.length;i++) {
                list.add(arrA[i]);
            }
            arrBits.add(list);
        } else {
            arrA[n - 1] = 0;
            nBits(n - 1);
            arrA[n - 1] = 1;
            nBits(n - 1);
        }
        return arrBits;
    }

    public static void main(String[] args) throws java.lang.Exception {
        int n = 3;
        NbitsStrings nbitsString = new NbitsStrings(n);
        ArrayList<ArrayList<Integer>> arrBits = nbitsString.nBits(n);
        System.out.println(arrBits);
    }
}