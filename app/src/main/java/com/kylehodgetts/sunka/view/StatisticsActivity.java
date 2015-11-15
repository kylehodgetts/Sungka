package com.kylehodgetts.sunka.view;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kylehodgetts.sunka.R;
import com.kylehodgetts.sunka.controller.StatisticsCollector;
import com.kylehodgetts.sunka.controller.statistics.ScoreListAdapter;
import com.kylehodgetts.sunka.model.PlayerScores;
import com.kylehodgetts.sunka.uiutil.Fonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.kylehodgetts.sunka.view.MainActivity.AVG_TIME;
import static com.kylehodgetts.sunka.view.MainActivity.GAMES_LOST;
import static com.kylehodgetts.sunka.view.MainActivity.GAMES_WON;
import static com.kylehodgetts.sunka.view.MainActivity.MAX_SCORE;
import static com.kylehodgetts.sunka.view.MainActivity.PREFERENCES;
import static com.kylehodgetts.sunka.view.MainActivity.SERVER_ID;
import static com.kylehodgetts.sunka.view.MainActivity.SERVER_URL;
import static com.kylehodgetts.sunka.view.MainActivity.STATS_LOCAL;
import static com.kylehodgetts.sunka.view.MainActivity.USER_NAME;
import static com.kylehodgetts.sunka.util.JsonUtil.getDouble;
import static com.kylehodgetts.sunka.util.JsonUtil.getInt;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *
 * Activity that displays the statistics for current player and shows the high scores for the other players
 */
public class StatisticsActivity extends Activity {

    private RequestQueue queue;
    private int maxPage;
    private int currentPage;
    private int ordering;
    private List<PlayerScores> scores;
    private ListView scoresList;

    /**
     * Instantiates view send requests to server
     * @param savedInstanceState infomation when restoring view
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        queue = Volley.newRequestQueue(this);
        scoresList = (ListView)findViewById(R.id.scores_list);
        scores = new ArrayList<>();
        ordering = 0;
        maxPage = 1;
        currentPage = 1;

        //Sets listener for moving to next page
        findViewById(R.id.next_stats).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage < maxPage)
                    currentPage++;
                updateButtons();
                getCurrentPage();
            }
        });
        ((Button)(findViewById(R.id.next_stats))).setTypeface(Fonts.getButtonFont(getApplicationContext()));

        //Sets listener for moving to previous page
        findViewById(R.id.prev_stats).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage > 1)
                    currentPage--;
                updateButtons();
                getCurrentPage();
            }
        });
        ((Button)(findViewById(R.id.prev_stats))).setTypeface(Fonts.getButtonFont(getApplicationContext()));

        setUpLocal();
        getPages();
        getCurrentPage();

    }

    /**
     * Updates the local statistics according to what is saved in the local file.
     */
    private void setUpLocal(){
        JSONObject object = new JSONObject();

        try {
            FileInputStream inp = openFileInput(STATS_LOCAL);
            byte[] bytes = new byte[(int) inp.getChannel().size()];
            inp.read(bytes);
            inp.close();
            object = new JSONObject(new String(bytes));

        } catch (java.io.IOException | JSONException ignored) {}

        SharedPreferences preferences = getSharedPreferences(PREFERENCES, 0);

        String name = preferences.getString(USER_NAME, "N/A");
        System.out.println(object);
        int won = getInt(object,GAMES_WON,0);
        int lost = getInt(object,GAMES_LOST,0);
        int topScore = getInt(object, MAX_SCORE, 0);
        double avg = getDouble(object, AVG_TIME, -1);

        DecimalFormat format = new DecimalFormat("#.#");

        ((TextView)findViewById(R.id.display_name)).setText(name);
        ((TextView)findViewById(R.id.display_name)).setTypeface(Fonts.getButtonFont(getApplicationContext()));
        ((TextView)findViewById(R.id.games_won)).setText(String.valueOf(won));
        ((TextView)findViewById(R.id.games_won)).setTypeface(Fonts.getButtonFont(getApplicationContext()));
        ((TextView)findViewById(R.id.games_lost)).setText(String.valueOf(lost));
        ((TextView)findViewById(R.id.games_lost)).setTypeface(Fonts.getButtonFont(getApplicationContext()));
        ((TextView)findViewById(R.id.max_score)).setText(String.valueOf(topScore));
        ((TextView)findViewById(R.id.max_score)).setTypeface(Fonts.getButtonFont(getApplicationContext()));
        ((TextView)findViewById(R.id.avg_time)).setText(avg == -1 ? "N/A" : format.format(avg / 1000));
        ((TextView)findViewById(R.id.avg_time)).setTypeface(Fonts.getButtonFont(getApplicationContext()));


        String serverId = getSharedPreferences(PREFERENCES,0).getString(SERVER_ID,null);
        if (serverId != null) {
            try {
                object.put(SERVER_ID,serverId);
                StatisticsCollector.sendToServer(object, queue);
            } catch (JSONException ignored) {}
        }
    }

    /**
     * Gets the high score values for the current pages from the server
     */
    private void getCurrentPage(){
        queue.add(new JsonArrayRequest(MainActivity.SERVER_URL + "/statistics/" + (currentPage - 1) + "/" + ordering, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                scores = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        scores.add(parsePlayerScore(response.getJSONObject(i)));
                    } catch (JSONException ignored) {
                    }
                }
                updateList();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("CURR_PAGE", "" + error.networkResponse.statusCode);
            }
        }));
    }

    /**
     * Gets the count of high score pages to display
     */
    private void getPages(){
        queue.add(new StringRequest(Request.Method.POST, SERVER_URL + "/statistics/pages", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("PAGES", response);
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
                updateButtons();
            }
        }));
    }

    /**
     * Parses one player scores from a json object gotten from server
     * @param object the object that was received from server, contains all the values
     * @return  PlayerScores object that has the values extracted from the json
     * @throws JSONException
     */
    private PlayerScores parsePlayerScore(JSONObject object) throws JSONException{
        int lost = object.getInt(GAMES_LOST);
        int wont = object.getInt(GAMES_WON);
        int max = object.getInt(MAX_SCORE);
        String name = object.getString(USER_NAME);
        return new PlayerScores(name,max,wont,lost);
    }

    /**
     * Updates the list view of currently viewed high scores
     */
    private void updateList(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scoresList.setAdapter(new ScoreListAdapter(getApplicationContext(),scores));
            }
        });
    }

    /**
     * Updates the buttons, ie sets them active/inactive depending on whether there is possible another move
     * also updates the current page value
     */
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
}
