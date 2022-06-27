package edu.school21.models;

import java.util.List;


public class Market {

    private List<Stock> stocks;

    public Market(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    @Override
    public String toString() {
        return "Market{" +
                "stocks=" + stocks +
                '}';
    }
}
