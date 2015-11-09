package com.kylehodgetts.sunka.controller.AI;

import com.kylehodgetts.sunka.model.GameState;

/**
 * @author Jonathan
 * @version 1.0
 */
public interface AI {

    String TAG = "SUNGKA_AI_LOGGING";

    int PLAYER_AI = 1;
    int PLAYER_HUMAN = 0;

    /**
     * Pick a tray to 'click' based off the current gamestate
     *
     * @param state the current state of the game
     * @return the index of the tray to click
     */
    int chooseTray(GameState state);

}
