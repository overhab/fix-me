package edu.school21.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileLog {
	private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");

	private static BufferedWriter WRITER = null;

	static {
		try {
			WRITER = new BufferedWriter(new FileWriter("fix-me.txt", true));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public FileLog() {
	}

	public static void LogMessage(String string) throws IOException {
		LocalDateTime now = LocalDateTime.now();
		WRITER.write(DTF.format(now) + ": " + string + "\n");
	}

	public static void close() throws IOException {
		WRITER.close();
	}
}
