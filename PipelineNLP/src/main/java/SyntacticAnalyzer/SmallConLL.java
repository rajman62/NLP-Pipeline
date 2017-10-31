package SyntacticAnalyzer;

import java.io.Serializable;

/**
 * @author MeryemMhamdi
 * @date 5/18/17.
 */
public class SmallConLL implements Serializable {
    private String head;
    private String posHead;
    private String dep;
    private String posDep;
    private String rel;

    public SmallConLL(String head,String posHead,String dep,String posDep,String rel){
        this.head = head;
        this.posHead = posHead;
        this.dep = dep;
        this.posDep = posDep;
        this.rel = rel;
    }

    public String getHead(){
        return this.head;
    }
    public String getPosHead(){
        return this.posHead;
    }
    public String getDep(){
        return this.dep;
    }
    public String getPosDep(){
        return this.posDep;
    }
    public String getRel(){
        return this.rel;
    }

    public void setHead(String head){
         this.head = head;
    }
    public void setPosHead(String posHead){
         this.posHead = posHead;
    }
    public void setDep(String dep){
        this.dep = dep;
    }
    public void setPosDep(String posDep){
        this.posDep = posDep;
    }
    public void setRel(String rel){
        this.rel = rel;
    }

    public String toString(){
        return this.head + ":"+ this.posHead + " -> " + this.dep + ":" + this.posDep + "(" +this.rel+")";
    }

}
