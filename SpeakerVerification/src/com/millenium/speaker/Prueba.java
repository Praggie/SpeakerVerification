package com.millenium.speaker;

import java.io.IOException;

public class Prueba{

	public static void main(String[] args) throws IOException, InterruptedException {
		String ubmPath= args[0];
		String audioPath=args[1];
		String [] input = {args[2], args[3]};//cedula y nombre de la grabacion

		//UserEnrollment usuario = new UserEnrollment(ubmPath, audioPath, input);
		//usuario.newAudioRegistration();
		UserVerification target = new UserVerification(ubmPath, audioPath, input);
		target.newAudioVerification();
		//target.znormverification();
	}
}
