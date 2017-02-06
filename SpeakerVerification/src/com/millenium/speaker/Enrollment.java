package com.millenium.speaker;

import java.io.IOException;

public class Enrollment {

	public static void main(String[] args) throws IOException, InterruptedException {
		String ubmPath= args[0];
		String audioPath=args[1];
		String [] input = {args[2], args[3]};

		UserEnrollment usuario = new UserEnrollment(ubmPath, audioPath, input);
		usuario.newAudioRegistration();
		//usuario.znorm();

	}
}