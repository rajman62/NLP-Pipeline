package MorphologicalAnalyzer;

import java.io.*;
import java.util.HashMap;

/**
 * Created by MeryemMhamdi on 7/18/17.
 */
public class CreateTagMappings {
    /******************************************************************************************************************/
    /**
     * LOCATION FILES TO BE REPLACED
     */
    private static String TEXT_SOURCE_MAPPING = "/Users/MeryemMhamdi/Desktop/NLP-Pipeline/" +
            "PipelineNLP/src/MorphologicalAnalyzer/tagMappings.txt";
    private static String STREAM_MAPPINGS = "/Users/MeryemMhamdi/Desktop/NLP-Pipeline" +
            "/PipelineNLP/src/MorphologicalAnalyzer/tagsMappings.ser";

    /******************************************************************************************************************/
    public static void main (String [] args){
        /**
         * Adding Map from E-MOR tags to Penn Treebank tags
         */

        try {
            BufferedReader br = new BufferedReader(new FileReader(TEXT_SOURCE_MAPPING));

            HashMap<String,String> tagsMap = new HashMap<>();

            String line = br.readLine();
            while (line != null) {
                String [] parts = line.split("=");
                if (parts.length==2) {
                    tagsMap.put(parts[0], parts[1]);
                }

                System.out.println(parts[0]+"****"+parts[1]);
                line = br.readLine();
            }

            FileOutputStream fos = new FileOutputStream(STREAM_MAPPINGS);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(tagsMap);
            os.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
