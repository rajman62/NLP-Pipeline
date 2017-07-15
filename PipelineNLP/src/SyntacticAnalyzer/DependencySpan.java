package SyntacticAnalyzer;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A contiguous span over some part of the input string representing
 * dependency (head -> modifier) relationships amongst words.  An atomic
 * span corresponds to only one word so it isn't a 'span' in the conventional
 * sense, as its _start_index = _end_index = _head_index for concatenation
 * purposes.  All other spans are assumed to have arcs between all nodes
 * within the start and end indexes of the span, and one head index corresponding
 * to the head word for the entire span.  This is the same as the root node if
 * the dependency structure were depicted as a graph.
 *
 * @author MeryemMhamdi
 * @date 5/9/17
 */
public class DependencySpan {
    private int startIndex;
    private int endIndex;
    private int headIndex;
    private int [] arcs;
    private String [] tags;

    public DependencySpan(int startIndex,int endIndex, int headIndex, int [] arcs, String [] tags){
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.headIndex = headIndex;
        this.arcs = arcs;
        this.tags = tags;
    }

    public int getHeadIndex(){
        return this.headIndex;
    }

    public int getStartIndex(){
        return this.startIndex;
    }

    public int getEndIndex(){
        return this.endIndex;
    }

    public int [] getArcs(){
        return this.arcs;
    }

    public String [] getTags() { return this.tags;}

    public String conciseToString(){
        return "Dependency Span with start index= "+this.startIndex + ", end index= "+this.endIndex+" head index= "+this.headIndex;
    }

    public String toString (){
        String result = "Dependency Span with start index= "+this.startIndex + ", end index= "+this.endIndex+" head index= "+this.headIndex;
        for (int i=0;i<arcs.length;i++){
            result = result + "\n ("+i+","+arcs[i]+","+tags[i]+")";
        }
        return result;
    }

    public boolean isTwoArrayListsWithSameValues(int [] list1, int [] list2)
    {
        //null checking
        if(list1==null && list2==null)
            return true;
        if((list1 == null && list2 != null) || (list1 != null && list2 == null))
            return false;

        if(list1.length!=list2.length)
            return false;
        for(int itemList1: list1)
        {
            if(!Arrays.asList(list2).contains(itemList1))
                return false;
        }

        return true;
    }
    public boolean equals(DependencySpan ds){
        if (this.headIndex == ds.getHeadIndex() && this.startIndex == ds.getStartIndex() && this.endIndex == ds.getEndIndex()
                &&  isTwoArrayListsWithSameValues(this.arcs,ds.getArcs())){
            return true;
        }
        return false;
    }


}

