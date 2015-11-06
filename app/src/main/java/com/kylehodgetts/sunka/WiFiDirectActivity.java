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

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.kylehodgetts.sunka.controller.wifi.PeerListAdapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
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
    private ListView foundServices;

    private NsdManager.DiscoveryListener discoveryListener;
    private NsdManager nsdManager;


    private List<NsdServiceInfo> services;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifidirect);

        services = new ArrayList<>();

//        btnConnect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "Connect clicked");
//                new MyClientTask(editAddress.getText().toString(),Integer.parseInt(editPort.getText().toString())).execute();
//            }
//        });
        btnHost = (Button) findViewById(R.id.btnHost);
        btnHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WiFiDirectActivity.this, HostActivity.class));
            }
        });
        initialiseDiscoveryListener();
        nsdManager = (NsdManager)getApplicationContext().getSystemService(Context.NSD_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            nsdManager.discoverServices("_http._tcp", NsdManager.PROTOCOL_DNS_SD, discoveryListener);
        }
        foundServices = (ListView) findViewById(R.id.list_found_services);
        foundServices.setAdapter(new ServiceAdapter(getApplicationContext(),services));
        renderList();
        foundServices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NsdServiceInfo serviceInfo = (NsdServiceInfo) parent.getItemAtPosition(position);
                new MyClientTask(serviceInfo.getHost().toString(), serviceInfo.getPort()).execute();
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


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void initialiseDiscoveryListener(){
        discoveryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.d("DISCOVERY SERVICE", " onStartDiscoveryFailed()");
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.d("DISCOVERY SERVICE", " onStopDiscoveryFailed()");
            }

            @Override
            public void onDiscoveryStarted(String serviceType) {
                Log.d("DISCOVERY_SERVICE","Discovery started");
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.d("DISCOVERY SERVICE", " onDiscoveryStopped()");
            }

            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {
                Log.d("DISCOVERY SERVICE", " onServiceFound()");
                if (serviceInfo.getServiceName().matches("Sunka-lynx-.*")){
                    nsdManager.resolveService(serviceInfo, new NsdManager.ResolveListener() {
                        @Override
                        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {

                        }

                        @Override
                        public void onServiceResolved(NsdServiceInfo serviceInfo) {
                            Log.d("RESOLVING","DID resolve" + serviceInfo);
                            if (serviceInfo.getServiceName().equals("")){
                                Log.d("RESOLVING","FOUND ourselves");
                                return;
                            }
                            services.add(serviceInfo);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    renderList();
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {
                Log.d("DISCOVERY SERVICE", " onServiceLost()");
            }
        };


    }

    private void renderList(){
        foundServices = (ListView) findViewById(R.id.list_found_services);
        foundServices.setAdapter(new ServiceAdapter(getApplicationContext(),services));
    }


    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        String response = "";

        MyClientTask(String addr, int port){
            dstAddress = addr.replaceAll("/", "");
            dstPort = port;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d(TAG, "MyClientTask Started");

            Socket socket = null;

            try {
                socket = new Socket(dstAddress, dstPort);

                ByteArrayOutputStream byteArrayOutputStream =
                        new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];

                int bytesRead;
                InputStream inputStream = socket.getInputStream();

    /*
     * notice:
     * inputStream.read() will block if no data return
     */
                while ((bytesRead = inputStream.read(buffer)) != -1){
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    response += byteArrayOutputStream.toString("UTF-8");
                }

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            }finally{
                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
           // textResponse.setText(response);
            super.onPostExecute(result);
        }

    }

}
