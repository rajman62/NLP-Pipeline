package MorphologicalAnalyzer;

import LexicalAnalyzer.FSA.WordTag;

import java.io.*;
import java.util.*;

/**
 * STEP 1:
 * This class checks for treebank words that are missing in the general EMOR FST Based Morphological Analyzer
 * It stores them along with their tags and lemmas in a separate file for later analysis to include them.
 * INPUT ====> wordLemmaTags from Tokenization stage
 * OUTPUT ====> Missing Words each in a separate file named with its tag.
 * @author MeryemMhamdi
 * @date 5/31/17.
 */

public class MissingWordsUDC {

    private static String INPUT_PATH_FOLDER = "/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results" +
            "/Tokenization Analysis/UDC/Train/";
    private static String OUTPUT_PATH_FOLDER = "/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results" +
            "/Morphological Analysis/UDC/Train/";
    private static String INPUT_STREAM_WORDS = INPUT_PATH_FOLDER +"UDCWordLemmaTag.ser";
    private static String EMOR_COMPILED_LIST = "/Users/MeryemMhamdi/Google Drive/Semester Project/3 Implementation & Algorithms/Morphological Analysis/EMOR/xtag-morph-1.5aOldVersion.txt"; // makes the search faster than running it from command line and using Proccess Builder to extract the output of the command line
    private static String MISSING_WORDS_OUTPUT_PATH = OUTPUT_PATH_FOLDER +"MissingWordsUDC/";

    private static String SYNTAX_PATH_FOLDER = "/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results" +
            "/Syntactic Analysis/UDC/Train/";

    private static String PATH_DISTINCT_TAGS = SYNTAX_PATH_FOLDER+"DistinctTags1.txt";

    private HashMap<String,String> EMORtoPenntags;

    public MissingWordsUDC(){
        EMORtoPenntags = new HashMap<>();
    }

