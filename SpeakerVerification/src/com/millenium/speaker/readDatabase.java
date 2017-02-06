package com.millenium.speaker;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class readDatabase {
	private String databasePath;
	private Map<String, Registro> database = new HashMap<String, Registro>();;

	public readDatabase(String databasePath) {
		super();
		this.databasePath = databasePath;
	}

	public void dataToRegistro() throws IOException {
		Map<String, Registro> database = new HashMap<String, Registro>();
		List<String> lineas = FileUtils.readLines(new File(databasePath));
		boolean primero = true;
		for (String string : lineas) {
			if (primero) {
				primero = false;
				continue;
			}
			String[] cols = StringUtils.split(string, " ");
			if (cols == null || cols.length != 5) {
				cols = StringUtils.split(StringUtils.replace(string, "\"", ""), "\t");
			}
			String cedula = cols[0];
			if (!database.containsKey(cedula)) {
				database.put(cedula, new Registro(cols[0], cols[1], cols[2], cols[3], cols[4], 1000000));
			}

		}

	}

	public Map<String, Registro> getDatabase() {
		return database;
	}
}
