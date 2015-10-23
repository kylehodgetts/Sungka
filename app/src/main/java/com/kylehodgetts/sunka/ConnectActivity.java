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
    private MulticastSocket multicastSocket;
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
    }

    // Create a find peers task
    class SocketReceiver extends AsyncTask<Void, String, Void> {

        @Override
        protected void onPreExecute() {
            try {
                multicastSocket = new MulticastSocket(2222);
                group = InetAddress.getByName("225.4.5.6");
                multicastSocket.joinGroup(group);

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
                    multicastSocket.receive(packet);
                    publishProgress(packet.getData().toString());
                }
                multicastSocket.leaveGroup(group);
                multicastSocket.close();
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
}
