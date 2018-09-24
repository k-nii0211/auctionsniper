package net.goos;

import net.goos.AuctionEventListener.PriceSource;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AuctionMessageTranslator implements MessageListener {
    private final AuctionEventListener listener;
    private final String sniperId;

    AuctionMessageTranslator(final String sniperId, final AuctionEventListener listener) {
        this.sniperId = sniperId;
        this.listener = listener;
    }

    @Override
    public void processMessage(final Chat chat, final Message message) {
        AuctionEvent event = AuctionEvent.from(message.getBody());

        String eventType = event.type();
        if ("CLOSE".equals(eventType)) {
            listener.auctionClosed();
        } else if ("PRICE".equals(eventType)) {
            listener.currentPrice(event.currentPrice(), event.increment(), event.isFrom(sniperId));
        }
    }


    private static class AuctionEvent {
        private final Map<String, String> fields = new HashMap<String, String>();

        public PriceSource isFrom(String sniperId) {
            return sniperId.equals(bidder()) ? PriceSource.FromSniper : PriceSource.FromOtherBidder;
        }

        String type() {
            return get("Event");
        }

        String bidder() {
            return get("Bidder");
        }

        int currentPrice() {
            return getInt("CurrentPrice");
        }

        int increment() {
            return getInt("Increment");
        }

        private String get(final String fieldName) {
            return fields.get(fieldName);
        }

        private int getInt(final String fieldName) {
            return Integer.parseInt(get(fieldName));
        }

        private void addField(String field) {
            final String[] pair = field.split(":");
            fields.put(pair[0].trim(), pair[1].trim());
        }

        private static String[] fieldsIn(final String messageBody) {
            return messageBody.split(";");
        }

        static AuctionEvent from(String messageBody) {
            AuctionEvent event = new AuctionEvent();
            for (String field : fieldsIn(messageBody)) {
                event.addField(field);
            }
            return event;
        }
    }
}
