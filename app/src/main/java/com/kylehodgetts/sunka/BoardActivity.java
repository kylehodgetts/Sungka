package com.kylehodgetts.sunka;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;

import com.kylehodgetts.sunka.controller.GameManager;
import com.kylehodgetts.sunka.controller.ViewManager;
import com.kylehodgetts.sunka.controller.bus.EventBus;
import com.kylehodgetts.sunka.event.NewGame;
import com.kylehodgetts.sunka.event.TrayOnClickListener;
import com.kylehodgetts.sunka.model.Board;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.model.Player;
import com.kylehodgetts.sunka.uiutil.ShellDrawable;

import java.util.Random;


/**
 * @author Phileas Hocquard
 * @author Charlie Baker
 * @author Jonathan Burton
 * @version 1.4
 */
public class BoardActivity extends AppCompatActivity {

    //TODO: Implement OnPause, OnResume, OnStop methods. And within all other necessary classes

    private View decorView;
    private GameState state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorView = getWindow().getDecorView();

        this.setContentView(R.layout.activity_board);
        state = new GameState(new Board(), new Player(), new Player());
        EventBus<GameState> bus = new EventBus<>(state,this);
        bus.registerHandler(new GameManager(bus));
        bus.registerHandler(new ViewManager(bus, this));
        makeXMLButtons(bus); bus.feedEvent(new NewGame());


}

    private void makeXMLButtons(EventBus bus) {
        GridLayout gridlayout = (GridLayout) findViewById(R.id.gridLayout);

        for(int i=0; i < 2; ++i) {
            for(int j=0; j < 7; ++j) {
                final LinearLayout linearLayout;
                if(i==0) { linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.buttonlayoutb, gridlayout, false); }
                else { linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.buttonlayouta, gridlayout, false); }
                linearLayout.setId(Integer.parseInt(i + "" + j));

                RelativeLayout button = (RelativeLayout) linearLayout.findViewById(R.id.button);

                GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    param.columnSpec = GridLayout.spec(i == 1?6-j:j,2f);
                    param.rowSpec = GridLayout.spec((i+1)%2, 2f);

                }
                param.width= GridLayout.LayoutParams.WRAP_CONTENT;
                param.height=GridLayout.LayoutParams.WRAP_CONTENT;
                param.setGravity(Gravity.FILL_HORIZONTAL);

                linearLayout.setLayoutParams(param);
                gridlayout.addView(linearLayout);

                button.setOnClickListener(new TrayOnClickListener(j, i, bus));
            }
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }

            GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);

            for(int i = 0; i < gridLayout.getChildCount(); ++i) {
                int width = gridLayout.getChildAt(i).getWidth();
                GridLayout.LayoutParams llparams = new GridLayout.LayoutParams();
                llparams.width = width;
                llparams.height = width;
                gridLayout.getChildAt(i).setLayoutParams(llparams);
                if(state.getInitialising() == -2) {
//                    createShells((RelativeLayout) gridLayout.getChildAt(i).findViewById(R.id.button));
                }
            }
        }
    }

    /**
     * Method that allows us to return to the main menu .
     */
    public void returnToMainMenu(View view){
        Intent intent = new Intent(BoardActivity.this,MainActivity.class);
        BoardActivity.this.startActivity(intent);
    }

    public void createShells(RelativeLayout button) {
        Random random = new Random();

        for(int shell=0; shell < 7; ++shell) {
            int[] coordinates = new int[2];
            button.getLocationOnScreen(coordinates);
            int xLeft = coordinates[0];
            int yTop = coordinates[1];
            int xRight = xLeft + button.getWidth() - button.getPaddingRight() - button.getPaddingLeft();
            int yBottom = yTop + button.getHeight() + button.getPaddingTop() - button.getPaddingBottom();
            Log.d("xLeft", xLeft+"");
            Log.d("yTop", yTop+"");
            Log.d("xRight", xRight+"");
            Log.d("yBottom", yBottom+"");

            ShellDrawable shellDrawable = new ShellDrawable(this, 0, 0, 40, 20);
            RelativeLayout.LayoutParams shellParams = new RelativeLayout.LayoutParams(40, 20);
            shellParams.leftMargin = random.nextInt(button.getWidth());
            shellParams.topMargin = random.nextInt(button.getHeight());
            shellParams.width = 40;
            shellParams.height = 40;
            shellDrawable.setLayoutParams(shellParams);
            shellDrawable.setBackgroundColor(Color.TRANSPARENT);
            button.addView(shellDrawable, shellParams);
        }
    }

}
