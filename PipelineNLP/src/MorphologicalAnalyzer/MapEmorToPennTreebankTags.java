package MorphologicalAnalyzer;

import LexicalAnalyzer.FSA.WordTag;

import java.io.*;
import java.util.*;

/**
 * Created by MeryemMhamdi on 5/22/17.
 */
public class MapEmorToPennTreebankTags {
    public static void main (String [] args){
        String location = "/Users/MeryemMhamdi/Google Drive/Semester Project/3 Implementation & Algorithms/Syntactic Analysis/";

        String grammarFile = "penn_treebank_grammar.txt";

        String tagsetFile = "pennTreebankTagset.txt";

        String emor = "/Users/MeryemMhamdi/Google Drive/Semester Project/3 Implementation & Algorithms/Morphological Analysis/EMOR/xtag-morph-1.5a.txt";


        ArrayList<String> tagset = new ArrayList<String>();
        ArrayList<WordTag> wordEntries = new ArrayList<WordTag>();


        try {
            BufferedReader br = new BufferedReader(new FileReader(location+tagsetFile));
            String line = br.readLine();
            while (line != null) {
                tagset.add(line.split(" ")[0]);
                line = br.readLine();
            }

            br = new BufferedReader(new FileReader(location+grammarFile));
            line = br.readLine();
            while (line != null) {
                tagset.add(line.split("->")[0].split(" ")[0]);
                line = br.readLine();
            }
            System.out.println(tagset);

            br = new BufferedReader(new FileReader(location+grammarFile));
            line = br.readLine();
            while (line != null) {
                String candidate = line.split("->")[1].split(" ")[1];
                String tag = line.split("->")[0].split(" ")[0];
                if(!tagset.contains(candidate)){
                    wordEntries.add(new WordTag(candidate.substring(1,candidate.length()-1),tag));
                }
                line = br.readLine();
            }

            System.out.println("Transform Word Tags List to HashMap");
            HashMap<String,ArrayList<String>> wordTags = new HashMap<String,ArrayList<String>>();
            for (int i=0;i<wordEntries.size();i++){
                if (wordTags.containsKey(wordEntries.get(i).getWord())){
                    ArrayList<String> values = wordTags.get(wordEntries.get(i).getWord());
                    values.add(wordEntries.get(i).getTag());
                    wordTags.put(wordEntries.get(i).getWord(),values);
                } else {
                    ArrayList<String> values = new ArrayList<String>();
                    values.add(wordEntries.get(i).getTag());
                    wordTags.put(wordEntries.get(i).getWord(),values);
                }
            }
            System.out.println("Running of EMOR Analysis"+wordTags.size());
            br = new BufferedReader(new FileReader(emor));
            line = br.readLine();

            ArrayList<WordTag> emorList = new ArrayList<WordTag>();

            while (line != null && line.length() > 1) {
                if (line.toString().contains("#")) {
                    String parts[] = line.toString().split("#");

                    String subParts[] = parts[0].split("\\s++");
                    String word = subParts[0];
                    String emorTags = "";
                    for (int j=2;j<subParts.length-1;j++){
                        emorTags = emorTags+ subParts[j]+" " ;
                    }
                    emorTags = emorTags+ subParts[subParts.length-1] ;
                    System.out.println("Word= "+subParts[0]+" Tag= "+subParts[1]+" Lemma= "+emorTags);
                    emorList.add(new WordTag(word,emorTags,subParts[1]));

                    for (int i = 1; i <parts.length;i++){
                        subParts = parts[i].split("\\s++");
                        emorTags = "";
                        for (int j=1;j<subParts.length-1;j++){
                            emorTags = emorTags+ subParts[j]+" " ;
                        }
                        emorTags = emorTags+ subParts[subParts.length-1] ;
                        System.out.println("Word= "+word+" Tag= "+subParts[1]+" Lemma= "+emorTags);
                        emorList.add(new WordTag(word,emorTags,subParts[0]));
                    }
                }else {
                    String parts[] = line.toString().split("\\s++");
                    String emorTags = "";
                    for (int j=2;j<parts.length-1;j++){
                        emorTags = emorTags+ parts[j]+" " ;
                    }
                    emorTags = emorTags+ parts[parts.length-1] ;
                    System.out.println("Word= "+parts[0]+" Tag= "+parts[1]+" Lemma= "+emorTags);
                    emorList.add(new WordTag(parts[0],emorTags,parts[1]));
                }
                line = br.readLine();
            }

            System.out.println("Transform Emor Tags List to HashMap");
            HashMap<String,ArrayList<String>> EMORHashMap = new HashMap<String,ArrayList<String>>();
            HashMap<String,ArrayList<String>> EMORHashMapLemma = new HashMap<String,ArrayList<String>>();

            for (int i=0;i<emorList.size();i++){
                if (EMORHashMap.containsKey(emorList.get(i).getWord())){
                    ArrayList<String> values = EMORHashMap.get(emorList.get(i).getWord());
                    values.add(emorList.get(i).getTag());
                    EMORHashMap.put(emorList.get(i).getWord(),values);
                } else {
                    ArrayList<String> values = new ArrayList<String>();
                    values.add(emorList.get(i).getTag());
                    EMORHashMap.put(emorList.get(i).getWord(),values);
                }
            }

            for (int i=0;i<emorList.size();i++){
                if (EMORHashMapLemma.containsKey(emorList.get(i).getWord())){
                    ArrayList<String> values = EMORHashMapLemma.get(emorList.get(i).getWord());
                    values.add(emorList.get(i).getLemma());
                    EMORHashMapLemma.put(emorList.get(i).getWord(),values);
                } else {
                    ArrayList<String> values = new ArrayList<String>();
                    values.add(emorList.get(i).getLemma());
                    EMORHashMapLemma.put(emorList.get(i).getWord(),values);
                }
            }

            System.out.println("Running the Mapping");
            HashMap<String,String> mappingPennTreebankEMOR = new HashMap<String,String>();
            int i = 0;
            for (String word: wordTags.keySet()) {
                i ++;
                if (i%10==0) {
                    System.out.println("i=> " + i);
                }

                //Process process = new ProcessBuilder("/Users/MeryemMhamdi/EPFL/Spring2017/SemesterProject/Morphological Analysis/SFST/src/fst-mor1", "/Users/MeryemMhamdi/EPFL/Spring2017/SemesterProject/Morphological Analysis/EMOR/emor.a", word).start();
                //InputStream is = process.getInputStream();
                //InputStreamReader isr = new InputStreamReader(is);
                //br = new BufferedReader(isr);

                //ArrayList<String> list = new ArrayList<String>();
                //line = br.readLine();

                //while (line != null && line.length() > 1) {
                //    list.add(line.toString());
                //    line = br.readLine();
                //}
                System.out.println(word);
                if (EMORHashMap.containsKey(word)) {
                    if (EMORHashMap.get(word).size() == 1) {
                        if (wordTags.get(word).size() == 1) {
                            String[] parts = EMORHashMap.get(word).get(0).split("\\s++");
                            String result = "";
                            for (int p = 0; p < parts.length; p++) {
                                result = result + "<" + parts[p] + ">";
                            }
                            mappingPennTreebankEMOR.put(result,wordTags.get(word).get(0));

                        }
                    }
                }

            }
            System.out.println("mappingPennTreebankEMOR="+mappingPennTreebankEMOR);
            String location_TagsMap = "/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/tagsMap.ser";
            FileOutputStream fos = new FileOutputStream(location_TagsMap);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(mappingPennTreebankEMOR);
            os.flush();

            List<String> pennTags = new ArrayList<String>();
            for (i=0;i<wordEntries.size();i++){
                pennTags.add(wordEntries.get(i).getTag());
            }
            Set<String> uniquePennTags = new HashSet<String>(pennTags);

            List<String> emorTags = new ArrayList<String>();
            for (i=0;i<emorList.size();i++){
                emorTags.add(emorList.get(i).getTag());
            }
            Set<String> uniqueEmorTags = new HashSet<String>(emorTags);


            BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/PennTags.txt"));
            writer.write(uniquePennTags.toString());
            writer.close();

            writer = new BufferedWriter(new FileWriter("/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/EMORTags.txt"));
            writer.write(uniqueEmorTags.toString());
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
