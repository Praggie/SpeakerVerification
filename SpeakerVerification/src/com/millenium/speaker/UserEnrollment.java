package com.millenium.speaker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.math3.stat.StatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//registro(cedula,uniquecall,date,phone,gender,filesize)
public class UserEnrollment {
	private static Logger log = LoggerFactory.getLogger(UserEnrollment.class);
	private String ubmPath;
	private String audioPath;
	private String[] input;// cedula y uniquecallid

	public UserEnrollment(String ubmPath, String audioPath, String[] input) {
		super();
		this.ubmPath = ubmPath;
		this.audioPath = audioPath;
		this.input = input;
	}

	// audio processing adjusts gain, noise and silence removal
	public void newAudioRegistration() throws IOException, InterruptedException {
		String origen = String.format("%s.wav", FilenameUtils.concat(audioPath, input[1]));
		String destino = String.format("%s.wav", FilenameUtils.concat(ubmPath, input[1]));
		FileUtils.copyFile(new File(origen), new File(destino));
		List<String> cmd = new ArrayList<String>();
		// audio processing
		new File(FilenameUtils.concat(ubmPath, "preprocessing.sh")).setExecutable(true);
		SystemCommandExecutor exec = new SystemCommandExecutor(cmd);
		cmd.addAll(Arrays.asList(FilenameUtils.concat(ubmPath, "./preprocessing.sh"), input[1] + ".wav"));
		log.info("Comando a ejecutar: " + cmd.toString());
		exec.executeCommand();
		String output = exec.getOutput();
		cmd.clear();
		// feature super vector generation
		// bin/sfbcep -m -k 0.97 -p19 -n 24 -r 22 -e -D -A -F PCM16 data/$i.wav
		// data/prm/$i.tmp.prm
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
		log.info("Comando a ejecutar: " + cmd.toString());
		exec = new SystemCommandExecutor(cmd);
		exec.executeCommand();
		output = exec.getOutput();
		log.info(output);
		// Model Training
		cmd.clear();
		String datatrain = new String(input[0] + " " + input[1]);
		FileUtils.write(new File(getTrainOneModelFile(input[0])), datatrain);
		//new File(FilenameUtils.concat(ubmPath, "trainingtarget.sh")).setExecutable(true);
		//cmd.add(FilenameUtils.concat(ubmPath, "./trainingtarget.sh"));
		cmd.addAll(Arrays.asList("/bin/sh", "-c", "cd " + ubmPath+ " && TrainTarget --inputFeatureFilename " +getTrainOneModelFile(input[0])+" --targetIdList "+ getTrainOneModelFile(input[0])+ " --config "+FilenameUtils.concat(getCfgDest(),"TrainTarget.cfg")));
		log.info("Comando a ejecutar: " + cmd.toString());
		exec = new SystemCommandExecutor(cmd);
		exec.executeCommand();
		output = exec.getOutput();
		log.info(output);
		// model trained

	}

	public void znorm() throws IOException, InterruptedException {

		List<String> implist = FileUtils.readLines(new File(getImpostorList()));
		List<String> targetimp = new ArrayList<String>();
		for (int i = 0; i < implist.size(); i++) {
			targetimp.add(implist.get(i) + " " + input[0]);
		}
		FileUtils.writeLines(new File(getTargetimpFile()), targetimp);

		List<String> cmd = new ArrayList<String>();
		SystemCommandExecutor exec = new SystemCommandExecutor(cmd);
		cmd.addAll(Arrays.asList("ComputeTest", "--config", "cfg/ComputeTest.cfg"));
		log.info("Comando a ejecutar: " + cmd.toString());
		exec = new SystemCommandExecutor(cmd);
		exec.executeCommand();
		String output = exec.getOutput();
		log.info(output);
		cmd.clear();
		getUserStats();
	}

	public void getUserStats() throws IOException{
		List<String> znorm = FileUtils.readLines(new File(getznormfile()));
		String cedula = StringUtils.substringBetween(znorm.get(0), " ", " ");
		double [] scores = new double [znorm.size()];
		//M 84454757 1 10299141-M 1.16726
		for (int i = 0; i < znorm.size(); i++) {
			scores[i]=NumberUtils.toDouble(StringUtils.substringAfterLast(znorm.get(i), " "));
		}
		double miu = StatUtils.mean(scores);
		double sd = Math.sqrt(StatUtils.variance(scores));
		log.info(String.format("cedula:%s media:%s desviacion:%s", cedula, miu, sd));

	}

	public String getNdxDest() {
		return FilenameUtils.concat(ubmPath, "ndx");
	}

	public String getCfgDest() {
		return FilenameUtils.concat(ubmPath, "cfg");
	}

	public String getResDest(){
		return FilenameUtils.concat(ubmPath, "res");
	}

	public String getznormfile(){
		return FilenameUtils.concat(getResDest(),"target-imp_gmm.res");
	}

	public String getLstDest() {
		return FilenameUtils.concat(ubmPath, "lst");
	}

	public String getTrainmodelFile() {
		return FilenameUtils.concat(getNdxDest(), "trainModel.ndx");
	}


	public String getTrainOneModelFile(String cedula) {
		return FilenameUtils.concat(getNdxDest(), cedula+".ndx");
	}
	public String getTargetimpFile() {
		return FilenameUtils.concat(getNdxDest(), "computetest_gmm_target-imp.ndx");
	}

	public String getImpostorList() {
		return FilenameUtils.concat(getLstDest(), "impostor.lst");
	}

}
