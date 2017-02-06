package com.millenium.speaker;

public class Rates {
	private float te;// total error
	private float fa;// false acceptance
	private float fr; // false rejection
	private float sr;// success rate

	public Rates(float fa, float fr, float sr) {
		super();
		this.setFa(fa);
		this.setFr(fr);
		this.setTe(fa, fr);
		this.setSr(sr);
	}

	@Override
	public String toString() {
		return String.format("[%s, %s]", fr, fa);
	}

	public float getFa() {
		return fa;
	}

	public void setFa(float fa) {
		this.fa = fa;
	}

	public float getFr() {
		return fr;
	}

	public void setFr(float fr) {
		this.fr = fr;
	}

	public float getSr() {
		return sr;
	}

	public void setSr(float sr) {
		this.sr = sr;
	}

	public void setTe(float fa, float fr) {
		this.te = fa + fr;
	}

	public float getTe() {
		return te;
	}
}
