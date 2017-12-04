


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import weka.classifiers.rules.mcar.datastructures.RCounter.RTAG;

public class PostProcess {

    public static void toCVS(String rarg, String indir, String outDir) throws IOException {
        toCVS(RunResult.read(new File(rarg), RunArg.class),
                new File(indir), new File(outDir));
    }

    public static void printMaxCorrect(File indir) throws IOException {
        File[] rfiles = indir.listFiles();

        Map<String, Set<RunResult>> imap = new TreeMap<String, Set<RunResult>>();

        for (File f : rfiles) {
            RunResult r = RunResult.read(f.getAbsolutePath(), RunResult.class);
            String datasetName = RunResult.datasetName(r.getDataset());
            Set<RunResult> tmpSet = imap.get(datasetName);
            if (tmpSet == null) tmpSet = new HashSet<RunResult>();
            tmpSet.add(r);
            imap.put(datasetName, tmpSet);

        }
        System.out.println(lineCVSHeader());
        for (String nm : imap.keySet()) {
//			System.out.println(" processing "+ nm);


            double maxCorrect = Double.MIN_VALUE;
            RunResult maxResult = null;


            for (RunResult rr : imap.get(nm)) {
                if (rr.getCorrect() > maxCorrect) {
                    maxCorrect = rr.getCorrect();
                    maxResult = rr;
                }
            }

            System.out.println(lineCVS(maxResult, nm));
        }

    }

    public static void toCVS(RunArg rarg, File indir, File outDir) throws IOException {
        if (indir == null || outDir == null) return;

        if (!indir.exists() || indir.isFile()) {
            System.err.println("isFile :" + indir);
            return;
        }

        if (!outDir.exists() || outDir.isFile())
            outDir.mkdirs();

        File[] rfiles = indir.listFiles();

        Map<String, Set<RunResult>> imap = new TreeMap<String, Set<RunResult>>();

        for (File f : rfiles) {
            RunResult r = RunResult.read(f.getAbsolutePath(), RunResult.class);
            String datasetName = RunResult.datasetName(r.getDataset());
            Set<RunResult> tmpSet = imap.get(datasetName);
            if (tmpSet == null) tmpSet = new HashSet<RunResult>();
            tmpSet.add(r);
            imap.put(datasetName, tmpSet);

        }

        for (String nm : imap.keySet()) {
            System.out.println(" processing " + nm);

            StringBuffer buff = new StringBuffer();
            buff.append(lineCVSHeader());

            TreeSet<RunResult> sortedResult = new TreeSet<RunResult>();

            for (RunResult rr : imap.get(nm)) {
                sortedResult.add(rr);
            }

            List<RunResult> sortedlist = new ArrayList<RunResult>();
            for (RunResult runResult : sortedResult) {
                sortedlist.add(runResult);
            }
//			List<RunResult> list=new ArrayList<RunResult>(imap.get(nm));

//			int indexCounter =0;

            while (sortedlist.size() > 0) {
                RunResult result = sortedlist.remove(0);
                result.setClassName(nm);

                String line = lineCVS(result, nm);
                buff.append("\n" + line);

            }
//			int indexConf=0;

            File dDir = new File(outDir.getAbsolutePath() + "/" + nm);
            dDir.mkdirs();
            FileWriter out = new FileWriter(dDir.getAbsolutePath() + "/" + nm + ".csv");
            out.write(buff.toString());
            out.close();

        }
    }

    public static String lineCVSOld(RunResult r, int index, int iconf, int isupp, String name) {
        StringBuffer sb = new StringBuffer();

        sb.append("name," + name +
                ",tag," + r.getTag() +
                ",id," + r.getId() +
                ",index," + index +
                ",idconf," + iconf +
                ",isupp," + isupp +
                ",conf," + r.getConfidence() +
                ",supp," + r.getSupport() +
                ",correct," + r.getCorrect() +
                ",ncorrect," + r.getNcorrect() +
                ",unclassified," + r.getUnclassified() +
                ",num_rules," + r.getNumRules() +
                ",same_conf," + r.getRcounter().get(RTAG.CONF.ordinal()) +
                ",same_supp," + r.getRcounter().get(RTAG.SUPP.ordinal()) +
                ",same_card," + r.getRcounter().get(RTAG.CARD.ordinal()) +
                ",same_col," + r.getRcounter().get(RTAG.COL.ordinal()) +
                ",same_row," + r.getRcounter().get(RTAG.ROW.ordinal())
        );
        return sb.toString();
    }

    public static String lineCVSHeader() {
        StringBuffer sb = new StringBuffer();

        sb.append("name," +
                "tag," +
                "id," +
                "idconf," +
                "isupp," +
                "conf," +
                "supp," +
                "correct," +
                "ncorrect," +
                "unclassified," +
                "num_rules," +
                "same_conf," +
                "same_supp," +
                "same_card," +
                "same_col," +
                "same_row," +
                "rank,"
        );
        return sb.toString();
    }

    public static String lineCVS(RunResult r, String name) {
        StringBuffer sb = new StringBuffer();

        sb.append(name +
                "," + r.getTag() +
                "," + r.getId() +
                "," + r.getIconf() +
                "," + r.getIsupp() +
                "," + r.getConfidence() +
                "," + r.getSupport() +
                "," + r.getCorrect() +
                "," + r.getNcorrect() +
                "," + r.getUnclassified() +
                "," + r.getNumRules() +
                "," + r.getRcounter().get(RTAG.CONF.ordinal()) +
                "," + r.getRcounter().get(RTAG.SUPP.ordinal()) +
                "," + r.getRcounter().get(RTAG.CARD.ordinal()) +
                "," + r.getRcounter().get(RTAG.COL.ordinal()) +
                "," + r.getRcounter().get(RTAG.ROW.ordinal()) +
                "," + r.getRankType()
        );
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {

        printMax();
        if (true) return;
//		RunArg rarg= RunResult.read("data/in.json", RunArg.class);
//		System.out.println(rarg);
//		

        String indir =
                "/home/hadoop/workspace/work.weka/weka.test/data/amazon/out";
        File[] files = new File(indir).listFiles();

        for (File file : files) {
            System.out.println(file.getName());
            toCVS("data/amazon/in.json", file.getAbsolutePath(), "data/groupPrediction");
        }
//		RunResult r= RunResult.read("data/contact-00001.txt", RunResult.class);


    }

    public static void printMax() {
        String dir =
                "/home/hadoop/workspace/work.weka/resources/exe/results/ok/1_supp_rcard_conf/2";
        try {
            printMaxCorrect(new File(dir));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
