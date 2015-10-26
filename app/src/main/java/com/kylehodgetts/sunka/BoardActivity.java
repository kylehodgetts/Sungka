package com.kylehodgetts.sunka;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.kylehodgetts.sunka.controller.GameManager;
import com.kylehodgetts.sunka.controller.bus.EventBus;
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
    private static int gameMode;

    GameManager manager;
    GameState state;
    EventBus<GameState> bus;

    TextView tvDebugging;
    TextView tvPlayerA;
    TextView tvPlayerB;
    TextView bscoreA;
    TextView bscoreB;

    //TODO: Implement OnPause, OnResume, OnStop methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        manager = new GameManager();
        state = new GameState(new Board(),new Player(),new Player(), -1);
        bus = new EventBus<>(state,this);
        bus.registerHandler(manager);

        tvDebugging = (TextView)findViewById(R.id.tvDebugging);
        tvPlayerA = (TextView) findViewById(R.id.tvPlayerA);
        tvPlayerB=(TextView)findViewById(R.id.tvPlayerB);
        bscoreA = (Button) findViewById(R.id.buttonas);
        bscoreB = (Button) findViewById(R.id.buttonbs);

        makeXMLButtons();
        bscoreA.setText(Integer.toString(state.getPlayer1().getStonesInPot()));
        bscoreB.setText(Integer.toString(state.getPlayer2().getStonesInPot()));
    }

    public void makeXMLButtons(){
        GridLayout gridlayout = (GridLayout)findViewById(R.id.gridLayout);

        for(int i=0; i < 2; ++i) {
            for(int j=0; j < 7; ++j) {
                final Button button;
                if(i==0) { button = (Button) getLayoutInflater().inflate(R.layout.buttonlayouta, gridlayout, false); }
                else { button = (Button) getLayoutInflater().inflate(R.layout.buttonlayoutb, gridlayout, false); }

                button.setId(Integer.parseInt(i+""+j));
                button.setText(Integer.toString(state.getBoard().getTray(i, j)));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = button.getId();
                        bus.feedEvent(new PlayerMove(id%10, id/10, state.getPlayerOneTurn()));
                        manager.render(state, BoardActivity.this);
                    }
                });

                GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                param.columnSpec = GridLayout.spec(j);
                param.rowSpec = GridLayout.spec(i);
                button.setLayoutParams(param);
                gridlayout.addView(button);
            }
        }
    }




}
