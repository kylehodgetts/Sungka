package com.kylehodgetts.sunka.controller.wifi;

import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kylehodgetts.sunka.R;
import com.kylehodgetts.sunka.uiutil.Fonts;

import java.util.List;

/**
 * @author Kyle Hodgetts
 * @author Adam Chlupacek
 * @version 1.0
 *
 * Adapter that returns data view of a found service.
 */
public class ServiceAdapter extends ArrayAdapter<NsdServiceInfo> {

    /**
     * Constructor
     * @param context Current Application Context
     * @param objects List of <code>FoundService</code>s
     */
    public ServiceAdapter(Context context, List<NsdServiceInfo> objects) {
        super(context, R.layout.row_device, objects);
    }

    /**
     *
     * @param position      Position of item clicked
     * @param convertView
     * @param parent        parent <code>ViewGroup</code>
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());

        View device = inflater.inflate(R.layout.row_device, parent, false);

        String serviceName = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            serviceName = getItem(position).getServiceName();
        }

        TextView deviceName = (TextView) device.findViewById(R.id.deviceName);
        deviceName.setText(serviceName);
        deviceName.setTypeface(Fonts.getButtonFont(getContext()));
        deviceName.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        return device;
    }
}
