package com.kylehodgetts.sunka;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kylehodgetts.sunka.controller.statistics.ScoreListAdapter;
import com.kylehodgetts.sunka.model.PlayerScores;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static com.kylehodgetts.sunka.MainActivity.*;


public class StatisticsActivity extends AppCompatActivity {


    private String serverId;
    private RequestQueue queue;
    private int maxPage;
    private int currentPage;
    private int ordering;
    private List<PlayerScores> scores;
    private ListView scoresList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        queue = Volley.newRequestQueue(this);
        scores = new ArrayList<>();
        ordering = 1;
        maxPage = 1;
        currentPage = 1;

        SharedPreferences preferences = getSharedPreferences(PREFERENCES, 0);


        String name = preferences.getString(USER_NAME, "N/A");
        serverId = preferences.getString(SERVER_ID,null);
        JSONObject object = new JSONObject();

        try {
            FileInputStream inp = openFileInput(STATS_LOCAL);
            byte[] bytes = new byte[(int) inp.getChannel().size()];
            inp.read(bytes);
            inp.close();
            object = new JSONObject(new String(bytes));
        } catch (java.io.IOException | JSONException ignored) {}

        int won = getInt(object,GAMES_WON,0);
        int lost = getInt(object,GAMES_LOST,0);
        int topScore = getInt(object,MAX_SCORE,0);
        double avg = getDouble(object,AVG_TIME,-1);

        scoresList = (ListView)findViewById(R.id.scores_list);

        ((TextView)findViewById(R.id.display_name)).setText(name);
        ((TextView)findViewById(R.id.games_won)).setText(String.valueOf(won));
        ((TextView)findViewById(R.id.games_lost)).setText(String.valueOf(lost));
        ((TextView)findViewById(R.id.max_score)).setText(String.valueOf(topScore));
        ((TextView)findViewById(R.id.avg_time)).setText(avg == -1?"N/A":String.valueOf(avg));
        findViewById(R.id.next_stats).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage < maxPage)
                    currentPage++;
                updateButtons();
                getCurrentPage();
            }
        });
        findViewById(R.id.prev_stats).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage > 1)
                    currentPage--;
                updateButtons();
                getCurrentPage();
            }
        });

        queue.add(new StringRequest(Request.Method.POST, SERVER_URL + "/statistics/pages", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("PAges", response);
                try {
                    maxPage = Math.max(Integer.parseInt(response), 1);
                } catch (NumberFormatException ex) {
                    maxPage = 0;
                }
                updateButtons();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("PAGES", "" + error.networkResponse.statusCode);
            }
        }));

        getCurrentPage();

    }

    private void getCurrentPage(){
        queue.add(new JsonArrayRequest(MainActivity.SERVER_URL+"/statistics/"+(currentPage-1)+"/"+ordering, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                scores = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        scores.add(parsePlayerScore(response.getJSONObject(i)));
                    } catch (JSONException ignored) {}
                }
                updateList();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("CURR_PAGE",""+error.networkResponse.statusCode);
            }
        }));
    }

    private PlayerScores parsePlayerScore(JSONObject object) throws JSONException{
        int lost = object.getInt(GAMES_LOST);
        int wont = object.getInt(GAMES_WON);
        int max = object.getInt(MAX_SCORE);
        String name = object.getString(USER_NAME);
        return new PlayerScores(name,max,wont,lost);
    }

    private void updateList(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scoresList.setAdapter(new ScoreListAdapter(getApplicationContext(),scores));
            }
        });
    }

    private void updateButtons(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.current_page)).setText(maxPage == 0 ? "N/A" : currentPage + " / " + maxPage);
                findViewById(R.id.next_stats).setEnabled(currentPage < maxPage && maxPage != 0);
                findViewById(R.id.prev_stats).setEnabled(currentPage > 1 && maxPage != 0);
            }
        });
    }

    private int getInt(JSONObject object,String key, int defaultValue){
        try {
            return object.getInt(key);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    private double getDouble(JSONObject object,String key, double defaultValue){
        try {
            return object.getDouble(key);
        } catch (JSONException e) {
            return defaultValue;
        }
    }
}
