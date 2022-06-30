package edu.school21.models;

import java.util.List;


public class Market {

    private List<String> stocks;

    public Market(List<String> stocks) {
        this.stocks = stocks;
    }

    public List<String> getStocks() {
        return stocks;
    }

    public void setStocks(List<String> stocks) {
        this.stocks = stocks;
    }

    public String findStock(String stock) {
        for (String s : stocks) {
            if (s.equals(stock)) {
                return s;
            }
        }
        return "";
    }

    @Override
    public String toString() {
        return "Market{" +
                "stocks=" + stocks +
                '}';
    }
}
