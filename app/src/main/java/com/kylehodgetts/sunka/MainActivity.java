package com.kylehodgetts.sunka;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.kylehodgetts.sunka.uiutil.Fonts;
import com.kylehodgetts.sunka.util.RequestWithHeaders;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView txtTeamName;
    private Button mmbone;
    private Button mmbtwo;
    private Button mmbonline;
    private Button mmbStatistics;
    private Button mmbexit;

    //Statics for preferences
    public static final String USER_NAME ="UserName";
    public static final String SERVER_ID ="ServerID";
    public static final String GAMES_WON ="GamesWon";
    public static final String GAMES_LOST ="GamesLost";
    public static final String MAX_SCORE ="MaxScore";
    public static final String AVG_TIME ="AvgTime";

    public static final String SERVER_URL ="http://178.62.56.190:8080";
    public static final String PREFERENCES ="lynx_preferences";
    public static final String STATS_LOCAL ="lynx_stat.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(PREFERENCES, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
        if(!prefs.contains(USER_NAME)){
            setContentView(R.layout.new_user);
            findViewById(R.id.done_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setName();
                }
            });
        }else {
            setUpMainScreen();
            setServerId(prefs.getString(USER_NAME, "N/A"));
        }


    }

    public void setName(){
        findViewById(R.id.user_name);
        String userName = ((EditText) findViewById(R.id.user_name)).getText().toString().trim();
        if (!userName.isEmpty()){

            SharedPreferences prefs = getSharedPreferences(PREFERENCES, 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(USER_NAME,userName);
            editor.apply();

            setServerId(userName);
            setUpMainScreen();
        }
    }

    /**
     * Sets up the main menu screen and assigns listeners to the buttons
     */
    private void setUpMainScreen(){

        setContentView(R.layout.activity_main);

        txtTeamName = (TextView) findViewById(R.id.teamName);
        txtTeamName.setTypeface(Fonts.getTitleFont(this));

        /** Menu Buttons **/
        mmbone = (Button) findViewById(R.id.main_menu_button_one);
        mmbone.setTypeface(Fonts.getButtonFont(this));
        mmbone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameOption(v);
            }
        });
        mmbtwo = (Button) findViewById(R.id.main_menu_button_two);
        mmbtwo.setTypeface(Fonts.getButtonFont(this));
        mmbtwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameOption(v);
            }
        });
        mmbonline = (Button) findViewById(R.id.main_menu_button_online);
        mmbonline.setTypeface(Fonts.getButtonFont(this));
        mmbonline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, WiFiDirectActivity.class);
                startActivity(i);
            }
        });

        mmbStatistics = (Button) findViewById(R.id.main_menu_button_statistics);
        mmbStatistics.setTypeface(Fonts.getButtonFont(this));
        mmbStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
                startActivity(intent);
            }
        });
        mmbexit = (Button) findViewById(R.id.main_menu_button_exit);
        mmbexit.setTypeface(Fonts.getButtonFont(this));
        mmbexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    /**
     * Gives the type of game that will be played
     * and switches activity to BoardActivity
     */
    public void gameOption(View view) {
        Button selectedButton = (Button) view;
        int gameType = -1;

        if (selectedButton.equals(mmbone)) {
            gameType = BoardActivity.ONEPLAYER;
        } else if (selectedButton.equals(mmbtwo)) {
            gameType = BoardActivity.TWOPLAYER;
        }

        Intent intent = new Intent(MainActivity.this, BoardActivity.class);
        intent.putExtra(BoardActivity.EXTRA_INT, gameType);
        MainActivity.this.startActivity(intent);
    }

    private void setServerId(String userName){
        final SharedPreferences prefs = getSharedPreferences(PREFERENCES, 0);

        if (!prefs.contains(SERVER_ID)){
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = SERVER_URL+"/user/id";

            JSONObject json = new JSONObject();
            try {
                json.put(USER_NAME, userName);

                Request request = new RequestWithHeaders(Request.Method.POST, url,json, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.v("USER_ID","Gotten ID- " + response);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString(SERVER_ID,response.getString(SERVER_ID));
                            editor.apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("USER_ID","Could not get id for the user from server");
                        Log.v("USER_ID code: ",""+error.networkResponse.statusCode);
                        String err = error.getMessage();
                        if (err!=null)
                            Log.v("USER_ID",error.getMessage());
                    }
                });

                queue.add(request);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
