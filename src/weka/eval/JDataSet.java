package weka.eval;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

import java.io.FileReader;
import java.io.IOException;

import static weka.eval.JUtils.enum2Stream;

public class JDataSet implements JObject {
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
    @JsonProperty
    int numClasses;
    @JsonProperty
    int missingValues;
    @JsonProperty
    int conitnousAttributes;


    public static int getNumAttributes(Instances data, AttributeType atp) {
        //TODO check order in both: AttributeType and Attribute.
        return (int) enum2Stream(data.enumerateAttributes())
                .filter(a -> a.type() == atp.ordinal())
                .count();
    }

    public static int getMissingValues(Instances data) {
        return (int) enum2Stream(data.enumerateInstances())
                .filter(Instance::hasMissingValue)
                .count();
    }


    /**
     * Factory method to get JDataSet Object from Weka Instances object
     *
     * @param data
     * @return new JDataSet instance representing Weka.Instances object
     */
    public static JDataSet ofInstances(Instances data) {
        JDataSet result = new JDataSet();
        result.relationName = data.relationName();
        result.numInstances = data.numInstances();
        result.numAttributes = data.numAttributes();
        result.attributes = enum2Stream(data.enumerateAttributes())
                .map(Attribute::name)
                .toArray(String[]::new);
        result.attributesTypes = enum2Stream(data.enumerateAttributes())
                .mapToInt(Attribute::type)
                .mapToObj(AttributeType::of)
                .toArray(AttributeType[]::new);
        result.attributesItems = enum2Stream(data.enumerateAttributes())
                .mapToInt(Attribute::numValues)
                .toArray();
        result.classIndex = data.classIndex();
        result.numClasses = data.classIndex() > 0 ?
                data.classAttribute().numValues() :
                0;
        result.missingValues = getMissingValues(data);
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
        JDataSet jdata = ofInstances(data);
        JObject.save("files/tmp/ds_pretty.json", jdata, true);
        JObject.save("files/tmp/ds_not_pretty.json", jdata, false);

        JDataSet ds = JObject.read("files/tmp/ds_not_pretty.json", JDataSet.class);
        System.out.println("ds.getString() = " + JObject.getString(ds));
        System.out.println("ds.json = " + JObject.getJsonString(ds));
    }

    public static void main(String[] args) {
        testJDataSet();
    }
}
