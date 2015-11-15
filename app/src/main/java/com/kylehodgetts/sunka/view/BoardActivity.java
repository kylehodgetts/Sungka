package com.kylehodgetts.sunka.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kylehodgetts.sunka.controller.AIManager;
import com.kylehodgetts.sunka.controller.AnimationManager;
import com.kylehodgetts.sunka.controller.GameManager;
import com.kylehodgetts.sunka.controller.OnlineGameManager;
import com.kylehodgetts.sunka.controller.ViewManager;
import com.kylehodgetts.sunka.controller.StatisticsCollector;
import com.kylehodgetts.sunka.controller.bus.EventBus;
import com.kylehodgetts.sunka.event.NewGame;
import com.kylehodgetts.sunka.event.TrayOnClickListener;
import com.kylehodgetts.sunka.model.Board;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.model.Player;
import com.kylehodgetts.sunka.uiutil.Fonts;
import com.kylehodgetts.sunka.uiutil.ShellDrawable;
import com.kylehodgetts.sunka.util.FileUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Board Activity Class used to create the game board. Inflate and style the board to the user's
 * device screen. Also to instantiate the logic objects to control and handle the game events.
 *
 * @author Phileas Hocquard
 * @author Charlie Baker
 * @author Jonathan Burton
 * @author Kyle Hodgetts
 * @version 1.8
 */
public class BoardActivity extends AppCompatActivity {

    private static int gameType;

    public static final int ONEPLAYER = 1;
    public static final int TWOPLAYER = 2;
    public static final int ONLINE = 3;

    public static final String EXTRA_INT = "com.kylehodgetts.sunka.boardactivity.gametype";
    private static final String FILE_NAME = "sungkasave";

    public static final int PLAYER_A_STORE = 20;
    public static final int PLAYER_B_STORE = 21;

    private View decorView;
    private boolean areShellsCreated;
    private GameState state;

    private HashMap<Integer, ArrayList<ShellDrawable>> shellAllocations;

