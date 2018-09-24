package net.goos;

import net.goos.ui.MainWindow;

import static net.goos.FakeAuctionServer.AUCTION_RESOURCE;
import static net.goos.FakeAuctionServer.XMPP_HOSTNAME;
import static net.goos.ui.MainWindow.STATUS_JOINING;

public class ApplicationRunner {
    static final String SNIPER_ID = "sniper";
    static final String SNIPER_PASSWORD = "sniper";

    private String itemId;

    public static final String SNIPER_XMPP_ID = SNIPER_ID + "@" + XMPP_HOSTNAME + "/" + AUCTION_RESOURCE;

    private AuctionSniperDriver driver;

    public void startBiddingIn(final FakeAuctionServer auction) {
        itemId = auction.getItemId();
        final Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                try {
                    Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        driver = new AuctionSniperDriver(1000);
//        driver.showsSniperStatus(STATUS_JOINING);
    }

    public void showsSnipersHasLostAuction() {
        driver.showsSniperStatus(MainWindow.STATUS_LOST);
    }

    public void showsSniperHasWonAuction(final int lastPrice) {
        driver.showsSniperStatus(itemId, lastPrice, lastPrice, MainWindow.STATUS_WON);
    }

    public void hasShownSniperIsBidding(final int lastPrice, final int lastBid) {
        driver.showsSniperStatus(itemId, lastPrice, lastBid, MainWindow.STATUS_BIDDING);
    }

    public void hasShownSniperIsWinning(final int lastPrice) {
        driver.showsSniperStatus(itemId, lastPrice, lastPrice, MainWindow.STATUS_WINNING);
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }
}
