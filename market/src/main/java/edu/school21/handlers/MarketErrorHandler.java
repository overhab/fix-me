package edu.school21.handlers;

import edu.school21.models.FixMessage;
import edu.school21.models.Market;
import edu.school21.service.MarketService;
import edu.school21.utils.Utils;

import java.nio.channels.AsynchronousSocketChannel;

public class MarketErrorHandler extends MarketHandler{

	public MarketErrorHandler(int type, MarketService marketService, Market market) {
		super(type, marketService, market);
	}

	@Override
	public String handle(FixMessage message, AsynchronousSocketChannel socket) {
		String errorMessage = "";

		if (ERROR_CODE == 1) {
			errorMessage = "[ERROR] Instrument not found";
		} else if (ERROR_CODE == 2) {
			errorMessage = "[ERROR] No stock found";
		} else if (ERROR_CODE == 3) {
			errorMessage = "[ERROR] Null pointer exception";
		}

		Utils.sendRequest(errorMessage, socket);

		return errorMessage;
	}
}
