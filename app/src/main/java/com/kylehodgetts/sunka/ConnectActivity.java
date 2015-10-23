package com.kylehodgetts.sunka;

import android.app.Activity;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * @author Kyle Hodgetts
 * @author Jonthan Burton
 * @version 1.0
 */
public class ConnectActivity extends Activity {

    private ListView peerList;
    private PeerListAdapter peerListAdapter;
    private ArrayList<String> devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_connect);

        peerList = (ListView) findViewById(R.id.peersList);
        devices = new ArrayList<String>();
        peerListAdapter = new PeerListAdapter(this, devices);
        peerList.setAdapter(peerListAdapter);

    }

    // Create a find peers task
}
