package com.millenium.speaker;

import org.apache.commons.lang3.math.NumberUtils;

public class Registro extends Records {

	private String date;
	private String phone;
	private long filesize;

	public Registro(String cedula, String uniqueCall, String date, String phone, String gender, long filesize) {
		super();
		this.cedula = cedula;
		this.uniqueCall = uniqueCall;
		this.gender = gender;
		this.date = date;
		this.phone = phone;
		this.filesize = filesize;
	}



	public boolean isCelular() {
		return phone.length() == 10;
	}

	public boolean isLandLine() {
		return phone.length() == 8;
	}

	public boolean isUnknown() {
		return  phone==null || phone.equals("ANONYMOUS") || phone.trim().length()==0 || (String.valueOf(NumberUtils.toLong(phone,0)).length()<7);
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public long getFilesize() {
		return filesize;
	}

	public void setFileSize(long filesize) {
		this.filesize = filesize;
	}

	public boolean isEntryType(String type) {
		if(type.equals("fijo") && isLandLine()) {
			return true;
		}
		if(type.equals("celular") && isCelular()) {
			return true;
		}
		if(type.equals("anonimo") && isUnknown()) {
			return true;
		}
		if(type.equals("todos")) {
			return true;
		}
		return false;
	}





}
