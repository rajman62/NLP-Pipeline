package MorphologicalAnalyzer;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** STEP 2: A simple life hack just to speed up the process of morphology analyzer
 * @author MeryemMhamdi
 * @date 6/1/17.
 */
public class MapEmor {
    private static String INPUT_PATH_FOLDER = "/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results" +
            "/Tokenization Analysis/UDC/Train/";
    private static String OUTPUT_PATH_FOLDER = "/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results" +
            "/Morphological Analysis/UDC/Train/";
    public static String PATH_COMPILED_EMOR = "/Users/MeryemMhamdi/Google Drive/Semester Project/3 Implementation & Algorithms/Morphological Analysis/EMOR/xtag-morph-1.5a.txt";
    private static String PATH_DISTINCT_WORDS = INPUT_PATH_FOLDER+"UDCDistinctWords.txt";
    private static String PATH_OUTPUT_MAP = OUTPUT_PATH_FOLDER+"emor_map.ser";

    public static void main (String [] args){
        Map<String,ArrayList<LemmaTag>> emorMapAll = new HashMap<String,ArrayList<LemmaTag>>();
        ArrayList<String> udcWords =  new ArrayList<String>();
        ArrayList<Tuple> emorList = new ArrayList<Tuple> ();
        try {
            // Read Distinct Words
            BufferedReader br1 = new BufferedReader(new FileReader(PATH_DISTINCT_WORDS));
            String line1 = br1.readLine();
            while (line1 != null ){
                udcWords.add(line1);
                line1 = br1.readLine();
            }

            BufferedReader br = new BufferedReader(new FileReader(PATH_COMPILED_EMOR));
            String line = br.readLine();
            while (line != null){ //&& line.length() > 1) {
                if (line.toString().contains("#")&& line.toString().indexOf("#") !=0 && !line.toString().contains("http") && !line.toString().contains("DOC") && !line.toString().contains("F%#king") && !line.toString().contains("6871082#") && !line.toString().contains("XLS")) {
                    String parts[] = line.toString().split("#");
                    String subParts[] = parts[0].split("\\s++");
                    String word = subParts[0];
                    String emorTags = "";
                    for (int j=2;j<subParts.length-1;j++){
                        emorTags = emorTags+ "<"+ subParts[j]+">" ;
                    }
                    emorTags = emorTags+ "<" + subParts[subParts.length-1] + ">" ;
                    System.out.println(emorTags);
                    //System.out.println("Word= "+subParts[0]+" Lemma= "+subParts[1]+" Tags= "+emorTags);
                    if (udcWords.contains(word)) {
                        emorList.add(new Tuple(word,new LemmaTag(subParts[1],emorTags)));
                    }

                    for (int i = 1; i <parts.length;i++){
                        subParts = parts[i].split("\\s++");
                        emorTags = "";
                        for (int j=1;j<subParts.length-1;j++){
                            emorTags = emorTags+ "<" +subParts[j]+">" ;
                        }
                        emorTags = emorTags+ "<"+subParts[subParts.length-1]+">" ;
                        System.out.println(emorTags);
                        //System.out.println("Word= "+word+" Tag= "+subParts[1]+" Lemma= "+emorTags);
                        if (udcWords.contains(word)) {
                            emorList.add(new Tuple(word,new LemmaTag(subParts[0],emorTags)));
                        }
                    }
                }else {
                    String parts[] = line.toString().split("\\s++");
                    String emorTags = "";
                    for (int j=2;j<parts.length-1;j++){
                        emorTags = emorTags+"<"+ parts[j]+">" ;
                    }
                    emorTags = emorTags+ "<"+ parts[parts.length-1] +">" ;

                    System.out.println(emorTags);
                    if (udcWords.contains(parts[0])) {
                        emorList.add(new Tuple(parts[0],new LemmaTag(parts[1],emorTags)));
                    }
                }
                System.out.println(line);
                line = br.readLine();
            }




            for (int i=0;i<emorList.size();i++){
                if (emorMapAll.containsKey(emorList.get(i).getWord())){
                    ArrayList<LemmaTag> values = emorMapAll.get(emorList.get(i).getWord());
                    if (!values.contains(emorList.get(i).getCannonicalForm())) {
                        values.add(emorList.get(i).getCannonicalForm());
                        emorMapAll.put(emorList.get(i).getWord(), values);
                    }
                } else {
                    ArrayList<LemmaTag> values = new ArrayList<LemmaTag>();
                    values.add(emorList.get(i).getCannonicalForm());
                    emorMapAll.put(emorList.get(i).getWord(),values);
                }
            }

            for (int i=0;i<udcWords.size();i++){
                if (!emorMapAll.containsKey(udcWords.get(i))) {
                    System.out.println(udcWords.get(i));
                }
            }
            System.out.println("Size of udclist ==>"+udcWords.size());
            System.out.println("Size of emorMapAll ==>"+emorMapAll.size());


            FileOutputStream fos = new FileOutputStream(PATH_OUTPUT_MAP);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(emorMapAll);
            os.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
