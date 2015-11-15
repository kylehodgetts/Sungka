package com.kylehodgetts.sunka;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView txtTeamName;
    private Button mmbone;
    private Button mmbtwo;
    private Button mmbonline;
    private Button mmbexit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTeamName = (TextView) findViewById(R.id.teamName);
        Typeface teamFont = Typeface.createFromAsset(getAssets(), String.format("fonts/%s", "fordscript.ttf"));
        txtTeamName.setTypeface(teamFont);

        /** Menu Buttons **/
        Typeface buttonFont = Typeface.createFromAsset(getAssets(), String.format("fonts/%s", "special_elite.ttf"));
        mmbone = (Button) findViewById(R.id.main_menu_button_one);
        mmbone.setTypeface(buttonFont);
        mmbone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameOption(v);
            }
        });
        mmbtwo = (Button) findViewById(R.id.main_menu_button_two);
        mmbtwo.setTypeface(buttonFont);
        mmbtwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameOption(v);
            }
        });
        mmbonline = (Button) findViewById(R.id.main_menu_button_online);
        mmbonline.setTypeface(buttonFont);
        mmbonline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, WiFiDirectActivity.class);
                startActivity(i);
            }
        });
        mmbexit = (Button) findViewById(R.id.main_menu_button_exit);
        mmbexit.setTypeface(buttonFont);
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
