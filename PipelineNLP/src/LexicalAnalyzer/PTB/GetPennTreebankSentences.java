package LexicalAnalyzer.PTB;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Created by MeryemMhamdi on 5/19/17.
 */
public class GetPennTreebankSentences {
    public static void main (String args[]){
        ArrayList<String> sentences = new ArrayList<String>();

        try(Stream<Path> paths = Files.walk(Paths.get("/Users/MeryemMhamdi/Google Drive/Semester Project/3 Implementation & Algorithms/Datasets/treebank/RawCorpus"))) {
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    BufferedReader br = null;
                    try {
                        if (Files.isRegularFile(filePath) && !filePath.toString().equals("/Users/MeryemMhamdi/Google Drive/Semester Project/3 Implementation & Algorithms/Datasets/treebank/RawCorpus/.DS_Store")) {
                            br = new BufferedReader(new FileReader(filePath.toString()));
                            StringBuilder sb = new StringBuilder();
                            String line = br.readLine();
                            while (line != null) {
                                sb.append(line);
                                sb.append(System.lineSeparator());
                                line = br.readLine();
                            }
                            String everything = sb.toString();
                            //System.out.println(everything);

                            for (int o = 1; o < everything.split("\n+").length; o++) {
                                String sentence = everything.split("\n+")[o];
                                if (sentence.charAt(sentence.length()-1)==' ') {
                                    sentences.add(sentence.substring(0, sentence.length() - 1));
                                }else {
                                    sentences.add(sentence);
                                }
                                //br.close();
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/Sentences.txt"));
            for (int i=0;i<sentences.size();i++){
                writer.write(sentences.get(i)+"\n");
            }
            System.out.println(sentences.size());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
