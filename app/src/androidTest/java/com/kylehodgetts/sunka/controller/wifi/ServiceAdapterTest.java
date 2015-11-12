package com.kylehodgetts.sunka.controller.wifi;

import android.net.nsd.NsdServiceInfo;
import android.test.AndroidTestCase;
import android.view.View;
import android.widget.TextView;

import com.kylehodgetts.sunka.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kyle Hodgetts
 * @version 1.0
 * Reponsible for testing the service array adapter
 */
public class ServiceAdapterTest extends AndroidTestCase {

    private static final String SERVICE_NAME = "Test Service";
    private ServiceAdapter serviceAdapter;
    private NsdServiceInfo nsdServiceInfo;
    private List<NsdServiceInfo> list;

    public void setUp() throws Exception {
        super.setUp();
        nsdServiceInfo = new NsdServiceInfo();
        nsdServiceInfo.setServiceName(SERVICE_NAME);
        list = new ArrayList<>();
        list.add(nsdServiceInfo);
        serviceAdapter = new ServiceAdapter(getContext(), list);
    }

    public void testGetView() throws Exception {
        View v = serviceAdapter.getView(0, null, null);
        assertNotNull(v);
        TextView textView = (TextView) v.findViewById(R.id.deviceName);
        assertNotNull(textView);
        assertTrue(textView.getText().toString().equals(SERVICE_NAME));
    }
}