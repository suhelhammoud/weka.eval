package weka.eval;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClassifierResult implements JObject{
    @JsonProperty
    int id;
    @JsonProperty
    String dataset;
    @JsonProperty
    String className;
    @JsonProperty
    double nCorrect;
    @JsonProperty
    double nClassfied;
    @JsonProperty
    double nUnclassifed;
    @JsonProperty
    double classifierName;
    @JsonProperty
    int iteration;
    @JsonProperty
    double recall;
    @JsonProperty
    double errorRate;
    @JsonProperty
    int numInstances;
    @JsonProperty
    int numAttributes;
    @JsonProperty
    int classIndex;
    @JsonProperty
    double[][] confusionMatrix;
    @JsonProperty
    ClassifierTag classifierTag;
}