    public void processEmorToPennTags(String file){

        File oneSidedRulesFile = null;
        Scanner scanner = null;
        try
        {
            oneSidedRulesFile = new File(file);
            scanner = new Scanner(oneSidedRulesFile);

            // Read the one sided rules
            String scanned = scanner.nextLine();
            //System.out.println(scanned);
            String[] line = scanned.split("=");
            do
            {

                String tags [] = line[0].split("\\s++");
                String newTags = "";
                for (int i=0;i<tags.length-1;i++){
                    newTags = newTags + tags[i]+ "\t";
                }
                newTags = newTags + tags[tags.length-1];
                EMORtoPenntags.put(line[1],newTags);
                if (scanner.hasNextLine()){
                    scanned = scanner.nextLine();
                    //System.out.println(scanned);
                    line = scanned.split("=");
                }
                else
                    line = null;
            } while (line != null);
            scanner.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {

            MissingWordsUDC missingWordsUDC = new MissingWordsUDC();
            missingWordsUDC.processEmorToPennTags(PATH_DISTINCT_TAGS);

            ArrayList<WordTag> existingWords = new ArrayList<WordTag>();
            ArrayList<WordTag> existingWordsOriginalTags = new ArrayList<WordTag>();
            BufferedReader br1 = new BufferedReader(new FileReader(EMOR_COMPILED_LIST));
            String line = br1.readLine();
            while (line != null) {
                if (line.toString().contains("#")&& line.toString().indexOf("#") !=0 && !line.toString().contains("http") && !line.toString().contains("DOC") && !line.toString().contains("F%#king") && !line.toString().contains("6871082#") && !line.toString().contains("XLS")) {
                    String parts[] = line.toString().split("#");

                    String subParts[] = parts[0].split("\\s++");
                    String word = subParts[0];
                    String emorTags = "";
                    for (int j = 2; j < subParts.length - 1; j++) {
                        emorTags = emorTags + subParts[j] + "\t";
                    }
                    emorTags = emorTags + subParts[subParts.length - 1];
                    String newTags = emorTags;

                    WordTag wordTag = new WordTag(word, subParts[1], newTags);
                    existingWords.add(wordTag);
                    existingWordsOriginalTags.add(new WordTag(word, subParts[1], emorTags));

                    for (int k=1;k<parts.length;k++) {
                        subParts= parts[k].split("\\s++");
                        emorTags = "";
                        for (int j = 1; j < subParts.length - 1; j++) {
                            emorTags = emorTags + subParts[j] + "\t";
                        }
                        emorTags = emorTags + subParts[subParts.length - 1];
                        newTags = emorTags;
                        wordTag = new WordTag(word, subParts[0], newTags);
                        existingWords.add(wordTag);
                        existingWordsOriginalTags.add(new WordTag(word, subParts[0], emorTags));
                    }
                } else {
                    String parts[] = line.toString().split("\\s++");
                    String emorTags = "";
                    for (int j=2;j<parts.length-1;j++){
                        emorTags = emorTags+ parts[j]+"\t" ;
                    }
                    emorTags = emorTags + parts[parts.length-1];
                    String newTags = emorTags;

                    WordTag wordTag = new WordTag(parts[0],parts[1],newTags);
                    existingWords.add(wordTag);
                    existingWordsOriginalTags.add(new WordTag(parts[0],parts[1], emorTags));

                }
                line = br1.readLine();
            }

            FileInputStream in = new FileInputStream(INPUT_STREAM_WORDS);
            ObjectInputStream os = new ObjectInputStream(in);
            ArrayList<WordTag> wordLemmaTags = (ArrayList<WordTag>) os.readObject();

            System.out.println("SIZE=>"+wordLemmaTags.size());

            Map<String,ArrayList<WordTag>> missingWords = new HashMap<String,ArrayList<WordTag>>();
            Map<String,ArrayList<WordTag>> existingWordsOriginalHash = new HashMap<String,ArrayList<WordTag>>();

            for (int i=0;i<wordLemmaTags.size();i++) { //wordLemmaTags.size()
                if (i % 100 == 0) {
                    System.out.println(i);
                }

                if (!existingWords.contains(wordLemmaTags.get(i))) {
                    //System.out.println(wordLemmaTags.get(i));
                    if (missingWords.containsKey(wordLemmaTags.get(i).getWord())) {
                        ArrayList<WordTag> wordLemmaTagList = missingWords.get(wordLemmaTags.get(i).getWord());
                        wordLemmaTagList.add(wordLemmaTags.get(i));
                        missingWords.put(wordLemmaTags.get(i).getWord(), wordLemmaTagList);
                    } else {
                        ArrayList<WordTag> wordLemmaTagList = new ArrayList<WordTag>();
                        wordLemmaTagList.add(wordLemmaTags.get(i));
                        missingWords.put(wordLemmaTags.get(i).getWord(), wordLemmaTagList);
                    }

                }
            }

            System.out.println("SIZE OF MISSING WORDS="+missingWords.size());
            for (int i=0;i<existingWordsOriginalTags.size();i++){
                if (existingWordsOriginalHash.containsKey(existingWordsOriginalTags.get(i).getWord())){
                    ArrayList<WordTag> wordLemmaTagList = existingWordsOriginalHash.get(existingWordsOriginalTags.get(i).getWord());
                    wordLemmaTagList.add(existingWordsOriginalTags.get(i));
                    existingWordsOriginalHash.put(existingWordsOriginalTags.get(i).getWord(),wordLemmaTagList);
                } else {
                    ArrayList<WordTag> wordLemmaTagList = new ArrayList<WordTag>();
                    wordLemmaTagList.add(existingWordsOriginalTags.get(i));
                    existingWordsOriginalHash.put(existingWordsOriginalTags.get(i).getWord(),wordLemmaTagList);
                }
            }
            for (int i=0;i<wordLemmaTags.size();i++){ //wordLemmaTags.size()
                if (i%100==0){
                    System.out.println(i);
                }

                if (!existingWords.contains(wordLemmaTags.get(i))) {
                    if (existingWordsOriginalHash.containsKey(wordLemmaTags.get(i).getWord())) {
                        ArrayList<WordTag> wordLemmaTagList = existingWordsOriginalHash.get(wordLemmaTags.get(i).getWord());
                        wordLemmaTagList.add(wordLemmaTags.get(i));
                        existingWordsOriginalHash.put(wordLemmaTags.get(i).getWord(), wordLemmaTagList);
                    } else {
                        ArrayList<WordTag> wordLemmaTagList = new ArrayList<WordTag>();
                        wordLemmaTagList.add(wordLemmaTags.get(i));
                        existingWordsOriginalHash.put(wordLemmaTags.get(i).getWord(), wordLemmaTagList);
                    }
                }
            }

            // Saving the missing words to be added to EMOR in a file for that purpose
            /*
            for (String word: missingWords.keySet()) {
                ArrayList<WordTag> cannonicalForms = missingWords.get(word);
                try (FileWriter fw = new FileWriter(MISSING_WORDS_OUTPUT_PATH+"EMOR_TO_ADD1", true);
                     BufferedWriter writer = new BufferedWriter(fw);
                     PrintWriter out = new PrintWriter(writer)) {
                     if (cannonicalForms.size()==1) {
                         out.print(cannonicalForms.get(0).getWord() + "\t" + cannonicalForms.get(0).getLemma() + "\t" + cannonicalForms.get(0).getTag()+"\n");
                     } else {
                         out.print(cannonicalForms.get(0).getWord() + "\t" + cannonicalForms.get(0).getLemma() + "\t" + cannonicalForms.get(0).getTag());

                     }
                    for (int j=1;j<cannonicalForms.size();j++) {
                        if (j==cannonicalForms.size()-1){
                            out.print("#" + cannonicalForms.get(j).getLemma() + "\t" + cannonicalForms.get(j).getTag()+"\n");
                        } else {
                            out.print("#" + cannonicalForms.get(j).getLemma() + "\t" + cannonicalForms.get(j).getTag());
                        }

                    }
                }
            }*/

            // Saving the missing words to be added to EMOR in a file for that purpose
            for (String word: existingWordsOriginalHash.keySet()) {
                ArrayList<WordTag> cannonicalForms = existingWordsOriginalHash.get(word);
                try (FileWriter fw = new FileWriter(MISSING_WORDS_OUTPUT_PATH+"new_EMOR_normalized", true);
                     BufferedWriter writer = new BufferedWriter(fw);
                     PrintWriter out = new PrintWriter(writer)) {
                    String tag = cannonicalForms.get(0).getTag();
                    if (missingWordsUDC.EMORtoPenntags.containsKey(tag)){
                        tag = missingWordsUDC.EMORtoPenntags.get(tag);
                    }
                    if (cannonicalForms.size()==1) {
                        out.print(cannonicalForms.get(0).getWord() + "\t" + cannonicalForms.get(0).getLemma() + "\t" + tag+"\n");
                    } else {
                        out.print(cannonicalForms.get(0).getWord() + "\t" + cannonicalForms.get(0).getLemma() + "\t" + tag);

                    }
                    for (int j=1;j<cannonicalForms.size();j++) {
                        tag = cannonicalForms.get(j).getTag();
                        if (missingWordsUDC.EMORtoPenntags.containsKey(tag)){
                            tag = missingWordsUDC.EMORtoPenntags.get(tag);
                        }
                        if (j==cannonicalForms.size()-1){
                            out.print("#" + cannonicalForms.get(j).getLemma() + "\t" + tag+"\n");
                        } else {
                            out.print("#" + cannonicalForms.get(j).getLemma() + "\t" + tag);
                        }

                    }
                }
            }
        } catch(FileNotFoundException e1){
            e1.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
