package edu.school21.handlers;

import edu.school21.exceptions.ConnectionUnavailable;
import edu.school21.exceptions.InvalidFixMessage;
import edu.school21.models.FixMessage;
import edu.school21.utils.Utils;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Map;

public class FixMessageHandler extends RouterHandler {
	public FixMessageHandler(int type, Map<String, AsynchronousSocketChannel> routingTable, Map<String, String> ongoingRequests) {
		super(type, routingTable, ongoingRequests);
	}

	@Override
	public String handle(String message, AsynchronousSocketChannel socket, int handler) {
		if (handler == HandlerType.ERROR || handler == HandlerType.DISCONNECT) {
			return nextHandler.handle(message, socket, handler);
		}

		FixMessage fixMessage;

		try {
			fixMessage = new FixMessage(message, true);
			fixMessage.verifyChecksum();
			String target = fixMessage.getTargetId();
			AsynchronousSocketChannel targetSocket = routingTable.get(target);
			if (targetSocket == null) {
				return nextHandler.handle(
						"[ERROR] failed to send a request: market with such id [" + target + "] is not available",
						socket, HandlerType.ERROR);
			}
			Utils.sendRequest(message, targetSocket);
			if (Utils.getPort(socket.getLocalAddress()) == 5000) {
				ongoingRequests.put(target, fixMessage.getSenderId());
			}
			return "[INFO] message successfully sent to " + target;

		} catch (ConnectionUnavailable | IOException e) {
			return nextHandler.handle("", socket, HandlerType.DISCONNECT);
		} catch (InvalidFixMessage | IndexOutOfBoundsException e) {
			return nextHandler.handle("[ERROR] failed to send a request: " + e.getMessage(), socket, HandlerType.ERROR);
		}
	}
}
