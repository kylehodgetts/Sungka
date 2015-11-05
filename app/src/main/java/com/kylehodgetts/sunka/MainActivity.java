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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kylehodgetts.sunka.uiutil.Fonts;

public class MainActivity extends AppCompatActivity {

    private TextView txtTeamName;
    private Button mmbone;
    private Button mmbtwo;
    private Button mmbonline;
    private Button mmbexit;

    //Statics for preferences
    public static final String USER_NAME ="UserName";
    public static final String SERVER_ID ="ServerID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences prefs = getPreferences(0);
        if(!prefs.contains(USER_NAME)){
            setContentView(R.layout.new_user);
        }else {
            setUpMainScreen();
        }

        if (!prefs.contains(SERVER_ID)){
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://178.62.56.190:8080/user/id";

            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.v("USER_ID","Gotten ID- " + response);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(SERVER_ID,response.trim());
                    editor.apply();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.v("USER_ID","Could not get id for the user from server");
                    Log.v("USER_ID",error.getMessage());
                }
            });

            queue.add(request);
        }
    }

    public void setName(View view){
        findViewById(R.id.user_name);
        String userName = ((EditText) findViewById(R.id.user_name)).getText().toString();
        if (!userName.trim().isEmpty()){

            SharedPreferences prefs = getPreferences(0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(USER_NAME,userName.trim());
            editor.apply();

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
}
