package com.millenium.speaker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateLists {
	private static Logger log = LoggerFactory.getLogger(CreateLists.class);
	private TestsFolders dataPath;
	private ImpostorProcessing impostores;
	private RegistroProcessing target;
	private List<String> ubmdata = new ArrayList<String>();
	private List<String> data = new ArrayList<String>();
	private List<String> datatrain = new ArrayList<String>();
	private List<String> testdata = new ArrayList<String>();
	private List<String> pldadata = new ArrayList<String>();
	private List<String> totalvardata = new ArrayList<String>();
	private List<String> ivextractor = new ArrayList<String>();
	private List<String> alllstdata= new ArrayList<String>();

	public CreateLists(TestsFolders dataPath, ImpostorProcessing impostores, RegistroProcessing target) {
		super();
		this.dataPath = dataPath;
		this.impostores = impostores;
		this.target = target;

	}

	public Map<String, List<String>> createTargetImplists() throws IOException, InterruptedException {


			log.info("leyendo el ubm");
			List<String> ubmdata = FileUtils.readLines(new File(dataPath.getUbmlist()));
			log.info("copiando grabaciones ubm");
			for (int i = 0; i < ubmdata.size(); i++) {
				FileUtils.copyFile(new File(String.format("%s.wav", FilenameUtils.concat(dataPath.getOriginalAudio(), ubmdata.get(i)))),
						new File(String.format("%s.wav", FilenameUtils.concat(dataPath.getDataDest(), ubmdata.get(i)))));
			}

			data.addAll(ubmdata);
        log.info("iniciando lectura de registros");
		target.registroReading();
		log.info("iniciando filtro de registros");
		Map<String, List<String>> entradas = target.registroFilter();
		log.info("iniciando lectura de listado de impostores");
		Map<String, List<Impostor>> implist = impostores.impostorReading();
		List<String> targetimp = new ArrayList<String>();
		List<String> impseg = new ArrayList<String>();
		Set<String> temp = entradas.keySet();
		Set<String> temp2 = implist.keySet();
		for (Entry<String, List<Impostor>> entry : implist.entrySet()) {
			for (Impostor imp : entry.getValue()) {
				targetimp.add(imp.getUniqueCall() +" "+ Mapper.listToString(temp));
				data.add(imp.getUniqueCall());
			}
		}
		for (Entry<String, List<String>> entry : entradas.entrySet()) {
			pldadata.add(Mapper.listToString(entry.getValue(),1,Conf.getTestRecords()));
			pldadata.add(Mapper.listToString(entry.getValue(),1,1));
			ivextractor.add(entry.getValue().get(0)+ " " + entry.getValue().get(0));
			alllstdata.add(Mapper.listToString(entry.getValue()));
			for (int i = 1; i <= Conf.getTestRecords(); i++)
			{
				impseg.add(entry.getValue().get(i) + " " + Mapper.listToString(temp2));
				testdata.add(entry.getValue().get(i) + " " + Mapper.listToString(temp));
				totalvardata.add(entry.getValue().get(i));
				ivextractor.add(entry.getValue().get(i)+ " " + entry.getValue().get(i));
			}

			data.addAll(entry.getValue());
			datatrain.add(entry.getKey() + " " + entry.getValue().get(0));


		}
		FileUtils.writeLines(new File(dataPath.getUBMFile()), ubmdata);
		FileUtils.writeLines(new File(dataPath.getOutputFile()), data);
		FileUtils.writeLines(new File(dataPath.getTrainmodelFile()), datatrain);
		FileUtils.writeLines(new File(dataPath.getIvExtractorFile()),ivextractor);
		FileUtils.writeLines(new File(dataPath.getComputetestFile()), testdata);
		FileUtils.writeLines(new File(dataPath.getIvTestFile()),testdata);
		FileUtils.writeLines(new File(dataPath.getPldaFile()),pldadata);
		FileUtils.writeLines(new File(dataPath.getTotalvariability()), totalvardata);
		FileUtils.writeLines(new File(dataPath.getIvNormFile()), pldadata);
		FileUtils.writeLines(new File(dataPath.getAllLstFile()), alllstdata);
		FileUtils.writeLines(new File(dataPath.getComputetargetimpFile()), targetimp);
		FileUtils.writeLines(new File(dataPath.getTargetimpFile()), targetimp);
		FileUtils.writeLines(new File(dataPath.getComputeimpsegFile()), impseg);
		log.info("Finalizo la creacion de listas de registro");
		return entradas;

	}
}
