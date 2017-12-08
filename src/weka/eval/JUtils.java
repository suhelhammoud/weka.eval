package weka.eval;

import weka.core.Instances;
import weka.core.WekaPackageClassLoaderManager;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JUtils {
    static Logger log = Logger.getLogger(JUtils.class.getName());

    /**
     * Class loader using WekaPackageClassLoaderManager
     *
     * @param className to be loaded (Classifier, AttributeEval, etc)
     * @return new Instance of "className", should be casted outside this method
     */
    public static Optional<?> forName(String className) {
        try {
            Class<?> cls = WekaPackageClassLoaderManager.forName(className);
            return Optional.ofNullable(cls.newInstance());
        } catch (ClassNotFoundException e) {
            log.log(Level.SEVERE, "ClassNotFound", e);
        } catch (IllegalAccessException e) {
            log.log(Level.SEVERE, "ClassNotFound", e);
        } catch (InstantiationException e) {
            log.log(Level.SEVERE, "ClassNotFound", e);
        }
        log.log(Level.SEVERE, "No Class ofInstances name {} where found", className);
        return Optional.empty();
    }

    /**
     * Used to tranform java enumeration into java8 stream,
     * candidate usage in Instances methods :enumerateInstatnces, enumerateAttributes
     * benefit to do
     *
     * @param e   enumerateion
     * @param <T> class type
     * @return stream of T type
     */
    public static <T> Stream<T> enum2Stream(Enumeration<T> e) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        new Iterator<T>() {
                            public T next() {
                                return e.nextElement();
                            }

                            public boolean hasNext() {
                                return e.hasMoreElements();
                            }
                        },
                        Spliterator.ORDERED), false); //TODO: parallel flag to true later!
    }


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
        int floor = (int) Math.floor(part);
        return part - floor < 0.00001 ?
                (a[floor] + a[floor - 1]) / 2 :
                a[floor];
    }

    public static int[] aboveMedianIndexes(double[] values, double ratio) {
        double median = median(values, ratio);
        return IntStream.range(0, values.length)
                .filter(i -> values[i] >= median)
                .toArray();
    }

    public static void main(String[] args) {
        double[] a = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        System.out.println(
                Arrays.toString(aboveMedianIndexes(a, .75)));
    }

}
