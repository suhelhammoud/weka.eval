package weka.eval;

/**
 * List All general constants to be used in the application
 */

enum AttributeType {
    NUMERIC, NOMINAL, STRING, DATE, RELATIONAL;

    private static AttributeType[] values = values();

    public static AttributeType of(int i) {
        if (i < 0 || i > values.length - 1) {
            throw new ArrayIndexOutOfBoundsException("i out of length of values");
        }
        return values[i];
    }
}

enum ClassifierTag {
    JRIP, NP;
}

enum FilterTag {
    PAS, EN, MCHI;
}

enum AttributeSelectorTag {
    IG, CHI, VA, VACFS, CFS, L2;
}
