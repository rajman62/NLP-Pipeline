package LexicalAnalyzer.Tokenizer;

import LexicalAnalyzer.FSA.Automaton;
import LexicalAnalyzer.FSA.RegExp;
import java.io.*;

/**
 * STEP 4:
 * This class creates two FSA one is tokFSA (extended from wordFSA created in STEP 3) and sepFSA
 * INPUT ===> wordFSA
 * OUTPUT ====> tokFSA
 *              sepFSA
 * @author MeryemMhamdi
 * @date 4/29/17.
 */
public class CreateFSAs {
    /******************************************************************************************************************/
    /**
     * LOCATION FILES TO BE REPLACED
     */
    private static String PATH_FOLDER = "/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results" +
            "/Tokenization Analysis/UDC/Train/"; // TO BE REPLACED
    private static String PATH_WORD_FSA = PATH_FOLDER+"UDCwordFSA.ser";
    private static String PATH_TOK_FSA = PATH_FOLDER+"UDCtokFSA.ser";
    private static String PATH_SEP_FSA = PATH_FOLDER+"UDCsepFSA.ser";
    /******************************************************************************************************************/

    public static void main(String args[]) {
            /***
             * 1. Create tokFSA
             *
             */

            /**
             * Construct two FSAs one is FSAtok with word entries and other regexes and the other is FSAsep with separators
             *
             */

            try {

                FileInputStream in = new FileInputStream(PATH_WORD_FSA);
                Automaton automaton = new Automaton();
                Automaton wordFSA = automaton.load(in);

                Automaton date = new RegExp("Jan[.] 1(st)?|Jan[.] 2(nd)?|Jan[.] 3(rd)?|Jan[.] [1-9]+(th)?").toAutomaton(); // OKAY
                Automaton date2 = new RegExp("Feb[.] 1(st)?|Feb[.] 2(nd)?|Feb[.] 3(rd)?|Feb[.] [1-9]+(th)?").toAutomaton(); // OKAY
                Automaton date3 = new RegExp("Mar[.] 1(st)?|Mar[.] 2(nd)?|Mar[.] 3(rd)?|Mar[.] [1-9]+(th)?").toAutomaton(); // OKAY
                Automaton date4 = new RegExp("Apr[.] 1(st)?|Apr[.] 2(nd)?|Apr[.] 3(rd)?|Apr[.] [1-9]+(th)?").toAutomaton(); // OKAY
                Automaton date5 = new RegExp("May[.] 1(st)?|May[.] 2(nd)?|May[.] 3(rd)?|May[.] [1-9]+(th)?").toAutomaton(); // OKAY
                Automaton date6 = new RegExp("Jun[.] 1(st)?|Jun[.] 2(nd)?|Jun[.] 3(rd)?|Jun[.] [1-9]+(th)?").toAutomaton(); // OKAY
                Automaton date7 = new RegExp("Jul[.] 1(st)?|Jul[.] 2(nd)?|Jul[.] 3(rd)?|Jul[.] [1-9]+(th)?").toAutomaton(); // OKAY
                Automaton date8 = new RegExp("Aug[.] 1(st)?|Aug[.] 2(nd)?|Aug[.] 3(rd)?|Aug[.] [1-9]+(th)?").toAutomaton(); // OKAY
                Automaton date9 = new RegExp("Sep[.] 1(st)?|Sep[.] 2(nd)?|Sep[.] 3(rd)?|Sep[.] [1-9]+(th)?").toAutomaton(); // OKAY
                Automaton date10 = new RegExp("Oct[.] 1(st)?|Oct[.] 2(nd)?|Oct[.] 3(rd)?|Oct[.] [1-9]+(th)?").toAutomaton(); // OKAY
                Automaton date11 = new RegExp("Nov[.] 1(st)?|Nov[.] 2(nd)?|Nov[.] 3(rd)?|Nov[.] [1-9]+(th)?").toAutomaton(); // OKAY
                Automaton date12 = new RegExp("Dec[.] 1(st)?|Dec[.] 2(nd)?|Dec[.] 3(rd)?|Dec[.] [1-9]+(th)?").toAutomaton(); // OKAY
                Automaton digit = new RegExp("[0-9]+").toAutomaton(); // OKAY
                Automaton fraction1 = new RegExp("([0-9]+)[.;]([0-9]+)|" + // OKAY
                        "([0-9]+)[.]([0-9]+)[.]([0-9]+)|([0-9]+)[,]([0-9]+)[.]([0-9]+)|" + // OKAY
                        "[0-9]+/[0-9]+|" + // OKAY
                        "[0-9]+;[0-9]").toAutomaton(); //([0-9]+)(.)([0-9]+) // OKAY

                Automaton fraction2 = new RegExp("([0-9]+)([,]([0-9]+))+|\\+[0-9]+|6871082\\#|[0-9]+[,][0-9]+").toAutomaton(); // OKAY
                Automaton hour = new RegExp("[0-9]+:[0-9]+|[0-9]+:[0-9]+:[0-9]+").toAutomaton(); // OKAY
                Automaton percent = new RegExp("(([0-9]+)|([0-9]+)[.]([0-9]+))([\\%])").toAutomaton(); // okay
                Automaton dollar = new RegExp("([\\$])(([0-9]+)|([0-9]+)[.]([0-9]+)|([0-9]+)([,]([0-9]+))+)").toAutomaton(); // OKAY
                Automaton us_dollar = new RegExp("(US)([\\$])(([0-9]+)|([0-9]+)[.]([0-9]+)|([0-9]+)([,]([0-9]+))+)").toAutomaton(); // OKAY
                Automaton dollar_word = new RegExp("\\$ervice|Thi\\$|U\\$|varie\\$|US\\$|Ye\\$|\\$ome|unde\\$tood|\\$involved|Assh\\@\\%\\$e|\\$ometime\\$|co\\$t|F\\%\\#king|me\\$\\$age|1\\%P701![.]doc|ld2d-\\#69336-1[.]XLS").toAutomaton(); // OKAY
                Automaton docs = new RegExp("ld2d-\\#69366-1[.]DOC|ld2d-\\#69334-1[.]DOC|ld2d-\\#69397-1[.]DOC|ld2d-\\#69381-1[.]DOC|ld2d-\\#69396-1[.]DOC|ld2d-\\#69345-1[.]DOC|ld2d-\\#69377-1[.]XLS|ENRONR\\~1[.]DOC").toAutomaton(); // OKAY
                Automaton datesSpecialWriting = new RegExp("[0-9]+/[0-9]+/[0-9]+").toAutomaton(); // OKAY
                Automaton email = new RegExp("[a-zA-Z0-9][a-zA-Z0-9._-:]+[\\@][a-zA-Z0-9._-]+[a-zA-Z0-9]|" +
                                                "[a-zA-Z0-9][a-zA-Z0-9_\\%:]+\\@[a-zA-Z0-9_]+[.][a-zA-Z0-9_]+[a-zA-Z0-9]").toAutomaton();  // OKAY
                Automaton fraction3 = new RegExp("[0-9]+[-0-9/]+[0-9]+|[0-9]+[-0-9/]+[0-9]+").toAutomaton(); // OKAY |[0-9][0-9.]+

                Automaton special_sequences = new RegExp("G\\&G|O\\&M|P\\&I|P\\&L|S\\&S|T\\&D|A\\&E|A\\&K|B\\&B|PG\\&E|p\\&l|AT\\&T|C\\&IC|SDG\\&E|d\u00E9cor|" + // OKAY
                        "entr\u00E9e|Guant\u00E1namo|Igua\u00E7u|\u00C3\u00B3l|Levi\\`s|basic\u00ADally|1002`s|[0-9]+\'s|n\\’t|" + // OKAY
                        "\\’ve|\\’s|\\'m|\\`s|\\’m|-Stip|\\#\'s|·").toAutomaton(); // OKAY
                Automaton emojis = new RegExp("[.]:|:\\(|:\\)|:D|;\\)|:O|:P|\\<-|;P|\\<3|\\<\\<|=\\(|=\\)|\\>=|\\>\\>|D:|:-\\)|;-\\)|\\>:\\(|\\^_\\^|\\>\\>\\>").toAutomaton(); // OKAY

                Automaton missingWords = new RegExp("w\\/|E\\@tG|----(\\>)===(\\})(\\*)(\\{)===(\\<)----|('|’)([0-9]+)|[.][0-9]+|b\\*\\*\\*\\*|" +
                                                        "I\\/C|I\\/S|L\\/C|S\\@P|b\\/t|l\\/c|w\\/o|w\\/out|" +
                                                        "\\’\\’|online\\?u=mayursha\\&m=g\\&t=1|sh\\*t|f\\*ck|f\\*ed|n\'t|47\'s|n't|'ll|'s|'d|'m|'re|it's|'ve|Sha'lan|it's|AK47's|60's|Tuesday's|Monday's|OK'd|GTC's|\\#'s|'ve|'02|Ba'athist|d'etat|[a-zA-Z]+|" +
                                                        "non-stop|post-accident|\'s|anti-American|post-war|Anti-Israeli|pro-Palestinian|" +
                                                        "post-Saddam|pro-Zionist|US[\\$]|e-mail|’72|’73|Sha\'lan|mid-nineties|\'ll|post-Chavez" +
                                                        "|cross-examination|\'d|\'m|pro-India|mid-1980s|anti-Indian|non-Indians|" +
                                                        "non-interference|anti-army|multi-millionnaires|mid-evenings|5:30|6:00|mid-20s|re-routed|" +
                                                        "AK47\'s|it\'s|couter-cultural|\'re|over-generalizations|Sub-cultures|" +
                                                        "multi-nation|non-Arab|non-Moslem|60\'s|anti-Semite|mid-day|anti-democratic|counter-propaganda|" +
                                                        "pre-war|multi-national|9/11|pre-arrest|mid-February|5/18|6:30|coordinator\'s|" +
                                                        "S\\@P|Tuesday\'s|3:30|Monday\'s|I/S|re-looking|`s|Levi`s|11:30|mid-January" +
                                                        "|[0-9]+/[0-9]+-[0-9]+|[0-9]+th|30th|503/464-7927|Non-Bondad|718-780-0046|[0-9]+-[0-9]+-[0-9]+|" +
                                                        "10:30|10:00|7:00|2:30|2:300|4:00|1:30|11/8/2000|PG\\&E|6/14|\'ve|w/o|co-signing|re-run|e-mailed|" +
                                                        "mid-August|OK\'d|12[.]48|GTC\'s|\\#\'s|P\\&L|2nd|non-approved|e-commerce|200,987[.]33|79,000|567[.]77|" +
                                                        "567[.]77|8:00|9:30|3:00|7:30|1:00|12:30|713/853-5984|713/646-6505|853-7557|646-5847|3[.]30|5[.]30|" +
                                                        "3[.]5|888[.]916[.]7184|415[.]782[.]7822|415[.]621[.]8317|800[.]713[.]8600|mid-July|3[.]2|$|I/C|w/|\'02|e-mails" +
                                                        "|re-secured|vice-president|co-operate|non-essential|non-Hodgkin|pro-same|co-workers|multi-compartment|" +
                                                        "re-read|Holocaust-esque|non-human|pre-fabricated|pre-fab|re-type|mid-May|non-commercial|career-wise|anti-ship|re-enlist|" +
                                                        "mid-August|mid-October|mid-2004|mid-2003|re-election|mid-cities|e-reader|semi-automatic|" +
                                                        "mid-80s|non-veg|pre-killed|mid-July|non-crumbly|sub-par|co-workers|re-schedule|re-wiring|" +
                                                        "Over-rated|over-rated|Re-interviewed|over-priced|non-smokers|pre-owned|pre-screened|pre-made|" +
                                                        "re-trained|pre-owned|non-violent|mid-afternoon|semi-sketchiness|Ba\'athist|d\'etat|EB|3801A|nmemdrft8-7-01|[.]doc|ENRON|SCHEDULE|Para13" +
                                                        "|050901[.]doc|Analysis_0712|redlined|a[.]m[.]|11/2|ld2d-\\#([0-9]+)-1[.]DOC|ld2d-\\#69377-1[.]XLS|4-12|ETA_revision0307[.]doc|[0-9]+[.][0-9]+[.][0-9]+" +
                                                        "|[0-9]+[.][0-9]+[.][0-9]+[.][0-9]+|D[.]C[.]|11th|l/c|S\\&S|w/out|=|sh\\*t").toAutomaton();


                Automaton emails = new RegExp("percell\\@swbell[.]net|mailto:amy[.]cornell\\@compaq[.]com|mailto:mayur[.][.][.]\\@yahoo[.]com|mailto:galen[.]torneby\\@nepco[.]com|kaplan\\@iepa[.]com|jermeier\\@earthlink[.]net" +
                        "|mailto:rosario[.]gonzales\\@compaq[.]com|$online\\?\\u=mayursha\\&m=g\\&t=1|carol[.]st[.]clair@enron[.]com|leporjj@selectenergy[.]com|" +  // OKAY
                        "ymsgr:sendIM\\?\\mayursha\\&__Hi\\+Mayur[.][.][.]|dmetcalfe\\@cullenanddykman[.]com|kenneth[.]lay\\@enron[.]com|Jgerma5\\@aol[.]com|francisco[.]pinto[.]leite\\@enron[.]com|skush\\@swbell[.]net").toAutomaton(); // PPPPPPPRRRRRROBLEEEEEEEEEEEEEEEEEEEEEEEEEEEM
                Automaton url = new RegExp("(http:\\/\\/|www[.])([a-zA-Z\\#]+|[.]|\\/|[0-9]+|-|\\?|=|,|_)+|http:\\/\\/www[.]solutions[.]com\\/jump[.]jsp\\?").toAutomaton(); // PPPPPPPRRRRRROBLEEEEEEEEEEEEEEEEEEEEEEEEEEEM
                Automaton url2 = new RegExp("http:\\/\\/washington[.]hyatt[.]com/wasgh/index[.]html|http:\\/\\/www[.]adiccp[.]org/home/default[.]asp|http:\\/\\/www[.]solutions.com\\/jump[.]jsp\\?\\itemID[=]1361\\&itemType[=]PRODUCT\\&path[=]1\\%2C3\\%2C477\\&iProductID[=]1361" + // OKAY
                        "|http:\\/\\/www[.]sonic[.]net/\\~fsjob/TragiCore-TheCivetCat[.]mp3" + // OKAY
                        "|http:\\/\\/www[.]ebay[.]co[.]uk/itm/250927098564\\?\\var=550057729382\\&ssPageName=STRK:MEWAX:IT\\&_trksid=p3984[.]m1438[.]l2649\\#ht_" + //OKAY
                        "|http:\\/\\/www[.]ebay[.]co[.]uk/itm/250927098564\\?\\var=550057729382\\&ssPageName=STRK:MEWAX:IT\\&_trksid=p3984[.]m1438[.]l2649\\#ht_2079wt_893" + //OKAY
                        "|http:\\/\\/www[.]calguard[.]ca[.]gov\\/ia\\/Chernobyl-15\\%20years[.]htm" + //OKAY
                        "|http:\\/\\/www[.]utrechtart[.]com\\/Craft-Supplies\\/Woodworking\\%20Supplies\\/" +
                        "|http:\\/\\/haas[.]berkeley[.]edu\\/\\~borenste" + //OKAY
                        "|http:\\/\\/judiciary[.]senate[.]gov\\/testimony[.]cfm\\?\\id=1725\\&wit_id=4905" + //OKAY
                        "|http:\\/\\/www[.]ebay[.]co[.]uk\\/itm\\/130589513308\\?var=430034792128\\&ssPageName=STRK:MEWAX:IT\\&_trksid=p3984[.]m1438[.]l2648\\#ht_1500wt_660" +
                        "|http:\\/\\/lingerie[.]selectedsex[.]com" +
                        "|www[.]igourmet[.]com" +
                        "|http:\\/\\/www[.]budgieresearch[.]com" +
                        "|http:\\/\\/www[.]4gamer[.]net\\/news\\/image\\/2005[.]09\\/20050920111900_21big[.]html" +
                        "|http:\\/\\/www[.]4gamer[.]net\\/news\\/image\\/2005[.]09\\/20050919032951_41big[.]html" +
                        "|webmas[.][.][.]\\@globelingerie[.]com" +
                        "|http:\\/\\/www[.]4gamer[.]net\\/news\\/image\\/2005[.]09\\/20050919032951_21big[.]html" +
                        "|johnstaikos\\@paulhastings[.]com" +
                        "|adorabledelia6[.][.][.]\\@gmail[.]com" +
                        "|http:\\/\\/unitedway[.]enron[.]com" +
                        "|gottlie[.][.][.]\\@yahoo[.]com" +
                        "|janesmith\\@paulhastings[.]com" +
                        "|kent[.]shoemaker\\@ae[.]ge[.]com" +
                        "|http:\\/\\/www[.]thecatalbum[.]com" +
                        "|SOblander\\@carrfut[.]com" +
                        "|http:\\/\\/explorer[.]msn[.]com" +
                        "|http:\\/\\/www[.]stuff[.]co[.]nz\\/dominion-post\\/" +
                        "|http:\\/\\/seattlepi[.]nwsource[.]com/national/apmideast_story[.]asp\\?\\category=1107\\&slug=Palestinians\\%20Abbas" +
                        "|http:\\/\\/www[.]smooth-on[.]com\\/p132\\/Beginner-Brushable-Mold-Rubber-Options\\/pages[.]html" +
                        "|noc\\@paulhastings[.]com" +
                        "|http:\\/\\/www[.]usatoday[.]com\\/tech\\/science\\/space\\/2005-03-09-nasa-search_x[.]htm\\?\\csp=34" +
                        "|http:\\/\\/www[.]wildlifeofpakistan[.]com\\/PakistanBirdClub\\/index[.]html" +
                        "|RobBrnglsn\\@aol[.]com" +
                        "|sfig[.][.][.]\\@houston[.]rr[.]com" +
                        "|thecatal[.][.][.]\\@hotmail[.]com" +
                        "|http:\\/\\/www[.]4gamer[.]net\\/news\\/image\\/2005[.]09\\/20050917034321_2big[.]html" +
                        "|O\'Neill" +
                        "|http:\\/\\/www[.]accenture[.]com" +
                        "|Kevin[.]A[.]Boone\\@accenture[.]com" +
                        "|it\'s" +
                        "|http:\\/\\/www[.]4gamer[.]net\\/news\\/image\\/2005[.]09\\/20050919032951_32big[.]html" +
                        "|Griffith\\@ENRON" +
                        "|http:\\/\\/www[.]bbc[.]co[.]uk\\/email" +
                        "|http:\\/\\/www[.]4gamer[.]net\\/news\\/image\\/2005[.]09\\/20050909152208_14big[.]html" +
                        "|http:\\/\\/www[.]google[.]co[.]uk\\/search\\?\\q=forensic\\+photography\\&ie=utf-8\\&oe=utf-8\\&aq=t\\&rls=org[.]mozilla:en-US:official\\&client=firefox-a\\&safe=active\\&sout=1" +
                        "|http:\\/\\/www[.]4gamer[.]net\\/news\\/image\\/2005[.]09\\/20050917024918_1big[.]html" +
                        "|julie[.][.][.]\\@bellsouth[.]net" +
                        "|http:\\/\\/www[.]google[.]com\\/search\\?\\aq=0\\&oq=crazy\\+horse\\+mem\\&gcx=w\\&sourceid=chrome\\&ie=UTF-8\\&q=crazy\\+horse\\+memorial" +
                        "|gte114t\\@prism[.]gatech[.]edu" +
                        "|let\'s" +
                        "|http:\\/\\/www[.]bbc[.]co[.]uk\\/news" +
                        "|dailyem[.][.][.]\\@ebs[.]bbc[.]co[.]uk" +
                        "|http:\\/\\/www[.]4gamer[.]net\\/news\\/image\\/2005[.]09\\/20050919032951_39big[.]html" +
                        "|\'ll" +
                        "|\\'ve" +
                        "|franz371[.][.][.]\\@gmail[.]com" +
                        "|ded69[.][.][.]\\@hotmail[.]com" +
                        "|spahnn\\@hnks[.]com" +
                        "|http:\\/\\/news[.]bbc[.]co[.]uk\\/1\\/hi\\/world\\/middle_east\\/4281450[.]stm" +
                        "|danjones\\@paulhastings[.]com" +
                        "|davidr[.][.][.]\\@optonline[.]net" +
                        "|Bryngelson\\@AZURIX" +
                        "|http:\\/\\/en[.]wikipedia[.]org\\/wiki\\/Cubism" +
                        "|http:\\/\\/home[.]enron[.]com\\/employeemeeting" +
                        "|Vangie[.]McGilloway\\@powersrc[.]com" +
                        "|http:\\/\\/go[.]msn[.]com\\/bql\\/hmtag_itl_EN[.]asp" +
                        "|http:\\/\\/www[.]couchsurfing[.]org\\/" +
                        "|Warner\\@ENRON" +
                        "|http:\\/\\/www[.]4gamer[.]net\\/news\\/image\\/2005[.]09\\/20050917034321_15big[.]html" +
                        "|gtownsend\\@manorisd[.]net" +
                        "|http:\\/\\/www[.]4gamer[.]net\\/news\\/image\\/2005[.]09\\/20050919032951_40big[.]html" +
                        "|rreynol[.][.][.]\\\\@cogeco[.]ca" +
                        "|http:\\/\\/cambridgefoodfrivolity[.]blogspot[.]com\\/" +
                        "|mjmcdermott\\@hotmail[.]com" +
                        "|http:\\/\\/www[.]4gamer[.]net\\/news\\/image\\/2005[.]09\\/20050919032951_42big[.]html" +
                        "|http:\\/\\/explorer[.]msn[.]com/intl[.]asp" +
                        "|http:\\/\\/www[.]newscientistspace[.]com\\/article[.]ns\\?\\id=dn8293\\&feedId=human-spaceflight_atom03" +
                        "|goldconn\\@isye[.]gatech[.]edu" +
                        "|http:\\/\\/discountairlineticket[.]blogspot[.]com\\/2005\\/10/some-techniques-to-[.][.][.]" +
                        "|\\@paulhastings[.]com" +
                        "|vangie[.]mcgilloway\\@powersrc[.]com" +
                        "|ENRON\\@enronXgate" +
                        "|w\\/" +
                        "|f\\/2" +
                        "|b\\/c").toAutomaton(); // OKAY


                Automaton specialChars = new RegExp("&|[\\%]|[\\$]+|[\\£]|[\\~]|[\\#]|[\\+]|\\#\\#\\#|->|[\\/]|" +
                        "[\\@]|>[-]+\\||\\|[\\+][-]+>|[\\+]+|===>|*\\~\\*\\~\\*\\~\\*\\~\\*\\~\\*\\~\\*\\~\\*\\~\\*\\~\\*|----\\>===[\\}]*[\\{]===\\<----|[*]+|*").toAutomaton(); // PPPPPPPRRRRRROBLEEEEEEEEEEEEEEEEEEEEEEEEEEEM

                Automaton Specials = new RegExp("♥|O\'Keefe|\'S|\'d|\'m|\'s|3,|12\'s|*************************************|************************************************|***|----==|astronaut\'s").toAutomaton();



                Automaton dates = date.union(date2).union(date3).union(date4).union(date5).union(date6).union(date7).union(date8).union(date9).union(date10).union(date11).union(date12);


                Automaton specialCases = specialChars.union(emails).union(docs).union(emojis).union(email).union(url).union(special_sequences).union(dollar_word);


                Automaton tokFSATrain = wordFSA.union(digit).union(fraction1).union(fraction2).union(fraction3).union(hour).union(percent).union(dollar).union(us_dollar)
                        .union(dates).union(datesSpecialWriting).union(url2).union(specialCases).union(missingWords);


                Automaton tokFSA = wordFSA.union(digit).union(fraction1).union(fraction2).union(fraction3).union(hour).union(percent).union(dollar)
                        .union(us_dollar).union(dates).union(datesSpecialWriting).union(url2).union(dollar_word).union(Specials).union(specialChars);

                String [] tests = {"-"};
                for (int i=0;i<tests.length;i++) {
                    if (fraction3.run(tests[i])) {
                        System.out.println("url FOUND");
                    } else {
                        System.out.println("url NOT FOUND");
                    }
                }
                for (int i=0;i<tests.length;i++) {
                    if (tokFSATrain.run(tests[i])) {
                        System.out.println("url FOUND");
                    } else {
                        System.out.println("url NOT FOUND");
                    }
                }

                Automaton sepFSA = new RegExp("[.][.]|[.][.][.][.][.]| |(\\))(\\))|_|--|[\\<]|[\\>]|-|[.]| \\‘|’|“|”|[.]\\?\\?\\?|?|!|[\\(]:|',|\'\'|[\"\"]|`|![.]|[.]!|[.?]|,?|…|‘|!\\?!|[\\)\\)]" +
                        "|\"\"|[.]:|[.]/|[.][.]|,,|!+|\\‘\\’|\\?\\?|\\?\\?\\?\\?\\?\\?\\?\\?\\?\\?\\?\\?\\?\\?\\?|\\?\\?\\?\\?\\?\\?" +
                        "|[.][.][.]\\?!!!|…[.]|\\!\\!\\!\\!\\!\\!\\!\\!\\!\\!\\?|1\\?!\\?!\\?|!!!\\?|\\?\\?\\?\\?|,\\?|\\?\\?\\?\\?\\?|[.]\\?" +
                        "|\\?!\\?|\\?\\?\\?|!\\?|\\?|\\?!|\\?!\\?!\\?|:[.]|:/|!!![.]|[.]|[.][.][.]|'|,|;|:|[\"]|[\\(]|[\\)]|[\\{]|[\\}]|" +
                        "[.][.][.][.][.][.][.][.]|[.][.][.][.][.][.][.][.][.][.][.][.][.][.][.][.][.][.][.][.]|[.][.][.][.][.][.][.]|[.][.][.][.][.][.]|[.][.][.][.][.][.][.][.][.]|"+
                        "[.][.][.][.]|[.][.][.][.][.]|[.][.][.][.][.][.][.][.][.][.][.][.][.][.][.][.][.][.][.][.][.]|[.][.][.][.][.][.][.][.][.][.][.][.][.][.][.]|"+
                        "[.][.][.][.][.][.][.][.][.][.][.]|[.][.][.][.][.][.][.][.][.][.]|:\\?|!\\?\\!\\?\\!|\\?\\?[.]|[.][.]\\?|[\\?]+|!\\?\\?\\?\\?\\?\\!!!!|!!!!!!!!!!!!!\\?|"+
                        "\\]|\\[|[.]\\?|[.][.][.][.]").toAutomaton(); // |[\|]



                FileOutputStream fos = new FileOutputStream(PATH_TOK_FSA);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.writeObject(tokFSATrain); //tokFSA
                os.flush();

                fos = new FileOutputStream(PATH_SEP_FSA);
                os = new ObjectOutputStream(fos);
                os.writeObject(sepFSA);
                os.flush();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (InvalidClassException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


    }

}
