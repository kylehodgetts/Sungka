package com.kylehodgetts.sunka.controller;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kylehodgetts.sunka.MainActivity;
import com.kylehodgetts.sunka.controller.bus.Event;
import com.kylehodgetts.sunka.controller.bus.EventHandler;
import com.kylehodgetts.sunka.event.EndGame;
import com.kylehodgetts.sunka.event.NextTurn;
import com.kylehodgetts.sunka.event.PlayerChoseTray;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.util.RequestWithHeaders;
import com.kylehodgetts.sunka.util.Tuple2;
import static com.kylehodgetts.sunka.MainActivity.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <-INPUT DESC->
 */
public class StatisticsCollector extends EventHandler<GameState> {


    private RequestQueue queue;
    private FileOutputStream out;
    private JSONObject obj;
    private boolean collecting;
    private boolean online;
    private List<Long> turnTimes;
    private long startRecord;

    /**
     * Default constructor for event handler, assigns its id that should be unique
     *
     */
    public StatisticsCollector(Activity activity, String serverId) {
        super("StatisticsCollector");

        try {
            collecting = true;
            turnTimes = new ArrayList<>();
            queue = Volley.newRequestQueue(activity);
            out = activity.openFileOutput(STATS_LOCAL, Context.MODE_PRIVATE);
            try {
                FileInputStream inp = activity.openFileInput(STATS_LOCAL);
                byte[] bytes = new byte[(int) inp.getChannel().size()];
                inp.read(bytes);
                inp.close();
                obj = new JSONObject(new String(bytes));
            } catch (java.io.IOException | JSONException e) {
                obj = new JSONObject();
                online = false;
                if (serverId !=null)
                    try {
                        obj.put(MainActivity.SERVER_ID, serverId);
                        online = true;
                    } catch (JSONException ignored) {}
            }

        } catch (FileNotFoundException e) {
            collecting = false;
        }
    }

    @Override
    public Tuple2<GameState, Boolean> handleEvent(Event event, GameState state) {

        if (event instanceof NextTurn && state.getCurrentPlayerIndex() == 0)
            startRecord = System.currentTimeMillis();

        if (event instanceof PlayerChoseTray && startRecord != -1 && ((PlayerChoseTray) event).getPlayerIndex()==0){
            turnTimes.add(System.currentTimeMillis()-startRecord);
        }

        if (event instanceof EndGame && collecting)
            endGame(state);


        return new Tuple2<>(state,false);
    }

    @Override
    public void updateView(GameState state, Activity activity) {
        //NO-OP
    }

    private void endGame(GameState state){
        try {
            if (state.getPlayer1().getStonesInPot() > state.getPlayer2().getStonesInPot()){
                int won;
                try {
                    won = obj.getInt(MainActivity.GAMES_WON);
                } catch (JSONException e) {
                    won = 0;
                }
                obj.put(MainActivity.GAMES_WON,won+1);
            }else if (!obj.has(MainActivity.GAMES_WON)){
              obj.put(MainActivity.GAMES_WON,0);
            }

            if (state.getPlayer1().getStonesInPot() < state.getPlayer2().getStonesInPot()){
                int lost;
                try {
                    lost = obj.getInt(MainActivity.GAMES_LOST);
                } catch (JSONException e) {
                    lost = 0;
                }
                obj.put(MainActivity.GAMES_LOST,lost+1);
            }else if (!obj.has(MainActivity.GAMES_LOST)){
                obj.put(MainActivity.GAMES_LOST,0);
            }

            int topScore;
            try {
                topScore = obj.getInt(MainActivity.MAX_SCORE);
            } catch (JSONException e) {
                topScore = state.getPlayer1().getStonesInPot();
            }

            if (topScore<=state.getPlayer1().getStonesInPot())
                obj.put(MainActivity.MAX_SCORE,topScore);

            double avg = 0;
            for (Long time:turnTimes){
                avg+=time;
            }
            avg = avg/turnTimes.size();
            try {
                double pastAvg = obj.getDouble(MainActivity.AVG_TIME);
                avg = (avg+pastAvg)/2;
            } catch (JSONException ignored) {
                if (avg>0)
                    obj.put(MainActivity.AVG_TIME,avg);
            }


            byte[] bytes = obj.toString().getBytes();
            out.write(bytes);
            out.close();

            if(online){
                obj.remove(MainActivity.AVG_TIME);
                Request request = new RequestWithHeaders(Request.Method.POST, MainActivity.SERVER_URL + "/statistics/", obj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {}
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {}
                });
                queue.add(request);
            }
        } catch (IOException | JSONException e) {
           Log.v("STATS", "Could not write stats");
        }
    }

}
