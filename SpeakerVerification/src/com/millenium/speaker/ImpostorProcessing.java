package com.millenium.speaker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImpostorProcessing {
	private static Logger log = LoggerFactory.getLogger(ImpostorProcessing.class);
	private TestsFolders dataPath;

	public ImpostorProcessing(TestsFolders dataPath) {
		super();
		this.dataPath = dataPath;
		log.info("dataPath: " + dataPath.getImpostoreslist());
	}

	public Map<String, List<Impostor>> impostorReading() throws IOException {
		log.info("ejecutando impostor reading");
		Map<String, List<Impostor>> map = new HashMap<String, List<Impostor>>();
		List<Impostor> impostor = new ArrayList<Impostor>();
		List<String> line = FileUtils.readLines(new File(dataPath.getImpostoreslist()));
		List<String> implist = new ArrayList<String>();
		List<String> trainlist = new ArrayList<String>();
		List<String> impimp = new ArrayList<String>();
		boolean first = true;
		for (String string : line) {
			if (first) {
				first = false;
				continue;
			}
			String[] cols = StringUtils.split(string, " ");
			String cedula = cols[1];
			//long filesize = FileUtils.sizeOf(new File(dataPath.getOriginalAudio(String.format("%s.wav", cols[2]))));
			//log.info("llegue hasta aca "+string);
		    long filesize=Long.valueOf(cols[4]);
			Impostor reg = new Impostor(cols[0], cols[1], cols[2], cols[3], filesize);
			if (!map.containsKey(cedula) && filesize >= Conf.getMinImpFileSize() && Conf.get(Conf.KEY_GENERO).equals(reg.getgender())) {
				map.put(cedula, new ArrayList<Impostor>());
				impostor.add(reg);
				map.get(cedula).add(reg);
			}
			//log.info(map.toString());

		}

		impimp.addAll(map.keySet());
		List<String> toWrite = new ArrayList<String>();
		for (int i = 0; i < impostor.size(); i++) {
			Impostor listado = impostor.get(i);
			trainlist.add(listado.getCedula() + " " + listado.getUniqueCall());
			implist.add(listado.getUniqueCall());
			Collections.swap(impimp, 0, i);
			List<String> temp = new ArrayList<String>(impimp);
			temp.set(0, listado.getUniqueCall());
			toWrite.add(StringUtils.replaceEach(temp.toString(), new String[] { "[", "]", "," }, new String[] { "", "", "" }));
			FileUtils.copyFile(new File(String.format("%s.wav", FilenameUtils.concat(dataPath.getOriginalAudio(), listado.getUniqueCall()))),
					new File(String.format("%s.wav", FilenameUtils.concat(dataPath.getDataDest(), listado.getUniqueCall()))));
		}

		FileUtils.writeLines(new File(dataPath.getImpimpFile()), toWrite);
		FileUtils.writeLines(new File(dataPath.getComputeimpimpFile()), toWrite);
		FileUtils.writeLines(new File(dataPath.getImpostorFile()), implist);
		FileUtils.writeLines(new File(dataPath.getTrainimpFile()), trainlist);
		Results.getInstance().setImpostors(implist.size());
		return map;

	}

}
