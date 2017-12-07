package weka.eval;

import com.fasterxml.jackson.annotation.JsonProperty;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instances;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class JAttributeEvaluation {
    static Logger log = Logger.getLogger(JAttributeEvaluation.class.getName());

    @JsonProperty
    JDataSet dataSet;

    @JsonProperty
    AttributeSelectorTag selector;

    @JsonProperty
    AttributeThresholdType thresholdType;

    @JsonProperty
    double[] attributesRanks;

    @JsonProperty
    int numAttributeToSelect;

    @JsonProperty
    double threshould;

    public static ASEvaluation getAttributeEvaluation(AttributeSelectorTag tag) {
        return (ASEvaluation) JObject.forName(tag.className).get();
    }

    public static void runCVTest(Instances data) throws Exception {
        AttributeSelection select = new AttributeSelection();
        InfoGainAttributeEval eval = (InfoGainAttributeEval) JObject.forName(AttributeSelectorTag.IG.className).get();

        select.setEvaluator(eval);
        select.setFolds(10);

        String result = select.CrossValidateAttributes();
        log.log(Level.INFO, "CrossValidateAttributes = {}", result);
    }

    public static void evaluate(Instances data, ASEvaluation aseEval, boolean useCrossValidation)
            throws Exception {
        AttributeSelection eval = new AttributeSelection();
        Ranker searchMethod = new Ranker();
        searchMethod.setThreshold(0);

        eval.setSearch(searchMethod);
        eval.setEvaluator(aseEval);
        int numFolds = 10;
        eval.setFolds(numFolds);
        Instances inst = new Instances(data);
        if (useCrossValidation) {
            log.fine("Randomizing instances...");
            Random random = new Random(23232);
            inst.randomize(random);
            if (inst.attribute(inst.classIndex()).isNominal()) {
                log.fine("Stratifying instances...");
                inst.stratify(numFolds);
            }
            for (int fold = 0; fold < numFolds; fold++) {
                log.fine("Creating splits for fold " + (fold + 1)
                        + "...");
                Instances train = inst.trainCV(numFolds, fold, random);
                log.fine("Selecting attributes using all but fold "
                        + (fold + 1) + "...");

                eval.selectAttributesCVSplit(train);
            }
            System.out.println("eval.CVResultsString() = " + eval.CVResultsString());

            System.out.println(Arrays.toString(eval.rankedAttributes()));
        }
    }

    public static void main(String[] args) throws Exception {
        String jobFileName = "files/in/json/job.json";
        JJob job = JObject.read(jobFileName, JJob.class);
        List<String> fileNames = Files.list(Paths.get(job.inputPath))
                .map(Object::toString)
                .collect(Collectors.toList());
        System.out.println("fileNames = " + fileNames);
        Instances data = JUtils.instancesOf(fileNames.get(0));
        data.setClassIndex(data.numAttributes() - 1);
        log.info("data.relationName() = " + data.relationName());
        InfoGainAttributeEval igEval = (InfoGainAttributeEval)
                JObject.forName(AttributeSelectorTag.IG.className).get();
        evaluate(data, igEval, true);
    }
}
