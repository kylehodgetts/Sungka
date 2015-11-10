package com.kylehodgetts.sunka.controller.wifi;

import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kylehodgetts.sunka.R;

import java.util.List;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <-INPUT DESC->
 */
public class ServiceAdapter extends ArrayAdapter<NsdServiceInfo> {

    public ServiceAdapter(Context context, List<NsdServiceInfo> objects) {
        super(context, R.layout.row_device, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());

        View myView = inflater.inflate(R.layout.row_device, parent, false);

        String serviceName = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            serviceName = getItem(position).getServiceName();
        }

        ((TextView) myView.findViewById(R.id.deviceName)).setText(serviceName);

        return myView;
    }
}
