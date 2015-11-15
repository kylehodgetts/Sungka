package com.kylehodgetts.sunka.controller;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.kylehodgetts.sunka.MainActivity;
import com.kylehodgetts.sunka.controller.bus.Event;
import com.kylehodgetts.sunka.controller.bus.EventHandler;
import com.kylehodgetts.sunka.event.EndGame;
import com.kylehodgetts.sunka.event.NewGame;
import com.kylehodgetts.sunka.event.NextTurn;
import com.kylehodgetts.sunka.event.PlayerChoseTray;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.util.RequestWithHeaders;
import com.kylehodgetts.sunka.util.Tuple2;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.kylehodgetts.sunka.MainActivity.*;
import static com.kylehodgetts.sunka.util.JsonUtil.getInt;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * This manager collects the data about player one
 */
public class StatisticsCollector extends EventHandler<GameState> {

    private RequestQueue queue;
    private JSONObject obj;
    private boolean online;
    private List<Long> turnTimes;
    private long startRecord;
    private Context activity;

    /**
     * Default constructor for event handler
     * Reads the local statistics file and saves it for future reference when the game ends to keep
     * relevance when saving new stats for the user
     *
     */
    public StatisticsCollector(Context activity, String serverId) {
        super("StatisticsCollector");

        turnTimes = new ArrayList<>();
        startRecord = System.currentTimeMillis();
        this.activity = activity;
        online = false;
        obj = new JSONObject();

        try {
            File file = new File(activity.getFilesDir(), STATS_LOCAL);
            FileInputStream inp = new FileInputStream(file);
            byte[] bytes = new byte[(int) inp.getChannel().size()];
            inp.read(bytes);
            inp.close();
            obj = new JSONObject(new String(bytes));

        } catch (java.io.IOException | JSONException e) {}

        if (serverId !=null)
            try {
                queue = Volley.newRequestQueue(activity);
                obj.put(MainActivity.SERVER_ID, serverId);
                online = true;
            } catch (JSONException ignored) {}
    }

    /**
     * The handle of incoming events for this manager.
     *
     * This manager listens to only NewTurn, PlayerChoseTray and EndGame
     *
     * @param event The incoming event from the bus to this handler
     * @param state The current state of the bus
     * @return
     */
    @Override
    public Tuple2<GameState, Boolean> handleEvent(Event event, GameState state) {
        System.out.println(event);

        if ((event instanceof NextTurn && state.getCurrentPlayerIndex() == 0) || event instanceof NewGame)
            startRecord = System.currentTimeMillis();

        else if (event instanceof PlayerChoseTray && startRecord != -1 && ((PlayerChoseTray) event).getPlayerIndex()==0){
            long avg = (System.currentTimeMillis()-startRecord);
            Log.v("AVG",""+avg);
            turnTimes.add(avg);
        }

        else if (event instanceof EndGame)
            endGame(state);

        return new Tuple2<>(state,false);
    }

    /**
     * For this manager we don not update view in any way
     * @param state    The current state of the event bus
     * @param activity The active activity
     */
    @Override
    public void updateView(GameState state, Activity activity) {
        //NO-OP
    }

    /**
     * Finishes the statistics for this game. then saves them locally and sends it to server
     * @param state the current game state
     */
    private void endGame(GameState state){
        try {

            if (state.getPlayer1().getStonesInPot() > state.getPlayer2().getStonesInPot()){
                obj.put(MainActivity.GAMES_WON,getInt(obj,GAMES_WON,0)+1);
            }else if (!obj.has(MainActivity.GAMES_WON)){
                obj.put(MainActivity.GAMES_WON,0);
            }

            if (state.getPlayer1().getStonesInPot() < state.getPlayer2().getStonesInPot()){
                obj.put(MainActivity.GAMES_LOST,getInt(obj,GAMES_LOST,0)+1);
            }else if (!obj.has(MainActivity.GAMES_LOST)){
                obj.put(MainActivity.GAMES_LOST,0);
            }

            int topScore = getInt(obj,MAX_SCORE,state.getPlayer1().getStonesInPot());
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

            Log.v("AVG",avg+"");
            Log.v("AVG",obj.toString());

            byte[] bytes = obj.toString().getBytes();
            File file = new File(activity.getFilesDir(), STATS_LOCAL);
            FileOutputStream out = new FileOutputStream(file);
            out.write(bytes);
            out.close();

            if(online && queue != null)
                sendToServer(obj, queue);
        } catch (IOException | JSONException e) {
           Log.v("STATS", "Could not write stats");
        }
    }

    /**
     * Sends the updated statistics to the server
     * @param obj the statistics to be send to server
     * @param queue the request queue
     */
    public static void sendToServer(JSONObject obj, RequestQueue queue){
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

}
