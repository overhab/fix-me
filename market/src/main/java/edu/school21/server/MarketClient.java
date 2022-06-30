package edu.school21.server;

import edu.school21.handlers.*;
import edu.school21.models.FixMessage;
import edu.school21.models.Market;
import edu.school21.service.MarketService;
import edu.school21.models.Client;
import edu.school21.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MarketClient extends Client<FixMessage> {

	private Market market;
	private MarketService marketService;

	@Autowired
	public MarketClient(MarketService marketService) {
		this.marketService = marketService;
		this.market = marketService.createMarket();
	}

	public void start() throws IOException {
		handler = initHandlers();
		waitForRequest();
	}

	private void waitForRequest() throws IOException {
		System.out.println("Market: " + id + ". Stocks available: " + market.getStocks());
		while (true) {
			String req = Utils.getResponse(client);
			FixMessage fixMessage = new FixMessage(req, true);
			String status = handler.handle(fixMessage, client);
			System.out.println(status);
		}
	}

	@Override
	public Handler<FixMessage> initHandlers() {
		Handler<FixMessage> order = new OrderHandler(HandlerType.ORDER, marketService, market);
		Handler<FixMessage> error = new MarketErrorHandler(HandlerType.ERROR, marketService, market);

		order.setNextHandler(error);

		return order;
	}
}
