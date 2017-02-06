package com.millenium.speaker;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Init {

	private static Logger log = LoggerFactory.getLogger(Init.class);
	// 16kbytes por segundo
	// public final static boolean TESTMODE = true;
	public final static double TOLERANCE = 0.8;
	public final static long TRAIN_FILE_SIZE = 80000;// 12 digitos
	public final static long TEST_FILE_SIZE = 80000;// 8 digitos

	// public final static long MIN_IMP_FILE_SIZE = 400 * 1024;
	// public final static int NRECORDS = 2;
	// public final static int NTRAIN = 1;
	// public final static boolean VERIFYONLY = false;

	public static void main(String[] args) throws IOException, InterruptedException {

		log.info("Inicia ejecucion");
		TestsFolders dataPath = new TestsFolders(Conf.get(Conf.KEY_LISTS_PATH), Conf.get(Conf.KEY_TEST_PATH), Conf.get(Conf.KEY_FILES_PATH), Conf.get(Conf.KEY_RECORDINGS_PATH));
		if (!Conf.getVerificationMode()) {
			dataPath.CreateTest();
		}
		log.info("Finaliza la creación de carpetas ");
		ImpostorProcessing impostores = new ImpostorProcessing(dataPath);
		RegistroProcessing target = new RegistroProcessing(dataPath);
		CreateLists listas = new CreateLists(dataPath, impostores, target);
		Map<String, List<String>> entradas = listas.createTargetImplists();
		if (!Conf.getVerificationMode()) {
			FeatureExtraction execpath = new FeatureExtraction(dataPath.getTestName());
			execpath.features();
		}
		Verification resultspath = new Verification(dataPath.getResultsFile(), entradas);
		resultspath.verificacion();





		// for (float threshold = Conf.getUmbralmin(); threshold <=
		// Conf.getUmbralmax(); threshold+=0.5f) {
		//
		// //Verification resultwccn = new
		// Verification(dataPath.getWccnCosResultsFile(), entradas, threshold);
		// //Verification resultsjfa = new
		// Verification(dataPath.getJFAScoresFile(), entradas, threshold);
		// //Verification resultspldaln = new Verification(dataPath.getPLDAln(),
		// entradas, threshold);
		// Verification resultspath = new
		// Verification(dataPath.getResultsFile(), entradas, threshold);
		// Verification resultsztpath = new
		// Verification(dataPath.getTargetsegztnorm(), entradas, threshold);
		// Verification resultszpath = new
		// Verification(dataPath.getTargetsegznorm(), entradas, threshold);
		// Verification resultstpath = new
		// Verification(dataPath.getTargetsegtnorm(), entradas, threshold);
		// //log.info("Verificación JFA");
		// //resultsjfa.verificacion();
		// //log.info("Verificacion i-vector WCCN scoring");
		// //resultspldaln.verificacion();
		// log.info("Verificacion sin compensacion");
		// Rates rates_nc = resultspath.verificacion();
		// Results.getInstance().addRateNC(rates_nc);
		// log.info("Verificacion con zt norm");
		// Rates rates_zt = resultsztpath.verificacion();
		// Results.getInstance().addRateZT(rates_zt);
		// log.info("Verificacion con z norm");
		// Rates rates_z = resultszpath.verificacion();
		// Results.getInstance().addRateZ(rates_z);
		// log.info("Verificacion con T norm");
		// Rates rates_t = resultstpath.verificacion();
		// Results.getInstance().addRateT(rates_t);
		// log.info("Finaliza ejecucion");
		// String umbral = Float.toString(threshold);
		//
		// log.info("umbral  usado: "+umbral);
		// }
		//
		// /*
		// "usa": {
		// label: "USA",
		// data: [[1988, 483994], [1989, 479060], [1990, 457648], [1991,
		// 401949], [1992, 424705], [1993, 402375], [1994, 377867], [1995,
		// 357382], [1996, 337946], [1997, 336185], [1998, 328611], [1999,
		// 329421], [2000, 342172], [2001, 344932], [2002, 387303], [2003,
		// 440813], [2004, 480451], [2005, 504638], [2006, 528692]]
		// }
		// */
		// String dataTemplate = "\"%s\": { label: \"%s\", data: %s}\n";
		// List<String> datas = new ArrayList<String>();
		// for (Entry<String, List<Rates>> entry:
		// Results.getInstance().getRates().entrySet()) {
		// datas.add(String.format(dataTemplate, entry.getKey(), entry.getKey(),
		// entry.getValue().toString() ));
		// }
		// String template = FileUtils.readFileToString(new
		// File(Conf.getTemplatePath()));
		// FileUtils.writeStringToFile(new File(Conf.getReportPath()),
		// StringUtils.replaceOnce(template, "[[datasets]]",
		// StringUtils.join(datas,",")));
		//
	}

}
