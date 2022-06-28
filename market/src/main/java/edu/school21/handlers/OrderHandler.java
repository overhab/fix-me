package edu.school21.handlers;

import edu.school21.models.FixMessage;
import edu.school21.models.Market;
import edu.school21.service.MarketService;

import java.nio.channels.AsynchronousSocketChannel;

public class OrderHandler extends MarketHandler {

	public OrderHandler(int type, MarketService marketService, Market market) {
		super(type, marketService, market);
	}

	@Override
	public String handle(FixMessage fixMessage, AsynchronousSocketChannel socket) {
		try {
			String instrument = fixMessage.getInstrument();
			String stock = market.findStock(instrument);

			if (stock == null) {
				ERROR_CODE = 1; // instrument not found
				return nextHandler.handle(fixMessage, socket);
			}
			return nextHandler.handle(fixMessage, socket);
		} catch (NullPointerException e) {
			e.printStackTrace();
			ERROR_CODE = 2; // No stock found
			return nextHandler.handle(fixMessage, socket);
		}
	}
}
