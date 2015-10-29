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
public class WiFiDirectActivity extends Activity implements ChannelListener {

    public static final String TAG = "wifidirectactivity";
    private ListView peerList;
    private ArrayList<WifiP2pDevice> arrayListPeers;
    private PeerListAdapter peerListAdapter;

    private WifiP2pManager manager;
    private boolean isWifiP2pEnabled = false;
    private boolean retryChannel = false;

    private final IntentFilter intentFilter = new IntentFilter();
    private Channel channel;
    private BroadcastReceiver receiver = null;

    /**
     * @param isWifiP2pEnabled the isWifiP2pEnabled to set
     */
    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifidirect);
        arrayListPeers = new ArrayList<>();
        peerListAdapter = new PeerListAdapter(this, arrayListPeers);
        peerList = (ListView) findViewById(R.id.peerList);
        peerList.setAdapter(peerListAdapter);
        peerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Initiate connections
                Toast.makeText(WiFiDirectActivity.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
            }
        });

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int reason) {

            }
        });
    }

    /** register the BroadcastReceiver with the intent values to be matched */
    @Override
    public void onResume() {
        super.onResume();
        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public void connect(WifiP2pConfig config) {
        manager.connect(channel, config, new ActionListener() {

            @Override
            public void onSuccess() {
                // WiFiDirectBroadcastReceiver will notify us. Ignore for now.
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(WiFiDirectActivity.this, "Connect failed. Retry.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onChannelDisconnected() {}

    public void resetData() {}

    public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

        private WifiP2pManager manager;
        private Channel channel;
        private WiFiDirectActivity activity;

        private List listPeers = new ArrayList();
        private WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peers) {
                arrayListPeers.clear();
                arrayListPeers.addAll(peers.getDeviceList());
                peerListAdapter.notifyDataSetChanged();
                if (arrayListPeers.size() == 0) {
                    Log.d(WiFiDirectActivity.TAG, "No devices found");
                    return;
                }
            }
        };


        /**
         * @param manager WifiP2pManager system service
         * @param channel Wifi p2p channel
         * @param activity activity associated with the receiver
         */
        public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel,
                                           WiFiDirectActivity activity) {
            super();
            this.manager = manager;
            this.channel = channel;
            this.activity = activity;
        }

        /*
         * (non-Javadoc)
         * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
         * android.content.Intent)
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    activity.setIsWifiP2pEnabled(true);
                } else {
                    activity.setIsWifiP2pEnabled(false);
                    activity.resetData();

                }
                Log.d(WiFiDirectActivity.TAG, "P2P state changed - " + state);
            }
            else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
                if (manager != null) {
                    manager.requestPeers(channel, peerListListener);
                }
                Log.d(WiFiDirectActivity.TAG, "P2P peers changed");
            }
            else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            }
            else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
//            DeviceListFragment fragment = (DeviceListFragment) activity.getFragmentManager()
//                    .findFragmentById(R.id.frag_list);
//            fragment.updateThisDevice((WifiP2pDevice) intent.getParcelableExtra(
//                    WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));

            }
        }

    }
}
