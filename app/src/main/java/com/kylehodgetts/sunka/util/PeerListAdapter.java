package com.kylehodgetts.sunka.util;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.kylehodgetts.sunka.R;

import java.util.ArrayList;

/**
 * Created by kylehodgetts on 29/10/2015.
 */
public class PeerListAdapter extends ArrayAdapter<WifiP2pDevice> {

    public PeerListAdapter(Context context, ArrayList<WifiP2pDevice> peers) {
        super(context, R.layout.row_device, peers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(super.getContext());
        View row = inflater.inflate(R.layout.row_device, parent, false);

        String deviceName = getItem(position).deviceName;
        TextView txtDeviceName = (TextView) row.findViewById(R.id.deviceName);
        txtDeviceName.setText(deviceName);

        return row;
    }
}
