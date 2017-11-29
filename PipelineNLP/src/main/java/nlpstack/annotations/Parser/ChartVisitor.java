package nlpstack.annotations.Parser;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import nlpstack.communication.Chart;

import java.util.ArrayList;
import java.util.List;

public class ChartVisitor extends AnnotationParserBaseVisitor<Chart>{
    @Override
    public Chart visitChart(AnnotationParserParser.ChartContext ctx) {
        List<AnnotationParserParser.ChartTokenContext> tokensCtx = ctx.chartToken();
        List<AnnotationParserParser.ChartTagContext> chartTagsCtx = ctx.chartTag();

        List<String> tokenStrings = new ArrayList<>(tokensCtx.size());

        for (AnnotationParserParser.ChartTokenContext token : tokensCtx) {
            tokenStrings.add(
                    token.token().getText()
                            .replace("\\\\", "\\").replace("\\)", ")")
            );
        }



        Chart out = Chart.getEmptyChart(tokenStrings);

        for(AnnotationParserParser.ChartTagContext tag : chartTagsCtx) {
            out.addRule(Integer.parseInt(tag.number(0).getText()),
            Integer.parseInt(tag.number(1).getText()),
            tag.tag().getText()
                    .replace("\\\\", "\\").replace("\\\"", "\"")
            );
        }

        return out;
    }
}
