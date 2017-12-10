package weka.eval;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import static weka.eval.JUtils.listOf;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JAttributeEvaluator implements JObject {

    @JsonProperty
    TAttributeEvaluator tEvaluator;

    @JsonProperty
    TAttributeSearch tSearch;

    public String[] getOptions(boolean useClassName) {
        List<String> result = new ArrayList<>();
        if (useClassName) {
            result.add(TClassName.of(tEvaluator));
        }
        //do quick setup for only fixed set of options TODO change later
        switch (tEvaluator) {
            case CFS:
                result.addAll(listOf("-P 1 -E 1"));
                break;
            case L2:
                result.addAll(listOf("-F FIRUZ"));
                break;
        }
        return result.toArray(new String[0]);
    }

}
