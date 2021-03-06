package weka.eval;

import com.fasterxml.jackson.annotation.JsonProperty;
import weka.classifiers.Classifier;
import weka.classifiers.rules.OneR;

import java.util.Optional;
import java.util.logging.Logger;

public class JClassifier implements JObject {
    static Logger log = Logger.getLogger(JClassifier.class.getName());

    @JsonProperty
    int id;

    @JsonProperty
    String className;

    @JsonProperty
    JClassifierParams params;

    @JsonProperty
    JClassifierEvaluation eval;

    @JsonProperty
    JDataSet data;


    public static void main(String[] args) {
        Optional<Classifier> c = (Optional<Classifier>) JUtils.forName("done");
        System.out.println("c = " + c);
        OneR jrip = (OneR) JUtils.forName("weka.classifiers.rules.OneR").get();
        System.out.println(jrip.getTechnicalInformation().toString());
    }

}
