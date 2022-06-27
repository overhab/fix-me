package edu.school21.server;

import edu.school21.exceptions.InvalidFixMessage;
import edu.school21.models.FixMessage;
import edu.school21.utils.Utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RouterConnectionHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {

	private final Map<String, AsynchronousSocketChannel> routingTable;
	private final AsynchronousServerSocketChannel server;
	private static int id = 777000;
	private Future<String> submit;
	private FixMessage fixMessage;

	public RouterConnectionHandler(Map<String, AsynchronousSocketChannel> routingTable,
								   AsynchronousServerSocketChannel server) {
		this.routingTable = routingTable;
		this.server = server;
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

		Worker worker = new Worker(routingTable, client, id, port);
		id++;

		try {
			while (true) {
				submit = Executors.newSingleThreadExecutor().submit(worker);
				String response = submit.get();
				System.out.println("Response: " + response);

				forwardMessage(response, port);

			/*	if (response.equals("error")) {
					continue ;
				}*/

				if (response.contains("exit#")) {
					System.out.println("Client disconnected: " + response.split("#")[1]);
					break;
				}
			}
		} catch (InterruptedException | ExecutionException | IOException e) {
			e.printStackTrace(System.out);
		}
	}

	private void forwardMessage(String message, int port) throws IOException {
		fixMessage = new FixMessage(message, true);
		String sender = fixMessage.getSenderId();
		String target = fixMessage.getTargetId();

		try {
			fixMessage.verifyChecksum();
		} catch (InvalidFixMessage e) {
			e.printStackTrace();
			Utils.sendRequest("Checksum do not match", routingTable.get(sender));
			return ;
		}

		AsynchronousSocketChannel client = routingTable.get(target);
		if (client == null) {
			Utils.sendRequest("error", routingTable.get(sender));
		} else {
//			System.out.println("forwarding to port: " + port);
			Utils.sendRequest(message, client);
		}

		/*for (String id : routingTable.keySet()) {
			if (id.equals(msg[0])) {
				Utils.sendRequest(message, routingTable.get(id));
			}
		}*/
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

	private boolean checksum(String message) {


		return true;
	}
}
