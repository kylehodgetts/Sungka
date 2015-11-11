package com.kylehodgetts.sunka.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kylehodgetts.sunka.R;
import com.kylehodgetts.sunka.controller.bus.Event;
import com.kylehodgetts.sunka.controller.bus.EventBus;
import com.kylehodgetts.sunka.controller.bus.EventHandler;
import com.kylehodgetts.sunka.event.ShellStoreAnimation;
import com.kylehodgetts.sunka.event.ShellTrayAnimation;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.uiutil.ShellDrawable;
import com.kylehodgetts.sunka.util.Tuple2;

import java.util.ArrayList;
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
        if(event instanceof ShellTrayAnimation) {
            return new Tuple2<>(animateShells(state, (ShellTrayAnimation) event), false);
        }
        else if(event instanceof ShellStoreAnimation) {
            return new Tuple2<>(animateShellsToStore(state, (ShellStoreAnimation) event), false);
        }

        return new Tuple2<>(state, false);
    }

    private GameState animateShells(final GameState state, final ShellTrayAnimation event) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int shells = event.getShellsToMove();
                int player = event.getPlayer();
                int fromTray  = event.getFromTray();
                final int toTray = event.getToTray();

                if(toTray == 0) { player = (player+1)%2; }

                LinearLayout linearLayout = (LinearLayout) activity.findViewById(Integer.parseInt(player + "" + fromTray));
                final RelativeLayout trayButton = (RelativeLayout) linearLayout.findViewById(R.id.button);

                final LinearLayout nextLinearLayout = (LinearLayout) activity.findViewById(Integer.parseInt(player + "" + toTray));
                final RelativeLayout nextTrayButton = (RelativeLayout) nextLinearLayout.findViewById(R.id.button);

                final ArrayList<ShellDrawable> animatedShells = new ArrayList<>();

                for(int i=0; i < shells; ++i) {
                    final ShellDrawable shell = (ShellDrawable) trayButton.getChildAt(i);
                    animatedShells.add(shell);

                    ObjectAnimator objectAnimator;
                    if(event.getToTray() == 0) {
                        objectAnimator = ObjectAnimator.ofFloat(shell, "translationY", player == 1? trayButton.getHeight() : -trayButton.getHeight());
                    }
                    else {
                        objectAnimator = ObjectAnimator.ofFloat(shell, "translationX", player == 1? -trayButton.getWidth() : trayButton.getWidth());
                    }
                    objectAnimator.setDuration(1000);
                    objectAnimator.start();
                    objectAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
//                            for(final ShellDrawable currentShell : animatedShells) {
//                                ViewGroup parent = ((ViewGroup) currentShell.getParent());
//                                if(parent != null) { parent.removeView(currentShell); }
//                                nextTrayButton.addView(currentShell);
//                            }
                        }
                    });
                }
            }
        });
        return state;
    }

    private GameState animateShellsToStore(final GameState state, final ShellStoreAnimation event) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int player = event.getPlayer();
                int tray = event.getFromTray();

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
                storeButton.bringToFront();

                Random random = new Random();
                final ShellDrawable shell = (ShellDrawable) fromTrayButton.getChildAt(fromTrayButton.getChildCount()-1);
                ObjectAnimator animateX = ObjectAnimator.ofFloat(shell, "translationX", player == 1? -storeButton.getWidth() : storeButton.getWidth());
                ObjectAnimator animateY = ObjectAnimator.ofFloat(shell, "translationY", player == 1? -random.nextFloat() + storeButton.getHeight()/2 : random.nextFloat() + storeButton.getHeight()/2);
                animateX.setDuration(1000);
                animateY.setDuration(1000);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(animateX).with(animateY);
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        fromTrayButton.removeView(shell);
                        storeButton.addView(shell);
                    }
                });
                animatorSet.start();
            }
        });
        return state;
    }

    @Override
    public void updateView(GameState state, Activity activity) { }

}
