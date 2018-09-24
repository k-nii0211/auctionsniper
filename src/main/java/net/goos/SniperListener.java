package net.goos;

import java.util.EventListener;

public interface SniperListener extends EventListener {
    void sniperLost();

    void sniperStateChanged(final SniperSnapshot sniperSnapshot);

    void sniperWinning();

    void sniperWon();
}
