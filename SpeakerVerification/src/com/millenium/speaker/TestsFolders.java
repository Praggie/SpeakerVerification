package com.millenium.speaker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestsFolders {

	private static Logger log = LoggerFactory.getLogger(TestsFolders.class);
	public static final List<String> dirs = new ArrayList<String>();

	private String testName;
	private String originalPath;
	private String originalAudio;
	private String listPath;

	static {
		dirs.add("bin");
		dirs.add("cfg");
		dirs.add("data/prm");
		dirs.add("data/lbl");
		dirs.add("gmm");
		dirs.add("log");
		dirs.add("lst");
		dirs.add("mat");
		dirs.add("ndx");
		dirs.add("res");
		dirs.add("svec");
		dirs.add("iv/lengthNorm");
		dirs.add("iv/raw");
	}

	public TestsFolders(String listPath, String testName, String originalPath, String originalAudio) {
		super();
		this.testName = testName;
		this.originalPath = originalPath;
		this.originalAudio = originalAudio;
		this.listPath = listPath;
		log.info("dirTest: " + testName);
		log.info("originalPath: " + originalPath);
		log.info("originalAudio: " + originalAudio);
		log.info("listPath: " + listPath);
	}

	public void CreateTest() throws IOException {
		File dirTest = new File(testName);
		if (dirTest.exists()) {
			if (!Conf.getTestMode()) {
				FileUtils.deleteQuietly(dirTest);
			} else {
				// throw new IOException("This file already exists");
			}
		}
		dirTest.mkdirs();
		for (String dir : dirs) {
			FileUtils.forceMkdir(new File(FilenameUtils.concat(testName, dir)));
		}

		FileUtils.copyDirectory(new File(FilenameUtils.concat(originalPath, "cfg")), new File(getCfgDest()), null);
		FileUtils.copyDirectory(new File(FilenameUtils.concat(originalPath, "bin")), new File(getBinDest()), null);
		FileUtils.copyDirectory(new File(FilenameUtils.concat(originalPath, "scripts")), new File(getTestName()), null);
	}

	public String getTestName() {
		return testName;
	}

	public String getOriginalPath() {
		return originalPath;
	}

	public String getListPath() {
		return listPath;
	}

	public String getTargetlist() {
		return FilenameUtils.concat(getListPath(), Conf.get(Conf.KEY_TARGET_FILENAME));
	}

	public String getUbmlist() {
		return FilenameUtils.concat(getListPath(), Conf.get(Conf.KEY_UBM_FILENAME));
	}

	public String getImpostoreslist() {
		return FilenameUtils.concat(getListPath(), Conf.get(Conf.KEY_IMPOSTOR_FILENAME));
	}

	public String getCfgDest() {
		return FilenameUtils.concat(testName, "cfg");
	}

	public String getBinDest() {
		return FilenameUtils.concat(testName, "bin");
	}

	public String getLstDest() {
		return FilenameUtils.concat(testName, "lst");
	}

	public String getIvDest() {
		return FilenameUtils.concat(testName, "iv");
	}

	public String getDataDest() {
		return FilenameUtils.concat(testName, "data");
	}

	public String getNdxDest() {
		return FilenameUtils.concat(testName, "ndx");
	}

	public String getResDest() {
		return FilenameUtils.concat(testName, "res");
	}

	public String getOriginalAudio() {
		return originalAudio;
	}

	public String getOriginalAudio(String file) {
		return FilenameUtils.concat(originalAudio, file);
	}

	public String getOutputFile() {
		return FilenameUtils.concat(getDataDest(), "data.lst");
	}

	public String getAllLstFile(){
		return FilenameUtils.concat(getLstDest(), "all.lst");
	}

	public String getPLDAln(){
		return FilenameUtils.concat(getResDest(), "scores_PLDA_lengthnorm.txt");
	}

	public String getTrainmodelFile() {
		return FilenameUtils.concat(getNdxDest(), "trainModel.ndx");
	}

	public String getTrainimpFile() {
		return FilenameUtils.concat(getNdxDest(), "trainImp.ndx");
	}

	public String getUBMFile() {
		return FilenameUtils.concat(getLstDest(), "UBM.lst");
	}

	public String getImpostorFile() {
		return FilenameUtils.concat(getLstDest(), "impostor.lst");
	}

	public String getComputetestFile() {
		return FilenameUtils.concat(getNdxDest(), "computetest_gmm_target-seg.ndx");
	}

	public String getComputeimpsegFile() {
		return FilenameUtils.concat(getNdxDest(), "computetest_gmm_imp-seg.ndx");
	}

	public String getPldaFile() {
		return FilenameUtils.concat(getNdxDest(), "Plda.ndx");
	}

	public String getIvTestFile() {
		return FilenameUtils.concat(getNdxDest(), "ivTest_plda_target-seg.ndx");

	}

	public String getIvNormFile() {
		return FilenameUtils.concat(getNdxDest(), "ivNorm.ndx");
	}

	public String getIvExtractorFile() {
		return FilenameUtils.concat(getNdxDest(), "ivExtractor.ndx");
	}

	public String getTotalvariability() {
		return FilenameUtils.concat(getNdxDest(), "totalvariability.ndx");
	}

	public String getComputeimpimpFile() {
		return FilenameUtils.concat(getNdxDest(), "computetest_gmm_imp-imp.ndx");
	}

	public String getComputetargetimpFile() {
		return FilenameUtils.concat(getNdxDest(), "computetest_gmm_target-imp.ndx");
	}

	public String getResultsFile() {
		return FilenameUtils.concat(getResDest(), "target-seg_gmm.res");
	}

	public String getJFAScoresFile(){
		return FilenameUtils.concat(getResDest(), "scores_dotProduct.res");
	}

	public String getWccnCosResultsFile(){
		return FilenameUtils.concat(getResDest(), "scores_WCCN_Cosine.txt");
	}
	public String getPldaSphNormResultsFile(){
		return FilenameUtils.concat(getResDest(), "scores_SphNorm_PLDA.txt");
	}

	public String getImpsegFile() {
		return FilenameUtils.concat(getNdxDest(), "imp_seg.ndx");
	}

	public String getImpimpFile() {
		return FilenameUtils.concat(getNdxDest(), "imp_imp.ndx");
	}

	public String getTargetimpFile() {
		return FilenameUtils.concat(getNdxDest(), "target_imp.ndx");
	}

	public String getTargetsegztnorm() {
		return FilenameUtils.concat(getResDest(), "target-seg_gmm.res.ztnorm");
	}

	public String getTargetsegznorm() {
		return FilenameUtils.concat(getResDest(), "target-seg_gmm.res.znorm");
	}

	public String getTargetsegtnorm() {
		return FilenameUtils.concat(getResDest(), "target-seg_gmm.res.tnorm");
	}
}
