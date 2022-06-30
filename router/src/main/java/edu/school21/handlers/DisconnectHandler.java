package edu.school21.handlers;

import edu.school21.utils.Utils;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.Map;

public class DisconnectHandler extends RouterHandler {
	public DisconnectHandler(int type, Map<String, AsynchronousSocketChannel> routingTable, Map<String, String> ongoingRequests) {
		super(type, routingTable, ongoingRequests);
	}

	@Override
	public String handle(String message, AsynchronousSocketChannel socket) {
		String[] split = message.split("#");
		String id = split[1];
		if (ongoingRequests.containsKey(id)) {
			Utils.sendRequest("[ERROR] market " + id + " is unavailable", routingTable.get(ongoingRequests.get(id)));
		}
		routingTable.remove(id);
		ongoingRequests.remove(id);
		return "client disconnected";
	}
}

