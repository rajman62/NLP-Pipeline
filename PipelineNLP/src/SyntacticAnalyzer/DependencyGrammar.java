package SyntacticAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by MeryemMhamdi on 5/10/17.
 */
public class DependencyGrammar {

    public class Relation{
        private String head;
        private String dependent;
        public Relation(String head,String dependent){
            this.head = head;
            this.dependent = dependent;
        }
        public String getHead(){
            return this.head;
        }
        public String getDependent(){
            return this.dependent;
        }
        public void setHead(String head){
            this.head = head;
        }
        public void setDependent(String dependents){
            this.dependent = dependents;
        }
    }

    static private ArrayList<Relation> relations = new ArrayList<Relation>();

    static private ArrayList<String> labels = new ArrayList<String>();


    public DependencyGrammar(String file){
        processGrammarFile(file);
    }
    /**
     * Processes the grammar file and builds the HashMap of the list of terminals
     * and variables. Uses the Scanner object to read the grammar file.
     * @param file the string representing the path of the grammar file
     */
    public void processGrammarFile(String file)
    {

        File grammarFile = null;
        Scanner scanner = null;
        try
        {
            grammarFile = new File(file);
            scanner = new Scanner(grammarFile);
            String[] line = scanner.nextLine().split("->");


            //System.out.println(line[0]);

            do
            {
                String head = line[0];
                String[] dependents = line[1].substring(1,line[1].length()).split(":");
                /*
                for (int i=0;i<dependents.length;i++) {
                    System.out.println(dependents[i]);
                }
                */
                //String[] tags = dependents[].substring(1,line[1].length()).split(":");

                if (dependents != null)
                {
                    System.out.println(head.substring(1,head.length()-2));
                    //for (int i=0;i<dependents.length;i++) {
                    System.out.println(dependents[0].substring(1,dependents[0].length()-2));
                    relations.add(new Relation(head.substring(1,head.length()-2), dependents[0].substring(1,dependents[0].length()-2)));
                    labels.add(dependents[1]);
                    //}
                }
                if (scanner.hasNextLine())
                    line = scanner.nextLine().split("->");
                else
                    line = null;
            } while (line != null);
            scanner.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }


    }
    public ArrayList<Relation> getRelations(){
        return this.relations;
    }

    public ArrayList<String> getLabels(){
        return this.labels;
    }

    public int containsElement(String head,String dependent){
        for (int i=0;i<relations.size();i++){
            if (relations.get(i).getHead().equals(head) && relations.get(i).getDependent().equals(dependent)){
                return i;
            }
        }
        return -1;
    }
    public static void main (String [] args){
        DependencyGrammar dg = new DependencyGrammar("/Users/MeryemMhamdi/EPFL/Spring2017/SemesterProject/PipelineNLP/src/SyntacticAnalyzer/dependencyGrammar.txt");
        System.out.println(relations);
    }
}
