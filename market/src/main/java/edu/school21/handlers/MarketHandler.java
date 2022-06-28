package edu.school21.handlers;

import edu.school21.models.FixMessage;
import edu.school21.models.Market;
import edu.school21.service.MarketService;

public abstract class MarketHandler extends Handler<FixMessage> {

	protected MarketService marketService;
	protected Market market;
	protected static int ERROR_CODE = 0;

	public MarketHandler(int type, MarketService marketService, Market market) {
		super(type);
		this.marketService = marketService;
		this.market = market;
	}
}
