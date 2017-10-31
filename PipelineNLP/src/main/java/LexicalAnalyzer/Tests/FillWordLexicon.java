package LexicalAnalyzer.Tests;

import LexicalAnalyzer.FSA.WordTag;

import java.io.BufferedReader;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by MeryemMhamdi on 4/10/17.
 */
public class FillWordLexicon {
    public static void main(String[] args) {

        /**
         * 1. Read the Penn treebank tagset from the file
         */


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
                            mappingPennTreebankEMOR.put(wordTags.get(word).get(0), EMORHashMap.get(word).get(0));
                        }
                    }
                }
            }

            // Add each word to a file specific to its tag category in order to apply FSTs on it later

            FileWriter fw = new FileWriter(emor, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.println();

            HashMap<String,ArrayList<String>> missingWords = new HashMap<String,ArrayList<String>>(); // Tag, List<Words>
            for (String word: wordTags.keySet()){
                if (!EMORHashMap.containsKey(word)) {
                    String lower = word.toLowerCase();
                    if (!EMORHashMap.containsKey(lower)) {
                        ArrayList<String> tagList = wordTags.get(word);
                        for (String tag: tagList) {
                            if (missingWords.containsKey(tag)) {
                                ArrayList<String> words = missingWords.get(tag);
                                words.add(word);
                                missingWords.put(tag, words);
                            } else {
                                ArrayList<String> words = new ArrayList<String>();
                                words.add(word);
                                missingWords.put(tag, words);
                            }
                        }
                    } else {
                        String result = "";
                        result = result + word +"\t" + EMORHashMapLemma.get(lower).get(0)+"\t"+EMORHashMap.get(lower).get(0);
                        if (EMORHashMap.get(lower).size()>1){
                            result = result + "#";
                            for (int l=1;l<EMORHashMap.get(lower).size()-1;l++){
                                result = result + "\t" + EMORHashMapLemma.get(lower).get(l)+"\t"+EMORHashMap.get(lower).get(l)+ "#";
                            }
                            result = result + "\t" + EMORHashMapLemma.get(lower).get(EMORHashMapLemma.get(lower).size()-1)+"\t"+EMORHashMap.get(lower).get(EMORHashMap.get(lower).size()-1);
                        }
                        out.println(result);

                    }
                }
            }
            out.close();

            /*
            BufferedWriter writer;
            for (String tag: missingWords.keySet()){
                writer = new BufferedWriter(new FileWriter("/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/MissingWords/"+tag+".txt"));
                for (int k=0;k<missingWords.get(tag).size();k++){
                    writer.write(missingWords.get(tag).get(k)+"\n");
                }
                writer.close();
            }

            FileWriter fw = new FileWriter(emor, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.println("\n");


            String [] inflectedTypes = {"NNS","NNPS","VBD","VBG","VBN","VBP"};
            List<String> inflectedTypesList = Arrays.asList(inflectedTypes);
            for (String tag: missingWords.keySet()){
                if (!inflectedTypesList.contains(tag) && !mappingPennTreebankEMOR.containsKey(tag)){
                    for (int k=0;k<missingWords.get(tag).size();k++) {
                        out.println(missingWords.get(tag).get(k)+"\t"+missingWords.get(tag).get(k).toLowerCase()+"\t"+tag);
                    }
                } else if (!inflectedTypesList.contains(tag) && mappingPennTreebankEMOR.containsKey(tag)){
                    for (int k=0;k<missingWords.get(tag).size();k++) {
                        out.println(missingWords.get(tag).get(k)+"\t"+missingWords.get(tag).get(k).toLowerCase()+"\t"+mappingPennTreebankEMOR.get(tag));
                    }
                }
            }
            out.close();
            */


            // Check for Lower Case
            /*
            for (String tagPenn: mappingPennTreebankEMOR.keySet()){
                System.out.println("Tag Penn Treebank= "+tagPenn+" => EMOR Tag= "+mappingPennTreebankEMOR.get(tagPenn));
            }*/



            FileOutputStream fos = new FileOutputStream("/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/wordEntries.ser");
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(wordEntries);
            os.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        /*
        br = null;
        try {
            br = new BufferedReader(new FileReader(location+"penn_treebank_grammar.txt"));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            RegExp sep = new RegExp(" +");
            RegExp nonsep = new RegExp("start");

            Automaton sepAut = sep.toAutomaton();
            Automaton nonsepAut = nonsep.toAutomaton();

            while (line != null) {
                //System.out.println(line);
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
                if (line != null) {
                    String entry = line.split("\t+")[0];
                    if (line.split("\t+")[2].split(" +")[0].equals("Punct")){
                        //System.out.println("PUNCT:"+line.split("\t+")[2].split(" +")[0]);
                        String punct = line.split("\t+")[0].toString();
                        RegExp sep1 = new RegExp(punct.substring(0,punct.length()-1));
                        Automaton sep1Aut = sep1.toAutomaton();
                        System.out.println(sep1);
                        sepAut = sepAut.union(sep1Aut);
                    }
                    else {
                        //System.out.println("entry: " + entry);
                        //System.out.println("word category: " + line.split("\t+")[2].split(" +")[0]);
                        // Add it as it is
                        RegExp word = new RegExp(entry.substring(0,entry.length()-1).toString());
                        System.out.println(word);
                        nonsep = nonsep.makeUnion(nonsep,word);
                        Automaton nonsepAut1 = nonsep.toAutomaton();

                        nonsepAut = nonsepAut.union(nonsepAut1);
                        //words = words.makeUnion(words, word);
                        // Add it with first letter capitalized
                        char firstLetter = entry.charAt(0);
                        if (Character.isLowerCase(firstLetter)) {
                            String capitalEntry = new StringBuilder().append(Character.toUpperCase(firstLetter)).append(entry.substring(1, entry.length() - 1)).toString();
                            RegExp capitalWord = new RegExp(capitalEntry);
                            nonsep = nonsep.makeUnion(nonsep,capitalWord);
                        }
                    }
                }

            }

            Automaton start = new RegExp("S").toAutomaton();

            Automaton endSep = new RegExp(".|E").toAutomaton();

            Automaton end = new RegExp("E").toAutomaton();
            Automaton endNonSep = sepAut.union(end);


            Automaton nonSepEntry1 = start.concatenate(nonsepAut);
            Automaton nonSepEntry = nonSepEntry1.concatenate(endNonSep);

            Automaton sepEntry1 = start.concatenate(sepAut);
            Automaton sepEntry = sepEntry1.concatenate(endSep);

            Automaton lexicon = sepEntry.union(nonSepEntry);

            FileOutputStream fos = new FileOutputStream("/Users/MeryemMhamdi/EPFL/Spring2017/SemesterProject/Lexicon/myautomaton.ser");
            lexicon.store(fos);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

}
