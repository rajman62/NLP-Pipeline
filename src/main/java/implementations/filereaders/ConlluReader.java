package implementations.filereaders;

import implementations.filereaders.conlluobjects.ConlluIterator;
import implementations.filereaders.conlluobjects.Sentence;
import implementations.filereaders.conlluobjects.Word;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ConlluReader implements Iterable<Sentence> {
    private String path;

    private boolean statsCalculated = false;
    private int nbSentences = 0;
    private int nbForm = 0;
    private int nbLemmas = 0;
    private Set<String> xtags;
    private Set<String> utags;

    public ConlluReader(String path) {
        this.path = path;
    }

    public void calculateStats() {
        if (statsCalculated)
            return;

        statsCalculated = true;
        HashSet<String> wordSet = new HashSet<>();
        HashSet<String> lemmaSet = new HashSet<>();
        xtags = new HashSet<>();
        utags = new HashSet<>();

        for (Sentence sent : this) {
            nbSentences += 1;
            for (Word word : sent) {
                wordSet.add(word.form);
                lemmaSet.add(word.lemma);
                xtags.add(word.xpostag);
                utags.add(word.upostag);
            }
        }

        nbForm = wordSet.size();
        nbLemmas = lemmaSet.size();
    }

    private void checkStatsHaveBeenCalculated() {
        if (!statsCalculated) {
            throw new IllegalStateException(
                    "calculateStats should be called before stats are retrieved from ConlluReader");
        }
    }

    public int getNbSentences() {
        checkStatsHaveBeenCalculated();
        return nbSentences;
    }

    public int getNbForms() {
        checkStatsHaveBeenCalculated();
        return nbForm;
    }

    public int getNbLemmas() {
        checkStatsHaveBeenCalculated();
        return nbLemmas;
    }

    public Set<String> getUPostTags() {
        checkStatsHaveBeenCalculated();
        return new HashSet<>(utags);
    }

    public Set<String> getXPostTags() {
        checkStatsHaveBeenCalculated();
        return new HashSet<>(xtags);
    }

    @Override
    public Iterator<Sentence> iterator() {
        try {
            return new ConlluIterator(path);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        return null;
    }
}
