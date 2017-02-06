package com.millenium.speaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Results {
	private static Results results = new Results();
	private String genre;
	private int targets;
	private int impostors;
	private String type_test;
	private String type_ubm;
	private Map<String, List<Rates>> rates;

	private Results() {

	}

	public static Results getInstance() {
		if (results == null) {
			results = new Results();
		}
		return results;

	}

	private void addRate(String key, Rates value) {
		if(rates==null) {
			rates = new HashMap<String, List<Rates>>();
		}
		if(!rates.containsKey(key)) {
			rates.put(key, new ArrayList<Rates>());
		}
		rates.get(key).add(value);
	}

	public void addRateNC(Rates rate) {
		addRate("NC", rate);
	}

	public void addRateZT(Rates rate) {
		addRate("ZT", rate);
	}

	public void addRateZ(Rates rate) {
		addRate("Z", rate);
	}

	public void addRateT(Rates rate) {
		addRate("T", rate);
	}

	public void setType_ubm() {
		this.type_ubm = Conf.getUbmEntryType();
	}

	public String getType_ubm() {

		return type_ubm;
	}
	public void setType_test() {
		this.type_test = Conf.getEntryType();
	}

	public String getType_test() {
		return type_test;
	}

	public void setImpostors(int value) {
		this.impostors = value;
	}

	public int getImpostors() {
		return impostors;
	}

	public void setRates(Map<String, List<Rates>> output) {
		this.rates = output;

	}

	public Map<String, List<Rates>> getRates() {
		return rates;
	}

	public void setGenre() {
		this.genre = Conf.KEY_GENERO;
	}

	public String getGenre() {
		return genre;
	}

	public void setTargets(int value) {
		this.targets = value;
	}

	public int getTargets() {
		return targets;
	}

}
