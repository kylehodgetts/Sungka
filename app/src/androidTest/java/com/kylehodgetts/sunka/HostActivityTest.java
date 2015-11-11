package com.kylehodgetts.sunka;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import junit.framework.TestCase;

/**
 * @author Kyle Hodgetts
 * @version 1.0
 * Responsible for testing the host activity
 */
public class HostActivityTest extends ActivityInstrumentationTestCase2<HostActivity> {

    private Activity hostActivity;
    private TextView labelHostAddress;
    private TextView txtHostAddress;
    private TextView labelHostPort;
    private TextView txtHostPort;
    private TextView txtStatus;

    public HostActivityTest() {
        super(HostActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        hostActivity = getActivity();
        labelHostAddress = (TextView) hostActivity.findViewById(R.id.labelHostAddress);
        txtHostAddress = (TextView) hostActivity.findViewById(R.id.txtHostAddress);
        labelHostPort = (TextView) hostActivity.findViewById(R.id.labelHostPort);
        txtHostPort = (TextView) hostActivity.findViewById(R.id.txtHostPort);
        txtStatus = (TextView) hostActivity.findViewById(R.id.txtStatus);
    }

    public void testPreconditions() {
        assertNotNull(hostActivity);
        assertNotNull(labelHostAddress);
        assertNotNull(txtHostAddress);
        assertNotNull(labelHostPort);
        assertNotNull(txtHostPort);
        assertNotNull(txtStatus);
    }

//    public void testRegisterService() throws Exception {
//
//    }
//
//    public void testInitialiseRegistrationListener() throws Exception {
//
//    }
}
