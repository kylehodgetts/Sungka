package com.kylehodgetts.sunka;

import android.app.Activity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        txtAddress = (TextView) findViewById(R.id.txtHostAddress);
        txtPort = (TextView) findViewById(R.id.txtHostPort);
        txtStatus = (TextView) findViewById(R.id.txtStatus);

        txtAddress.setText(getIpAddress());

        Thread serverSocketThread = new Thread(new SocketServerThread());
        serverSocketThread.start();
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
            final String message = "I AM HERE";
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
