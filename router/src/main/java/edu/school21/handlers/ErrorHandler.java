package edu.school21.handlers;

import edu.school21.utils.Utils;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.Map;

public class ErrorHandler extends RouterHandler {

	public ErrorHandler(int type, Map<String, AsynchronousSocketChannel> routingTable) {
		super(type, routingTable);
	}

	@Override
	public String handle(String message, AsynchronousSocketChannel socket) {
		if (message.contains("disconnect#")) {
			return nextHandler.handle(message, socket);
		}
		Utils.sendRequest(message, socket);
		return message;
	}
}
