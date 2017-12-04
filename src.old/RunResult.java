import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.lang.reflect.Field;

import weka.classifiers.Evaluation;
import weka.core.Instance;
import weka.core.Instances;

import org.codehaus.jackson.map.ObjectMapper;
import weka.classifiers.rules.McarModified;
import weka.classifiers.rules.mcar.datastructures.RANK_ID;

public class RunResult implements Comparable {
    static private DecimalFormat format = new DecimalFormat("00000");

    private static AtomicInteger ID = new AtomicInteger();

    public static enum CTAG {MCAR1, MCAR2, J48, JRIP, PART}

    ;

    public RunResult() {
        id = ID.incrementAndGet();
    }

    public static Map<String, Instances> mapInstances(File... infiles) {
        Map<String, Instances> result = new HashMap<String, Instances>();
        for (File file : infiles) {
            Instances data = null;
            try {
                data = new Instances(new FileReader(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (data != null) {
                result.put(file.getAbsolutePath(), data);
            }
        }
        return result;
    }

    public static List<RunResult> generateTest(RunArg rarg, File... files) {

        List<RunResult> result = new ArrayList<RunResult>();

        for (File f : files) {
            int index = 0;
            int indexSupp = 0;

            for (double supp = rarg.getSupp_start();
                 supp <= rarg.getSupp_stop();
                 supp += rarg.getSupp_delta()) {

                int indexConf = 0;
                for (double conf = rarg.getConf_start();
                     conf <= rarg.getConf_stop();
                     conf += rarg.getConf_delta()) {

                    RunResult r = new RunResult();
                    r.setId(index);
                    r.setIsupp(indexSupp);
                    r.setIconf(indexConf);

                    r.setTag(CTAG.MCAR2);
                    r.setClassName("McarModified");
                    r.setConfidence(conf);
                    r.setSupport(supp);
                    r.setIteration(rarg.getIteration());
                    r.setDataset(f.getAbsolutePath());
                    r.setOutFile(rarg.getOutdir() + "/" + f.getName().replace(".arff", "") +
                            "-" + format.format(r.getId()) + ".txt");
                    result.add(r);

                    index++;
                    indexConf++;
                }
                indexSupp++;
            }
        }
        return result;
    }

    public static List<RunResult> generatetFixedSupp(RunArg rarg, File... files) {

        List<RunResult> result = new ArrayList<RunResult>();

        for (File f : files) {
            int indexSupp = 0;

            for (double supp = rarg.getSupp_start();
                 supp <= rarg.getSupp_stop();
                 supp += rarg.getSupp_delta()) {

                RunResult r = new RunResult();
                r.setIsupp(indexSupp);

                r.setTag(rarg.getTag());
                r.setClassName("McarModified2");
                r.setSupport(supp);
                r.setIteration(rarg.getIteration());
                r.setDataset(f.getAbsolutePath());
                r.setOutDir(rarg.getOutdir());
                r.setRankType(rarg.getRank());

                //				r.setOutFile(rarg.getOutdir()+"/"+f.getName().replace(".arff", "")+
                //						"-"+ format.format(r.getId())+".txt");
                result.add(r);

                indexSupp++;
            }
        }
        return result;
    }

    public static String datasetName(String n) {
        return (new File(n)).getName();
    }

    private String dataset;

    private RANK_ID rankType = RANK_ID.CONF_SUPP_CARD;

    public RANK_ID getRankType() {
        return rankType;
    }

    public void setRankType(RANK_ID rankType) {
        this.rankType = rankType;
    }

    private String outDir;

    public String getOutDir() {
        return outDir;
    }

    public void setOutDir(String outDir) {
        this.outDir = outDir;
    }

    private int iteration;

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    private List<Double> rcounter;

    public List<Double> getRcounter() {
        return rcounter;
    }

    public void setRcounter(List<Double> rcounter) {
        this.rcounter = rcounter;
    }

    private CTAG tag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public double getCorrect() {
        return correct;
    }

    public void setCorrect(double correct) {
        this.correct = correct;
    }

    public double getNcorrect() {
        return ncorrect;
    }

    public void setNcorrect(double ncorrect) {
        this.ncorrect = ncorrect;
    }

    public double getUnclassified() {
        return unclassified;
    }

    public void setUnclassified(double unclassified) {
        this.unclassified = unclassified;
    }

    public int getNumRules() {
        return numRules;
    }

    public void setNumRules(int numRules) {
        this.numRules = numRules;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public double getSupport() {
        return support;
    }

    public void setSupport(double support) {
        this.support = support;
    }

    public static String getClassiferName(Object cls) {
        String ss = cls.getClass().getName();
        System.out.println(ss);
        String[] s = ss.split("\\.");
        System.out.println(Arrays.toString(s));
        return s[s.length - 1];
    }

    private int id, isupp, iconf;

    public int getIsupp() {
        return isupp;
    }

    public void setIsupp(int isupp) {
        this.isupp = isupp;
    }

    public int getIconf() {
        return iconf;
    }

    public void setIconf(int iconf) {
        this.iconf = iconf;
    }

    private String className;

    double correct, ncorrect, unclassified;

    int numRules;

    double confidence, support;
    private String outFile;

    public static void main(String[] args) throws Exception {
        //RunResult
        if (args.length == 0)
            args = new String[]{"data/amazon/in.json"};

        RunArg rarg = read(args[0], RunArg.class);
        System.out.println("rarg :" + rarg);

        batchRun(rarg);
    }

    public static void batchRun(RunArg rarg) {

        File inFile = new File(rarg.getIndir());
        if (!inFile.exists()) {
            System.err.println("in dir does not exists " + rarg.getIndir());
            return;
        }
        double[] confs = null;
        {
            List<Double> confList = new ArrayList<Double>();
            for (double conf = rarg.getConf_start();
                 conf <= rarg.getConf_stop();
                 conf += rarg.getConf_delta()) {
                confList.add(conf);

            }
            confs = new double[confList.size()];
            for (int i = 0; i < confs.length; i++) {
                confs[i] = confList.get(i);
            }
        }

        System.out.println(Arrays.toString(confs));
        File outDirFile = new File(rarg.getOutdir());
        if (!outDirFile.exists())
            outDirFile.mkdirs();

        File[] datafiles = inFile.listFiles();

        Map<String, Instances> datamap = mapInstances(datafiles);

        System.out.println(datamap.keySet());
        List<RunResult> results = generatetFixedSupp(rarg, datafiles);

        System.out.println(results.size());
        ExecutorService exec = Executors.newFixedThreadPool(rarg.getNum_exec());

        for (RunResult rr : results) {
            Instances inst = datamap.get(rr.getDataset());
            BatchTask task = new BatchTask(rr, inst, confs);
            exec.submit(task);
        }
        exec.shutdown();

        System.out.println("done");
    }

    public static void test1() throws Exception {
        McarModified mcar = new McarModified();
        mcar.setMinSupport(0.09);
        Instances data = new Instances(new FileReader("data/amazon/in/Cleved.arff"));

        List<McarModified> mcars = mcar.batchBuildClassifier(
                mcar, data, 0.5, 0.7, 1.0);

        for (McarModified mcr : mcars) {
            System.out.println("number of rules: " + mcr.getNumRules());
        }
        for (McarModified mcr : mcars) {
            System.out.println(mcr.toString());
            System.out.println("--------------------------------");
        }
    }

    public static void run(RunArg rarg) {

        File inFile = new File(rarg.getIndir());
        if (!inFile.exists()) {
            System.err.println("in dir does not exists " + rarg.getIndir());
            return;
        }

        File outDirFile = new File(rarg.getOutdir());
        if (!outDirFile.exists())
            outDirFile.mkdirs();

        File[] datafiles = inFile.listFiles();

        Map<String, Instances> datamap = mapInstances(datafiles);

        System.out.println(datamap.keySet());
        List<RunResult> results = generateTest(rarg, datafiles);

        System.out.println(results.size());
        ExecutorService exec = Executors.newFixedThreadPool(rarg.getNum_exec());

        for (RunResult rr : results) {
            Instances inst = datamap.get(rr.getDataset());
            OneTask task = new OneTask(rr, inst);
            exec.submit(task);
        }
        exec.shutdown();
    }

    public static void runTest(RunResult r, Instances data)
            throws Exception {
        //		RunResult r = RunResult.read(rFile, RunResult.class);
        data.setClassIndex(data.numAttributes() - 1);
        McarModified mcar = new McarModified();
        mcar.setAddDefaultRule(true);

        switch (r.getTag()) {
            case MCAR1:
                mcar.setOldMCAR(true);
                break;
            case MCAR2:
                mcar.setOldMCAR(false);
                break;
            default:
                mcar.setOldMCAR(true);
                break;
        }

        mcar.setMinSupport(r.getSupport());
        mcar.setConfidence(r.getConfidence());

        mcar.setValidateSignificance(true);
        mcar.buildClassifier(data);
        r.setNumRules(mcar.getNumRules());
        r.setRcounter(mcar.getR_counter().getList());
        mcar.setValidateSignificance(false);


        Evaluation eval = new Evaluation(data);
        //		 eval.crossValidateModel(tree, newData, 10, new Random(2));

        for (int i = 0; i < r.getIteration(); i++) {
            eval.crossValidateModel(mcar, data, 10, new Random(i));
            r.setCorrect(r.getCorrect() + eval.pctCorrect());
            r.setNcorrect(r.getNcorrect() + eval.pctIncorrect());
            r.setUnclassified(r.getUnclassified() + eval.pctUnclassified());
        }
        //take tha average
        r.setCorrect(r.getCorrect() / r.getIteration());
        r.setNcorrect(r.getNcorrect() / r.getIteration());
        r.setUnclassified(r.getUnclassified() / r.getIteration());
    }

    public static McarModified getMCAR(RunResult r) {
        McarModified mcar = new McarModified();
        mcar.setAddDefaultRule(true);

        switch (r.getTag()) {
            case MCAR1:
                mcar.setOldMCAR(true);
                break;
            case MCAR2:
                mcar.setOldMCAR(false);
                break;
            default:
                mcar.setOldMCAR(true);
                break;
        }

        mcar.setMinSupport(r.getSupport());
        mcar.setConfidence(r.getConfidence());
        return mcar;
    }

    public static void save(String filename, Object r) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(filename), r);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <E> E read(String filename, Class<E> cls) {
        return read(new File(filename), cls);
    }

    @SuppressWarnings("unchecked")
    public static <E> E read(File file, Class<E> cls) {
        E result = null;
        try {
            if (!file.exists()) {
                System.err.println("error file " + file.getAbsolutePath() + " not found");
                throw new IOException();
            }
            ObjectMapper mapper = new ObjectMapper();
            result = mapper.readValue(file, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public CTAG getTag() {
        return tag;
    }

    public void setTag(CTAG tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        String result = "";
        try {
            Class cls = Class.forName(this.getClass().getName());
            Field[] flds = cls.getDeclaredFields();
            for (Field fld : flds)
                result += "\n" + fld.getName() + ":{" + fld.get(this) + "}";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getOutFile() {
        return outFile;
    }

    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }

    @Override
    public int compareTo(Object o) {
        RunResult r = (RunResult) o;

        double diff = support - r.support;
        if (diff != 0) return (int) Math.signum(diff);

        diff = confidence - r.confidence;
        if (diff != 0) return (int) Math.signum(diff);

        return id - r.id;
    }

    public static List<RunResult> batchRunTest(RunResult r, Instances data, double... confs)
            throws Exception {
        List<RunResult> result = new ArrayList<RunResult>();
        for (int i = 0; i < confs.length; i++) {
            RunResult rr = new RunResult();
            rr.setClassName(r.getClassName());
            rr.setDataset(r.getDataset());
            rr.setIsupp(r.getIsupp());
            rr.setIconf(i);
            rr.setTag(r.getTag());
            rr.setIteration(r.getIteration());
            String ofile = r.getOutDir() + "/" + RunResult.datasetName(r.getDataset().
                    replace(".arff", "")) + "-" + format.format(rr.getId());
            rr.setOutFile(ofile);
            rr.setSupport(r.getSupport());
            rr.setConfidence(confs[i]);
            rr.setRankType(r.getRankType());

            result.add(rr);
        }
        data.setClassIndex(data.numAttributes() - 1);

        McarModified mcar = new McarModified();
        mcar.setAddDefaultRule(true);
        mcar.setRankType(r.getRankType());

        switch (r.getTag()) {
            case MCAR1:
                mcar.setOldMCAR(true);
                break;
            case MCAR2:
                mcar.setOldMCAR(false);
                break;//Very Important
            default:
                mcar.setOldMCAR(true);
                break;
        }

        mcar.setMinSupport(r.getSupport());

        mcar.setValidateSignificance(true);

        for (int i = 0; i < confs.length; i++) {
            mcar.setConfidence(confs[i]);
            mcar.buildClassifier(data);
            result.get(i).setNumRules(mcar.getNumRules());
            result.get(i).setRcounter(mcar.getR_counter().getList());

        }
        mcar.setValidateSignificance(false);

        for (int i = 0; i < r.getIteration(); i++) {
            List<BatchResult> br = batchCrossValidateModel(
                    mcar, data, 10, new Random(i), confs);

            for (int j = 0; j < br.size(); j++) {
                BatchResult bresult = br.get(j);
                RunResult rn = result.get(j);

                rn.setCorrect(rn.getCorrect() + bresult.correct);
                rn.setNcorrect(rn.getNcorrect() + bresult.ncorrect);
                rn.setUnclassified(rn.getUnclassified() + bresult.unclassified);
            }
        }

        for (RunResult rslt : result) {
            rslt.setCorrect(rslt.getCorrect() / rslt.getIteration());
            rslt.setNcorrect(rslt.getNcorrect() / rslt.getIteration());
            rslt.setUnclassified(rslt.getUnclassified() / rslt.getIteration());
        }
        return result;
    }

    public static List<BatchResult> batchCrossValidateModel(McarModified classifier,
                                                            Instances data, int numFolds, Random random, double... confs)
            throws Exception {

        List<BatchResult> result = new ArrayList<BatchResult>(confs.length);
        // Make a copy of the data we can reorder
        data = new Instances(data);
        data.randomize(random);
        if (data.classAttribute().isNominal()) {
            data.stratify(numFolds);
        }

        // Do the folds
        double totalCount = 0.0;
        double[] corrects = new double[confs.length];

//		List<BatchResult> brslt=new ArrayList<BatchResult>(confs.length);
//		for (int i = 0; i < confs.length; i++) {
//			brslt.add(new BatchResult(0, 0, 0));
//		}

        for (int i = 0; i < numFolds; i++) {
            Instances train = data.trainCV(numFolds, i, random);
            List<McarModified> mcarList = McarModified
                    .batchBuildClassifier(classifier, train, confs);

            Instances test = data.testCV(numFolds, i);

            for (int line = 0; line < test.numInstances(); line++) {
                Instance inst = test.instance(line);
                totalCount++;

                for (int j = 0; j < confs.length; j++) {
                    double correct = mcarList.get(j).classifyInstanceSingle(inst);
                    corrects[j] += correct;
                }
            }

        }

        for (double crct : corrects) {
            double pctCorrect = crct / totalCount * 100.0;
            double pctNCorrect = 100.0 - pctCorrect;
            BatchResult br = new BatchResult(pctCorrect, pctNCorrect, 0.0);
            result.add(br);
        }
        return result;
    }
}


class OneTask implements Runnable {
    public OneTask(RunResult rr, Instances data) {
        super();
        this.rr = rr;
        this.data = data;
    }

    public final RunResult rr;
    public final Instances data;

    @Override
    public void run() {
        System.out.println("start task " + rr.getDataset() +
                " id :" + rr.getId() + ", supp:" + rr.getSupport() + ", conf:" + rr.getConfidence());
        try {
            RunResult.runTest(rr, data);
            RunResult.save(rr.getOutFile(), rr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class BatchTask implements Runnable {
    public BatchTask(RunResult rr, Instances data, double... confs) {
        super();

        this.rr = rr;
        this.data = data;
        this.confs = confs;
    }

    public final double[] confs;
    public final RunResult rr;
    public final Instances data;

    @Override
    public void run() {
        System.out.println(rr.getDataset() + " id :" + rr.getId() + ",supp:" + rr.getSupport());
        try {
            List<RunResult> result = RunResult.batchRunTest(rr, data, confs);
            for (RunResult runResult : result) {
                RunResult.save(runResult.getOutFile(), runResult);
            }
            System.out.println("save id: " + rr.getId());
//			RunResult.save(rr.getOutFile(), rr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class BatchResult {
    final double correct, ncorrect, unclassified;
    public double number, c, nc;

    public BatchResult(double correct, double ncorrect, double unclassified) {
        super();
        this.correct = correct;
        this.ncorrect = ncorrect;
        this.unclassified = unclassified;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("{");
        sb.append("correct:" + correct + ",ncorrect:" + ncorrect
                + ",unclassified:" + unclassified);
        return sb.toString();
    }
}