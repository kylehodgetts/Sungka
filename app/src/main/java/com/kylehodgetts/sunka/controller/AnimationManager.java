package com.kylehodgetts.sunka.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kylehodgetts.sunka.R;
import com.kylehodgetts.sunka.controller.bus.Event;
import com.kylehodgetts.sunka.controller.bus.EventBus;
import com.kylehodgetts.sunka.controller.bus.EventHandler;
import com.kylehodgetts.sunka.event.ShellMovement;
import com.kylehodgetts.sunka.event.ShellMovementToPot;
import com.kylehodgetts.sunka.event.ShellStoreAnimation;
import com.kylehodgetts.sunka.event.ShellTrayAnimation;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.uiutil.ShellDrawable;
import com.kylehodgetts.sunka.util.Tuple2;

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
    private HashMap<Integer, ArrayList<ShellDrawable>> shellAllocations;


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

        return new Tuple2<>(state, false);
    }

    private GameState animateShells(final GameState state, final ShellMovement event) {
        if(shellAllocations == null) {
            shellAllocations = new HashMap<>();
            initialiseShellAllocations();
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int shellsLeft = event.getShellsStillToBePlaced();
                int toTray = event.getTrayIndex();
                int fromTray = toTray == 0? 6 : toTray - 1;
                int player = event.getPlayerIndex();

                if(toTray == 0) { player = (player + 1) % 2; }

                LinearLayout fromTrayLayout = (LinearLayout) activity.findViewById(Integer.parseInt(player+""+fromTray));
                int buttonWidth = fromTrayLayout.getWidth();

                ArrayList<ShellDrawable> fromTrayArray = shellAllocations.get(Integer.parseInt(player + "" + fromTray));
                ArrayList<ShellDrawable> toMoveTrays = new ArrayList<>();

                for(int i=0; i < shellsLeft; ++i) {
                    ShellDrawable currentShell = fromTrayArray.get(i);
                    currentShell.bringToFront();
                    ObjectAnimator objectAnimator;
                    if(toTray == 0) {
                        objectAnimator = ObjectAnimator.ofFloat(currentShell, "translationY", player == 1? buttonWidth : -buttonWidth);
                    }
                    else {
                        objectAnimator = ObjectAnimator.ofFloat(currentShell, "translationX", player == 1? -buttonWidth : buttonWidth);
                    }
                    objectAnimator.setDuration(290);
                    objectAnimator.start();
                    toMoveTrays.add(currentShell);
                    fromTrayArray.remove(currentShell);
                }
//
//                for(int i=0; i < shellsLeft; ++i) {
//                    fromTrayArray.remove(0);
//                }

                ArrayList<ShellDrawable> toTrayArray = shellAllocations.get(Integer.parseInt(player+""+toTray));
                toTrayArray.addAll(toMoveTrays);
            }
        });


        return state;


//            activity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                final int shells = event.getShellsStillToBePlaced();
//                int player = event.getPlayerIndex();
//                final int toTray = event.getTrayIndex();
//                int fromTray = toTray == 0? 6 : toTray - 1;
//
//                if(toTray == 0) { player = (player+1)%2; }
//                Log.d("Shells Left: ", shells+"");
//                Log.d("player: ", player+"");
//                Log.d("from Tray: ", fromTray+"");
//                Log.d("to Tray: ", toTray+"");
//
//                LinearLayout linearLayout = (LinearLayout) activity.findViewById(Integer.parseInt(player + "" + fromTray));
//                final RelativeLayout trayButton = (RelativeLayout) linearLayout.findViewById(R.id.button);
//
//                final LinearLayout nextLinearLayout = (LinearLayout) activity.findViewById(Integer.parseInt(player + "" + toTray));
//                final RelativeLayout nextTrayButton = (RelativeLayout) nextLinearLayout.findViewById(R.id.button);
//
//                final ArrayList<ShellDrawable> shellArray = shellAllocations.get(Integer.parseInt(player+""+fromTray));
//                Log.d("Shell Array Length", shellArray.size()+"");
//                Log.d("Shell Array Contains: ", Arrays.toString(shellArray.toArray()));
//
//                for(int i=0; i < shellArray.size(); ++i) {
//                    final ShellDrawable shell = shellArray.get(i);
//
//                    ObjectAnimator objectAnimator;
//                    if(toTray == 0) {
//                        objectAnimator = ObjectAnimator.ofFloat(shell, "translationY", player == 1? trayButton.getHeight() : -trayButton.getHeight());
//                    }
//                    else {
//                        objectAnimator = ObjectAnimator.ofFloat(shell, "translationX", player == 1? -trayButton.getWidth() : trayButton.getWidth());
//                    }
//                    objectAnimator.setDuration(290);
//                    final int finalPlayer = player;
//                    objectAnimator.addListener(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            ArrayList<ShellDrawable> nextTray = shellAllocations.get(Integer.parseInt(finalPlayer +""+toTray));
//                            nextTray.add(shell);
//                            shellArray.remove(shell);
//                        }
//                    });
//                    objectAnimator.start();
//                }
//            }
//        });
    }

    private GameState animateShellsToStore(final GameState state, final ShellMovementToPot event) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final int player = event.getPlayerIndexOfThisPot();
                final int tray = 6;

                LinearLayout fromTrayLayout = (LinearLayout) activity.findViewById(Integer.parseInt(player+""+tray));
                final RelativeLayout fromTrayButton = (RelativeLayout) fromTrayLayout.findViewById(R.id.button);

                LinearLayout playerStoreLayout;
                final RelativeLayout storeButton;
                if(player == 0) {
                    playerStoreLayout = (LinearLayout) activity.findViewById(R.id.playerBStore);
                    storeButton = (RelativeLayout) playerStoreLayout.findViewById(R.id.buttonbs);
                }
                else {
                    playerStoreLayout = (LinearLayout) activity.findViewById(R.id.playerAStore);
                    storeButton = (RelativeLayout) playerStoreLayout.findViewById(R.id.buttonas);
                }

                ArrayList<ShellDrawable> trayArray = shellAllocations.get(Integer.parseInt(player+""+tray));

                Random random = new Random();
                final ShellDrawable shell = trayArray.get(0);
                ObjectAnimator animateX = ObjectAnimator.ofFloat(shell, "translationX", player == 1? -storeButton.getWidth() : storeButton.getWidth());
                ObjectAnimator animateY = ObjectAnimator.ofFloat(shell, "translationY", player == 1? -random.nextFloat() + storeButton.getHeight() : random.nextFloat() + storeButton.getHeight());
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
                                ArrayList<ShellDrawable> shellArray = shellAllocations.get(Integer.parseInt(player+""+tray));
                                shellArray.remove(shell);
                            }
                        });

                    }
                });
                animatorSet.start();
            }
        });
        return state;
    }

    private void initialiseShellAllocations() {
        for(int player=0; player < 2; ++player) {
            for(int tray=0; tray < 7; ++tray) {
                ArrayList<ShellDrawable> shellArrayList = new ArrayList<>();
                shellAllocations.put(Integer.parseInt(player+""+tray), shellArrayList);

                LinearLayout trayLinearLayout = (LinearLayout) activity.findViewById(Integer.parseInt(player+""+tray));
                RelativeLayout trayButton = (RelativeLayout) trayLinearLayout.findViewById(R.id.button);

                for(int shell=0; shell < 7; ++shell) {
                    shellArrayList.add((ShellDrawable) trayButton.getChildAt(shell));
                }

            }
        }
    }

    @Override
    public void updateView(GameState state, Activity activity) { }

}
