package com.kylehodgetts.sunka;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.kylehodgetts.sunka.controller.GameManager;
import com.kylehodgetts.sunka.controller.bus.EventBus;
import com.kylehodgetts.sunka.event.NewGame;
import com.kylehodgetts.sunka.event.PlayerMove;
import com.kylehodgetts.sunka.model.Board;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.model.Player;


/**
 *
 *
 * @author: Phileas Hocquard and Charlie Baker
 * @version 1.1
 * **/

public class BoardActivity extends AppCompatActivity {

    private EventBus<GameState> bus;

    //TODO: Implement OnPause, OnResume, OnStop methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        makeXMLButtons();

        GameState state = new GameState(new Board(),new Player(),new Player());
        bus = new EventBus<>(state,this);
        bus.registerHandler(new GameManager(bus));

        bus.feedEvent(new NewGame());
    }

    public void makeXMLButtons(){
        GridLayout gridlayout = (GridLayout)findViewById(R.id.gridLayout);

        for(int i=0; i < 2; ++i) {
            for(int j=0; j < 7; ++j) {
                final Button button;
                if(i==0) { button = (Button) getLayoutInflater().inflate(R.layout.buttonlayoutb, gridlayout, false); }
                else { button = (Button) getLayoutInflater().inflate(R.layout.buttonlayouta, gridlayout, false); }
                button.setId(Integer.parseInt(i + "" + j));

                GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                param.columnSpec = GridLayout.spec(i == 1?6-j:j);
                param.rowSpec = GridLayout.spec((i+1)%2);
                button.setLayoutParams(param);
                gridlayout.addView(button);
            }
        }
    }

    public EventBus<GameState> getBus() {
        return bus;
    }


}
