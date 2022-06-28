package edu.school21.handlers;

import edu.school21.models.FixMessage;
import edu.school21.utils.Utils;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.Map;

public class ForwardHandler extends RouterHandler {
	public ForwardHandler(int type, Map<String, AsynchronousSocketChannel> routingTable) {
		super(type, routingTable);
	}

	@Override
	public String handle(String message, AsynchronousSocketChannel socket) {
		if (message.contains("disconnect#")) {
			return nextHandler.handle(message, socket);
		}

		FixMessage fixMessage;
		String target;

		try {
			fixMessage = new FixMessage(message, true);
			target = fixMessage.getTargetId();
			AsynchronousSocketChannel targetSocket = routingTable.get(target);
			if (targetSocket == null) {
				return nextHandler.handle(
						"[ERROR] failed to send a request: market with such id [" + target + "] is not available",
						socket);
			}
			Utils.sendRequest(message, targetSocket);
			return "[INFO] message successfully sent to market " + target;
		} catch (IndexOutOfBoundsException e) {
			return nextHandler.handle("[ERROR] failed to send a request: " + e.getMessage(), socket);
		}
	}
}
