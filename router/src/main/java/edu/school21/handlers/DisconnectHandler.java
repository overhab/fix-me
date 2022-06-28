package edu.school21.handlers;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.Map;

public class DisconnectHandler extends RouterHandler {
	public DisconnectHandler(int type, Map<String, AsynchronousSocketChannel> routingTable) {
		super(type, routingTable);
	}

	@Override
	public String handle(String message, AsynchronousSocketChannel socket) {
		String[] split = message.split("#");
		String id = split[1];
		routingTable.remove(id);
		return  "client disconnected";
	}
}

