package com.kylehodgetts.sunka;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mmbone;
    private Button mmbtwo;
    private Button mmbonline;
    private Button mmbus;
    private Button mmbexit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Menu Buttons **/
        mmbone = (Button) findViewById(R.id.main_menu_button_one);
        mmbone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameOption(v);
            }
        });
        mmbtwo = (Button) findViewById(R.id.main_menu_button_two);
        mmbtwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameOption(v);
            }
        });
        mmbonline = (Button) findViewById(R.id.main_menu_button_online);
        mmbonline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, WiFiDirectActivity.class);
                startActivity(i);
            }
        });
        mmbus = (Button) findViewById(R.id.main_menu_button_us);
        mmbexit = (Button) findViewById(R.id.main_menu_button_exit);
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

    /**
     * Allows the user to switch to the AboutUsActivity
     * or exit the application.
     */
    public void otherOptions(View view) {
        Intent intent;
        Button selectedButton = (Button) view;
        if (selectedButton.equals(mmbus)) {
            //  intent = new Intent(MainActivity.this, AboutUsActivity.class);
        } else {
            // Either make an Activity window asking "Are you sure? YES/NO" or directly Kill.
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

}
