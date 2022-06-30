package edu.school21.handlers;

import edu.school21.models.FixMessage;
import edu.school21.models.Market;
import edu.school21.models.Stock;
import edu.school21.service.MarketService;
import edu.school21.utils.Utils;

import java.nio.channels.AsynchronousSocketChannel;

public class OrderHandler extends MarketHandler {

	public OrderHandler(int type, MarketService marketService, Market market) {
		super(type, marketService, market);
	}

	@Override
	public String handle(FixMessage fixMessage, AsynchronousSocketChannel socket) {
		try {
			String stockName = fixMessage.getInstrument();
			if (market.findStock(stockName).isEmpty()) {
				ERROR_CODE = 1; // instrument not found
				return nextHandler.handle(fixMessage, socket);
			}
			Stock stock = marketService.findStockByName(fixMessage.getInstrument());

			if (stock.getQuantity() < fixMessage.getQuantity()) {
				fixMessage.setStatus(8);
			} else {
				if (fixMessage.getType().equals("buy") || fixMessage.getType().equals("1")) {
					stock.setQuantity(stock.getQuantity() - fixMessage.getQuantity());
				} else if (fixMessage.getType().equals("sell") || fixMessage.getType().equals("2")) {
					stock.setQuantity(stock.getQuantity() + fixMessage.getQuantity());
				}
				marketService.updateStock(stock);
				fixMessage.setStatus(2);
			}

			fixMessage.prepareResponse();
			Utils.sendRequest(fixMessage.getFixMessage(), socket);
			return "Order " + fixMessage.getOrderId() + " completed. " +
					"Status: " + ((fixMessage.getStatus() == 8) ? "rejected" : "executed");

		} catch (NullPointerException e) {
			e.printStackTrace();
			ERROR_CODE = 2; // No stock found
			return nextHandler.handle(fixMessage, socket);
		}
	}
}
