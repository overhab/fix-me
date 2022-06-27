package edu.school21.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Utils {

	public static void sendRequest(String msg, AsynchronousSocketChannel channel) {
		ByteBuffer buffer = ByteBuffer.allocate(msg.length() + 20);
		Future<Integer> byteCount;

		buffer.put(msg.getBytes());
		buffer.flip();
		byteCount = channel.write(buffer);

		try {
			System.out.println("SENDING MESSAGE: \n[" + msg + "]\nbytes: " + byteCount.get() + "\n-----------------");
		} catch (ExecutionException | InterruptedException e) {
			e.printStackTrace();
		}

		buffer.clear();
	}

	public static String getResponse(AsynchronousSocketChannel channel) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(200);
		String response;

		Future<Integer> readResult = channel.read(buffer);

		try {
			readResult.get();
		} catch ( InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		response = new String(buffer.array()).trim();
		return response;
	}
}
