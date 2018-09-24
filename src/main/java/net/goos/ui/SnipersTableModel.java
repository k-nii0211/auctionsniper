package net.goos.ui;

import net.goos.Column;
import net.goos.SniperSnapshot;
import net.goos.SniperState;

import javax.swing.table.AbstractTableModel;

import static net.goos.ui.MainWindow.STATUS_JOINING;

public class SnipersTableModel extends AbstractTableModel {
    private final static String[] STATUS_TEXT = { MainWindow.STATUS_JOINING, MainWindow.STATUS_BIDDING };
    private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, SniperState.JOINING);
    private String statusText = STATUS_JOINING;
    private SniperSnapshot sniperSnapshot = STARTING_UP;

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (Column.at(columnIndex)) {
            case ITEM_IDENTIFIER:
                return sniperSnapshot.itemId;
            case LAST_PRICE:
                return sniperSnapshot.lastPrice;
            case LAST_BID:
                return sniperSnapshot.lastBid;
            case SNIPER_STATUS:
                return statusText;
            default:
                throw new IllegalArgumentException("No column at" + columnIndex);
        }
    }

    public void sniperStatusChanged(final SniperSnapshot sniperSnapshot) {
        this.sniperSnapshot = sniperSnapshot;
        this.statusText = STATUS_TEXT[sniperSnapshot.state.ordinal()];

        fireTableRowsUpdated(0, 0);
    }
}
