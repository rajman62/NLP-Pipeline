package implementations.syntacticutils;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class BinaryGrammar extends Grammar {
    private HashMap<Pair<Sigma, Sigma>, Set<Rule>> rightSideIndexingPairs;

    public BinaryGrammar() {
        super();
        rightSideIndexingPairs = new HashMap<>();
    }

    public BinaryGrammar(Grammar grammar) {
        super();
        rightSideIndexingPairs = new HashMap<>();
        for (Rule rule : grammar.getAllRules())
            this.addRule(rule);
    }

    @Override
    public void addRule(Rule newRule) {
        for (Rule rule : normalizeRule(newRule)) {
            super.addRule(rule);

            if (rule.getRight().length == 2) {
                Pair<Sigma, Sigma> right = Pair.of(rule.getRight()[0], rule.getRight()[1]);
                if (!rightSideIndexingPairs.containsKey(right))
                    rightSideIndexingPairs.put(right, new HashSet<>());
                rightSideIndexingPairs.get(right).add(rule);
            }
        }
    }

    private List<Rule> normalizeRule(Rule rule) {
        List<Rule> out = new LinkedList<>();

        NonTerminal left = rule.getLeft();
        Sigma[] right = rule.getRight();

        if (right.length == 1)
            out.add(rule);

        else if (right.length >= 2) {
            int i;
            for (i = 0; i < right.length - 2; i++) {
                AbstractNonTerminal newLeft = new AbstractNonTerminal();
                Sigma[] newRight = new Sigma[2];
                newRight[0] = right[i];
                newRight[1] = newLeft;
                out.add(new Rule(left, newRight));
                left = newLeft;
            }

            out.add(new Rule(left, Arrays.copyOfRange(right, right.length - 2, right.length)));
        }

        return out;
    }

    public boolean containsRulesWithRight(Pair<Sigma, Sigma> right) {
        return rightSideIndexingPairs.containsKey(right);
    }

    public Set<Rule> getRulesWithRight(Pair<Sigma, Sigma> right) {
        return rightSideIndexingPairs.get(right);
    }
}
