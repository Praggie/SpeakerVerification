package com.millenium.speaker;

public class Scores extends Records {

	private boolean test;
	private float rawscore;

	public Scores(String gender, String cedula, boolean test, String recordname, float rawscore) {
		super();
		this.cedula = cedula;
		this.uniqueCall = recordname;
		this.gender = gender;
		this.test = test;
		this.rawscore = rawscore;

	}

	@Override
	public String toString() {
		return String.format("rawscore=%s cedula=%s uniqueCall=%s", rawscore, cedula, uniqueCall);
	}

	public boolean isTest() {
		return test;
	}

	public void setTest(boolean test) {
		this.test = test;
	}

	public float getRawscore() {
		return rawscore;
	}

	public void setRawscore(float rawscore) {
		this.rawscore = rawscore;
	}

}
