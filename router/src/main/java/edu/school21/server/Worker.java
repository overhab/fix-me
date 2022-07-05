package edu.school21.server;

import edu.school21.handlers.Handler;
import edu.school21.handlers.HandlerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Callable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Worker implements Callable<String> {

	public Worker(AsynchronousSocketChannel client, int id, int port, Handler<String> handler) {
		this.client = client;
		this.id = id;
		this.port = port;
		this.handler = handler;
	}

	private AsynchronousSocketChannel client;
	private int id;
	private int port;
	private Handler<String> handler;
	private ByteBuffer buffer;

	@Override
	public String call() {
		return id + ": " + handler.handle(String.valueOf(id), client, HandlerType.REQUEST);
	}
}
