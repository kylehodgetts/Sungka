package com.kylehodgetts.sunka.event;

import android.view.View;

import com.kylehodgetts.sunka.controller.bus.EventBus;
import com.kylehodgetts.sunka.controller.wifi.SingletonSocket;
import com.kylehodgetts.sunka.event.PlayerChoseTray;
import com.kylehodgetts.sunka.model.GameState;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

/**
 * @author Adam Chlupacek
 * @version 1.1
 *          A listener for tray clicks
 */
public class TrayOnClickListener implements View.OnClickListener {

    private int trayIndex, playerIndex;
    private EventBus<GameState> bus;

    public TrayOnClickListener(int playerIndex, int trayIndex, EventBus<GameState> bus) {
        this.playerIndex = playerIndex;
        this.trayIndex = trayIndex;
        this.bus = bus;
    }

    @Override
    public void onClick(View v) {
        bus.feedEvent(new PlayerChoseTray(playerIndex, trayIndex));
    }
}
