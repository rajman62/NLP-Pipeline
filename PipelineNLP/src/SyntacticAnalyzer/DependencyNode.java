package SyntacticAnalyzer;

/**
 * Created by MeryemMhamdi on 5/13/17.
 */
public class DependencyNode {
    private double id;
    private String lemma;
    private String xpostag;
    private String deprel;
    private int pointer;
    public DependencyNode(double id, String lemma, String xpostag, String deprel,int pointer){
        this.id = id;
        this.lemma = lemma;
        this.xpostag = xpostag;
        this.deprel = deprel;
        this.pointer = pointer;
    }
    public DependencyNode(double id, String lemma, String xpostag, String deprel){
        this.id = id;
        this.lemma = lemma;
        this.xpostag = xpostag;
        this.deprel = deprel;
    }
    public double getId(){
        return this.id;
    }
    public String getLemma(){
        return this.lemma;
    }
    public String getXpostag(){
        return this.xpostag;
    }
    public String getDeprel(){
        return this.deprel;
    }
    public int getPointer() {
        return this.pointer;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setLemma(String lemma){
        this.lemma = lemma;
    }
    public void setXpostag(String xpostag){
        this.xpostag = xpostag;
    }
    public void setDeprel(String deprel){
        this.deprel = deprel;
    }
    public void setPointer(int pointer){
        this.pointer = pointer;
    }

    @Override
    public boolean equals(Object obj){
        if (obj instanceof DependencyNode){
            DependencyNode node = (DependencyNode) obj;
            if(node.getId()==this.getId() && node.getLemma().equals(this.getLemma())
                    && node.getDeprel().equals(this.getDeprel()) && node.getXpostag().equals(this.getXpostag())){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    @Override
    public int hashCode() {
        int hash = 134;
        hash = hash * 17 + (int)this.getId();
        hash = hash * 31 + this.getLemma().hashCode();
        hash = hash * 13 + this.getXpostag().hashCode();
        hash = hash * 40 + this.getDeprel().hashCode();
        return hash;
    }
    public String toString(){
        return this.id+ ":"+this.lemma + ":" + this.xpostag + ":" + this.deprel;
    }
}