package edu.school21.modules;

public class FixMessage {
    private final String instrument;
    private final long quantity;
    private final int price;
    private final int marketId;

    public FixMessage(String instrument, long quantity, int price, int marketId) {
        this.instrument = instrument;
        this.quantity = quantity;
        this.price = price;
        this.marketId = marketId;
    }

    public String getInstrument() {
        return instrument;
    }

    public long getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public int getMarketId() {
        return marketId;
    }
}
