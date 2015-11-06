/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kylehodgetts.sunka;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.kylehodgetts.sunka.controller.wifi.PeerListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kyle Hodgetts
 * @version 1.0
 * @see {@link "developer.android.com/training/connect-devices-wirelessly/wifi-direct.html"}
 */
public class WiFiDirectActivity extends Activity {
    public static final String TAG = "wifidirectactivity";

    private EditText editAddress;
    private EditText editPort;
    private Button btnConnect;
    private Button btnHost;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifidirect);

        editAddress = (EditText) findViewById(R.id.editTextAddress);
        editPort = (EditText) findViewById(R.id.editTextPort);
        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnHost = (Button) findViewById(R.id.btnHost);
        btnHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WiFiDirectActivity.this, HostActivity.class));
            }
        });

    }

    /** register the BroadcastReceiver with the intent values to be matched */
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }



}
