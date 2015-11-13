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
 *          <-INPUT DESC->
 */
public class ScoreListAdapter extends ArrayAdapter<PlayerScores> {
    public ScoreListAdapter(Context context, List<PlayerScores> objects) {
        super(context, R.layout.user_stat, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PlayerScores playerScores = getItem(position);

        ((TextView) convertView.findViewById(R.id.user_name)).setText(playerScores.getName());
        ((TextView) convertView.findViewById(R.id.games_won)).setText(playerScores.getGamesWon());
        ((TextView) convertView.findViewById(R.id.games_lost)).setText(playerScores.getGamesLost());
        ((TextView) convertView.findViewById(R.id.max_score)).setText(playerScores.getMaxScore());


        return convertView;
    }
}
