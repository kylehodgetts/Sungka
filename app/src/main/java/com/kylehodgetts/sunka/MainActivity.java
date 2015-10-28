package com.kylehodgetts.sunka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button mmbone;
    Button mmbtwo;
    Button mmbonline;
    Button mmbus;
    Button mmbexit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Menu Buttons **/
        mmbone =   (Button) findViewById(R.id.main_menu_button_one);
        mmbtwo =   (Button) findViewById(R.id.main_menu_button_two);
        mmbonline= (Button) findViewById(R.id.main_menu_button_online);
        mmbus=     (Button) findViewById(R.id.main_menu_button_us);
        mmbexit =  (Button) findViewById(R.id.main_menu_button_exit);
    }

    /**
     * Gives the type of game that will be played
     * and switches activity to BoardActivity
     */
    public void gameOption(View view) {
        Button selectedButton = (Button) view;
//        if (selectedButton.equals(mmbone)) {
//            BoardActivity.setGameMode(1);
//        } else if (selectedButton.equals(mmbtwo)) {
//            BoardActivity.setGameMode(2);
//        } else if (selectedButton.equals(mmbonline)) {
//            BoardActivity.setGameMode(3);
//        }
        Intent intent = new Intent(MainActivity.this, BoardActivity.class);
        MainActivity.this.startActivity(intent);
    }

    /**
     *Allows the user to switch to the AboutUsActivity
     * or exit the application.
     */
    public void otherOptions(View view){
        Intent intent;
        Button selectedButton = (Button) view;
        if (selectedButton.equals(mmbus)) {
            //  intent = new Intent(MainActivity.this, AboutUsActivity.class);
        } else{
            // Either make an Activity window asking "Are you sure? YES/NO" or directly Kill.
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

}
