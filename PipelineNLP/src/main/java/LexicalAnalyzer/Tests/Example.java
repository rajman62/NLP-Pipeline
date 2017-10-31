package LexicalAnalyzer.Tests;

import LexicalAnalyzer.FSA.Automaton;
import LexicalAnalyzer.FSA.RegExp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MeryemMhamdi on 4/10/17.
 */
public class Example {

    public static void main(String[] args) {

        RegExp r1 = new RegExp("ab(c)+");
        RegExp r2 = new RegExp("backdrop");
        RegExp r3 = new RegExp("somethingelse");
        List<Automaton> automatons = new ArrayList<Automaton>();
        Automaton a1 = r1.toAutomaton();
        Automaton a2 = r2.toAutomaton();
        Automaton a3 = r3.toAutomaton();
        automatons.add(a2);
        automatons.add(a1);
        automatons.add(a3);
        Automaton concatenated = (a1.union(a2)).union(a3);

        Automaton concat = new Automaton();


        try {
            BufferedReader br = new BufferedReader(new FileReader("/Users/MeryemMhamdi/Downloads/lexicon.txt"));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();

            String[] ar = everything.split(",");

            String listStrings[] = {"eat|go|read|open",};//,"abatang","abatangkio","ang"};//, "abut", "accur", "acquit","adlib", "admit", "aerobat", "aerosol", "agendaset","allot", "alot", "anagram", "annul", "appal","apparel", "armbar", "aver", "babysit", "airdrop","appal", "blackleg", "bobsled", "bur", "chum","confab", "counterplot", "curet", "dib", "backdrop","backfil", "backflip", "backlog", "backpedal", "backslap","backstab", "bag", "balfun", "ballot", "ban","bar", "barbel", "bareleg", "barrel", "bat","bayonet", "becom", "bed", "bedevil", "bedwet","beenhop", "befit", "befog", "beg", "beget","begin", "bejewel", "bemedal", "benefit", "benum","beset", "besot", "bestir", "bet", "betassel","bevel", "bewig", "bib", "bid", "billet","bin", "bip", "bit", "bitmap", "blab","blag", "blam", "blan", "blat", "bles","blim", "blip", "blob", "bloodlet", "blot","blub", "blur", "bob", "bodypop", "bog","booby-trap", "boobytrap", "booksel", "bootleg", "bop","bot", "bowel", "bracket", "brag", "brig","brim", "bud", "buffet", "bug", "bullshit","bum", "bun", "bus", "but", "cab","cabal", "cam", "can", "cancel", "cap","caracol", "caravan", "carburet", "carnap", "carol","carpetbag", "castanet", "cat", "catcal", "catnap","cavil", "chan", "chanel", "channel", "chap","char", "chargecap", "chat", "chin", "chip","chir", "chirrup", "chisel", "chop", "chug","chur", "clam", "clap", "clearcut", "clip","clodhop", "clog", "clop", "closet", "clot","club", "co-occur", "co-program", "co-refer", "co-run","co-star", "cob", "cobweb", "cod", "coif","com", "combat", "comit", "commit", "compel","con", "concur", "confer", "confiscat", "control","cop", "coquet", "coral", "corbel", "corral","cosset", "cotransmit", "councel", "council", "counsel","court-martial", "crab", "cram", "crap", "crib","crop", "crossleg", "cub", "cudgel", "cum","cun", "cup", "cut", "dab", "dag","dam", "dan", "dap", "daysit", "de-control","de-gazet", "de-hul", "de-instal", "de-mob", "de-program","de-rig", "de-skil", "deadpan", "debag", "debar"};
            RegExp r0 = new RegExp(listStrings[0]);
            Automaton a0 = r0.toAutomaton();
            for (int i = 1; i < listStrings.length; i++) {
                //System.out.println(listStrings[i].toString());
                RegExp r = new RegExp(listStrings[i]);
                Automaton a = r.toAutomaton();
                concat = a0.union(a);
                a0 = concat;
            }
            br.close();
        } catch (IOException e) {
            System.out.println("File Read Error");
        }


        String s = "satellite antenna";//"abat$abatachorking$achor";//"abat$abatachorking$abatachor";//"abatachorking";//"$abatachor$abatachorking$kingabatking";//"abatachor"; //"abatachor";//"abat$abatachorking$abatachor";//abetabatangkio";


        //RunAutomaton runconcat = new RunAutomaton(concat);
        //AutomatonMatcher matcher = new AutomatonMatcher(s,runconcat);

        //ArrayList<String> englishWords = new ArrayList<String>();

        //englishWords.add("jolie","somethingbad","somethingbadbad","bad","something","badbad","king");

        Automaton aaaaaa = new Automaton();
        Automaton unionAutomat = aaaaaa.makeStringUnion("something", " ", "somethinggood");

        //System.out.println("Automaton: "+ concat.toString());
        //System.out.println("Match: " + unionAutomat.run(s)); // prints: true
        //System.out.println("List of tokens: " + concat.traverse(s));
        //System.out.println("Printing Tree");
        ArrayList<ArrayList<String>> tokens = concat.traverse(s);
        System.out.println("List of Tokens:" + tokens);
        //Graph graph = concat.buildGraph(tokens);
        //graph.printGraph();
        //System.out.println(graph.traverseGraph());
        //System.out.println("Match: " + matcher.start());
        //System.out.println("Running Automaton: "+runconcat.run(s));

        ArrayList<ArrayList<String>> chart = concat.traverse("satellite antenna");

        System.out.println("Printing the chart>>>\n"+chart.toString());

    }

}
