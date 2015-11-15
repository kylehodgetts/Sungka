package com.kylehodgetts.sunka;

import android.test.ActivityInstrumentationTestCase2;

import com.kylehodgetts.sunka.view.HostActivity;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Kyle Hodgetts
 * @version 1.0
 * Responsible for testing the host activity
 */
public class HostActivityTest extends ActivityInstrumentationTestCase2<HostActivity> {

    private HostActivity hostActivity;

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
