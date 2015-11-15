package com.kylehodgetts.sunka.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kylehodgetts.sunka.view.BoardActivity;
import com.kylehodgetts.sunka.R;
import com.kylehodgetts.sunka.controller.bus.Event;
import com.kylehodgetts.sunka.controller.bus.EventBus;
import com.kylehodgetts.sunka.controller.bus.EventHandler;
import com.kylehodgetts.sunka.event.EndGame;
import com.kylehodgetts.sunka.event.ShellMovement;
import com.kylehodgetts.sunka.event.ShellMovementToPot;
import com.kylehodgetts.sunka.event.ShellSteal;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.uiutil.ShellDrawable;
import com.kylehodgetts.sunka.util.FileUtility;
import com.kylehodgetts.sunka.util.Tuple2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Class to handle all the animations based on the game events received by the {@link EventBus}
 * to display these to the user.
 *
 * @author Charlie Baker
 * @version 1.2
 */
public class AnimationManager extends EventHandler<GameState> {

    /**
     * {@link EventBus} of the game that passes to the Game {@link Event}'s to this {@link EventHandler}
     */
    private EventBus<GameState> bus;

    /**
     * {@link Activity} relating to the {@link BoardActivity} of the main game where the view needs
     * to be rendered.
     */
    private Activity activity;


    /**
     * Default constructor to create an instance of the {@link EventHandler} to display animations
     * based on {@link GameState} {@link Event}'s
     *
     * @param bus {@link EventBus} of the Game
     * @param activity {@link Activity} referring to the Activity to be updated
     */
    public AnimationManager(EventBus<GameState> bus, Activity activity) {
        super("AnimationManager");
        this.bus = bus;
        this.activity = activity;
    }

    /**
     * Determine's what {@link Event} has been received through the {@link EventBus} to this {@link EventHandler}
     * @param event The incoming event from the bus to this handler
     * @param state The current state of the bus
     * @return
     */
    @Override
    public Tuple2<GameState, Boolean> handleEvent(Event event, GameState state) {
        if(event instanceof ShellMovement) {
            return new Tuple2<>(animateShells(state, (ShellMovement) event), false);
        }
        else if(event instanceof ShellMovementToPot) {
            return new Tuple2<>(animateShellsToStore(state, (ShellMovementToPot) event), false);
        }
        else if(event instanceof ShellSteal) {
            return new Tuple2<>(animateStealShells(state, (ShellSteal) event), false);
        }else if(event instanceof EndGame) {
            return new Tuple2<>(endOfGame(state, (EndGame) event), false);
        }
        return new Tuple2<>(state, false);
    }

    /**
     * Method that removes all {@link ShellDrawable} objects from the board
     * @param state current {@link GameState}
     * @param event current {@link Event}
     * @return new {@link GameState}
     */
    private GameState endOfGame(GameState state, EndGame event) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RelativeLayout relativeLayout = (RelativeLayout) activity.findViewById(R.id.relativeLayout);
                for(int i=0; i < relativeLayout.getChildCount(); ++i) {
                    View child = relativeLayout.getChildAt(i);
                    if(child instanceof ShellDrawable) { relativeLayout.removeView(child); }
                }
                RelativeLayout playerAStore = (RelativeLayout) activity.findViewById(R.id.buttonbs);
                playerAStore.removeAllViews();

                RelativeLayout playerBStore = (RelativeLayout) activity.findViewById(R.id.buttonas);
                playerBStore.removeAllViews();

