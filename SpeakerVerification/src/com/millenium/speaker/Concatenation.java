package com.millenium.speaker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Concatenation {
	private static Logger log = LoggerFactory.getLogger(Concatenation.class);
	private List<Registro> records;
	private String newName;
	private String filesPath;
	private String savePath;

	public Concatenation(List<Registro> records, String filesPath, String savePath, boolean istrain) {
		super();
		this.records = records;
		if (istrain) {
			this.newName = records.get(0).getCedula() + "_newr";
		} else {
			this.newName = records.get(0).getCedula() + "_test";
		}

		this.filesPath = filesPath;
		this.savePath = savePath;
	}

	public void mergeWav(Integer blocknumber) throws IOException, InterruptedException {

		// FilenameUtils.c
		if (records.size() == 1) {
			FileUtils.copyFile(new File(String.format("%s.wav", FilenameUtils.concat(filesPath, records.get(0).getUniqueCall()))), new File(String.format("%s.wav", FilenameUtils.concat(savePath, newName))));
		} else {
			List<String> cmd = new ArrayList<String>();
			cmd.add("sox");
			for (Registro reg : records) {
				cmd.add(String.format("%s.wav", FilenameUtils.concat(filesPath, reg.getUniqueCall())));
			}
			if (blocknumber==null){
			cmd.add(String.format("%s.wav", FilenameUtils.concat(savePath, newName)));
			}else{
				cmd.add(String.format("%s.wav", FilenameUtils.concat(savePath, newName+blocknumber)));
			}
			SystemCommandExecutor exec = new SystemCommandExecutor(cmd);
			exec.executeCommand();
			System.out.println(exec.getOutput());

		}
	}

	public void mergeWav() throws IOException, InterruptedException {
		mergeWav(null);
	}

	public String getNewName() {
		return newName;
	}

//	public void trimTest() throws IOException, InterruptedException {
//
//		List<String> cmd = new ArrayList<String>();
//
//		for (Registro reg : records) {
//			cmd.clear();
//			cmd.add("sox");
//			cmd.add(String.format("%s.wav", FilenameUtils.concat(filesPath, reg.getUniqueCall())));
//			cmd.add(String.format("%s.wav", FilenameUtils.concat(savePath, reg.getUniqueCall())));
//			cmd.addAll(Arrays.asList("trim", "0", "30")); // cut to 30 seconds
//			SystemCommandExecutor exec = new SystemCommandExecutor(cmd);
//			exec.executeCommand();
//			log.info(cmd.toString());
//			System.out.println(exec.getOutput());
//		}
//
//	}
}
