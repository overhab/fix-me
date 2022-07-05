package edu.school21.utils;

import edu.school21.exceptions.ConnectionUnavailable;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.*;

public class Utils {

	private static final ExecutorService executor = Executors.newCachedThreadPool();

	public static void sendRequest(String msg, AsynchronousSocketChannel channel) {
		executor.execute(() -> {
			ByteBuffer buffer1 = ByteBuffer.allocate(msg.length());
			Future<Integer> byteCount;

			buffer1.put(msg.getBytes());
			buffer1.flip();
			byteCount = channel.write(buffer1);
			try {
				Integer bytes = byteCount.get();
				FileLog.LogMessage("SENDING MESSAGE: [" + msg + "]bytes: " + bytes);
			} catch (ExecutionException | InterruptedException | IOException e) {
				throw new ConnectionUnavailable(e.getMessage());
			}

			buffer1.clear();
		});
	}

	public static String getResponse(AsynchronousSocketChannel channel) throws IOException {
		Future<String> submit = executor.submit(() -> {
			ByteBuffer buffer1 = ByteBuffer.allocate(256);
			String response;

			Future<Integer> readResult = channel.read(buffer1);

			try {
				readResult.get();
			} catch ( InterruptedException | ExecutionException e) {
				throw new ConnectionUnavailable(e.getMessage());
			}

			response = new String(buffer1.array()).trim();
			buffer1.clear();
			return response;
		});
		String response = null;
		try {
			response = submit.get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
		return response;
	}

	public static int getPort(SocketAddress address) throws IOException {
		if (address instanceof InetSocketAddress) {
			return ((InetSocketAddress) address).getPort();
		} else {
			throw new IOException("Unknown address type");
		}
	}
}
