package com.kylehodgetts.sunka;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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
        btnHost = (Button) findViewById(R.id.btnHost);
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
        if(discoveryListener == null) {
            initialiseDiscoveryListener();
        }
        nsdManager = (NsdManager)getApplicationContext().getSystemService(Context.NSD_SERVICE);
        nsdManager.discoverServices("_http._tcp", NsdManager.PROTOCOL_DNS_SD, discoveryListener);
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
    protected void onResume() {
        super.onResume();
        if(discoveryListener == null) {
            initialiseDiscoveryListener();
        }
    }

    @Override
    protected void onPause() {
        if(discoveryListener != null){
            nsdManager.stopServiceDiscovery(discoveryListener);
            discoveryListener = null;
        }
        super.onPause();

    }

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

            Socket socket = null;

            try {
                socket = new Socket(dstAddress, dstPort);
                Intent i = new Intent(WiFiDirectActivity.this, BoardActivity.class);
                SingletonSocket.setSocket(socket);
                i.putExtra("gamemode", 3);

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
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
