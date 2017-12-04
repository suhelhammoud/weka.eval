




import java.lang.reflect.Field;

import weka.classifiers.rules.mcar.datastructures.RANK_ID;


public class RunArg{
	private double conf_start, conf_stop, conf_delta;
	private double supp_start, supp_stop, supp_delta;
	private int iteration,num_exec;
	private RunResult.CTAG tag;
	
	private RANK_ID rank=RANK_ID.CONF_SUPP_CARD;
	
	public RANK_ID getRank() {
		return rank;
	}
	public void setRank(RANK_ID rank) {
		this.rank = rank;
	}
	public RunResult.CTAG getTag() {
		return tag;
	}
	public void setTag(RunResult.CTAG tag) {
		this.tag = tag;
	}
	public int getNum_exec() {
		return num_exec;
	}
	public void setNum_exec(int num_exec) {
		this.num_exec = num_exec;
	}
	public int getIteration() {
		return iteration;
	}
	public void setIteration(int iteration) {
		this.iteration = iteration;
	}
	private String indir,outdir;
	public double getConf_start() {
		return conf_start;
	}
	public void setConf_start(double conf_start) {
		this.conf_start = conf_start;
	}
	public double getConf_stop() {
		return conf_stop;
	}
	public void setConf_stop(double conf_stop) {
		this.conf_stop = conf_stop;
	}
	public double getConf_delta() {
		return conf_delta;
	}
	public void setConf_delta(double conf_delta) {
		this.conf_delta = conf_delta;
	}
	public double getSupp_start() {
		return supp_start;
	}
	public void setSupp_start(double supp_start) {
		this.supp_start = supp_start;
	}
	public double getSupp_stop() {
		return supp_stop;
	}
	public void setSupp_stop(double supp_stop) {
		this.supp_stop = supp_stop;
	}
	public double getSupp_delta() {
		return supp_delta;
	}
	public void setSupp_delta(double supp_delta) {
		this.supp_delta = supp_delta;
	}
	public String getIndir() {
		return indir;
	}
	public void setIndir(String indir) {
		this.indir = indir;
	}
	public String getOutdir() {
		return outdir;
	}
	public void setOutdir(String outdir) {
		this.outdir = outdir;
	}
	
	@Override
	public String toString() {
		String result="";
		try {
			Class cls=Class.forName(this.getClass().getName());
			Field[] flds= cls.getDeclaredFields();
			for (Field fld : flds) 
				result+= "\n"+fld.getName()+":{"+fld.get(this)+"}";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void main(String[] args) {
		RunArg rarg=new RunArg();
		rarg.setIndir("data/amazon/in");
		rarg.setOutdir("data/amazon/out/1");
		rarg.setIteration(10);
		
		RunResult.save("data/amazon/rarg-01.json", rarg);
	}
}
