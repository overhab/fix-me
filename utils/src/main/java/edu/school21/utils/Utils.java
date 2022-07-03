package edu.school21.utils;

import edu.school21.exceptions.ConnectionUnavailable;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
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
//			System.out.println("SENDING MESSAGE: [" + msg + "]\nbytes: " + byteCount.get() + "\n-----------------");
			FileLog.LogMessage("SENDING MESSAGE: [" + msg + "]bytes: " + byteCount.get());
		} catch (ExecutionException | InterruptedException | IOException e) {
			throw new ConnectionUnavailable(e.getMessage());
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
		buffer.flip();
		buffer.clear();
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
