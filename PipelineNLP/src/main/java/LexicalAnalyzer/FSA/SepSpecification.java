package LexicalAnalyzer.FSA;

/**
 * Created by MeryemMhamdi on 4/29/17.
 */
public class SepSpecification {
    private boolean persistent;
    private boolean eos;

    public SepSpecification(boolean persistent, boolean eos){
        this.persistent = persistent;
        this.eos = eos;
    }

    public void setPersistent(boolean persistent){
        this.persistent = persistent;
    }

    public void setEos(boolean eos){
        this.eos = eos;
    }

    public boolean getPersistent(){
        return this.persistent;
    }

    public boolean getEOS(){
        return this.eos;
    }
}
