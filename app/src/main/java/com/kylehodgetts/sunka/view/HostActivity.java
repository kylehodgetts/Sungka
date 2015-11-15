package com.kylehodgetts.sunka.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.kylehodgetts.sunka.R;
import com.kylehodgetts.sunka.controller.wifi.SingletonSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Kyle Hodgetts
 * @author Adam Chlupacek
 * @version 1.0
 *
 * Activity that commences that starts the game server on the hosting device
 */
public class HostActivity extends Activity {
    private static final int PORT = 8080;
    private ProgressDialog progressDialog;

    private Thread serverSocketThread;
    private ServerSocket serverSocket;
    private NsdManager.RegistrationListener registrationListener;
    public static String serviceName = "";
    private NsdManager nsdManager;
    private NsdServiceInfo serviceInfo;

    /**
     * Instantiates view and registers service
     * @param savedInstanceState infomation when restoring view
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Hosting");
        progressDialog.setMessage("Awaiting opponent connection...");
        progressDialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(registrationListener == null) {
            registerService(PORT);
        }
    }

    /**
     * Uninitialises registration listener and server socket
     */
    @Override
    protected void onStop(){
        if(registrationListener != null){
            nsdManager.unregisterService(registrationListener);
            registrationListener = null;
        }
        try {
            if(serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onStop();
        Log.d("HostActivity: ", "onDestroy");
    }

    /**
     *
     * @param port port to register service to.
     */
    public void registerService(int port) {
        serviceInfo = new NsdServiceInfo();
        serviceInfo.setServiceName("Sunka-lynx-" + String.valueOf((int)Math.random()*9000)+1000);
        serviceInfo.setServiceType("_http._tcp.");
        serviceInfo.setPort(port);
        initialiseRegistrationListener();
        nsdManager = (NsdManager)getApplicationContext().getSystemService(Context.NSD_SERVICE);
        nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener);
    }

    /**
     * Invoked when the registration listener is null
     */
    public void initialiseRegistrationListener() {
        registrationListener = new NsdManager.RegistrationListener() {
            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {

            }

            @Override
            public void onServiceRegistered(NsdServiceInfo serviceInfo) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    serviceName = serviceInfo.getServiceName();
                    Log.d("HOST ACTIVITY: ", "Service registered");
                }
                serverSocketThread = new Thread(new SocketServerThread());
                serverSocketThread.start();
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo serviceInfo) {

            }
        };
    }

    /**
     *
     * @return true if registration listener is initialised, false otherwise
     */
    public boolean isRegistrationListenerInitialised() {
        return registrationListener != null;
    }

    /**
     *
     * @return true if service info is set, false otherwise
     */
    public boolean isServiceInfoSet() {
        return  serviceInfo.getServiceName() != null
                && serviceInfo.getServiceType().equals("_http._tcp.")
                && serviceInfo.getPort() == PORT;
    }

    /**
     *
     * @return true if server socket is initialised, false otherwise
     */
    public boolean isServerSocketInitialised() {
        return serverSocket != null;
    }


    /**
     * @author Kyle Hodgetts
     * @version 1.0
     * Responsible for the server connection
     */
    private class SocketServerThread extends Thread {
        static final int PORT = 8080;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(PORT);
                Socket socket = serverSocket.accept();
                Log.d("HostActivity: ", "Connection accepted");
                progressDialog.dismiss();

                /*
                 * At this point, the connection has been accepted
                 * Create intent, set the socket and open the board activity
                 */
                Intent i = new Intent(HostActivity.this, BoardActivity.class);
                SingletonSocket.setSocket(socket);
                i.putExtra(BoardActivity.EXTRA_INT, BoardActivity.ONLINE);
                startActivity(i);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
