package LexicalAnalyzer.FSA;

import java.io.Serializable;

/**
 * Created by MeryemMhamdi on 5/21/17.
 */
public class WordTag implements Serializable{
    private String word;
    private String tag;
    private String lemma;
    public WordTag(String word,String tag){
        this.word = word;
        this.tag = tag;
    }
    public WordTag(String word,String lemma,String tag){
        this.word = word;
        this.tag = tag;
        this.lemma = lemma;
    }
    public String getWord(){
        return this.word;
    }
    public String getTag(){
        return this.tag;
    }
    public String getLemma(){
        return this.lemma;
    }
    public void setWord(String word){
        this.word = word;
    }
    public void setTag(String tag){
        this.tag = tag;
    }
    public void setLemma(String lemma){
        this.lemma = lemma;
    }
    @Override
    public boolean equals(Object ob){
        if (ob instanceof WordTag){
            WordTag wordTag = (WordTag) ob;
            if (wordTag.getWord().equals(this.getWord()) && wordTag.getTag().equals(this.getTag()) && wordTag.getLemma().equals(this.getLemma())){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    public String toString(){
        return "Word= "+this.word + " Tag= "+this.tag+ " Lemma= "+this.lemma ;
    }

    @Override
    public int hashCode() {
        int hash = 134;
        hash = hash * 17 + this.getWord().hashCode();
        hash = hash * 31 + this.getLemma().hashCode();
        hash = hash * 13 + this.getTag().hashCode();
        return hash;
    }

}
