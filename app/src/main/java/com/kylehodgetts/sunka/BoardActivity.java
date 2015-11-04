package com.kylehodgetts.sunka;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.GridLayout;

import com.kylehodgetts.sunka.controller.GameManager;
import com.kylehodgetts.sunka.controller.bus.EventBus;
import com.kylehodgetts.sunka.event.NewGame;
import com.kylehodgetts.sunka.model.Board;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.model.Player;


/**
 * @author Phileas Hocquard
 * @author Charlie Baker
 * @author Jonathan Burton
 * @version 1.3
 */
public class BoardActivity extends AppCompatActivity {

    //TODO: Implement OnPause, OnResume, OnStop methods. And within all other necessary classes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        //TODO add gametype check here, and pass that to the relevant object below

        GameState state = new GameState(new Board(), new Player(), new Player());
        EventBus<GameState> bus = new EventBus<>(state, this);
        bus.registerHandler(new GameManager(bus));

        makeXMLButtons(bus);

        bus.feedEvent(new NewGame());
    }

    private void makeXMLButtons(EventBus bus) {
        GridLayout gridlayout = (GridLayout) findViewById(R.id.gridLayout);

        for (int player = 0; player < 2; ++player) {
            for (int tray = 0; tray < 7; ++tray) {
                final Button button;
                if (player == 0) {
                    button = (Button) getLayoutInflater().inflate(R.layout.buttonlayoutb, gridlayout, false);
                } else {
                    button = (Button) getLayoutInflater().inflate(R.layout.buttonlayouta, gridlayout, false);
                }
                button.setId(Integer.parseInt(player + "" + tray));

                GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                if(player == 1) {
                    param.columnSpec = GridLayout.spec(6 - tray);
                }
                else {
                    param.columnSpec = GridLayout.spec(tray);
                }
                param.rowSpec = GridLayout.spec((player + 1) % 2);
                button.setLayoutParams(param);
                gridlayout.addView(button);

                button.setOnClickListener(new TrayOnClick(tray, player, bus));
            }
        }
    }
}
