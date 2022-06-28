package edu.school21.handlers;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.Map;

public abstract class RouterHandler extends Handler<String> {

	protected Map<String, AsynchronousSocketChannel> routingTable;

	public RouterHandler(int type, Map<String, AsynchronousSocketChannel> routingTable) {
		super(type);
		this.routingTable = routingTable;
	}
}
