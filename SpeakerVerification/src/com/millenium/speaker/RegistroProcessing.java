package com.millenium.speaker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegistroProcessing {
	private static Logger log = LoggerFactory.getLogger(CreateLists.class);
	private TestsFolders dataPath;
	private Map<String, List<Registro>> elegidos = new HashMap<String, List<Registro>>();

	public RegistroProcessing(TestsFolders dataPath) {
		super();
		this.dataPath = dataPath;
	}

	public void registroReading() throws IOException, InterruptedException {

		Map<String, List<Registro>> map = new HashMap<String, List<Registro>>();
		List<Registro> registros = new ArrayList<Registro>();
		List<String> lineas = FileUtils.readLines(new File(dataPath.getTargetlist()));
		boolean primero = true;
		for (String string : lineas) {
			if (primero) {
				primero = false;
				continue;
			}
			String[] cols = StringUtils.split(string, " ");
			if (cols == null || cols.length != 5) {
				cols = StringUtils.split(StringUtils.replace(string, "\"", ""), " ");
			}
			String cedula = cols[0];
			if (!map.containsKey(cedula)) {
				// log.info(cedula);
				map.put(cedula, new ArrayList<Registro>());
			}

			// long filesize = FileUtils.sizeOf(new
			// File(dataPath.getOriginalAudio(String.format("%s.wav",
			// cols[1]))));
			long filesize = Long.valueOf(cols[5]);
			Registro reg = new Registro(cols[0], cols[1], cols[2], cols[3], cols[4], filesize);
			map.get(cedula).add(reg);
			registros.add(reg);

			if (!elegidos.containsKey(cedula)) {
				elegidos.put(cedula, new ArrayList<Registro>());
			}
			// if (reg.isEntryType(Conf.get(Conf.KEY_ENTRY_TYPE)) && filesize >=
			// Conf.getMinFileSize() &&
			// reg.getGender().equals(Conf.get(Conf.KEY_GENERO))) {
			elegidos.get(cedula).add(reg);

			// }

		}
		log.info(elegidos.toString());

	}

	public Map<String, List<String>> registroFilter() throws IOException, InterruptedException {
		Map<String, List<String>> testList = new HashMap<String, List<String>>();
		List<Registro> testrecord = new ArrayList<Registro>();
		List<String> toRemove = new ArrayList<String>();
		List<String> grabaciones = new ArrayList<String>();
		for (Entry<String, List<Registro>> entry : elegidos.entrySet()) {
			if (entry.getValue().size() < Conf.getTotalRecords()) {
				toRemove.add(entry.getKey());
			}
		}
		for (String key : toRemove) {
			elegidos.remove(key);
		}
		for (Entry<String, List<Registro>> entry : elegidos.entrySet()) {
			testrecord.addAll(entry.getValue().subList(0, Conf.getTotalRecords()));
			for (Registro temp : testrecord) {
				grabaciones.add(temp.getUniqueCall());
				String origen = String.format("%s.wav",FilenameUtils.concat(dataPath.getOriginalAudio(), temp.getUniqueCall()));
				String destino = String.format("%s.wav", FilenameUtils.concat(dataPath.getDataDest(), temp.getUniqueCall()));
				FileUtils.copyFile(new File(origen), new File(destino));
			}
			if (!testList.containsKey(entry.getKey())) {
				testList.put(entry.getKey(), new ArrayList<String>());
				testList.get(entry.getKey()).addAll(grabaciones);
			}



			grabaciones.clear();
			testrecord.clear();
		}

		return testList;

	}

}

// public Map<String, List<String>> registroFilter() throws IOException,
// InterruptedException {
// Map<String, List<String>> testList = new HashMap<String, List<String>>();
// List<Registro> recordings = new ArrayList<Registro>();
// List<Registro> testrecord = new ArrayList<Registro>();
// List<String> toRemove = new ArrayList<String>();
// List<String> grabaciones = new ArrayList<String>();
// // for (Entry<String, List<Registro>> entry : elegidos.entrySet()) {
// // if (entry.getValue().size() < Conf.getTotalRecords()) {
// // toRemove.add(entry.getKey());
// // }
// // }
// // for (String key : toRemove) {
// // elegidos.remove(key);
// // }
// log.info(elegidos.entrySet().toString());
//
// log.info("Copiando archivos y creando listas de registro");
// List<Registro> temporal;
// for (Entry<String, List<Registro>> entry : elegidos.entrySet()) {
// long duration=0;
// int i=0;
// while (duration<Init.TRAIN_FILE_SIZE){
// duration+=entry.getValue().get(i).getFilesize();
// i++;}
// int trainrecords=i;
// recordings.addAll(entry.getValue().subList(0, trainrecords));
// Concatenation newRecording = new Concatenation(recordings,
// dataPath.getOriginalAudio(), dataPath.getDataDest(),true);
// newRecording.mergeWav();
// grabaciones.add(newRecording.getNewName());
// //log.info("trainrecords:"+trainrecords);
// //recordings.addAll(entry.getValue().subList(0, Conf.getTrainRecords()));
// int testrecords=trainrecords+1;
// int blocknumber=0;
// if(entry.getValue().size()-trainrecords+1>0){
// while(testrecords < entry.getValue().size()-testrecords) {
// duration=0;
// i=0;
// testrecord.clear();
// while
// (duration<Init.TEST_FILE_SIZE){duration+=entry.getValue().get(testrecords).getFilesize();
// i++;}
// testrecord.addAll(entry.getValue().subList(testrecords, testrecords+i));
// log.info("testrecord"+testrecords);
// testrecords+=i;
// blocknumber+=1;
// Concatenation newtestrecord = new
// Concatenation(testrecord,dataPath.getOriginalAudio(),
// dataPath.getDataDest(),false);
// newtestrecord.mergeWav(blocknumber);
// grabaciones.add(newtestrecord.getNewName()+blocknumber);
// }
//
// }else{
// log.error("no hay suficiente audio para el test de "+entry.getKey());
// }
//
//
// //log.info("testrecords:"+testrecords);
//
// // testrecord.addAll(entry.getValue().subList(trainrecords,
// entry.getValue().size()));
// //testrecord.addAll(entry.getValue().subList(Conf.getTrainRecords(),
// Conf.getTotalRecords()));
//
//
// // for (Registro temp : testrecord) {
// // grabaciones.add(temp.getUniqueCall());
// // }
// if (!testList.containsKey(entry.getKey())) {
// testList.put(entry.getKey(), new ArrayList<String>());
// testList.get(entry.getKey()).addAll(grabaciones);
// }
// testrecord.clear();
// recordings.clear();
// grabaciones.clear();
// /*for (Registro otros : entry.getValue().subList(Conf.getTrainRecords(),
// Conf.getTotalRecords())) {
// // aqui copiamos
// String origen = String.format("%s.wav",
// FilenameUtils.concat(dataPath.getOriginalAudio(), otros.getUniqueCall()));
// String destino = String.format("%s.wav",
// FilenameUtils.concat(dataPath.getDataDest(), otros.getUniqueCall()));
// FileUtils.copyFile(new File(origen), new File(destino));
// }*/
//
//
// }
// Results.getInstance().setTargets(testList.size());
// log.info("testlist : "+testList.toString());
// return testList;
// }
