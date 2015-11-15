package com.kylehodgetts.sunka;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.kylehodgetts.sunka.controller.wifi.ServiceAdapter;
import com.kylehodgetts.sunka.controller.wifi.SingletonSocket;
import com.kylehodgetts.sunka.uiutil.Fonts;

import java.io.IOException;
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

    private Button btnHost;
    private ListView foundServices;

    private NsdManager.DiscoveryListener discoveryListener;
    private NsdManager nsdManager;


    private List<NsdServiceInfo> services;

    private MyClientTask myClientTask;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifidirect);
        services = new ArrayList<>();
        btnHost = (Button) findViewById(R.id.btnHost);
        btnHost.setTypeface(Fonts.getButtonFont(this));
        btnHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nsdManager != null) {
                    try{
                        nsdManager.stopServiceDiscovery(discoveryListener);
                        discoveryListener = null;
                    }
                    catch(IllegalArgumentException e) {
                        discoveryListener = null;
                    }
                }
                startActivity(new Intent(WiFiDirectActivity.this, HostActivity.class));
            }
        });
        initialiseDiscoveryListener();
        nsdManager = (NsdManager)getApplicationContext().getSystemService(Context.NSD_SERVICE);
        nsdManager.discoverServices("_http._tcp", NsdManager.PROTOCOL_DNS_SD, discoveryListener);
        foundServices = (ListView) findViewById(R.id.list_found_services);
        foundServices.setAdapter(new ServiceAdapter(getApplicationContext(), services));
        renderList();
        foundServices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NsdServiceInfo serviceInfo = (NsdServiceInfo) parent.getItemAtPosition(position);
                myClientTask = new MyClientTask(serviceInfo.getHost().toString(), serviceInfo.getPort());
                myClientTask.execute();
            }
        });
    }

    /**
     * Initialises discovery listener upon resume of the application
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(discoveryListener == null) {
            initialiseDiscoveryListener();
        }
    }

    /**
     * On Pause stops any further service discovery.
     * Uninitialises discovery listener
     */
    @Override
    protected void onPause() {
        if(discoveryListener != null) {
            try {
                nsdManager.stopServiceDiscovery(discoveryListener);
                discoveryListener = null;
            }
            catch(Exception e){
                discoveryListener = null;
            }

        }
        super.onPause();
    }

    /**
     * Initialises discovery listener to find devices hosting games
     */
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
                            Toast.makeText(WiFiDirectActivity.this, "Service Resolved Failed", Toast.LENGTH_LONG);
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
                Log.d("onServiceLost ", serviceInfo.getServiceName());
                for(NsdServiceInfo service : services) {
                    Log.d("onServiceLost ", service.getServiceName());
                    if(service.getServiceName().equals(serviceInfo.getServiceName())){
                        services.remove(service);
                        break;
                    }
                }
                Log.d("onServiceLost: ", "Will render list");
                renderList();
            }
        };


    }

    private void renderList(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                foundServices = (ListView) findViewById(R.id.list_found_services);
                foundServices.setAdapter(new ServiceAdapter(getApplicationContext(), services));
            }
        });
    }

    /**
     *
     * @return true if the client task is running on another thread, false otherwise
     */
    public boolean isClientTaskRunning(){
        return !myClientTask.isCancelled();
    }

    /**
     *
     * @return true if discovery listener, false otherwise
     */
    public boolean isDiscoveryListenerInitialised() {
        return discoveryListener != null;
    }

    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;

        MyClientTask(String addr, int port){
            dstAddress = addr.replaceAll("/", "");
            dstPort = port;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d(TAG, "MyClientTask Started");

            Socket socket;

            try {
                socket = new Socket(dstAddress, dstPort);
                Intent i = new Intent(WiFiDirectActivity.this, BoardActivity.class);
                SingletonSocket.setSocket(socket);
                i.putExtra(BoardActivity.EXTRA_INT, BoardActivity.ONLINE);
                startActivity(i);

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}
