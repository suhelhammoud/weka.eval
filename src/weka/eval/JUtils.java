package weka.eval;

import weka.core.Instances;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class JUtils {
    static Logger log = Logger.getLogger(JUtils.class.getName());

    public static Instances instancesOf(String fileName) {
        Instances result = null;
        try {
            return new Instances(new FileReader(fileName));
        } catch (IOException e) {
            log.log(Level.SEVERE, "Exception in loading {} file", fileName);
            e.printStackTrace();
        }
        return result;
    }

    public static double median(double[] values, double ratio) {
        double[] a = Arrays.copyOf(values, values.length);
        Arrays.sort(a);
        double part = a.length * ratio;
        int floor = (int)Math.floor(part);
        return part - floor < 0.00001 ?
                (a[floor] + a[floor - 1]) / 2 :
                a[floor];
    }

    public static int[] aboveMedianIndexes(double[] values) {
        double median = median(values, .75);
        return IntStream.range(0, values.length)
                .filter(i -> values[i] >= median)
                .toArray();
    }

    public static void main(String[] args) {
        double[] a = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        System.out.println(Arrays.toString(aboveMedianIndexes(a)));
    }

}
