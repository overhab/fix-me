package edu.school21.handlers;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class RequestHandler extends RouterHandler {
	public RequestHandler(int type, Map<String, AsynchronousSocketChannel> routingTable) {
		super(type, routingTable);
	}

	@Override
	public String handle(String message, AsynchronousSocketChannel socket) {
		if (message.contains("request#")) {
			String id = message.split("#")[1];
			Future<Integer> readResult;
			String response;

			ByteBuffer buffer = ByteBuffer.allocate(200);
			readResult = socket.read(buffer);
			try {
				readResult.get();
			} catch (InterruptedException | ExecutionException e) {
				return nextHandler.handle("disconnect#" + id, socket);
			}
			response = new String(buffer.array()).trim();
			buffer.clear();
			return nextHandler.handle(response, socket);
		} else {
			return nextHandler.handle(message, socket);
		}
	}
}
