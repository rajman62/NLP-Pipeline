package LexicalAnalyzer.Tokenizer;

import LexicalAnalyzer.FSA.Automaton;
import LexicalAnalyzer.FSA.RegExp;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * STEP 2:
 * This class separates pure words or  from common regexes such as numbers, dates, urls to refine their representation in the FSA
 * INPUT ===> list of distinct words
 * OUTPUT ===> list of cleaned text distinct words
 *             list of non-text distinct entries
 * Created by MeryemMhamdi on 6/2/17.
 */
public class CleanTokenFSA {

    private static String PATH_FOLDER = "/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results" +
            "/Tokenization Analysis/UDC/Test/";
    private static String INPUT_TEXT_WORDS = PATH_FOLDER+ "UDCDistinctWords.txt";
    private static String CLEANED_TEXT_WORDS =PATH_FOLDER+ "CLEANEDUDCDistinctWords.txt";
    private static String CLEANED_NON_TEXT_WORDS = PATH_FOLDER+ "NONTEXTUDCDistinctWords.txt";

    public static void main(String [] args){

        BufferedReader br = null;

        ArrayList<String> cleanedWordEntries = new ArrayList<String>();

        ArrayList<String> nonAlphaSequences = new ArrayList<String>();
        Map<String,String> specialCharsMap = new HashMap<String,String>();

        try {
            br = new BufferedReader(new FileReader(INPUT_TEXT_WORDS));
            String line = br.readLine();


            Automaton digit = new RegExp("[0-9]+").toAutomaton();
            Automaton specialChars = new RegExp("&|[\\%]|[\\$]|[\\£]|[\\~]|[\\#]|[\\+]|[-]|--|\\#\\#\\#|->|—|–|_|[\\/]|[\\<]|[\\=]|[\\>]|[\\@]|*|[=]+|[-]+|[_]+|[*]+|>[-]+\\||\\|[-]+[\\+][-]+>|[\\+]+|===>|*~*~*~*~*~*~*~*~*~*|----\\>===[\\}]*[\\{]===\\<----").toAutomaton();
            Automaton fraction1 = new RegExp("([0-9]+)[.;]([0-9]+)|([0-9]+)[.]([0-9]+)[.]([0-9]+)|([0-9]+)[,]([0-9]+)[.]([0-9]+)|[0-9]+/[0-9]+|[0-9]+;[0-9]").toAutomaton(); //([0-9]+)(.)([0-9]+)
            Automaton fraction2 = new RegExp("([0-9]+)([,]([0-9]+))+|\\+[0-9]+|6871082\\#").toAutomaton();
            Automaton fraction3 = new RegExp("([0-9]+)([-]([0-9]+))+|[0-9/-]+|[0-9.]+|[\'\\’][0-9]+").toAutomaton();
            Automaton hour = new RegExp("[0-9]+:[0-9]+|[0-9]+:[0-9]+:[0-9]+").toAutomaton();
            Automaton percent = new RegExp("(([0-9]+)|([0-9]+)[.]([0-9]+))([\\%])").toAutomaton();
            Automaton dollar = new RegExp("([\\$])(([0-9]+)|([0-9]+)[.]([0-9]+)|([0-9]+)([,]([0-9]+))+)").toAutomaton();
            Automaton us_dollar = new RegExp("(US)([\\$])(([0-9]+)|([0-9]+)[.]([0-9]+)|([0-9]+)([,]([0-9]+))+)").toAutomaton();
            Automaton emojis = new RegExp("[.]:|:\\(|:\\)|:D|;\\)|:O|:P|\\<-|;P|\\<3|\\<\\<|=\\(|=\\)|\\>=|\\>\\>|D:|:-\\)|;-\\)|\\>:\\(|\\^_\\^|\\>\\>\\>").toAutomaton();
            Automaton special_sequences = new RegExp("G\\&G|O\\&M|P\\&I|P\\&L|S\\&S|T\\&D|A\\&E|A\\&K|B\\&B|PG\\&E|p\\&l|AT\\&T|C\\&IC|SDG\\&E|\\’ve|1002`s|[0-9]+\'s|\\’s|n\\’t|\\'m|Levi\\`s|\\`s|\\’m|-Stip|basic\u00ADally|\\#\'s|·|d\u00E9cor|entr\u00E9e|Guant\u00E1namo|Igua\u00E7u|\u00C3\u00B3l").toAutomaton();

            Automaton dollar_word = new RegExp("\\$ervice|Thi\\$|U\\$|varie\\$|US\\$|Ye\\$|\\$ome|unde\\$tood|\\$involved|Assh\\@\\%\\$e|\\$ometime\\$|co\\$t|F\\%\\#king|me\\$\\$age|1\\%P701![.]doc|ld2d-\\#69336-1[.]XLS").toAutomaton();

            Automaton sepFSA = new RegExp("(\\))(\\))|_|--|[\\<]|[\\>]|-|[.]| | \\‘|’|“|”|[.]\\?\\?\\?|?|!|[\\(]:|',|\'\'|[\"\"]|`|![.]|[.]!|[.?]|,?|…|‘|!\\?!|[\\)\\)]" +
                    "|\"\"|[.]:|[.]/|[.][.]|,,|!+|\\‘\\’|\\?\\?|\\?\\?\\?\\?\\?\\?\\?\\?\\?\\?\\?\\?\\?\\?\\?|\\?\\?\\?\\?\\?\\?" +
                    "|[.][.][.]\\?!!!|…[.]|\\!\\!\\!\\!\\!\\!\\!\\!\\!\\!\\?|1\\?!\\?!\\?|!!!\\?|\\?\\?\\?\\?|,\\?|\\?\\?\\?\\?\\?|[.]\\?" +
                    "|\\?!\\?|\\?\\?\\?|!\\?|\\?|\\?!|\\?!\\?!\\?|:[.]|:/|!!![.]|[.]|[.][.][.]|'|,|;|:|[\"]|[\\(]|[\\)]|[\\{]|[\\}]|" +
                    "[.][.][.][.][.][.][.][.]|[.][.][.][.][.][.][.][.][.][.][.][.][.][.][.][.][.][.][.][.]|[.][.][.][.][.][.][.]|[.][.][.][.][.][.]|[.][.][.][.][.][.][.][.][.]|"+
                    "[.][.][.][.]|[.][.][.][.][.]|[.][.][.][.][.][.][.][.][.][.][.][.][.][.][.][.][.][.][.][.][.]|[.][.][.][.][.][.][.][.][.][.][.][.][.][.][.]|"+
                    "[.][.][.][.][.][.][.][.][.][.][.]|[.][.][.][.][.][.][.][.][.][.]|:\\?|!\\?\\!\\?\\!|\\?\\?[.]|[.][.]\\?|[\\?]+|!\\?\\?\\?\\?\\?\\!!!!|!!!!!!!!!!!!!\\?|"+
                    "|[\\]]|[\\[]|[\\|]").toAutomaton();

            //http://www.csmonitor.com/2006/0509/p02s01-ussc.html?s=t5

            Automaton date = new RegExp("Jan[.] 1(st)?|Jan[.] 2(nd)?|Jan[.] 3(rd)?|Jan[.] [1-9]+(th)?").toAutomaton();
            Automaton date2 = new RegExp("Feb[.] 1(st)?|Feb[.] 2(nd)?|Feb[.] 3(rd)?|Feb[.] [1-9]+(th)?").toAutomaton();
            Automaton date3 = new RegExp("Mar[.] 1(st)?|Mar[.] 2(nd)?|Mar[.] 3(rd)?|Mar[.] [1-9]+(th)?").toAutomaton();
            Automaton date4 = new RegExp("Apr[.] 1(st)?|Apr[.] 2(nd)?|Apr[.] 3(rd)?|Apr[.] [1-9]+(th)?").toAutomaton();
            Automaton date5 = new RegExp("May[.] 1(st)?|May[.] 2(nd)?|May[.] 3(rd)?|May[.] [1-9]+(th)?").toAutomaton();
            Automaton date6 = new RegExp("Jun[.] 1(st)?|Jun[.] 2(nd)?|Jun[.] 3(rd)?|Jun[.] [1-9]+(th)?").toAutomaton();
            Automaton date7 = new RegExp("Jul[.] 1(st)?|Jul[.] 2(nd)?|Jul[.] 3(rd)?|Jul[.] [1-9]+(th)?").toAutomaton();
            Automaton date8 = new RegExp("Aug[.] 1(st)?|Aug[.] 2(nd)?|Aug[.] 3(rd)?|Aug[.] [1-9]+(th)?").toAutomaton();
            Automaton date9 = new RegExp("Sep[.] 1(st)?|Sep[.] 2(nd)?|Sep[.] 3(rd)?|Sep[.] [1-9]+(th)?").toAutomaton();
            Automaton date10 = new RegExp("Oct[.] 1(st)?|Oct[.] 2(nd)?|Oct[.] 3(rd)?|Oct[.] [1-9]+(th)?").toAutomaton();
            Automaton date11 = new RegExp("Nov[.] 1(st)?|Nov[.] 2(nd)?|Nov[.] 3(rd)?|Nov[.] [1-9]+(th)?").toAutomaton();
            Automaton date12 = new RegExp("Dec[.] 1(st)?|Dec[.] 2(nd)?|Dec[.] 3(rd)?|Dec[.] [1-9]+(th)?").toAutomaton();


            Automaton url = new RegExp("(http:\\/\\/|www[.])([a-zA-Z\\#]+|[.]|\\/|[0-9]+|-|\\?|=|,|_)+|http:\\/\\/www[.]solutions[.]com\\/jump[.]jsp\\?").toAutomaton();

            Automaton url2 = new RegExp("http:\\/\\/www[.]solutions.com\\/jump[.]jsp\\?\\itemID[=]1361\\&itemType[=]PRODUCT\\&path[=]1\\%2C3\\%2C477\\&iProductID[=]1361" +
                    "|http:\\/\\/www[.]sonic[.]net/\\~fsjob/TragiCore-TheCivetCat[.]mp3" +
                    "|http:\\/\\/www[.]ebay[.]co[.]uk/itm/250927098564\\?\\var=550057729382\\&ssPageName=STRK:MEWAX:IT\\&_trksid=p3984[.]m1438[.]l2649\\#ht_" +
                    "|http:\\/\\/www[.]ebay[.]co[.]uk/itm/250927098564\\?\\var=550057729382\\&ssPageName=STRK:MEWAX:IT\\&_trksid=p3984[.]m1438[.]l2649\\#ht_2079wt_893" +
                    "|http:\\/\\/www[.]calguard[.]ca[.]gov\\/ia\\/Chernobyl-15\\%20years[.]htm" +
                    "|http:\\/\\/www[.]utrechtart[.]com\\/Craft-Supplies\\/Woodworking\\%20Supplies\\/" +
                    "|http:\\/\\/haas[.]berkeley[.]edu\\/\\~borenste" +
                    "|http:\\/\\/judiciary[.]senate[.]gov\\/testimony[.]cfm\\?\\id=1725\\&wit_id=4905" +
                    "|http:\\/\\/www[.]ebay[.]co[.]uk\\/itm\\/130589513308\\?var=430034792128\\&ssPageName=STRK:MEWAX:IT\\&_trksid=p3984[.]m1438[.]l2648\\#ht_1500wt_660").toAutomaton();
            Automaton email = new RegExp("[a-zA-Z0-9._-:]+[\\@][a-zA-Z0-9._-]+|[a-zA-Z0-9_\\%:]+\\@[a-zA-Z0-9_]+[.][a-zA-Z0_9_]+").toAutomaton();

            Automaton emails = new RegExp("mailto:amy[.]cornell\\@compaq[.]com|mailto:mayur[.][.][.]\\@yahoo[.]com|mailto:galen[.]torneby\\@nepco[.]com|mailto:rosario[.]gonzales\\@compaq[.]com|online\\?\\u=mayursha\\&m=g\\&t=1|ymsgr:sendIM\\?\\mayursha\\&__Hi\\+Mayur[.][.][.]").toAutomaton();

            Automaton docs = new RegExp("ld2d-\\#69366-1[.]DOC|ld2d-\\#69334-1[.]DOC|ld2d-\\#69397-1[.]DOC|ld2d-\\#69381-1[.]DOC|ld2d-\\#69396-1[.]DOC|ld2d-\\#69345-1[.]DOC|ld2d-\\#69377-1[.]XLS|ENRONR\\~1[.]DOC").toAutomaton();

            Automaton dates = date.union(date2).union(date3).union(date4).union(date5).union(date6).union(date7).union(date8).union(date9).union(date10).union(date11).union(date12);

            Automaton datesSpecialWriting = new RegExp("[0-9]+/[0-9]+/[0-9]+").toAutomaton();

            Automaton specialCasesTrain = sepFSA.union(specialChars).union(digit).union(fraction1).union(fraction2).union(hour).union(url2).union(emails).union(docs).union(emojis)
                    .union(fraction3).union(percent).union(dollar).union(us_dollar).union(dates).union(datesSpecialWriting).union(email).union(url).union(special_sequences).union(dollar_word);

            Automaton specialCases = sepFSA.union(digit).union(fraction1).union(fraction2).union(fraction3).union(hour).union(emojis).union(percent).union(dollar)
                                    .union(us_dollar).union(dates).union(datesSpecialWriting).union(dollar_word);

            Automaton alpha = new RegExp("[a-zA-Z]+|[a-zA-Z]+[-][a-zA-Z]+|[0-9]+[a-zA-Z]+|[a-zA-Z]+[0-9]+|[a-zA-Z.]+|[a-zA-Z0-9]+|[a-zA-Z'*_.\\/,!]+|[a-zA-Z0-9-._=]+|[a-zA-Z0-9']+").toAutomaton();


            while (line != null){
                if (!specialCases.run(line)) {
                    cleanedWordEntries.add(line);
                }
                line = br.readLine();
            }


            System.out.println("NUMBER OF DISTINCT WORDS AFTER CLEANING==>"+cleanedWordEntries.size());
            BufferedWriter writer = new BufferedWriter(new FileWriter(CLEANED_TEXT_WORDS));
            for (int i=0;i<cleanedWordEntries.size();i++){
                if (!alpha.run(cleanedWordEntries.get(i))) {
                    nonAlphaSequences.add(cleanedWordEntries.get(i));
                }
                writer.write(cleanedWordEntries.get(i) + "\n");
            }
            writer.close();

            writer = new BufferedWriter(new FileWriter(CLEANED_NON_TEXT_WORDS));
            for (int i=0;i<nonAlphaSequences.size();i++){
                writer.write(nonAlphaSequences.get(i) + "\n");
            }
            writer.close();
            System.out.println("NUMBER OF NON ALPHA SEQUENCES AFTER CLEANING==>"+nonAlphaSequences.size());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
