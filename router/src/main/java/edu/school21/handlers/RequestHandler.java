package edu.school21.handlers;

import edu.school21.exceptions.InvalidFixMessage;
import edu.school21.models.FixMessage;
import edu.school21.utils.Utils;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class RequestHandler extends RouterHandler {
	public RequestHandler(int type, Map<String, AsynchronousSocketChannel> routingTable, Map<String, String> ongoingRequests) {
		super(type, routingTable, ongoingRequests);
	}

	@Override
	public String handle(String message, AsynchronousSocketChannel socket) {
		try {
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

			System.out.println(response);
			FixMessage fixMessage = new FixMessage(response, true);
			fixMessage.verifyChecksum();
			String target = fixMessage.getTargetId();
			AsynchronousSocketChannel targetSocket = routingTable.get(target);
			if (targetSocket == null) {
				return nextHandler.handle(
						"[ERROR] failed to send a request: market with such id [" + target + "] is not available",
						socket);
			}
			Utils.sendRequest(response, targetSocket);
			ongoingRequests.put(target, id);
			return "[INFO] message successfully sent to " + target;

		} catch (InvalidFixMessage | IndexOutOfBoundsException e) {
			return nextHandler.handle("[ERROR] failed to send a request: " + e.getMessage(), socket);
		}
	}
}
