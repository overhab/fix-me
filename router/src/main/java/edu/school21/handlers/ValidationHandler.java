package edu.school21.handlers;

import edu.school21.exceptions.InvalidFixMessage;
import edu.school21.models.FixMessage;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.Map;

public class ValidationHandler extends RouterHandler {
	public ValidationHandler(int type, Map<String, AsynchronousSocketChannel> routingTable) {
		super(type, routingTable);
	}

	@Override
	public String handle(String message, AsynchronousSocketChannel socket) {
		if (message.contains("disconnect#")) {
			return nextHandler.handle(message, socket);
		}

		try {
			FixMessage fixMessage = new FixMessage(message, true);
			fixMessage.verifyChecksum();
			return nextHandler.handle(message, socket);
		} catch (InvalidFixMessage | IndexOutOfBoundsException e) {
			e.printStackTrace();
			return nextHandler.handle("[ERROR] sending a request: " + e.getMessage(), socket);
		}
	}
}