    private TextView txtYourScoreLabel;
    private TextView txtYourScore;
    private Button btnReturnToMainMenu;
    private TextView txtTheirScoreLabel;
    private TextView txtTheirScore;


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
        txtYourScoreLabel = (TextView) findViewById(R.id.your_score_label);
        txtYourScoreLabel.setTypeface(Fonts.getButtonFont(this));
        txtYourScoreLabel.setTextColor(ContextCompat.getColor(this, R.color.white));
        txtYourScore = (TextView) findViewById(R.id.your_score);
        txtYourScore.setTypeface(Fonts.getButtonFont(this));
        txtYourScore.setTextColor(ContextCompat.getColor(this, R.color.white));
        txtTheirScoreLabel = (TextView) findViewById(R.id.opponent_score_label);
        txtTheirScoreLabel.setTypeface(Fonts.getButtonFont(this));
        txtTheirScoreLabel.setTextColor(ContextCompat.getColor(this, R.color.white));
        txtTheirScore = (TextView) findViewById(R.id.opponent_score);
        txtTheirScore.setTypeface(Fonts.getButtonFont(this));
        txtTheirScore.setTextColor(ContextCompat.getColor(this, R.color.white));
        btnReturnToMainMenu = (Button) findViewById(R.id.bMenu);
        btnReturnToMainMenu.setTypeface(Fonts.getButtonFont(this));
        btnReturnToMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMainMenu();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setContentView(R.layout.activity_board);
        }
        else { // uses another XML layout should the Android version be less than Lollipop using Linear Layouts
            this.setContentView(R.layout.activity_board_prev_compatable);
        }

        gameType = getIntent().getIntExtra(EXTRA_INT, 0);
        state = (GameState) FileUtility.readFromSaveFile(this, FILE_NAME);
        if(state == null) {
            state = new GameState(new Board(), new Player(), new Player());
        }

        EventBus<GameState> bus = new EventBus<>(state, this);
        bus.registerHandler(new GameManager(bus));
        bus.registerHandler(new ViewManager(bus, this));
        bus.registerHandler(new AnimationManager(bus, this));
        bus.registerHandler(new StatisticsCollector(this,getSharedPreferences(MainActivity.PREFERENCES, 0).getString(MainActivity.SERVER_ID,null)));

        if (gameType == ONEPLAYER) {
            bus.registerHandler(new AIManager(bus));
            makeXMLButtons(bus, false);
        } else if (gameType == TWOPLAYER) {
            makeXMLButtons(bus, true);
        } else if (gameType == ONLINE) {
            bus.registerHandler(new OnlineGameManager(bus));
            makeXMLButtons(bus, false);
        }
        bus.registerHandler(new GameManager(bus));
        bus.registerHandler(new ViewManager(bus, this));
        bus.registerHandler(new AnimationManager(bus, this));
        bus.feedEvent(new NewGame());

        setupReturnToMenuButton();
    }

    /**
     * Set's the {@link android.view.View.OnClickListener} for the return to main menu button after a
     * game has finished.
     *
     */
    private void setupReturnToMenuButton() {
        Button mainMenuButton = (Button) findViewById(R.id.bMenu);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMainMenu();
            }
        });
    }

    /**
     * Triggers on the device's system back key being pressed
     * @param keyCode   Key Code
     * @param event     Key Event
     * @return          onKeyDown event
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setMessage("Will you want to return to this game?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            FileUtility.saveGame(BoardActivity.this, FILE_NAME, state);
                            returnToMainMenu();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            returnToMainMenu();
                        }
                    })
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    })
                    .show();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Method used to inflate the relevant trays in the correct place on the game activity board.
     * This method also attaches the necessary onClickListener to each tray.
     *
     * @param bus The game Event Bus so the tray can be attached with the onClickListener
     */
    private void makeXMLButtons(EventBus bus, boolean bothSetsButtonsClickable) {

        /*
           Set's up the grid layout in the event that the device Android version is Lollipop or greater
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            GridLayout gridlayout = (GridLayout) findViewById(R.id.gridLayout);

            for (int i = 1; i >= 0; --i) {
                for (int j = 6; j >= 0; --j) {
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
                    param.columnSpec = GridLayout.spec(i == 1 ? 6 - j : j, 1f);
                    param.rowSpec = GridLayout.spec((i + 1) % 2, 1f);
                    param.width = GridLayout.LayoutParams.WRAP_CONTENT;
                    param.height = GridLayout.LayoutParams.WRAP_CONTENT;
                    param.setGravity(Gravity.FILL_HORIZONTAL);
                    linearLayout.setLayoutParams(param);
                    gridlayout.addView(linearLayout);

                    //we don't want the opposite side clickable if there are not two local players
                    if (i == 0 || i == 1 && bothSetsButtonsClickable) {
                        button.setOnClickListener(new TrayOnClickListener(i, j, bus));
                    }
                }
            }
        }
        else { // Set's up the Linear Layout's for previous Android versions
            LinearLayout topRow = (LinearLayout) findViewById(R.id.topRow);
            LinearLayout bottomRow = (LinearLayout) findViewById(R.id.bottomRow);

            for(int i=0; i < 2; ++i) {
                for(int j=0; j < 7; ++j) {
                    LinearLayout trayButtonLayout;
                    if(i == 0) {
                        trayButtonLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.buttonlayoutb, bottomRow, false);
                    }
                    else {
                        trayButtonLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.buttonlayouta, topRow, false);
                    }
                    trayButtonLayout.setId(Integer.parseInt((i==0?1:0)+""+(i==0?6-j:j)));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    params.gravity = Gravity.FILL_HORIZONTAL;
                    params.weight = 1;
                    trayButtonLayout.setLayoutParams(params);
                    if(i==0) { topRow.addView(trayButtonLayout); }
                    else { bottomRow.addView(trayButtonLayout); }

                    RelativeLayout trayButton = (RelativeLayout) trayButtonLayout.findViewById(R.id.button);
                    if(i == 1 || i == 0 && bothSetsButtonsClickable) {
                        trayButton.setOnClickListener(new TrayOnClickListener(i==0?1:0, i==0? 6-j:j, bus));
                    }
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

            // Resizes and creates the new shells in the case that the Android version is greater than Lollipop
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);

                for (int i = 0; i < gridLayout.getChildCount(); ++i) {
                    LinearLayout child = (LinearLayout) gridLayout.getChildAt(i);
                    int width = gridLayout.getChildAt(i).getWidth();
                    GridLayout.LayoutParams llparams = (GridLayout.LayoutParams) child.getLayoutParams();
                    llparams.width = width;
                    llparams.height = width + child.findViewById(R.id.tv).getHeight();
                    gridLayout.getChildAt(i).setLayoutParams(llparams);

                    // Creates shells if they have not already been created on the board
                    if (!areShellsCreated) {
                        createShells((RelativeLayout) gridLayout.getChildAt(i).findViewById(R.id.button));
                        initialiseShellAllocations();
                    }
                }
                if(!areShellsCreated) {
                    createShells((RelativeLayout) findViewById(R.id.buttonas));
                    createShells((RelativeLayout) findViewById(R.id.buttonbs));
                }

            }
            else { // resizes all elements for previous android versions and creates the Shells
                LinearLayout topRow = (LinearLayout) findViewById(R.id.topRow);
                LinearLayout trayButtonLayout = (LinearLayout) topRow.getChildAt(0);
                LinearLayout.LayoutParams topRowParams = (LinearLayout.LayoutParams) topRow.getLayoutParams();
                topRowParams.height = trayButtonLayout.getWidth() + trayButtonLayout.findViewById(R.id.tv).getHeight();
                topRow.setLayoutParams(topRowParams);

                LinearLayout bottomRow = (LinearLayout) findViewById(R.id.bottomRow);
                LinearLayout.LayoutParams bottomRowParams = (LinearLayout.LayoutParams) bottomRow.getLayoutParams();
                bottomRowParams.height = topRowParams.height;
                bottomRow.setLayoutParams(bottomRowParams);

                if(!areShellsCreated) { // creates the shells in the correct order to be visible
                    for (int i = 6; i >= 0; --i) {
                        LinearLayout currentTrayButtonLayout = (LinearLayout) topRow.getChildAt(i);
                        RelativeLayout currentTrayButton = (RelativeLayout) currentTrayButtonLayout.findViewById(R.id.button);
                        TextView textView = (TextView) currentTrayButtonLayout.findViewById(R.id.tv);
                        textView.bringToFront();
                        currentTrayButtonLayout.getLayoutParams().width = 0;
                        createShells(currentTrayButton);
                        initialiseShellAllocations();
                    }
                    for (int j = 0; j < 7; ++j) { // creates the shells in the correct order to be visible
                        LinearLayout currentTrayButtonLayout = (LinearLayout) bottomRow.getChildAt(j);
                        RelativeLayout currentTrayButton = (RelativeLayout) currentTrayButtonLayout.findViewById(R.id.button);
                        currentTrayButton.bringToFront();
                        currentTrayButton.setAlpha(0.7f);
                        currentTrayButtonLayout.getLayoutParams().width = 0;
                        createShells(currentTrayButton);
                        initialiseShellAllocations();
                    }
                    createShells((RelativeLayout) findViewById(R.id.buttonas));
                    createShells((RelativeLayout) findViewById(R.id.buttonbs));
                }
            }
            areShellsCreated = true;
        }
    }

    /**
     * Method that allows us to return to the main menu .
     */
    public void returnToMainMenu() {
        Intent intent = new Intent(BoardActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     *
     * @return
     *          1: Player is playing against an AI player,
     *          2: Player is playing head to head with another player
     *          3: Player is playing with another player over WiFi
     */
    public static int getGameType() {
        return gameType;
    }

    /**
     * Creates the shell drawable views for each tray and adds them to the relevant parent being their
     * currently allocated tray for the current game state.
     *
     * @param button indicating the tray button the shells are to be assigned to
     */
    public void createShells(RelativeLayout button) {
        Random random = new Random();
        LinearLayout buttonParent = (LinearLayout) button.getParent();

        int numberOfShells = 0;
        if(button.getId() == R.id.buttonas) {
            numberOfShells = state.getPlayer2().getStonesInPot();
        }
        else if(button.getId() == R.id.buttonbs) {
            numberOfShells = state.getPlayer1().getStonesInPot();
        }
        else {
            numberOfShells = state.getBoard().getTray(buttonParent.getId()/10, buttonParent.getId()%10);
        }

        for(int shell=0; shell < numberOfShells; ++shell) {
            int shellWidth = 40;
            int shellLength = 20;

            ShellDrawable shellDrawable = new ShellDrawable(this, random.nextInt(button.getWidth()/2),
                    random.nextInt(button.getHeight()/2), shellWidth, shellLength);
            shellDrawable.setRandomColour();
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

    /**
     * Initialises the HashMap containing mappings to ArrayLists holding the allocatation of each
     * shell to it's currently allocated tray based on the current game state.
     *
     */
    public void initialiseShellAllocations() {
        shellAllocations = new HashMap<>();
        for(int player=0; player < 2; ++player) {
            for(int tray=0; tray < 7; ++tray) {
                ArrayList<ShellDrawable> shellArrayList = new ArrayList<>();
                shellAllocations.put(Integer.parseInt(player+""+tray), shellArrayList);

                LinearLayout trayLinearLayout = (LinearLayout) findViewById(Integer.parseInt(player+""+tray));
                RelativeLayout trayButton = (RelativeLayout) trayLinearLayout.findViewById(R.id.button);

                int numberOfShells = state.getBoard().getTray(player, tray);
                for(int shell=0; shell < numberOfShells; ++shell) {
                    shellArrayList.add((ShellDrawable) trayButton.getChildAt(shell));
                }
            }
        }
        shellAllocations.put(PLAYER_A_STORE, new ArrayList<ShellDrawable>());
        shellAllocations.put(PLAYER_B_STORE, new ArrayList<ShellDrawable>());
    }

    /**
     * Get's the Map referencing the array lists of all the allocations of Shells
     * @return {@link HashMap}
     */
    public HashMap<Integer, ArrayList<ShellDrawable>> getShellAllocations() { return shellAllocations; }

    /**
     * Set's the boolean to tell the Activity if the shells need to be created on the board again
     * @param newValue new boolean value
     */
    public void setAreShellsCreated(boolean newValue) { areShellsCreated = newValue; }
}
