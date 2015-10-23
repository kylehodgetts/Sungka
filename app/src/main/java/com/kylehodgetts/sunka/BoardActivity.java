package com.kylehodgetts.sunka;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kylehodgetts.sunka.model.Board;
import com.kylehodgetts.sunka.model.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** @author :Phileas Hocquard 20/10/15
 *
 */
public class BoardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
     }
}
