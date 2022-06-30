package edu.school21.handlers;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.Map;

public abstract class RouterHandler extends Handler<String> {

	protected Map<String, AsynchronousSocketChannel> routingTable;
	protected Map<String, String> ongoingRequests;

	public RouterHandler(int type, Map<String, AsynchronousSocketChannel> routingTable,
						 Map<String, String> ongoingRequests) {
		super(type);
		this.routingTable = routingTable;
		this.ongoingRequests = ongoingRequests;
	}
}
