package SyntacticAnalyzer;

import java.io.Serializable;

/** Class to encapsulate the information of each word in conllu format
 * @author MeryemMhamdi
 * @date 5/13/17.
 */
public class DependencyConLL implements Serializable {

    private double id; // Word index, integer starting at 1 for each new sentence;
    // may be a range for multiword tokens; may be a decimal number for empty nodes.
    private String form; //Word form or punctuation symbol
    private String lemma; //Lemma or stem of word form
    private String upostag; //Universal part-of-speech tag
    private String xpostag; //Language-specific part-of-speech tag; underscore if not available
    private String feats; //List of morphological features from the universal feature inventory
    // or from a defined language-specific extension; underscore if not available
    private double head; //Head of the current word, which is either a value of ID or zero (0)
    private String deprel; //Universal dependency relation to the HEAD (root iff HEAD = 0)
    // or a defined language-specific subtype of one
    private String deps; //Enhanced dependency graph in the form of a list of head-deprel pairs
    private String misc; //Any other annotation

    public DependencyConLL( double id, String form, String lemma, String upostag, String xpostag, String feats,
                            double head, String deprel, String deps, String misc){
        this.id = id;
        this.form = form;
        this.lemma = lemma;
        this.upostag = upostag;
        this.xpostag = xpostag;
        this.feats = feats;
        this.head = head;
        this.deprel = deprel;
        this.deps = deps;
        this.misc = misc;
    }

    public double getId() {
        return this.id;
    }

    public String getForm() {
        return this.form;
    }

    public String getLemma() {
        return this.lemma;
    }

    public String getUPosTag() {
        return this.upostag;
    }

    public String getXPosTag() {
        return this.xpostag;
    }

    public String getFeats() {
        return this.feats;
    }

    public double getHead() {
        return this.head;
    }

    public String getDeprel() {
        return this.deprel;
    }

    public String getDeps() {
        return this.deps;
    }

    public String getMisc() {
        return this.misc;
    }


    public void setId(double id) {
        this.id = id;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public void setUPosTag(String ctag) {
        this.upostag = upostag;
    }

    public void setXPosTag(String xpostag) {
        this.xpostag = xpostag;
    }

    public void setFeats(String feats) {
        this.feats = feats;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public void setDeprel(String deprel) {
        this.deprel = deprel;
    }

    public void setDeps(String deps) {
        this.deps = deps;
    }

    public void setMisc(String misc) {
        this.misc = misc;
    }

    @Override
    public boolean equals(Object obj){
        if (obj instanceof DependencyConLL) {
            DependencyConLL dependencyConLL = (DependencyConLL) obj;
            if (dependencyConLL.getLemma().equals(this.getLemma())&& dependencyConLL.getXPosTag().equals(this.getXPosTag())
                    && dependencyConLL.getHead() == this.getHead() && dependencyConLL.getDeprel().equals(this.getDeprel())){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
