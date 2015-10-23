package com.kylehodgetts.sunka;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
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

    private SocketReceiver socketReciever;
    private SocketSender socketSender;
    private MulticastSocket receivingSocket;
    private MulticastSocket sendingSocket;
    private InetAddress group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_connect);

        peerList = (ListView) findViewById(R.id.peersList);
        devices = new ArrayList<String>();
        peerListAdapter = new PeerListAdapter(this, devices);
        peerList.setAdapter(peerListAdapter);
        socketReciever = new SocketReceiver();
        socketReciever.execute();
        socketSender = new SocketSender();
        socketSender.execute();
    }

    // Create a find peers task
    class SocketReceiver extends AsyncTask<Void, String, Void> {

        @Override
        protected void onPreExecute() {
            try {
                receivingSocket = new MulticastSocket(2222);
                group = InetAddress.getByName("225.4.5.6");
                receivingSocket.joinGroup(group);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                DatagramPacket packet;
                while(!isCancelled()) {
                     byte[] buf = new byte[256];
                    packet = new DatagramPacket(buf, buf.length);
                    receivingSocket.receive(packet);
                    publishProgress(packet.getData().toString());
                }
                receivingSocket.leaveGroup(group);
                receivingSocket.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            devices.add(values[0]);
            peerListAdapter.notifyDataSetChanged();
            return;
        }
    }

    class SocketSender extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            try {
                sendingSocket = new MulticastSocket();
                group = InetAddress.getByName("225.4.5.6");
                receivingSocket.joinGroup(group);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                DatagramPacket datagramPacket;
                while(!isCancelled()) {
                    byte[] bytes = "Hello".getBytes();
                    datagramPacket = new DatagramPacket(bytes, bytes.length);
                    datagramPacket.setPort(2222);
                    sendingSocket.send(datagramPacket);
                    Thread.sleep(5000);
                }
            }
            catch(IOException e){
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
