import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClassifierResult {
    @JsonProperty int id;
    @JsonProperty String dataset;
    @JsonProperty String className;
    @JsonProperty double nCorrect;
    @JsonProperty double nClassfied;
    @JsonProperty double nUnclassifed;
    @JsonProperty double classifierName;
    @JsonProperty int iteration;
    @JsonProperty double recall;
    @JsonProperty double errorRate;
    @JsonProperty int numInstances;
    @JsonProperty int numAttributes;
    @JsonProperty int classIndex;
    @JsonProperty ClassifierTag classifierTag;

    public static void main(String[] args) {
        ClassifierResult cr = new ClassifierResult();
        cr.dataset = "iris.arff";
        cr.className = ClassifierResult.class.getName();
        cr.nCorrect = .4;
        cr.numInstances = 40;
        cr.classifierTag = ClassifierTag.JRIP;

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(Files.newBufferedWriter(Paths.get("files/in/1.json")),
                    cr);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
