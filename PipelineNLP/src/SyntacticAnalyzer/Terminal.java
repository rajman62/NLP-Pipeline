package SyntacticAnalyzer;

/**
 * Created by MeryemMhamdi on 4/21/17.
 */
public class Terminal {
    private String word;
    private String tag;

    public Terminal(String word, String tag){
        this.word = word;
        this.tag = tag;
    }
    public String getWord(){
        return this.word;
    }
    public String getTag(){
        return this.tag;
    }
    public void setWord(String word){
        this.word = word;
    }
    public void setTag(String tag){
        this.tag = tag;
    }
    public String toString(){
        return this.getTag() + " = " +this.getWord();
    }

}
