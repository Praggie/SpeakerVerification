package com.millenium.speaker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.math.NumberUtils;

public class Conf {
	private static final String FILENAME = "config.properties";
	public static final String KEY_TARGET_FILENAME = "target_filename";
	public static final String KEY_IMPOSTOR_FILENAME = "impostor_filename";
	public static final String KEY_UBM_FILENAME = "ubm_filename";
	public static final String KEY_MIN_FILE_SIZE = "min_file_size";
	public static final String KEY_MIN_IMP_FILE_SIZE = "min_imp_file_size";
	public static final String KEY_ENTRY_TYPE = "tipo_entrada";
	public static final String KEY_UBM_ENTRY_TYPE = "tipo_entrada_ubm";
	public static final String KEY_GENERO = "genero";
	public static final String KEY_VERIFICATION_MODE = "solo_verificacion";
	public static final String KEY_TEST_MODE = "solo_test";
	public static final String KEY_LISTS_PATH = "lists_path";
	public static final String KEY_TEST_PATH = "test_path";
	public static final String KEY_FILES_PATH = "files_path";
	public static final String KEY_RECORDINGS_PATH = "recordings_path";
	public static final String KEY_TEST_RECORDS = "test_records";
	public static final String KEY_TRAIN_RECORDS = "train_records";
	public static final String KEY_UMBRAL_MIN = "umbral_min";
	public static final String KEY_UMBRAL_MAX = "umbral_max";
	public static final String KEY_REPORT_PATH= "report_path";


	private static Properties properties;

	private static void loadProperties() {
		if (properties != null) {
			return;
		}
		try {
			InputStream inputStream = new FileInputStream(new File(FILENAME));
			properties = new Properties();
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String get(String key, String defaultValue) {
		loadProperties();
		return properties.getProperty(key, defaultValue);
	}

	public static String get(String key) {
		loadProperties();
		return properties.getProperty(key);
	}
	public static String getUbmList() {
		loadProperties();
		return properties.getProperty(KEY_UBM_FILENAME);
	}

	public static long getMinFileSize() {
		loadProperties();
		String value = properties.getProperty(KEY_MIN_FILE_SIZE);
		return NumberUtils.toLong(value, 1000 * 1024);
	}

	public static long getMinImpFileSize() {
		loadProperties();
		String value = properties.getProperty(KEY_MIN_IMP_FILE_SIZE);
		return NumberUtils.toLong(value, 400 * 1024);
	}

	public static int getTrainRecords() {
		loadProperties();
		String value = properties.getProperty(KEY_TRAIN_RECORDS);
		return NumberUtils.toInt(value, 1);
	}

	public static int getTestRecords() {
		loadProperties();
		String value = properties.getProperty(KEY_TEST_RECORDS);
		return NumberUtils.toInt(value, 1);
	}

	public static int getTotalRecords() {
		loadProperties();
		return getTrainRecords() + getTestRecords();
	}

	public static boolean getVerificationMode() {
		loadProperties();
		String value = properties.getProperty(KEY_VERIFICATION_MODE);
		if (value.equals("true")) {
			return true;
		}

		else {
			return false;
		}
	}

	public static boolean getTestMode() {
		loadProperties();
		String value = properties.getProperty(KEY_TEST_MODE);
		if (value.equals("true")) {
			return true;
		}

		else {
			return false;
		}
	}

	public static float getUmbralmin() {
		loadProperties();
		String value = properties.getProperty(KEY_UMBRAL_MIN);
		return NumberUtils.toFloat(value, 0);
	}

	public static float getUmbralmax() {
		loadProperties();
		String value = properties.getProperty(KEY_UMBRAL_MAX);
		return NumberUtils.toFloat(value, 0);
	}

	public static String getReportPath() {
		loadProperties();
		String value = properties.getProperty(KEY_REPORT_PATH)+"/report.html";
		return value;
	}

	public static String getTemplatePath() {
		loadProperties();
		String value = properties.getProperty(KEY_REPORT_PATH)+"/index.html";
		return value;
	}

	public static String getEntryType(){
		String value = properties.getProperty(KEY_ENTRY_TYPE);
		return value;

	}public static String getUbmEntryType(){
		String value = properties.getProperty(KEY_UBM_ENTRY_TYPE);
		return value;

	}





}
