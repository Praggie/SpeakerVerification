package com.millenium.speaker;

import org.apache.commons.lang3.math.NumberUtils;

public class Impostor extends Records {

	private String phone;
	private long filesize;

	public Impostor(String gender, String cedula, String uniqueCall, String phone, long filesize) {
		this.cedula = cedula;
		this.gender = gender;
		this.uniqueCall = uniqueCall;
		this.setFilesize(filesize);
		this.setPhone(phone);

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

	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}
	public String getgender() {
		return gender;
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

	public boolean isCelular() {
		return phone.length() == 10;
	}

	public boolean isLandLine() {
		return phone.length() == 8;
	}

	public boolean isUnknown() {
		return  phone==null || phone.equals("ANONYMOUS") || phone.trim().length()==0 || (String.valueOf(NumberUtils.toLong(phone,0)).length()<7);
	}

}