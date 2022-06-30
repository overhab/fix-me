package edu.school21.server;

import edu.school21.handlers.Handler;
import edu.school21.utils.Utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class RouterConnectionHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {

	private final Map<String, AsynchronousSocketChannel> routingTable;
	private final AsynchronousServerSocketChannel server;
	private static int id = 777000;
	private Future<String> submit;
	private Handler<String> handler;

	public RouterConnectionHandler(Map<String, AsynchronousSocketChannel> routingTable,
								   AsynchronousServerSocketChannel server,
								   Handler<String> handler) {
		this.routingTable = routingTable;
		this.server = server;
		this.handler = handler;
	}

	@Override
	public void completed(AsynchronousSocketChannel client, Void attachment) {
		server.accept(null, this);
		Utils.sendRequest(String.valueOf(id), client);
		routingTable.put(String.valueOf(id), client);

		int port = 0;
		try {
			port = getPort(client.getLocalAddress());
		} catch (IOException e) {
			e.printStackTrace();
			failed(e, null);
		}
		System.out.println("Connected: " + id + " - port " + port);

		Worker worker = new Worker(client, id, port, handler);
		id++;

		String response;

		while (true) {
			submit = Executors.newSingleThreadExecutor().submit(worker);
			try {
				response = submit.get();
				System.out.println("Handler returned: " + response);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
				break ;
			}
			if (!routingTable.containsKey(String.valueOf(worker.getId()))) {
				break ;
			}
		}

	}

	@Override
	public void failed(Throwable exc, Void attachment) {

	}

	private int getPort(SocketAddress address) throws IOException {
		if (address instanceof InetSocketAddress) {
			return ((InetSocketAddress) address).getPort();
		} else {
			throw new IOException("Unknown address type");
		}
	}
}
