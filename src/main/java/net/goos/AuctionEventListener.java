package net.goos;

public interface AuctionEventListener {
    void auctionClosed();

    void currentPrice(final int price, final int increment, final PriceSource priceSource);

    enum PriceSource {
        FromSniper, FromOtherBidder;
    }
}
