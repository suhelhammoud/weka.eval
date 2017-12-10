package weka.eval;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JAttributeSearch implements JObject {

    @JsonProperty
    TAttributeSearch tSearch;

    @JsonProperty
    double threshold;// T
    @JsonProperty
    int numToSelect = -1;// N
    @JsonProperty
    String startSet; //P
    @JsonProperty
    String direction; //Direction of the tSearch 0:backward, 1:forward, 2: bi-directional
    //TODO add all options for BestFirst and for GreedyStepwise

    public String[] getOptions(boolean withClassName) {
//        return tSearch.ge
        List<String> result = new ArrayList<>(10);
        if (withClassName)
            result.add(TClassName.of(tSearch));

        if (startSet != null || startSet.equals("")) {
            result.add("-P");
            result.add(startSet);
        }
        ;

        if (numToSelect > 0) {
            result.add("-N");
            result.add(String.valueOf(numToSelect));
        }

        switch (tSearch) {
            case Ranker:
                result.add("-T");
                result.add(String.valueOf(threshold));
                break;
            case BestFirst:
                if (direction != null || direction.length() > 0) {
                    result.add("-D");
                    result.add(direction);
                }
                break;
        }
        return result.toArray(new String[0]);
    }
}
