package com.millenium.speaker;

public class Records {

	protected String cedula;
	protected String uniqueCall;
	protected String gender;


	@Override
	public boolean equals(Object obj) {
		if(this == null || obj == null) {
			return false;
		}
		Records rec =(Records) obj;

		if(cedula.equals(rec.cedula) && uniqueCall.equals(rec.uniqueCall)) {
			return true;
		}
		return super.equals(obj);
	}


	public String getCedula() {
		return cedula;
	}
	public void setCedula(String cedula) {
		this.cedula = cedula;
	}
	public String getUniqueCall() {
		return uniqueCall;
	}
	public void setUniqueCall(String uniqueCall) {
		this.uniqueCall = uniqueCall;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}

}
