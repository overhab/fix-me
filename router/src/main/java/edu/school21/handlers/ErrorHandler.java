package edu.school21.handlers;

import edu.school21.utils.Utils;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.Map;

public class ErrorHandler extends RouterHandler {

	public ErrorHandler(int type, Map<String, AsynchronousSocketChannel> routingTable, Map<String, String> ongoingRequests) {
		super(type, routingTable, ongoingRequests);
	}

	@Override
	public String handle(String message, AsynchronousSocketChannel socket, int handler) {
		if (handler == HandlerType.DISCONNECT) {
			return nextHandler.handle(message, socket, handler);
		}
		Utils.sendRequest(message, socket);
		return message;
	}
}
