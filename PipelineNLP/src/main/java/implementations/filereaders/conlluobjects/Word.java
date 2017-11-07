package implementations.filereaders.conlluobjects;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Word {
    // conllu v2 specifies 3 different types of id
    public Integer idInt = null;
    public Pair<Integer, Integer> idFloat = null;
    public Range<Integer> idRange = null;

    // not in the conllu file, to be used by Sentence
    // id can be null if multiTokenHead is not
    public Integer id;
    public Integer multiTokenHead = null;

    // can be null in the case of multi token (they're for they are)
    public Integer head;

    // strings take value "_" when undefined
    public String form;
    public String lemma;
    public String upostag;
    public String xpostag;
    public String feats;
    public String deprel;
    public String deps;
    public String misc;

    private static Pattern linePattern = Pattern.compile(
            "("
                    + "(?<idint>\\d+)|"
                    + "(?<idrange>(?<idrange1>\\d+)-(?<idrange2>\\d+))|"
                    + "(?<idfloat>(?<idfloat1>\\d+).(?<idfloat2>\\d+))" +
            ")\\s+"
            + "(?<form>[^\\s]+)\\s+"
            + "(?<lemma>[^\\s]+)\\s+"
            + "(?<upostag>[^\\s]+)\\s+"
            + "(?<xpostag>[^\\s]+)\\s+"
            + "(?<feats>[^\\s]+)\\s+"
            + "(?<head>(\\d+)|_)\\s+"
            + "(?<deprel>[^\\s]+)\\s+"
            + "(?<deps>[^\\s]+)\\s+"
            + "(?<misc>[^\\s]+)\\s*"
    );

    public static Word parse(String line) {
        Word out = new Word();
        Matcher parsedLine = linePattern.matcher(line);
        if (!parsedLine.matches())
            throw new IllegalArgumentException("");

        if (parsedLine.group("idint") != null)
            out.idInt = Integer.parseInt(parsedLine.group("idint"));

        else if ((parsedLine.group("idfloat") != null))
            out.idFloat = Pair.of(
                    Integer.parseInt(parsedLine.group("idfloat1")),
                    Integer.parseInt(parsedLine.group("idfloat2")));

        else if (parsedLine.group("idrange") != null)
            out.idRange = Range.between(
                    Integer.parseInt(parsedLine.group("idrange1")),
                    Integer.parseInt(parsedLine.group("idrange2")));

        out.form = parsedLine.group("form");
        out.lemma = parsedLine.group("lemma");
        out.upostag = parsedLine.group("upostag");
        out.xpostag = parsedLine.group("xpostag");
        out.feats = parsedLine.group("feats");
        if (parsedLine.group("head").equals("_"))
            out.head = null;
        else
            out.head = Integer.parseInt(parsedLine.group("head"));
        out.deprel = parsedLine.group("deprel");
        out.deps = parsedLine.group("deps");
        out.misc = parsedLine.group("misc");

        return out;
    }
}
