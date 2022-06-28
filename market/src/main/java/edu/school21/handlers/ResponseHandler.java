package edu.school21.handlers;

import edu.school21.models.FixMessage;
import edu.school21.models.Market;
import edu.school21.service.MarketService;
import edu.school21.utils.Utils;

import java.nio.channels.AsynchronousSocketChannel;

public class ResponseHandler extends MarketHandler {

	public ResponseHandler(int type, MarketService marketService, Market market) {
		super(type, marketService, market);
	}

	@Override
	public String handle(FixMessage fixMessage, AsynchronousSocketChannel socket) {
		if (ERROR_CODE != 0) {
			return nextHandler.handle(fixMessage, socket);
		}

		String from = fixMessage.getSenderId();
		fixMessage.setSenderId(fixMessage.getTargetId());
		fixMessage.setTargetId(from);
		try {
			fixMessage.parseFixMessage();
		} catch (NullPointerException e) {
			ERROR_CODE = 3; // Invalid message (null pointer)
			nextHandler.handle(fixMessage, socket);
			return "Null pointer";
		}
		Utils.sendRequest(fixMessage.getFixMessage(), socket);

		return "Order " + fixMessage.getOrderId() + " completed. " +
				"Status: " + ((fixMessage.getStatus() == 8) ? "rejected" : "executed");
	}
}
