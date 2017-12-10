package weka.eval;

;

/**
 * List All general constants to be used in the application
 */

enum TAttributeType {
    NUMERIC, NOMINAL, STRING, DATE, RELATIONAL;

    private static TAttributeType[] values = values();

    public static TAttributeType of(int i) {
        if (i < 0 || i > values.length - 1) {
            throw new ArrayIndexOutOfBoundsException("i out ofInstances length ofInstances values");
        }
        return values[i];
    }
}

enum TClassifier {
    JRIP,
    NaiveBayes;

    public final String className() {
        return TClassName.of(this);
    }
}

enum TFilter {
    PAS, EN, MCHI;
}

enum TAttributeSearch {
    Ranker, BestFirst;
}

enum TAttributeEvaluator {
    IG, CHI, VA, VACFS, CFS, L2;

    public final String className() {
        return TClassName.of(this);
    }
}

enum TThresholdType {
    LEVEL, MEDIAN, EAS
}

enum TClassName {
    AttributeSelection("weka.filters.supervised.attribute.AttributeSelection"),
    IG("weka.attributeSelection.InfoGainAttributeEval"),
    CHI("weka.attributeSelection.ChiSquaredAttributeEval"),
    VA("weka.attributeSelection.Va"),
    VACFS("weka.attributeSelection.VaCfsEval"),
    CFS("weka.attributeSelection.CfsSubsetEval"),
    L2("weka.attributeSelection.L2AttributeEval"),
    JRIP("weka.classifiers.rules.JRip"),
    NaiveBayes("weka.classifiers.bayes.NaiveBayes"),
    Ranker("weka.attributeSelection.Ranker"),
    BestFirst("weka.attributeSelection.BestFirst");

    final public String className;

    TClassName(String cn) {
        className = cn;
    }

    public static String of(Enum enm) {
        return valueOf(enm.name()).className;
    }
}
