package edu.school21.server;

import edu.school21.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Worker implements Callable<String> {

	private Map<String, AsynchronousSocketChannel> routingTable;
	private AsynchronousSocketChannel client;
	private int id;
	private int port;

	@Override
	public String call() throws Exception {
		Future<Integer> readResult;
		String response;

		ByteBuffer buffer = ByteBuffer.allocate(200);
		readResult = client.read(buffer);
		readResult.get();
		response = new String(buffer.array()).trim();

		if (response.equalsIgnoreCase("exit")) {
			String id = Utils.getResponse(client);
			client.close();
			routingTable.remove(id);
			return "exit#" + id;
		}

		buffer.clear();

		return response;
	}
}
