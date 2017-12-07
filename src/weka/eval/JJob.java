package weka.eval;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JJob {
    @JsonProperty
    String inputPath;
    @JsonProperty
    String outputPath;

    public static void main(String[] args) {
        JJob job = new JJob();
        job.inputPath = "files/in/arff";
        job.outputPath = "files/out/";
        JObject.save("files/in/json/job.json", job, true);
    }
}
