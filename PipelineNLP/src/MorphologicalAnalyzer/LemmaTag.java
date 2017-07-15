package MorphologicalAnalyzer;

import LexicalAnalyzer.FSA.WordTag;

import java.io.Serializable;

/**
 * Created by MeryemMhamdi on 6/9/17.
 */
public class LemmaTag implements Serializable {
    private String lemma;
    private String tag;
    public LemmaTag(String lemma,String tag){
        this.lemma = lemma;
        this.tag = tag;
    }
    public String getLemma(){
        return this.lemma;
    }
    public String getTag(){
        return this.tag;
    }
    public void setLemma(String lemma){
        this.lemma = lemma;
    }
    public void setTag(String tag){
        this.tag = tag;
    }
    @Override
    public boolean equals(Object ob){
        if (ob instanceof WordTag){
            LemmaTag wordTag = (LemmaTag) ob;
            if (wordTag.getLemma().equals(this.getLemma()) && wordTag.getTag().equals(this.getTag())){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    public String toString(){
        return " Lemma= "+this.lemma+" Tag= "+this.tag ;
    }

    @Override
    public int hashCode() {
        int hash = 134;
        hash = hash * 31 + this.getLemma().hashCode();
        hash = hash * 13 + this.getTag().hashCode();
        return hash;
    }

}
