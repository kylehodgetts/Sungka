package com.kylehodgetts.sunka.controller;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.kylehodgetts.sunka.R;
import com.kylehodgetts.sunka.controller.bus.Event;
import com.kylehodgetts.sunka.controller.bus.EventBus;
import com.kylehodgetts.sunka.controller.bus.EventHandler;
import com.kylehodgetts.sunka.event.EndGame;
import com.kylehodgetts.sunka.event.HighLightTray;
import com.kylehodgetts.sunka.event.NewGame;
import com.kylehodgetts.sunka.model.Board;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.util.Tuple2;


/**
 *
 * @author Charlie Baker
 * @version 1.0
 * V1.2 Phileas Hocquard
 */
public class ViewManager extends EventHandler<GameState> {

    private EventBus<GameState> bus;
    private Activity activity;


    public ViewManager(EventBus<GameState> bus, Activity activity) {
        super("ViewManager");
        this.bus = bus;
        this.activity = activity;
    }

    @Override
    public Tuple2<GameState, Boolean> handleEvent(Event event, GameState state) {
        if(event instanceof HighLightTray) {
            highlightTray(event);
            return new Tuple2<>(state, false);
        }

        if (event instanceof EndGame){
            flipLayout(state);
        }

        return new Tuple2<>(state, false); //default case to make the eventBus not do anything
    }

