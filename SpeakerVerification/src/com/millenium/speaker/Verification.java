package com.millenium.speaker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.stat.StatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class Verification {
	private static Logger log = LoggerFactory.getLogger(Verification.class);
	private String resultspath;
//	private float threshold;
	private Map<String, List<Scores>> map = new HashMap<String, List<Scores>>();
	private Map<String, List<String>> entradas;

	public Verification(String resultspath, Map<String, List<String>> entradas) {
		super();
		this.resultspath = resultspath;
		this.entradas = entradas;
//		this.threshold = threshold;
	}

	public Rates verificacion() throws IOException {
		float falseacc = 0f, falserej = 0f, successrate = 0f;
		Rates rates = new Rates(falseacc, falserej, successrate);
		List<Scores> recordtrue = new ArrayList<Scores>();
		List<Scores> recordfalse = new ArrayList<Scores>();
		List<Scores> results = new ArrayList<Scores>();
		List<String> line = FileUtils.readLines(new File(resultspath));
		log.info("Leyendo archivo de resultados");
		DbHelper.clearScores();
		for (String string : line) {

			String[] cols = StringUtils.split(string, " ");
			String cedula = cols[1];
			if (!map.containsKey(cedula)) {
				map.put(cedula, new ArrayList<Scores>());

			}
			float score = Float.parseFloat(cols[4]);
			boolean test = Boolean.parseBoolean(cols[2]);
			Scores reg = new Scores(cols[0], cols[1], test, cols[3], score);
			DbHelper.saveScore(cedula,cols[3],score );
			map.get(cedula).add(reg);
			results.add(reg);

		}

		for(String cedula : map.keySet()) {
          double score=DbHelper.getFirstScore(cedula);
          DbHelper.saveUmbral(cedula, score*Init.TOLERANCE);
		}

//		File temp = new File(resultspath);
//
//		if (temp.getName().equals("target-seg_gmm.res")) {
//			for (Entry<String, List<Scores>> entry : map.entrySet()) {
//				StatSpeaker(entry.getKey()); //Speaker Dependent Normalization
//
//			}
//		}


		List<Scores> registros = new ArrayList<Scores>();

		for (Entry<String, List<String>> entry : entradas.entrySet()) {
			for (int i = 1; i <= Conf.getTestRecords(); i++) {
				registros.add(new Scores("A", entry.getKey(), true, entry.getValue().get(i), 0f));

			}
		}

		// en entradas estan las dos llamadas y el key es la cedula. la 0 es la
		// de training y el resto es el test
		Map<String, List<Scores>> diffFA = new HashMap<String, List<Scores>>();
		Map<String, List<Scores>> diffFR = new HashMap<String, List<Scores>>();
		float diferenciaA = 0f, diferenciaR = 0f;

		for (Entry<String, List<Scores>> entry : map.entrySet()) {

			List<Scores> testtrue = new ArrayList<Scores>(Collections2.filter(entry.getValue(), new Predicate<Scores>() {
				@Override
				public boolean apply(Scores arg0) {
					return arg0.getRawscore() >=DbHelper.getUmbral(arg0.getCedula());
				}
			}));

			if (registros != null) {
				List<Scores> newlist = new ArrayList<Scores>(testtrue);
				newlist.removeAll(registros);
				diferenciaA += newlist.size();
				if (newlist.size() > 0) {
					diffFA.put(entry.getKey(), newlist);
				}
			}

			List<Scores> testfalse = new ArrayList<Scores>(Collections2.filter(entry.getValue(), new Predicate<Scores>() {
				@Override
				public boolean apply(Scores arg0) {
					return arg0.getRawscore() < DbHelper.getUmbral(arg0.getCedula());

				}
			}));
			if (registros != null) {
				List<Scores> newlist = new ArrayList<Scores>(testfalse);
				newlist.retainAll(registros);
				diferenciaR += newlist.size();
				if (newlist.size() > 0) {
					diffFR.put(entry.getKey(), new ArrayList<Scores>());
					diffFR.get(entry.getKey()).addAll(newlist);

				}
			}

			recordtrue.addAll(testtrue);
			recordfalse.addAll(testfalse);
		}// muere el for

		falseacc = 100 * (diferenciaA / (Integer.valueOf(results.size()).floatValue() - Integer.valueOf(registros.size()).floatValue()));
		falserej = 100 * (diferenciaR / Integer.valueOf(registros.size()).floatValue());
		successrate = 100 * (1 - (diferenciaA + diferenciaR) / (Integer.valueOf(results.size()).floatValue()));
		float TOTAL_ERROR = falseacc + falserej;
		float cmiss = 10f;
		float cfa = 1f;
		float ptarget = 0.01f;// Integer.valueOf(registros.size()).floatValue()/Integer.valueOf(results.size()).floatValue();
		float cdet = cmiss * (falserej/100) * ptarget + cfa * (falseacc/100) * (1 - ptarget);
		log.info("Error Total: " + TOTAL_ERROR + "%");
		log.info("Cost detection Error: " + cdet);
		log.info("False Acceptance rate:" + falseacc + "%" + ",False Rejection rate:" + falserej + "%, true list size: " + recordtrue.size() + " false list size: " + recordfalse.size());
		log.info("diferenciaA" + ", " + diferenciaA);
		log.info("diferenciaR" + ", " + diferenciaR);
		log.info("registros" + ", " + registros.size());
		log.info("resultados" + ", " + results.size());
		log.info("Success Rate: " + successrate + "%");
		log.info("recordtrue.size(): " + recordtrue.size() + ", " + Integer.valueOf(recordtrue.size()).floatValue());
		log.info("recordfalse.size(): " + recordfalse.size() + ", " + Integer.valueOf(recordfalse.size()).floatValue());
		rates = new Rates(falseacc, falserej, successrate);

		return rates;

	}

	private void StatSpeaker(String cedula) {
		List<Scores> scores = map.get(cedula);
		double[] x = new double[scores.size()];
		for (int i = 0; i < scores.size(); i++) {
			x[i] = Double.valueOf(scores.get(i).getRawscore());
		}
		double[] y = StatUtils.normalize(x);
		double miu = StatUtils.mean(x);
		//double sigma = StatUtils.variance(x);
		//double outdata = 100;
		//while (outdata > 2 * sigma) {
			double[] d = new double[x.length];
			for (int i = 0; i < x.length; i++) {
				d[i] = Math.abs(x[i] + miu);// L1 norm
				scores.get(i).setRawscore((float)y[i]);
			}

		//	outdata = StatUtils.max(d);
			//x = ArrayUtils.remove(x, ArrayUtils.indexOf(d, outdata));
			//miu = StatUtils.mean(x);
			// miu = (((x.length + 1) * miu) - outdata) / x.length;// biased
			// mean
			// value
		//}
		//sigma = StatUtils.variance(x); // new biased variance
		//float newThreshold = (float)(miu+35*sigma);
	float newThreshold = (float) StatUtils.max(x)+ 0.02f;
		DbHelper.saveUmbral(cedula, newThreshold);
		//System.out.println(cedula + "," + newThreshold);

	}

}