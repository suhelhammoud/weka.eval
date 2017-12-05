package weka.eval;

import com.fasterxml.jackson.annotation.JsonProperty;
import weka.core.Attribute;
import weka.core.Instances;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;

public class JDataSet {
    @JsonProperty
    String relationName;
    @JsonProperty
    int numInstances;
    @JsonProperty
    int numAttributes;
    @JsonProperty
    String[] attributes;
    @JsonProperty
    AttributeType[] attributesTypes;
    @JsonProperty
    int[] attributesItems;
    @JsonProperty
    int classIndex;

    /**
     * Factory method to get JDataSet Object from Weka Instances object
     * @param data
     * @return new JDataSet instance representing Weka.Instances object
     */
    public static JDataSet of(Instances data) {
        JDataSet result = new JDataSet();
        result.relationName = data.relationName();
        result.numInstances = data.numInstances();
        result.numAttributes = data.numAttributes();
        result.attributes = Collections
                .list(data.enumerateAttributes())
                .stream()
                .map(Attribute::name)
                .toArray(String[]::new);
        result.attributesTypes = Collections
                .list(data.enumerateAttributes())
                .stream()
                .mapToInt(Attribute::type)
                .mapToObj(AttributeType::of)
                .toArray(AttributeType[]::new);
        result.attributesItems = Collections
                .list(data.enumerateAttributes())
                .stream()
                .mapToInt(Attribute::numValues)
                .toArray();
        result.classIndex = data.classIndex();
        return result;
    }

    static void testJDataSet() {
        String fileName = "files/data/iris.arff";
        Instances data = null;
        try {
            data = new Instances(new FileReader(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JDataSet jdata = of(data);
        JObject.save("files/tmp/ds_pretty.json", jdata, true);
        JObject.save("files/tmp/ds_not_pretty.json", jdata, false);

        JDataSet ds = JObject.read("files/tmp/ds_not_pretty.json", JDataSet.class);
        System.out.println("ds.toString() = " + JObject.toString(ds));
        System.out.println("ds.json = " + JObject.getJsonString(ds));
    }

    public static void main(String[] args) {
        testJDataSet();
    }


}
