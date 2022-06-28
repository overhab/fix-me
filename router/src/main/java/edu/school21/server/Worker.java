package edu.school21.server;

import edu.school21.handlers.Handler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Callable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Worker implements Callable<String> {

	private AsynchronousSocketChannel client;
	private int id;
	private int port;
	private Handler<String> handler;

	@Override
	public String call() {
		return id + ": " + handler.handle("request#" + id, client);
	}
}
