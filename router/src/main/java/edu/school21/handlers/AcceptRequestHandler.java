package edu.school21.handlers;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AcceptRequestHandler extends RouterHandler {
	public AcceptRequestHandler(int type, Map<String, AsynchronousSocketChannel> routingTable, Map<String, String> ongoingRequests) {
		super(type, routingTable, ongoingRequests);
	}

	@Override
	public String handle(String message, AsynchronousSocketChannel socket, int handler) {
		try {
			Future<Integer> readResult;
			String response;

			ByteBuffer acceptBuffer = ByteBuffer.allocate(200);
			readResult = socket.read(acceptBuffer);
			readResult.get();
			response = new String(acceptBuffer.array()).trim();
			acceptBuffer.clear();

			if (response.isEmpty()) {
				return nextHandler.handle(message, socket, HandlerType.DISCONNECT);
			}

			return nextHandler.handle(response, socket, HandlerType.MESSAGE);
		} catch (InterruptedException | ExecutionException e) {
			return nextHandler.handle(message, socket, HandlerType.DISCONNECT);
		}
	}
}
