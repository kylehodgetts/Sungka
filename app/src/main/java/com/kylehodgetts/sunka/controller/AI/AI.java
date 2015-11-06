package com.kylehodgetts.sunka.controller.AI;

import com.kylehodgetts.sunka.model.GameState;

/**
 * Created by Jonathan on 06/11/2015.
 */
public interface AI {

    int PLAYER_AI = 1;
    int PLAYER_HUMAN = 0;

    int chooseTray(GameState state);

}
