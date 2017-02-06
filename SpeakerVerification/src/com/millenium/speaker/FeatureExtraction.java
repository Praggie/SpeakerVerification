package com.millenium.speaker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeatureExtraction {
	private static Logger log = LoggerFactory.getLogger(FeatureExtraction.class);
	private String executablepath;

	public FeatureExtraction(String executablepath) {
		super();
		this.executablepath = executablepath;
	}

	public void features() throws IOException, InterruptedException {

		List<String> cmd = new ArrayList<String>();
		new File(FilenameUtils.concat(executablepath, "preprocessingbatch.sh")).setExecutable(true);
		cmd.add(executablepath + "/./preprocessingbatch.sh");
		log.info("Comando a ejecutar: " + cmd.toString());
		SystemCommandExecutor exec = new SystemCommandExecutor(cmd);
		//exec.executeCommand();
		String output = exec.getOutput();
		log.info(output);
		cmd.clear();
		new File(FilenameUtils.concat(executablepath, "sprofeaturextract.sh")).setExecutable(true);
		cmd.add(executablepath + "/./sprofeaturextract.sh");
		exec = new SystemCommandExecutor(cmd);
		log.info("Comando a ejecutar: " + cmd.toString());
		exec.executeCommand();
		                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  output = exec.getOutput();
		log.info(output);
		if (StringUtils.containsIgnoreCase(output, "not found")) {
			log.error("Finalizando por archivo no encontrado");
			throw new IOException("Finalizando por archivo no encontrado");
		}
		cmd.clear();
		new File(FilenameUtils.concat(executablepath, "normalization.sh")).setExecutable(true);
		cmd.add(FilenameUtils.concat(executablepath, "./normalization.sh"));
		log.info("Comando a ejecutar: " + cmd.toString());
		exec = new SystemCommandExecutor(cmd);
		exec.executeCommand();
		output = exec.getOutput();
		log.info(output);
		if (StringUtils.containsIgnoreCase(output, "Exception")) {
			log.error("La normalizacion de targets tuvo problemas");
		}
		cmd.clear();

		new File(FilenameUtils.concat(executablepath, "trainingubm.sh")).setExecutable(true);
		cmd.add(FilenameUtils.concat(executablepath, "./trainingubm.sh"));
		log.info("Comando a ejecutar: " + cmd.toString());
		String outputubm = exec.getOutput();
		log.info(outputubm);
		if (StringUtils.containsIgnoreCase(outputubm, "Save world model [world]")) {
			log.info("El World Model fue creado exitosamente");

		}

		exec = new SystemCommandExecutor(cmd);
		exec.executeCommand();
		output = exec.getOutput();
		log.info(output);
		cmd.clear();
		new File(FilenameUtils.concat(executablepath, "trainingtarget.sh")).setExecutable(true);
		cmd.add(FilenameUtils.concat(executablepath, "./trainingtarget.sh"));
		log.info("Comando a ejecutar: " + cmd.toString());
		exec = new SystemCommandExecutor(cmd);
		exec.executeCommand();
		output = exec.getOutput();
		log.info(output);
		cmd.clear();
		new File(FilenameUtils.concat(executablepath, "i-vector_background.sh")).setExecutable(true);
		cmd.add(FilenameUtils.concat(executablepath, "./i-vector_background.sh"));
		log.info("Comando a ejecutar: " + cmd.toString());
		exec = new SystemCommandExecutor(cmd);
		exec.executeCommand();
		output = exec.getOutput();
		log.info(output);
		cmd.clear();
		new File(FilenameUtils.concat(executablepath, "computetest.sh")).setExecutable(true);
		cmd.add(FilenameUtils.concat(executablepath, "./computetest.sh"));
		log.info("Comando a ejecutar: " + cmd.toString());
		exec = new SystemCommandExecutor(cmd);
		exec.executeCommand();
		output = exec.getOutput();
		log.info(output);
		cmd.clear();
		new File(FilenameUtils.concat(executablepath, "NormZT.sh")).setExecutable(true);
		cmd.add(FilenameUtils.concat(executablepath, "./NormZT.sh"));
		log.info("Comando a ejecutar: " + cmd.toString());
		exec = new SystemCommandExecutor(cmd);
		exec.executeCommand();
		String outputzt = exec.getOutput();
		if (StringUtils.containsIgnoreCase(outputzt, "not found")) {
			log.info("Fallo la normalizacion");
			throw new IOException("Finalizando normzt por archivo no encontrado");
		} else {
			log.error("Normalizacion ZT exitosa");
		}
	}
}