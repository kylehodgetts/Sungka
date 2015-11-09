package com.kylehodgetts.sunka;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.GridLayout;

import com.kylehodgetts.sunka.controller.AIManager;
import com.kylehodgetts.sunka.controller.GameManager;
import com.kylehodgetts.sunka.controller.bus.EventBus;
import com.kylehodgetts.sunka.event.NewGame;
import com.kylehodgetts.sunka.event.TrayOnClickListener;
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

    public static final int ONEPLAYER = 1;
    public static final int TWOPLAYER = 2;
    public static final int ONLINE = 3;

    public static final String EXTRA_INT = "com.kylehodgetts.sunka.boardactivity.gametype";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        //TODO add gametype check here, and pass that to the relevant object below

        int gameType = getIntent().getIntExtra(EXTRA_INT, 0);

        GameState state = new GameState(new Board(), new Player(), new Player());
        EventBus<GameState> bus = new EventBus<>(state, this);
        bus.registerHandler(new GameManager(bus));

        if (gameType == ONEPLAYER) {
            bus.registerHandler(new AIManager(bus));
            makeXMLButtons(bus, false);
        } else if (gameType == TWOPLAYER) {
            makeXMLButtons(bus, true);
        } else if (gameType == ONLINE) {
            //TODO bus.registerHandler(ONLINEHANDLER)
            makeXMLButtons(bus, false);
        }

        bus.feedEvent(new NewGame());
    }

    private void makeXMLButtons(EventBus bus, boolean bothSetsButtonsClickable) {
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
                if (player == 1) {
                    param.columnSpec = GridLayout.spec(6 - tray);
                } else {
                    param.columnSpec = GridLayout.spec(tray);
                }
                param.rowSpec = GridLayout.spec((player + 1) % 2);
                button.setLayoutParams(param);
                gridlayout.addView(button);

                //we don't want the opposite side clickable if there are not two local players
                if (player == 0 || player == 1 && bothSetsButtonsClickable) {
                    button.setOnClickListener(new TrayOnClickListener(tray, player, bus));
                }
            }
        }
    }
}
