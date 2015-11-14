package com.kylehodgetts.sunka.controller.statistics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.kylehodgetts.sunka.R;
import com.kylehodgetts.sunka.model.PlayerScores;

import java.util.List;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * An adapter to list that renders stats of users
 */
public class ScoreListAdapter extends ArrayAdapter<PlayerScores> {

    /**
     * Constructor to match the super of this adapter
     */
    public ScoreListAdapter(Context context, List<PlayerScores> objects) {
        super(context, R.layout.user_stat, objects);
    }

    /**
     * Creates the view for one given row in the list
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PlayerScores playerScores = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(getContext());

        View myView = inflater.inflate(R.layout.user_stat, parent, false);

        ((TextView) myView.findViewById(R.id.user_name)).setText(playerScores.getName());
        ((TextView) myView.findViewById(R.id.games_won_user)).setText(String.valueOf(playerScores.getGamesWon()));
        ((TextView) myView.findViewById(R.id.games_lost_user)).setText(String.valueOf(playerScores.getGamesLost()));
        ((TextView) myView.findViewById(R.id.max_score_user)).setText(String.valueOf(playerScores.getMaxScore()));

        return myView;
    }
}
