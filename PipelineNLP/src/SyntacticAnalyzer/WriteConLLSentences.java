package SyntacticAnalyzer;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MeryemMhamdi on 6/5/17.
 */
public class WriteConLLSentences {

    private static String INPUT_PATH_FOLDER = "/Users/MeryemMhamdi/EPFL/Spring2017/SemesterProject/Results/Big Data/parsedDependenciesConLL_Whole.ser";

    private static String OUTPUT_MAPPINGS_STREAM = "/Users/MeryemMhamdi/EPFL/Spring2017/SemesterProject/Results/Big Data/mappings_indices.ser";
    public static void main (String [] args){
        try {
            FileInputStream in = new FileInputStream(INPUT_PATH_FOLDER);
            ObjectInputStream stream = new ObjectInputStream(in);
            ArrayList<ArrayList<DependencyConLL>> parsedDependenciesConLL = (ArrayList<ArrayList<DependencyConLL>>) stream.readObject();

            int arr[] = {200, 400, 600, 800, 1000, 1200, 1400, 1600, 1800, 2000, 2200, 2400, 2600, 2800, 3000, 3200, 3400, 3600, 3800,
                    4000, 4200, 4400, 4600, 4800, 5000, 5200, 5400, 5600, 5800, 6000, 6200, 6400, 6600, 6800, 7000, 7200, 7400,
                    7600, 7800, 8000, 8200, 8400, 8600, 8800, 9000, 9200, 9400, 9600, 9800, 10000, 10200, 10400, 10600, 10800,
                    11000, 11200, 11400, 11600, 11800, 12000, 12200, 12400};

            HashMap<Integer,ArrayList<Integer>> mappings = new HashMap<Integer,ArrayList<Integer>>();
            ArrayList<Integer> values = new ArrayList<>();
            values.add(0);
            values.add(150);
            mappings.put(0,values);
            values = new ArrayList<>();
            values.add(150);
            values.add(325);
            mappings.put(200,values);
            values = new ArrayList<>();
            values.add(325);
            values.add(497);
            mappings.put(400,values);
            values = new ArrayList<>();
            values.add(497);
            values.add(677);
            mappings.put(600,values);
            values = new ArrayList<>();
            values.add(677);
            values.add(827);
            mappings.put(800,values);
            values = new ArrayList<>();
            values.add(827);
            values.add(1113);
            mappings.put(1000,values);
            values = new ArrayList<>();
            values.add(1113);
            values.add(1147);
            mappings.put(1200,values);
            values = new ArrayList<>();
            values.add(1147);
            values.add(1374);
            mappings.put(1400,values);
            values = new ArrayList<>();
            values.add(1374);
            values.add(1491);
            mappings.put(1600,values);
            values = new ArrayList<>();
            values.add(1491);
            values.add(1614);
            mappings.put(1800,values);
            values = new ArrayList<>();
            values.add(1614);
            values.add(1743);
            mappings.put(2000,values);
            values = new ArrayList<>();
            values.add(1743);
            values.add(1886);
            mappings.put(2200,values);
            values = new ArrayList<>();
            values.add(1886);
            values.add(1999);
            mappings.put(2400,values);
            values = new ArrayList<>();
            values.add(1999);
            values.add(2131);
            mappings.put(2600,values);
            values = new ArrayList<>();
            values.add(2131);
            values.add(2257);
            mappings.put(2800,values);
            values = new ArrayList<>();
            values.add(2257);
            values.add(2384);
            mappings.put(3000,values);
            values = new ArrayList<>();
            values.add(2384);
            values.add(2491);
            mappings.put(3200,values);
            values = new ArrayList<>();
            values.add(2491);
            values.add(2507);
            mappings.put(3400,values);
            values = new ArrayList<>();
            values.add(2507); // wrong
            values.add(2744);
            mappings.put(3600,values);
            values = new ArrayList<>();
            values.add(2744);
            values.add(2889);
            mappings.put(3800,values);
            values = new ArrayList<>();
            values.add(2889);
            values.add(2972);
            mappings.put(4000,values);
            values = new ArrayList<>();
            values.add(2972);
            values.add(3115);
            mappings.put(4200,values);
            values = new ArrayList<>();
            values.add(3115);
            values.add(3139);
            mappings.put(4400,values);
            values = new ArrayList<>();
            values.add(3139);
            values.add(3372);
            mappings.put(4600,values);
            values = new ArrayList<>();
            values.add(3372);
            values.add(3463);
            mappings.put(4800,values);
            values = new ArrayList<>();
            values.add(3463);
            values.add(3646);
            mappings.put(5000,values);
            values = new ArrayList<>();
            values.add(3646);
            values.add(3375);
            mappings.put(5200,values);
            values = new ArrayList<>();
            values.add(3375);
            values.add(3933);
            mappings.put(5400,values);
            values = new ArrayList<>();
            values.add(3933);
            values.add(4098);
            mappings.put(5600,values);
            values = new ArrayList<>();
            values.add(4098);
            values.add(4240);
            mappings.put(5800,values);
            values = new ArrayList<>();
            values.add(4240);
            values.add(4369);
            mappings.put(6000,values);
            values = new ArrayList<>();
            values.add(4369);
            values.add(4549);
            mappings.put(6200,values);
            values = new ArrayList<>();
            values.add(4549);
            values.add(4706);
            mappings.put(6400,values);
            values = new ArrayList<>();
            values.add(4706);
            values.add(4829);
            mappings.put(6600,values);
            values = new ArrayList<>();
            values.add(4829);
            values.add(4975);
            mappings.put(6800,values);
            values = new ArrayList<>();
            values.add(4975);
            values.add(5050);
            mappings.put(7000,values);
            values = new ArrayList<>();
            values.add(5050);
            values.add(5182);
            mappings.put(7200,values);
            values = new ArrayList<>();
            values.add(5182);
            values.add(5313);
            mappings.put(7400,values);
            values = new ArrayList<>();
            values.add(5313);
            values.add(5464);
            mappings.put(7600,values);
            values = new ArrayList<>();
            values.add(5464);
            values.add(5643);
            mappings.put(7800,values);
            values = new ArrayList<>();
            values.add(5643);
            values.add(5769);
            mappings.put(8000,values);
            values = new ArrayList<>();
            values.add(5769);
            values.add(5930);
            mappings.put(8200,values);
            values = new ArrayList<>();
            values.add(5930);
            values.add(6101);
            mappings.put(8400,values);
            values = new ArrayList<>();
            values.add(6101);
            values.add(6282);
            mappings.put(8600,values);
            values = new ArrayList<>();
            values.add(6282);
            values.add(6433);
            mappings.put(8800,values);
            values = new ArrayList<>();
            values.add(6433);
            values.add(6606);
            mappings.put(9000,values);
            values = new ArrayList<>();
            values.add(6606);
            values.add(6733);
            mappings.put(9200,values);
            values = new ArrayList<>();
            values.add(6733);
            values.add(6918);
            mappings.put(9400,values);
            values = new ArrayList<>();
            values.add(6918);
            values.add(7109);
            mappings.put(9600,values);
            values = new ArrayList<>();
            values.add(7109);
            values.add(7215);
            mappings.put(9800,values);
            values = new ArrayList<>();
            values.add(7215);
            values.add(7459);
            mappings.put(10000,values);
            values = new ArrayList<>();
            values.add(7459);
            values.add(7637);
            mappings.put(10200,values);
            values = new ArrayList<>();
            values.add(7637);
            values.add(7819);
            mappings.put(10400,values);
            values = new ArrayList<>();
            values.add(7819);
            values.add(8007);
            mappings.put(10600,values);
            values = new ArrayList<>();
            values.add(8007);
            values.add(8191);
            mappings.put(10800,values);
            values = new ArrayList<>();
            values.add(8191);
            values.add(8374);
            mappings.put(11000,values);
            values = new ArrayList<>();
            values.add(8374);
            values.add(8548);
            mappings.put(11200,values);
            values = new ArrayList<>();
            values.add(8548);
            values.add(8736);
            mappings.put(11400,values);
            values = new ArrayList<>();
            values.add(8736);
            values.add(8914);
            mappings.put(11600,values);
            values = new ArrayList<>();
            values.add(8914);
            values.add(9087);
            mappings.put(11800,values);
            values = new ArrayList<>();
            values.add(9087);
            values.add(9276);
            mappings.put(12000,values);
            values = new ArrayList<>();
            values.add(9276);
            values.add(9462);
            mappings.put(12200,values);
            values = new ArrayList<>();
            values.add(9462);
            values.add(9601);
            mappings.put(12400,values);

            for (int i = 0; i < arr.length; i++) {
                String sentence = "";
                for (int j=0;j<parsedDependenciesConLL.get(arr[i]).size();j++){
                    sentence = sentence + parsedDependenciesConLL.get(arr[i]).get(j).getForm()+ " ";
                }
                System.out.println("i= "+arr[i]+" => Sentence===> "+sentence);
            }


            FileOutputStream fos = new FileOutputStream(OUTPUT_MAPPINGS_STREAM);
            ObjectOutputStream s = new ObjectOutputStream(fos);
            s.writeObject(mappings);
            s.flush();

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
