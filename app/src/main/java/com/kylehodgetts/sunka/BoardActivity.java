package com.kylehodgetts.sunka;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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

    //TODO: Implement OnPause, OnResume, OnStop methods. And within all other necessary classes
    public static final int ONEPLAYER = 1;
    public static final int TWOPLAYER = 2;
    public static final int ONLINE = 3;

    public static final String EXTRA_INT = "com.kylehodgetts.sunka.boardactivity.gametype";

    View decorView;

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

        this.setContentView(R.layout.activity_board);

        int gameType = getIntent().getIntExtra(EXTRA_INT, 0);

        GameState state = new GameState(new Board(), new Player(), new Player());
        EventBus<GameState> bus = new EventBus<>(state, this);
        bus.registerHandler(new GameManager(bus));
        bus.registerHandler(new ViewManager(bus, this));
        if (gameType == ONEPLAYER) {
            bus.registerHandler(new AIManager(bus));
            makeXMLButtons(bus, false);
        } else if (gameType == TWOPLAYER) {
            makeXMLButtons(bus, true);
        } else if (gameType == ONLINE) {
            //TODO bus.registerHandler(ONLINEHANDLER)
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

        for (int player = 0; player < 2; ++player) {
            for (int tray = 0; tray < 7; ++tray) {
                final LinearLayout linearLayout;
                if (player == 0) {
                    linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.buttonlayoutb, gridlayout, false);
                } else {
                    linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.buttonlayouta, gridlayout, false);
                }
                linearLayout.setId(Integer.parseInt(player + "" + tray));

                ImageButton button = (ImageButton) linearLayout.findViewById(R.id.button);

                // Due to grid layout weights only being available from API 21 onwards to scale the layout
                GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    param.columnSpec = GridLayout.spec(player == 1 ? 6 - tray : tray, 2f);
                    param.rowSpec = GridLayout.spec((player + 1) % 2, 2f);

                }
                param.width = GridLayout.LayoutParams.WRAP_CONTENT;
                param.height = GridLayout.LayoutParams.WRAP_CONTENT;
                param.setGravity(Gravity.FILL_HORIZONTAL);
                linearLayout.setLayoutParams(param);
                gridlayout.addView(linearLayout);
                
                //we don't want the opposite side clickable if there are not two local players
                if (player == 0 || player == 1 && bothSetsButtonsClickable) {
                    button.setOnClickListener(new TrayOnClickListener(tray, player, bus));
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

            for (int i = 0; i < gridLayout.getChildCount(); ++i) {
                int width = gridLayout.getChildAt(i).getWidth();
                gridLayout.getChildAt(i).setMinimumHeight(width);
            }
        }
    }

    /**
     * Method that allows us to return to the main menu .
     */
    public void returnToMainMenu(View view) {
        Intent intent = new Intent(BoardActivity.this, MainActivity.class);
        BoardActivity.this.startActivity(intent);
    }

}
