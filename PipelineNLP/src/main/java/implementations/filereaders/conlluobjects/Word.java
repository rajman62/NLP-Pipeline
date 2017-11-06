package implementations.filereaders.conlluobjects;

import org.apache.commons.lang3.Range;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Word {
    // strings take value "_" when undefined
    public Range<Float> id;
    public String form;
    public String lemma;
    public String upostag;
    public String xpostag;
    public String feats;
    public Integer head; // can be null
    public String deprel;
    public String deps;
    public String misc;

    private static Pattern linePattern = Pattern.compile(
            "(?<id1>\\d+(\\.\\d+)?)(-(?<id2>\\d+(\\.\\d+)?))?\\s+"
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

        if (parsedLine.group("id2") != null)
            out.id = Range.between(
                    Float.parseFloat(parsedLine.group("id1")),
                    Float.parseFloat(parsedLine.group("id2")));
        else
            out.id = Range.between(
                    Float.parseFloat(parsedLine.group("id1")),
                    Float.parseFloat(parsedLine.group("id1")));

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
