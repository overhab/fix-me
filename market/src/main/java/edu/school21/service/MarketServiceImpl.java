package edu.school21.service;


import edu.school21.models.Market;
import edu.school21.models.Stock;
import edu.school21.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketServiceImpl implements MarketService {

	private final StockRepository stockRepository;

	@Autowired
	public MarketServiceImpl(StockRepository stockRepository) {
		this.stockRepository = stockRepository;
	}

	@Override
	public Market createMarket() {
		List<String> stocks = stockRepository.findAllWithLimit();
		return new Market(stocks);
	}

	@Override
	public Stock findStockByName(String name) {
		return stockRepository.findByName(name);
	}

	@Override
	public void addStock(Stock stock) {
		stockRepository.save(stock);
	}

	@Override
	public void updateStock(Stock stock) {
		stockRepository.update(stock);
	}
}
