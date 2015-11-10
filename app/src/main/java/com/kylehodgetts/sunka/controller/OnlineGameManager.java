package com.kylehodgetts.sunka.controller;

import android.app.Activity;
import android.util.Log;

import com.kylehodgetts.sunka.controller.wifi.SingletonSocket;
import com.kylehodgetts.sunka.controller.bus.Event;
import com.kylehodgetts.sunka.controller.bus.EventBus;
import com.kylehodgetts.sunka.controller.bus.EventHandler;
import com.kylehodgetts.sunka.event.PlayerChoseTray;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.util.Tuple2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by kylehodgetts on 06/11/2015.
 */
public class OnlineGameManager extends EventHandler<GameState>{

    private EventBus<GameState> bus;
    private Socket socket;
    private boolean gameIsRunning;

    public OnlineGameManager(final EventBus<GameState> bus) {
        super("OnlineManager");
        socket = SingletonSocket.getSocket();
        this.bus = bus;
        gameIsRunning = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    gameIsRunning = true;
                    Log.d("OnlineGameManager: ", "Game is running");
                    while(!Thread.interrupted()){
                        if(socket.getInputStream().available() > 0) {
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            String recieved = bufferedReader.readLine();
                            Log.d("Being recieved", recieved);
                            String[] read = bufferedReader.readLine().split("-");

                            int tray, player;
                            if(read.length == 2) {
                                tray = Integer.parseInt(read[0]);
                                player = Integer.parseInt(read[1]);
                                bus.feedEvent(new PlayerChoseTray(tray, player));
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public Tuple2<GameState, Boolean> handleEvent(Event event, GameState state) {
        if (event instanceof PlayerChoseTray){
            try {
                PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                writer.println(((PlayerChoseTray) event).getTrayIndex() + "-" + ((PlayerChoseTray) event).getPlayerIndex());
                Log.d("Being sent", ((PlayerChoseTray) event).getTrayIndex() + "-" + ((PlayerChoseTray) event).getPlayerIndex());
                writer.flush();
                return new Tuple2<>(state, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Tuple2<>(state, false);
    }

    @Override
    public void updateView(GameState state, Activity activity) {
        Log.d("OnlineGameManager", "updateView()");
    }
}
