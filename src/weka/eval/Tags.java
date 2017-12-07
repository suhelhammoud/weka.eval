package weka.eval;

;

/**
 * List All general constants to be used in the application
 */

enum AttributeType {
    NUMERIC, NOMINAL, STRING, DATE, RELATIONAL;

    private static AttributeType[] values = values();

    public static AttributeType of(int i) {
        if (i < 0 || i > values.length - 1) {
            throw new ArrayIndexOutOfBoundsException("i out ofInstances length ofInstances values");
        }
        return values[i];
    }
}

enum ClassifierTag {
    JRIP("weka.classifiers.rules.JRip"),
    NaiveBayes("weka.classifiers.bayes.NaiveBayes");

    public final String className;

    ClassifierTag(String className) {
        this.className = className;
    }
}

enum FilterTag {
    PAS, EN, MCHI;
}

enum AttributeSelectorTag {
    IG("weka.attributeSelection.InfoGainAttributeEval"),
    CHI("weka.attributeSelection.ChiSquaredAttributeEval"),
    VA("weka.attributeSelection.Va"),
    VACFS("weka.attributeSelection.VaCfsEval"),
    CFS("weka.attributeSelection.CfsSubsetEval"),
    L2("weka.attributeSelection.L2AttributeEval");

    public final String className;

    AttributeSelectorTag(String className) {
        this.className = className;
    }
}

enum AttributeThresholdType {
    LEVEL, MEDIAN, EAS
}