    /**
     * Method to Flip Between the boardLayout and the GameOver Layout
     * (This avoid loading a new intent of Activity. )
     */
    public void flipLayout(final GameState state){

        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                ViewFlipper vf = (ViewFlipper) activity.findViewById(R.id.viewFlipper);
                vf.showNext();
                TextView leftScore = (TextView) activity.findViewById(R.id.your_score);
                TextView rightScore = (TextView) activity.findViewById(R.id.opponent_score);
                leftScore.setText(""+state.getPlayer2().getWonGames());
                rightScore.setText(""+state.getPlayer1().getWonGames());

                 Button restart = (Button) activity.findViewById(R.id.bAgain);
                restart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bus.feedEvent(new NewGame());
                        flipLayout(state);
                    }

                });

            }

           });
        }


    private void highlightTray(final Event event) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int player = ((HighLightTray) event).getPlayer();
                int tray = ((HighLightTray) event).getTray();
                int currentPlayersTurn = ((HighLightTray) event).getCurrentPlayersTurn();

                LinearLayout linearLayout = (LinearLayout) activity.findViewById(Integer.parseInt(player+""+tray));
                ImageButton imageButton = (ImageButton) linearLayout.findViewById(R.id.button);
                GradientDrawable drawable = (GradientDrawable) imageButton.getBackground().getConstantState().newDrawable().mutate();

                drawable.setStroke(8, currentPlayersTurn == 0 ? Color.parseColor("#C4213C") : Color.parseColor("#4CACC4"));
                imageButton.setBackground(drawable);
                imageButton.setPadding(35,35,35,35);
            }
        });
    }

    @Override
    public void updateView(final GameState state, final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayout storeOne = (LinearLayout) activity.findViewById(R.id.playerBStore);
                ImageButton playerOneStoreButton = (ImageButton) storeOne.findViewById(R.id.buttonbs);
                TextView tvPlayerAStoreCount = (TextView) activity.findViewById(R.id.tvPlayerBStoreCount);
                tvPlayerAStoreCount.setText(Integer.toString(state.getPlayer2().getStonesInPot()));
                int playerAStoreShellCount = state.getPlayer1().getStonesInPot();
                if (playerAStoreShellCount < 9) {
                    playerOneStoreButton.setImageResource(activity.getResources().getIdentifier("s" + playerAStoreShellCount, "drawable", activity.getPackageName()));
                } else {
                    playerOneStoreButton.setImageResource(activity.getResources().getIdentifier("s9", "drawable", activity.getPackageName()));
                }

                LinearLayout storeTwo = (LinearLayout) activity.findViewById(R.id.playerAStore);
                ImageButton playerTwoStoreButton = (ImageButton) storeTwo.findViewById(R.id.buttonas);
                TextView tvPlayerBStoreCount = (TextView) activity.findViewById(R.id.tvPlayerAStoreCount);
                tvPlayerBStoreCount.setText(Integer.toString(state.getPlayer1().getStonesInPot()));
                int playerBStoreShellCount = state.getPlayer2().getStonesInPot();
                if (playerBStoreShellCount < 9) {
                    playerTwoStoreButton.setImageResource(activity.getResources().getIdentifier("s" + playerBStoreShellCount, "drawable", activity.getPackageName()));
                } else {
                    playerTwoStoreButton.setImageResource(activity.getResources().getIdentifier("s9", "drawable", activity.getPackageName()));
                }

                Board currentBoard = state.getBoard();
                for (int row = 0; row < 2; ++row) {
                    for (int column = 0; column < 7; ++column) {
                        LinearLayout linearLayout = (LinearLayout) activity.findViewById(Integer.parseInt(row + "" + column));
                        TextView textView = (TextView) linearLayout.findViewById(R.id.tv);
                        textView.setText(Integer.toString(currentBoard.getTray(row, column)));
                        ImageButton button = (ImageButton) linearLayout.findViewById(R.id.button);
                        button.setPadding(35, 35, 35, 35); // In order to make the image scale
                        int currentTrayShellCount = currentBoard.getTray(row, column);
                        if (currentTrayShellCount < 9) {
                            button.setImageResource(activity.getResources().getIdentifier("s" + currentTrayShellCount, "drawable", activity.getPackageName()));
                        } else {
                            button.setImageResource(activity.getResources().getIdentifier("s9", "drawable", activity.getPackageName()));
                        }
                    }
                }

                selectPlayer(activity, state, state.getCurrentPlayerIndex());
            }
        });
    }


    /**
     * Method to show the user whose turn it is by highlighting their trays that are currently
     * touchable/clickable. It also rotates the text and numbers to the correct player's view.
     *
     * @param activity    The Board activity to be updated
     * @param state       The current GameState
     * @param playersTurn Current player's turn integer
     */
    public void selectPlayer(Activity activity, GameState state, int playersTurn) {
        LinearLayout playerAStore = (LinearLayout) activity.findViewById(R.id.playerAStore);
        LinearLayout playerBStore = (LinearLayout) activity.findViewById(R.id.playerBStore);
        TextView tvPlayerA = (TextView) activity.findViewById(R.id.tvPlayerA);
        TextView tvPlayerB = (TextView) activity.findViewById(R.id.tvPlayerB);


        for (int player = 0; player < 2; ++player) {
            for (int tray = 0; tray < 7; ++tray) {
                LinearLayout linearLayout = (LinearLayout) activity.findViewById(Integer.parseInt(player + "" + tray));
                ImageButton imageButton = (ImageButton) linearLayout.findViewById(R.id.button);
                imageButton.setBackgroundResource(player == 0 ? R.drawable.buttonb : R.drawable.buttona);
                imageButton.setPadding(35, 35, 35, 35);

                if (!state.isInitialising()) {
                    TextView tv = (TextView) linearLayout.findViewById(R.id.tv);
                    int angle = playersTurn == 1 ? 180 : 0;
                    tv.setRotation(angle);
                    tvPlayerA.setRotation(angle);
                    tvPlayerB.setRotation(angle);
                }
            }
        }

        if (state.isInitialising()) {
            tvPlayerB.setBackgroundColor(Color.parseColor("#2D8BA8"));
            tvPlayerA.setBackgroundColor(Color.parseColor("#A84136"));
            tvPlayerB.setTextColor(Color.WHITE);
            tvPlayerA.setTextColor(Color.WHITE);
            return;
        } else if (playersTurn == 0) {
            playerAStore.setBackgroundResource(R.drawable.buttonstoretransparent);
            playerBStore.setBackgroundResource(R.drawable.buttonbscore);
            tvPlayerB.setBackgroundColor(Color.TRANSPARENT);
            tvPlayerB.setTextColor(Color.BLACK);
            tvPlayerA.setBackgroundColor(Color.parseColor("#A84136"));
            tvPlayerA.setTextColor(Color.WHITE);
        } else if (playersTurn == 1) {
            playerBStore.setBackgroundResource(R.drawable.buttonstoretransparent);
            playerAStore.setBackgroundResource(R.drawable.buttonascore);
            tvPlayerB.setBackgroundColor(Color.parseColor("#2D8BA8"));
            tvPlayerA.setBackgroundColor(Color.TRANSPARENT);
            tvPlayerB.setTextColor(Color.WHITE);
            tvPlayerA.setTextColor(Color.BLACK);
        }

        for (int tray = 0; tray < 7; ++tray) {
            LinearLayout linearLayout = (LinearLayout) activity.findViewById(Integer.parseInt((playersTurn == 0 ? "1" : "0") + "" + tray));
            ImageButton imageButton = (ImageButton) linearLayout.findViewById(R.id.button);
            imageButton.setBackgroundResource(R.drawable.buttontransparent);
            imageButton.setPadding(35, 35, 35, 35);
        }
    }

}
