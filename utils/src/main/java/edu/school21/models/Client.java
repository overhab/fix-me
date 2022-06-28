package edu.school21.models;

import edu.school21.handlers.*;
import edu.school21.utils.Utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public abstract class Client<T> {

	protected String id;
	protected AsynchronousSocketChannel client;
	protected FixMessage fixMessage = new FixMessage();
	protected Handler<T> handler;

	public Client() {
	}

	public void init(int port) throws IOException {
		client = AsynchronousSocketChannel.open();
		Future<Void> future = client.connect(new InetSocketAddress("localhost", port));
		System.out.println("Connected to server...");
		try {
			future.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		assignId();
	}

	private void assignId() {
		try {
			String response = Utils.getResponse(client);
			this.id = response;
		} catch (IOException ex) {
			System.out.println("[ERROR]: " + ex.getMessage());
		}
	}

	public void requestId() {
		Utils.sendRequest(id, client);
	}

	public abstract Handler<T> initHandlers();
}
