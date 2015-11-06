package com.kylehodgetts.sunka;

import android.app.Activity;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by kylehodgetts on 06/11/2015.
 */
public class HostActivity extends Activity {
    private TextView txtAddress;
    private TextView txtPort;
    private TextView txtStatus;

    private ServerSocket serverSocket;
    private NsdManager.RegistrationListener registrationListener;
    public static String serviceName = "";
    private NsdManager nsdManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        txtAddress = (TextView) findViewById(R.id.txtHostAddress);
        txtPort = (TextView) findViewById(R.id.txtHostPort);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        txtAddress.setText(getIpAddress());

        registerService(8080);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerService(int port) {
        NsdServiceInfo serviceInfo = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            serviceInfo = new NsdServiceInfo();
            serviceInfo.setServiceName("Sunka-lynx-" + System.currentTimeMillis());
            serviceInfo.setServiceType("_http._tcp.");
            serviceInfo.setPort(port);
            initialiseRegistrationListener();
            nsdManager = (NsdManager)getApplicationContext().getSystemService(Context.NSD_SERVICE);
            nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener);
        }
    }

    public void initialiseRegistrationListener() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
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
                        Log.d("HOST ACTIVITY: ", "Something happened");
                    }
                    Thread serverSocketThread = new Thread(new SocketServerThread());
                    serverSocketThread.start();
                }

                @Override
                public void onServiceUnregistered(NsdServiceInfo serviceInfo) {

                }
            };
        }
    }

    private class SocketServerThread extends Thread {
        static final int PORT = 8080;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(PORT);
                HostActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtPort.setText(""+serverSocket.getLocalPort());
                    }
                });


                while(true) {
                    final Socket socket = serverSocket.accept();
                    Log.d(WiFiDirectActivity.TAG, "Server Accepted");
                    HostActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtStatus.setText("Connected from " + socket.getInetAddress() + ":" + socket.getPort());
                        }

                    });
                    SocketServerReplyThread socketServerReplyThread = new SocketServerReplyThread(socket);
                    socketServerReplyThread.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private class SocketServerReplyThread extends Thread {
     private Socket hostThreadSocket;

        public SocketServerReplyThread(Socket hostThreadSocket) {
            this.hostThreadSocket = hostThreadSocket;
        }

        @Override
        public void run() {
            OutputStream outputStream;
            final String message = "Connected";
            try{
                outputStream = hostThreadSocket.getOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                printStream.print(message);
                printStream.close();

                HostActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtStatus.setText(txtStatus.getText() + " " + message);
                    }
                });
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    private String getIpAddress() {
        String ip = "";
        try{
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            while(enumeration.hasMoreElements()) {
                NetworkInterface networkInterface = enumeration.nextElement();
                Enumeration<InetAddress> enumInet = networkInterface.getInetAddresses();

                while(enumInet.hasMoreElements()) {
                    InetAddress inetAddress = enumInet.nextElement();

                    if(inetAddress.isSiteLocalAddress()) {
                        ip += inetAddress.getHostAddress() + "\n";
                    }
                }
            }
        }
        catch(SocketException e) {
            e.printStackTrace();
        }

        return ip;
    }
}
