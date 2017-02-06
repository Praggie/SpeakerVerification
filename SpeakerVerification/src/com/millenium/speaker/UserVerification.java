package com.millenium.speaker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserVerification {
	private static Logger log = LoggerFactory.getLogger(UserVerification.class);
	private String ubmPath;
	private String audioPath;
	private String[] input;

	public UserVerification(String ubmPath, String audioPath, String[] input) {
		super();
		this.ubmPath = ubmPath;
		this.audioPath = audioPath;
		this.input = input;
	}

	public void newAudioVerification() throws IOException, InterruptedException {
		String origen = String.format("%s.wav", FilenameUtils.concat(audioPath, input[1]));
		String destino = String.format("%s.wav", FilenameUtils.concat(ubmPath, input[1]));
		FileUtils.copyFile(new File(origen), new File(destino));
		List<String> cmd = new ArrayList<String>();
		// audio processing
		new File(FilenameUtils.concat(ubmPath, "preprocessing.sh")).setExecutable(true);
		SystemCommandExecutor exec = new SystemCommandExecutor(cmd);
		cmd.add(FilenameUtils.concat(ubmPath, "./preprocessing.sh"));
		cmd.add(input[1] + ".wav");
		log.info("Comando a ejecutar: " + cmd.toString());
		exec.executeCommand();
		String output = exec.getOutput();
		cmd.clear();
		// feature super vector generation
		cmd.addAll(Arrays.asList("sfbcep", "-m", "-k", "0.97", "-p19", "-n", "24", "-r", "22", "-e", "-D", "-A", "-F", "PCM16", input[1] + ".wav", "data/prm/" + input[1] + ".tmp.prm"));
		log.info("Comando a ejecutar: " + cmd.toString());
		exec = new SystemCommandExecutor(cmd);
		exec.executeCommand();
		output = exec.getOutput();
		log.info(output);
		// feature normalization
		cmd.clear();
		cmd.addAll(Arrays.asList("NormFeat", "--config", "cfg/NormFeat_energy_SPro.cfg", "--inputFeatureFilename", input[1], "--featureFilesPath", "data/prm/"));
		log.info("Comando a ejecutar: " + cmd.toString());
		exec = new SystemCommandExecutor(cmd);
		exec.executeCommand();
		output = exec.getOutput();
		log.info(output);
		cmd.clear();
		cmd.addAll(Arrays.asList("EnergyDetector", "--config", "cfg/EnergyDetector_SPro.cfg", "--inputFeatureFilename", input[1], "--featureFilesPath", "data/prm/", "--labelFilesPath", "data/lbl/"));
		log.info("Comando a ejecutar: " + cmd.toString());
		exec = new SystemCommandExecutor(cmd);
		exec.executeCommand();
		output = exec.getOutput();
		log.info(output);
		cmd.clear();
		cmd.addAll(Arrays.asList("NormFeat", "--config", "cfg/NormFeat_SPro.cfg", "--inputFeatureFilename", input[1], "--featureFilesPath", "data/prm/", "--labelFilesPath", "data/lbl/"));
		exec = new SystemCommandExecutor(cmd);
		log.info("Comando a ejecutar: " + cmd.toString());
		exec.executeCommand();
		output = exec.getOutput();
		log.info(output);
		String datatest = new String(input[1] + " " + input[0]);
		FileUtils.write(new File(getComputeOneTestFile(input[0])), datatest);
		// Scoring
		cmd.clear();
		//new File(FilenameUtils.concat(ubmPath, "computetest.sh")).setExecutable(true);
		//cmd.add(FilenameUtils.concat(ubmPath, "./computetest.sh"));
		cmd.addAll(Arrays.asList("/bin/sh", "-c", "cd " +ubmPath+ " && ComputeTest --outputFilename "+ getResOneTest(input[0])+ " --ndxFilename "+getComputeOneTestFile(input[0])+" --config cfg/ComputeTest_GMM.cfg"));
		log.info("Comando a ejecutar: " + cmd.toString());
		exec = new SystemCommandExecutor(cmd);
		exec.executeCommand();
		output = exec.getOutput();
	}

	public void znormverification() throws IOException, InterruptedException {
		List<String> cmd = new ArrayList<String>();
		SystemCommandExecutor exec = new SystemCommandExecutor(cmd);
		cmd.addAll(Arrays.asList("ComputeNorm", "--config", "cfg/ComputeNorm_znorm.cfg"));
		log.info("Comando a ejecutar: " + cmd.toString());
		exec = new SystemCommandExecutor(cmd);
		exec.executeCommand();
		String output = exec.getOutput();
		log.info(output);
		cmd.clear();
	}

	public String getNdxDest() {
		return FilenameUtils.concat(ubmPath, "ndx");
	}

	public String getResDest(){
		return FilenameUtils.concat(ubmPath, "res");
	}

	public String getResOneTest(String cedula){
		return FilenameUtils.concat(getResDest(), String.format("%s.res", cedula));
	}

	public String getComputetestFile() {
		return FilenameUtils.concat(getNdxDest(), "computetest_gmm_target-seg.ndx");
	}
	public String getComputeOneTestFile(String cedula) {
		return FilenameUtils.concat(getNdxDest(), String.format("%s_target-seg.ndx", cedula));
	}
}
