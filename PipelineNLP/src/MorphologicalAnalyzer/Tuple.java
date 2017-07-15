package MorphologicalAnalyzer;

/**
 * Created by MeryemMhamdi on 6/2/17.
 */
public class Tuple {
    String word;
    LemmaTag cannonicalForm;
    public Tuple(String word, LemmaTag cannonicalForm){
        this.word = word;
        this.cannonicalForm = cannonicalForm;
    }
    public String getWord(){
        return this.word;
    }
    public LemmaTag getCannonicalForm(){
        return this.cannonicalForm;
    }
    public void setWord(String word){
        this.word = word;
    }
    public void setCannonicalForm(LemmaTag cannonicalForm){
        this.cannonicalForm = cannonicalForm;
    }
    @Override
    public boolean equals(Object obj){
        if (obj instanceof Tuple){
            Tuple tuple = (Tuple) obj;
            if (this.word.equals(tuple.getWord()) && this.cannonicalForm.toString().equals(tuple.getCannonicalForm().toString())){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