                ((BoardActivity) activity).initialiseShellAllocations();
                ((BoardActivity) activity).setAreShellsCreated(false);
            }
        });
        return state;
    }

    /**
     * Performs the animation to simulate the capture rule when the player steals their opponent's
     * shells.
     *
     * @param state current {@link GameState} of the game.
     * @param event {@link ShellSteal} {@link Event} that has been received
     * @return {@link GameState} of the game
     */
    private GameState animateStealShells(final GameState state, final ShellSteal event) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int fromTray = event.getPlayersTray();
                int trayToStealFrom = event.getTrayToStealFrom();
                int player = event.getPlayer();

                LinearLayout fromTrayLayout = (LinearLayout) activity.findViewById(Integer.parseInt(player+""+fromTray));
                int width = fromTrayLayout.getWidth();

                LinearLayout playerStore = (LinearLayout) activity.findViewById(R.id.playerAStore);
                int storeWidth = playerStore.getWidth();

                // Map storing arrays of shell allocations for each tray
                HashMap<Integer, ArrayList<ShellDrawable>> shellAllocations = ((BoardActivity) activity).getShellAllocations();
                ArrayList<ShellDrawable> fromTrayArray = shellAllocations.get(Integer.parseInt(player + "" + fromTray));
                ArrayList<ShellDrawable> trayToStealFromArray = shellAllocations.get(Integer.parseInt(((player+1)%2)+""+trayToStealFrom));


                ArrayList<ShellDrawable> playerStoreArray;
                if (player == 0) {
                    playerStoreArray = shellAllocations.get(BoardActivity.PLAYER_A_STORE);
                }
                else {
                    playerStoreArray = shellAllocations.get(BoardActivity.PLAYER_B_STORE);
                }

                if(trayToStealFromArray.size() >= 10) { FileUtility.playSound(activity, R.raw.evil_laugh); }
                else { FileUtility.playSound(activity, R.raw.short_laugh); }

                // Removes & animates all shells from the opponent's tray to the player's store
                for (ShellDrawable shellDrawable : trayToStealFromArray) {
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(shellDrawable, "translationX", (player == 0 ? (width * (6 - fromTray)) + storeWidth : -((width * (6 - fromTray))) - storeWidth));
                    objectAnimator.setDuration(290);
                    objectAnimator.start();
                    playerStoreArray.add(shellDrawable);
                }

                // Removes & animates all shells from the current player's tray always being 1 shell
                for (ShellDrawable shellDrawable : fromTrayArray) {
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(shellDrawable, "translationX", (player == 0 ? (width * (6 - fromTray)) + storeWidth : -((width * (6 - fromTray))) - storeWidth));
                    objectAnimator.setDuration(290);
                    objectAnimator.start();
                    playerStoreArray.add(shellDrawable);
                }

                // Clears both arrays as all the shells are now in the store
                fromTrayArray.clear();
                trayToStealFromArray.clear();
            }
        });


        return state;
    }

    /**
     * Animate's the shells normally around the board based on the current {@link ShellMovement} {@link Event}
     * received from the {@link EventBus} to this {@link EventHandler}
     *
     * @param state current {@link GameState} of the game
     * @param event current {@link ShellMovement} {@link Event}
     * @return the current {@link GameState}
     */
    private GameState animateShells(final GameState state, final ShellMovement event) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int shellsLeft = event.getShellsStillToBePlaced();
                int toTray = event.getTrayIndex();
                int fromTray = toTray == 0? 6 : toTray - 1;
                int player = event.getPlayerIndex();

                HashMap<Integer, ArrayList<ShellDrawable>> shellAllocations = ((BoardActivity) activity).getShellAllocations();

                final LinearLayout fromTrayLayout = (LinearLayout) activity.findViewById(Integer.parseInt((toTray == 0? (player+1) % 2 : player)+""+fromTray));
                int buttonWidth = fromTrayLayout.getWidth();

                ArrayList<ShellDrawable> fromTrayArray = shellAllocations.get(Integer.parseInt((toTray == 0 ? (player + 1) % 2 : player) + "" + fromTray));
                ArrayList<ShellDrawable> toTrayArray = shellAllocations.get(Integer.parseInt(event.getPlayerIndex()+""+toTray));

                // Moves all the shells left to distribute to the next tray
                for(int i=0; i < shellsLeft; ++i) {
                    final ShellDrawable currentShell = fromTrayArray.get(fromTrayArray.size() > 0? fromTrayArray.size()-1 : 0);
                    final ShellDrawable shellCopy = new ShellDrawable(activity, currentShell.getShellX(), currentShell.getShellY(), currentShell.getShellWidth(), currentShell.getLength());
                    shellCopy.setColour(currentShell.getColour());
                    shellCopy.setRotation(currentShell.getRotation());

                    LinearLayout trayLayout = (LinearLayout) activity.findViewById(Integer.parseInt(event.getPlayerIndex()+""+toTray));
                    final RelativeLayout newTrayButton = (RelativeLayout) trayLayout.findViewById(R.id.button);

                    currentShell.bringToFront();

                    ObjectAnimator objectAnimator;
                    if(toTray == 0) { // Moves the shells up of down to the next player's row should the next tray be on the other player's side
                        objectAnimator = ObjectAnimator.ofFloat(currentShell, "translationY", player == 1? -buttonWidth : buttonWidth);
                    }
                    else { // Moves the shells along on the player's own side
                        objectAnimator = ObjectAnimator.ofFloat(currentShell, "translationX", player == 1? -buttonWidth : buttonWidth);
                    }
                    objectAnimator.setDuration(290);
                    objectAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            // Moves and reallocates the shells as needed
                            RelativeLayout previousTray = (RelativeLayout) fromTrayLayout.findViewById(R.id.button);
                            previousTray.removeView(currentShell);
                            newTrayButton.addView(shellCopy, currentShell.getLayoutParams());
                        }
                    });
                    objectAnimator.start();
                    toTrayArray.add(shellCopy);
                    shellCopy.bringToFront();
                    fromTrayArray.remove(currentShell);
                }
                shellAllocations.put(Integer.parseInt(player + "" + toTray), toTrayArray);
                boolean played = FileUtility.playSound(activity, R.raw.blop);
            }
        });
        return state;
    }

    /**
     * Animates a shell to the player's store should a {@link ShellMovementToPot} {@link Event}
     * be received to this {@link EventHandler} from the {@link EventBus}
     *
     * @param state current {@link GameState} of the game
     * @param event {@link ShellMovementToPot} {@link Event} received to this handler
     * @return {@link GameState} of the game
     */
    private GameState animateShellsToStore(final GameState state, final ShellMovementToPot event) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final int player = event.getPlayerIndexOfThisPot();
                final int tray = 6;
                final HashMap<Integer, ArrayList<ShellDrawable>> shellAllocations = ((BoardActivity) activity).getShellAllocations();

                LinearLayout playerStoreLayout;
                if(player == 0) {
                    playerStoreLayout = (LinearLayout) activity.findViewById(R.id.playerBStore);
                }
                else {
                    playerStoreLayout = (LinearLayout) activity.findViewById(R.id.playerAStore);
                }

                ArrayList<ShellDrawable> trayArray = shellAllocations.get(Integer.parseInt(player+""+tray));
                final LinearLayout fromTrayLayout = (LinearLayout) activity.findViewById(Integer.parseInt(player+""+tray));



                Random random = new Random();
                final ShellDrawable shell = trayArray.get(trayArray.size() - 1);
                shell.bringToFront();

                // Animates to the correct position based on which store the player's shells are going to
                ObjectAnimator animateX = ObjectAnimator.ofFloat(shell, "translationX", player == 1? -playerStoreLayout.getWidth() : playerStoreLayout.getWidth());
                ObjectAnimator animateY = ObjectAnimator.ofFloat(shell, "translationY", player == 1? (-random.nextFloat() + playerStoreLayout.getHeight()/2)/2 : (random.nextFloat() - playerStoreLayout.getHeight()/2)/2);
                animateX.setDuration(290);
                animateY.setDuration(290);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(animateX).with(animateY);
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<ShellDrawable> shellArray = shellAllocations.get(Integer.parseInt(player + "" + tray));
                                shellArray.remove(shell);
                                fromTrayLayout.removeView(shell);
                                ArrayList<ShellDrawable> playerStore;
                                if (player == 0) {
                                    playerStore = shellAllocations.get(BoardActivity.PLAYER_A_STORE);
                                }
                                else {
                                    playerStore = shellAllocations.get(BoardActivity.PLAYER_B_STORE);
                                }
                                playerStore.add(shell);
                            }
                        });
                    }
                });
                animatorSet.start();
                FileUtility.playSound(activity, R.raw.blop);

                ArrayList<ShellDrawable> shellArray = shellAllocations.get(Integer.parseInt(player+""+tray));
                shellArray.remove(shell);
            }
        });
        return state;
    }

    @Override
    public void updateView(GameState state, Activity activity) { }

}
