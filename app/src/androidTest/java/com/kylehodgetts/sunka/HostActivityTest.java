package com.kylehodgetts.sunka;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.TextView;

import com.kylehodgetts.sunka.controller.wifi.SingletonSocket;

import junit.framework.TestCase;

import org.junit.BeforeClass;
import org.junit.Test;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * @author Kyle Hodgetts
 * @version 1.0
 * Responsible for testing the host activity
 */
public class HostActivityTest extends ActivityInstrumentationTestCase2<HostActivity> {

    private HostActivity hostActivity;
    private TextView labelHostAddress;
    private TextView txtHostAddress;
    private TextView labelHostPort;
    private TextView txtHostPort;
    private TextView txtStatus;

    public HostActivityTest() {
        super(HostActivity.class);
    }

    /**
     * Set up references to components
     * @throws Exception
     */
    @BeforeClass
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

    /**
     * Assert that view components are not null post onCreate execution
     * @throws Exception
     */
    @Test
    public void testPreconditions() throws Exception {
        assertNotNull(hostActivity);
        assertNotNull(labelHostAddress);
        assertNotNull(txtHostAddress);
        assertNotNull(labelHostPort);
        assertNotNull(txtHostPort);
        assertNotNull(txtStatus);
    }

    /**
     * Assert that the registration listener is initialised
     * so an online game can be published as a service
     * @throws Exception
     */
    @Test
    public void testInitialiseRegistrationListener() throws Exception {
        assertTrue(hostActivity.isRegistrationListenerInitialised());
    }

    /**
     * Assert that an online game is being registered as a service
     * so a client can connect
     * @throws Exception
     */
    @Test
    public void testRegisterService() throws Exception {
        assertTrue(hostActivity.isServiceInfoSet());
        assertTrue(txtHostAddress.getText().toString().equals(hostActivity.getIpAddress()));
    }

    /**
     * Assert that, on pause of the application, that the server socket has been closed
     * and the registration listener has been uninitialised to conserve resources.
     * @throws Exception
     */
    @Test
    public void testOnStop() throws Exception {
        getInstrumentation().callActivityOnStop(hostActivity);
        getInstrumentation().waitForIdleSync();
        assertFalse(hostActivity.isRegistrationListenerInitialised());
        assertFalse(hostActivity.isServerSocketInitialised());
    }
}
