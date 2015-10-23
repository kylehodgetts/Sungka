package com.kylehodgetts.sunka;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @author Kyle Hodgetts
 * @author Jonathan Burton
 * @version 1.0
 * Adapter for PeerList Listview
 */
public class PeerListAdapter extends ArrayAdapter<WifiP2pDevice> {

    public PeerListAdapter(Context context, ArrayList<WifiP2pDevice> values){
        super(context, R.layout.peer_row, values);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(super.getContext());

        View row = inflater.inflate(R.layout.peer_row, parent, false);
        TextView deviceName = (TextView) row.findViewById(R.id.deviceName);
        deviceName.setText(getItem(position).deviceName);
        return row;
    }
}
