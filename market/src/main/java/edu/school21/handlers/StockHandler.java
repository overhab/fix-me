package edu.school21.handlers;

import edu.school21.models.FixMessage;
import edu.school21.models.Market;
import edu.school21.models.Stock;
import edu.school21.service.MarketService;

import java.nio.channels.AsynchronousSocketChannel;

public class StockHandler extends MarketHandler {

	public StockHandler(int type, MarketService marketService, Market market) {
		super(type, marketService, market);
	}

	@Override
	public String handle(FixMessage fixMessage, AsynchronousSocketChannel socket) {
		if (ERROR_CODE != 0) {
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
		return nextHandler.handle(fixMessage, socket);
	}
}
