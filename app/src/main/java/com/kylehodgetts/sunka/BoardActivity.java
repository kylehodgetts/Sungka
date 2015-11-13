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

import com.kylehodgetts.sunka.controller.AnimationManager;
import com.kylehodgetts.sunka.controller.AIManager;
import com.kylehodgetts.sunka.controller.GameManager;
import com.kylehodgetts.sunka.controller.OnlineGameManager;
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
 * Board Activity Class used to create the game board. Inflate and style the board to the user's
 * device screen. Also to instantiate the logic objects to control and handle the game events.
 *
 * @author Phileas Hocquard
 * @author Charlie Baker
 * @author Jonathan Burton
 * @version 1.6
 */
public class BoardActivity extends AppCompatActivity {

    private static int gameType;

    //TODO: Implement OnPause, OnResume, OnStop methods. And within all other necessary classes
    public static final int ONEPLAYER = 1;
    public static final int TWOPLAYER = 2;
    public static final int ONLINE = 3;

    public static final String EXTRA_INT = "com.kylehodgetts.sunka.boardactivity.gametype";

    private View decorView;
    private boolean areShellsCreated;

    /**
     * Creates the board activity, instantiates all necessary objects and sets the content view for
     * the user's device screen to the activity board xml file.
     *
     * @param savedInstanceState Bundle used to restore any previously stored information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorView = getWindow().getDecorView();
        areShellsCreated = false;

        this.setContentView(R.layout.activity_board);

        gameType = getIntent().getIntExtra(EXTRA_INT, 0);

        GameState state = new GameState(new Board(), new Player(), new Player());
        EventBus<GameState> bus = new EventBus<>(state, this);
        bus.registerHandler(new GameManager(bus));
        bus.registerHandler(new ViewManager(bus, this));
        bus.registerHandler(new AnimationManager(bus, this));

        if (gameType == ONEPLAYER) {
            bus.registerHandler(new AIManager(bus));
            makeXMLButtons(bus, false);
        } else if (gameType == TWOPLAYER) {
            makeXMLButtons(bus, true);
        } else if (gameType == ONLINE) {
            bus.registerHandler(new OnlineGameManager(bus));
            makeXMLButtons(bus, false);
        }


        bus.feedEvent(new NewGame());
    }



    /**
     * Method used to inflate the relevant trays in the correct place on the game activity board.
     * This method also attaches the necessary onClickListener to each tray.
     *
     * @param bus The game Event Bus so the tray can be attached with the onClickListener
     */
    private void makeXMLButtons(EventBus bus, boolean bothSetsButtonsClickable) {
        GridLayout gridlayout = (GridLayout) findViewById(R.id.gridLayout);

       for(int i=1; i >= 0; --i) {
            for(int j=6; j >= 0; --j) {
                final LinearLayout linearLayout;
                if (i == 0) {
                    linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.buttonlayoutb, gridlayout, false);
                } else {
                    linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.buttonlayouta, gridlayout, false);
                }
                linearLayout.setId(Integer.parseInt(i + "" + j));

                RelativeLayout button = (RelativeLayout) linearLayout.findViewById(R.id.button);

                // Due to grid layout weights only being available from API 21 onwards to scale the layout
                GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    param.columnSpec = GridLayout.spec(i == 1?6-j:j,1f);
                    param.rowSpec = GridLayout.spec((i+1)%2, 1f);

                }
                param.width = GridLayout.LayoutParams.WRAP_CONTENT;
                param.height = GridLayout.LayoutParams.WRAP_CONTENT;
                param.setGravity(Gravity.FILL_HORIZONTAL);
                param.width= GridLayout.LayoutParams.WRAP_CONTENT;
                param.height=GridLayout.LayoutParams.WRAP_CONTENT;
                param.setGravity(Gravity.FILL_HORIZONTAL);

                linearLayout.setLayoutParams(param);
                gridlayout.addView(linearLayout);
                
                //we don't want the opposite side clickable if there are not two local players
                if (i == 0 || i == 1 && bothSetsButtonsClickable) {
                    button.setOnClickListener(new TrayOnClickListener(j, i, bus));
                }
            }
        }
    }


    /**
     * Makes the game board activity completely full screen, but allowing access to the users status bar
     * by them swiping from the top of the screen.
     * This method also ensures each of the trays are square by setting their height to match the trays
     * inflated width.
     *
     * @param hasFocus boolean if the activity currently has focus by the user.
     */
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
                LinearLayout child = (LinearLayout) gridLayout.getChildAt(i);
                int width = gridLayout.getChildAt(i).getWidth();
                GridLayout.LayoutParams llparams = (GridLayout.LayoutParams) child.getLayoutParams();
                llparams.width = width;
                llparams.height = width + child.findViewById(R.id.tv).getHeight();
                gridLayout.getChildAt(i).setLayoutParams(llparams);
                if(!areShellsCreated) {
                    createShells((RelativeLayout) gridLayout.getChildAt(i).findViewById(R.id.button), 7);
                }
            }
            areShellsCreated = true;
        }
    }

    /**
     * Method that allows us to return to the main menu .
     */
    public void returnToMainMenu(View view) {
        Intent intent = new Intent(BoardActivity.this, MainActivity.class);
        BoardActivity.this.startActivity(intent);
    }

    public void createShells(RelativeLayout button, int numberOfShells) {
        Random random = new Random();

        for(int shell=0; shell < numberOfShells; ++shell) {
            ShellDrawable shellDrawable = new ShellDrawable(this, random.nextInt(button.getWidth()/2),
                    random.nextInt(button.getHeight()/2), 40, 20);
            RelativeLayout.LayoutParams shellParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            shellParams.leftMargin = 0;
            shellParams.topMargin = 0;
            shellParams.rightMargin = 0;
            shellParams.bottomMargin = 0;

            shellDrawable.setLayoutParams(shellParams);
            shellDrawable.setRotation(new Random().nextInt(360));
            button.addView(shellDrawable, shellParams);

        }
    }

    public static int getGameType() {
        return gameType;
    }

}
