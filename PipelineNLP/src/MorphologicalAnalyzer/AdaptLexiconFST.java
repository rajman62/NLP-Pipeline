package MorphologicalAnalyzer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by MeryemMhamdi on 5/20/17.
 */
public class AdaptLexiconFST {
    public static void main(String [] args){
        try {
            ArrayList<String> existingWords = new ArrayList<String>();
            BufferedReader br1 = new BufferedReader(new FileReader("/Users/MeryemMhamdi/Google Drive/Semester Project/3 Implementation & Algorithms/Morphological Analysis/EMOR/xtag-morph-1.5a.txt"));
            String line = br1.readLine();
            int countBig = 0;
            while (line != null) {
                countBig ++;
                String parts [] = line.split("\\s++");
                existingWords.add(parts[0].substring(0,parts[0].length()));
                line = br1.readLine();
            }
            ArrayList<String> missingWords = new ArrayList<String>();
            BufferedReader br = new BufferedReader(new FileReader("/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/wordslist.txt"));
            line = br.readLine();
            int count = 0;
            int count1 = 0;
            while (line != null) {
                count1 ++;
                if (!existingWords.contains(line)) {
                    String lower = line.toLowerCase();
                    if (!existingWords.contains(lower)) {
                        count++;
                        missingWords.add(line);
                    }
                }
                line = br.readLine();
            }
            for (int i=0;i<missingWords.size();i++){
                System.out.println(missingWords.get(i));
            }

            System.out.println("count >>>> "+count);
            System.out.println("count1 >>>> "+count1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
