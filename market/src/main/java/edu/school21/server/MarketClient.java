package edu.school21.server;

import edu.school21.models.FixMessage;
import edu.school21.models.Market;
import edu.school21.service.MarketService;
import edu.school21.models.Client;
import edu.school21.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Scanner;

@Component
public class MarketClient extends Client {

	private final Market market;
	private MarketService marketService;

	@Autowired
	public MarketClient(MarketService marketService) throws IOException {
		market = marketService.createMarket();
	}

	public void start() throws IOException {
//		sendMessages();
		waitForRequest();
	}

	private void sendMessages() throws IOException {
		Scanner scanner = new Scanner(System.in);
		String input, response;

		while (true) {
			input = scanner.nextLine();

			Utils.sendRequest(input, client);
			if (input.equalsIgnoreCase("exit")) {
				requestId();
				break ;
			}
			response = Utils.getResponse(client);
			System.out.println(id + "#received back: " + response);
		}

		client.close();
	}

	private void waitForRequest() throws IOException {
		while (true) {
			String req = Utils.getResponse(client);
			fixMessage = new FixMessage(req, true);
			System.out.println("Received request: " + fixMessage.getOrderId());

			//market logic
			//String res = marketService.executeOrder(fixMessage);
			String from = fixMessage.getSenderId();
			fixMessage.setSenderId(id);
			fixMessage.setTargetId(from);
			fixMessage.setStatus(2);
			fixMessage.parseFixMessage();

			Utils.sendRequest(fixMessage.getFixMessage(), client);
		}
	}

}
