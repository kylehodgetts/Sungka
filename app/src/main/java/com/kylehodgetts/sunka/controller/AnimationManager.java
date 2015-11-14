package com.kylehodgetts.sunka.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kylehodgetts.sunka.BoardActivity;
import com.kylehodgetts.sunka.R;
import com.kylehodgetts.sunka.controller.bus.Event;
import com.kylehodgetts.sunka.controller.bus.EventBus;
import com.kylehodgetts.sunka.controller.bus.EventHandler;
import com.kylehodgetts.sunka.event.ShellMovement;
import com.kylehodgetts.sunka.event.ShellMovementToPot;
import com.kylehodgetts.sunka.event.ShellSteal;
import com.kylehodgetts.sunka.event.ShellStoreAnimation;
import com.kylehodgetts.sunka.event.ShellTrayAnimation;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.model.ShellReference;
import com.kylehodgetts.sunka.uiutil.ShellDrawable;
import com.kylehodgetts.sunka.util.Tuple2;

import java.io.LineNumberReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 *
 * @author Charlie Baker
 * @version 1.0
 */
public class AnimationManager extends EventHandler<GameState> {

    private EventBus<GameState> bus;
    private Activity activity;


    public AnimationManager(EventBus<GameState> bus, Activity activity) {
        super("AnimationManager");
        this.bus = bus;
        this.activity = activity;
    }

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
        }

        return new Tuple2<>(state, false);
    }

    private GameState animateStealShells(GameState state, final ShellSteal event) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("animateStealShells()", "called");
                int fromTray = event.getPlayersTray();
                int trayToStealFrom = event.getTrayToStealFrom();
                int player = event.getPlayer();

                LinearLayout fromTrayLayout = (LinearLayout) activity.findViewById(Integer.parseInt(player+""+fromTray));
                int width = fromTrayLayout.getWidth();

                LinearLayout playerStore = (LinearLayout) activity.findViewById(R.id.playerAStore);
                int storeWidth = playerStore.getWidth();

                HashMap<Integer, ArrayList<ShellDrawable>> shellAllocations = ((BoardActivity) activity).getShellAllocations();
                ArrayList<ShellDrawable> fromTrayArray = shellAllocations.get(Integer.parseInt(player + "" + fromTray));
                ArrayList<ShellDrawable> trayToStealFromArray = shellAllocations.get(Integer.parseInt(((player+1)%2)+""+trayToStealFrom));

                for (ShellDrawable shellDrawable : trayToStealFromArray) {
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(shellDrawable, "translationX", (player == 0? (width*(6-fromTray)) + storeWidth : -((width*(6-fromTray))) - storeWidth ));
                    objectAnimator.setDuration(290);
                    objectAnimator.start();
                }

                for (ShellDrawable shellDrawable : fromTrayArray) {
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(shellDrawable, "translationX", (player == 0? (width*(6-fromTray)) + storeWidth : -((width*(6-fromTray))) - storeWidth ));
                    objectAnimator.setDuration(290);
                    objectAnimator.start();
                }

                fromTrayArray.clear();
                trayToStealFromArray.clear();
            }
        });


        return state;
    }

    private GameState animateShells(final GameState state, final ShellMovement event) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int shellsLeft = event.getShellsStillToBePlaced();
                int toTray = event.getTrayIndex();
                int fromTray = toTray == 0? 6 : toTray - 1;
                int player = event.getPlayerIndex();
                Log.d("From Event - shellsLeft", shellsLeft+"");
                Log.d("From Event - toTray", toTray+"");
                Log.d("From Event - formTray", fromTray+"");
                Log.d("From Event - player", player+"");


                HashMap<Integer, ArrayList<ShellDrawable>> shellAllocations = ((BoardActivity) activity).getShellAllocations();

                final LinearLayout fromTrayLayout = (LinearLayout) activity.findViewById(Integer.parseInt((toTray == 0? (player+1) % 2 : player)+""+fromTray));
                int buttonWidth = fromTrayLayout.getWidth();

                ArrayList<ShellDrawable> fromTrayArray = shellAllocations.get(Integer.parseInt((toTray == 0? (player+1) % 2 : player)+""+fromTray));
                Log.d("from Tray Array", Arrays.toString(fromTrayArray.toArray()));

                ArrayList<ShellDrawable> toTrayArray = shellAllocations.get(Integer.parseInt(event.getPlayerIndex()+""+toTray));

                Random random = new Random();

                for(int i=0; i < shellsLeft; ++i) {
                    Log.d("iteration", i + "");
                    final ShellDrawable currentShell = fromTrayArray.get(fromTrayArray.size() > 0? fromTrayArray.size()-1 : 0);
                    final ShellDrawable shellCopy = new ShellDrawable(activity, currentShell.getShellX(), currentShell.getShellY(), currentShell.getShellWidth(), currentShell.getLength());
                    shellCopy.setColour(currentShell.getColour());
                    shellCopy.setRotation(currentShell.getRotation());

                    LinearLayout trayLayout = (LinearLayout) activity.findViewById(Integer.parseInt(event.getPlayerIndex()+""+toTray));
                    final RelativeLayout newTrayButton = (RelativeLayout) trayLayout.findViewById(R.id.button);

                    currentShell.bringToFront();

                    ObjectAnimator objectAnimator;
                    if(toTray == 0) {
                        objectAnimator = ObjectAnimator.ofFloat(currentShell, "translationY", player == 1? -buttonWidth : buttonWidth);
                    }
                    else {
                        Log.d("SHELLS X",""+currentShell.getX());
                        Log.d("OFFSET X",""+(currentShell.getX() *buttonWidth));
                        objectAnimator = ObjectAnimator.ofFloat(currentShell, "translationX", player == 1? -buttonWidth : buttonWidth);
                    }
                    objectAnimator.setDuration(290);
                    objectAnimator.addListener(new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationEnd(Animator animation) {
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
                Log.d("to Tray Array", Arrays.toString(toTrayArray.toArray()));
                Log.d("from Tray Array", Arrays.toString(fromTrayArray.toArray()));

            }
        });


        return state;
    }

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
                            }
                        });
                    }
                });
                animatorSet.start();
                ArrayList<ShellDrawable> shellArray = shellAllocations.get(Integer.parseInt(player+""+tray));
                shellArray.remove(shell);
            }
        });
        return state;
    }


    @Override
    public void updateView(GameState state, Activity activity) { }

}
