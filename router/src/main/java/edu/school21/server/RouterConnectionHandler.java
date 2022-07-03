package edu.school21.server;

import edu.school21.handlers.Handler;
import edu.school21.utils.Utils;

import java.io.IOException;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
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
		int id = nextId();
		Utils.sendRequest(String.valueOf(id), client);
		routingTable.put(String.valueOf(id), client);

		int port = 0;
		try {
			port = Utils.getPort(client.getLocalAddress());
		} catch (IOException e) {
			e.printStackTrace();
			failed(e, null);
		}
		System.out.println("Connected: " + id + " - port " + port);

		Worker worker = new Worker(client, id, port, handler);

		String response;

		while (true) {
			submit = Executors.newSingleThreadExecutor().submit(worker);
			try {
				response = submit.get();
				System.out.println(response);
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

	private int nextId() {
		return id++;
	}
}
