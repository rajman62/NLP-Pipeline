package implementations.syntacticutils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Grammar implements Iterable<Rule> {
    private HashSet<Rule> allRules;

    // rules with only one right elements, and rules multiple right elements
    private HashSet<Rule> singleRightSideRules;
    private HashSet<Rule> multiRightSideRules;

    private HashMap<NonTerminal, HashSet<Rule>> leftSideIndexing;

    public Grammar() {
        allRules = new HashSet<>();
        leftSideIndexing = new HashMap<>();
        singleRightSideRules =  new HashSet<>();
        multiRightSideRules = new HashSet<>();
    }

    public void addRule(Rule rule) {
        allRules.add(rule);
        if (rule.getRight().length == 1)
            singleRightSideRules.add(rule);
        else if (rule.getRight().length >= 2)
            multiRightSideRules.add(rule);
        if(leftSideIndexing.containsKey(rule.getLeft()))
            leftSideIndexing.get(rule.getLeft()).add(rule);
        else {
            HashSet<Rule> hashSet = new HashSet<>();
            hashSet.add(rule);
            leftSideIndexing.put(rule.getLeft(), hashSet);
        }
    }

    public Set<Rule> getRulesWithLeft(NonTerminal leftSide) {
        return leftSideIndexing.get(leftSide);
    }

    @Override
    public Iterator<Rule> iterator() {
        return allRules.iterator();
    }

    public HashSet<Rule> getSingleRightSideRules() {
        return singleRightSideRules;
    }

    public HashSet<Rule> getMultiRightSideRules() {
        return multiRightSideRules;
    }
}
