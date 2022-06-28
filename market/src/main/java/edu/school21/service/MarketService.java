package edu.school21.service;

import edu.school21.models.Market;
import edu.school21.models.Stock;

public interface MarketService {
	Market createMarket();
	Stock findStockByName(String name);
	void addStock(Stock stock);
	void updateStock(Stock stock);
}
