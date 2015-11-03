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
 *
 *
 * @author: Phileas Hocquard and Charlie Baker
 * @version 1.1
 * **/

public class BoardActivity extends AppCompatActivity {

    //TODO: Implement OnPause, OnResume, OnStop methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        makeXMLButtons();

        //IF the gameMode is in 1player mode aka AI mode
        //TODO: ADD 1 Second delayed effect after (321 GO! Screen)
        //place gameMode, and ai initialisation somewhere.
        int gameMode = 0;
         GameState state;
        AI ai = new AI();

        if(gameMode == 0){
            state = ai.getSide() == 0 ?new GameState(new Board(),ai,new Player())
                    : new GameState(new Board(),new Player(),ai);
        }
        else {
            state = new GameState(new Board(), new Player(), new Player());
        }
        EventBus<GameState> bus = new EventBus<>(state, this);
        bus.registerHandler(new GameManager(bus));
        bus.feedEvent(new NewGame());
        if(gameMode == 0)state.aiInitialisationMove(bus,ai.getSide());
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
}
