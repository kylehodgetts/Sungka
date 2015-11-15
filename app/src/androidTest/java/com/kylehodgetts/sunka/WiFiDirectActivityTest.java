package com.kylehodgetts.sunka;

import android.app.Instrumentation;
import android.net.nsd.NsdServiceInfo;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import com.kylehodgetts.sunka.controller.wifi.ServiceAdapter;
import com.kylehodgetts.sunka.view.HostActivity;
import com.kylehodgetts.sunka.view.WiFiDirectActivity;

import org.junit.BeforeClass;
import org.junit.Test;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Kyle Hodgetts
 * @version 1.0
 * Tests the wifi direct activity's behaviours
 */
public class WiFiDirectActivityTest extends ActivityInstrumentationTestCase2<WiFiDirectActivity> {

    private List<NsdServiceInfo> list;
    private final NsdServiceInfo nsdServiceInfo = new NsdServiceInfo();

    private WiFiDirectActivity wiFiDirectActivity;
    private ListView listView;
    private Button button;

    private Instrumentation.ActivityMonitor activityMonitor;

    public WiFiDirectActivityTest(){
        super(WiFiDirectActivity.class);
    }

    @BeforeClass
    @Override
    public void setUp() throws Exception {
        super.setUp();
        wiFiDirectActivity = getActivity();
        listView = (ListView) wiFiDirectActivity.findViewById(R.id.list_found_services);
        button = (Button) wiFiDirectActivity.findViewById(R.id.btnHost);

        list = new ArrayList<>();
        nsdServiceInfo.setServiceName("TestService");
        nsdServiceInfo.setHost(InetAddress.getLocalHost());
        nsdServiceInfo.setPort(8888);
        list.add(nsdServiceInfo);
        final ServiceAdapter serviceAdapter = new ServiceAdapter(wiFiDirectActivity, list);
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                listView.setAdapter(serviceAdapter);
            }
        });
    }

    /**
     * Assert that view components are not null post onCreate execution
     * @throws Exception e
     */
    @Test
    public void testPreConditions() throws Exception {
        assertNotNull(wiFiDirectActivity);
        assertNotNull(listView);
        assertNotNull(button);
    }

    /**
     * Assert that the button layout constraints hold
     * @throws Exception
     */
    @Test
    public void testHostButton_Layout() throws Exception {
        final View decorView = wiFiDirectActivity.getWindow().getDecorView();
        ViewAsserts.assertOnScreen(decorView, button);

        final ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
        assertEquals(layoutParams.width, WindowManager.LayoutParams.MATCH_PARENT);
        assertEquals(layoutParams.height, WindowManager.LayoutParams.WRAP_CONTENT);

        assertEquals(button.getText().toString(), "Host");
    }

    /**
     * Assert that, upon press of the host game button, it launches the host activity
     * @throws Exception
     */
    @Test
    public void testHostButton_onClick() throws Exception {
        activityMonitor = new Instrumentation.ActivityMonitor(HostActivity.class.getName(), null, false);
        getInstrumentation().addMonitor(activityMonitor);
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                button.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();
        HostActivity hostActivity = (HostActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        assertNotNull(hostActivity);
        hostActivity.finish();
    }

    /**
     * Assert that the found services list layout constraints hold
     * @throws Exception
     */
    @Test
    public void testFoundServicesList_Layout() throws Exception {
        final View decorView = wiFiDirectActivity.getWindow().getDecorView();
        ViewAsserts.assertOnScreen(decorView, listView);

        final ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();

        assertNotNull(layoutParams);
        assertEquals(layoutParams.width, WindowManager.LayoutParams.MATCH_PARENT);
        assertEquals(layoutParams.height, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    /**
     * Assert that found services appear in the found services list
     * @throws Exception
     */
    @Test
    public void testFoundServicesList_FoundServicesAppear() throws Exception {
        assertEquals(listView.getAdapter().getCount(), list.size());
        assertEquals(((NsdServiceInfo)listView.getItemAtPosition(0)).getServiceName(), nsdServiceInfo.getServiceName());
    }

    /**
     * Assert that, upon click of a found service, the client connection task is running.
     * @throws Exception
     */
    @Test
    public void testFoundServicesList_ClickService() throws Exception{
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                listView.performItemClick(listView.getAdapter().getView(0, null, null),
                        0, listView.getAdapter().getItemId(0));

            }
        });
        getInstrumentation().waitForIdleSync();
        assertTrue(wiFiDirectActivity.isClientTaskRunning());
    }

    /**
     * Assert that the discovery listener has been initialised in order to discover services
     * @throws Exception
     */
    @Test
    public void testInitialiseDiscoveryListener() throws Exception {
        assertTrue(wiFiDirectActivity.isDiscoveryListenerInitialised());
    }

    /**
     * Assert that, on pause, the discovery listener is unititialised to conserve resources
     */
    @Test
    public void testOnPause() {
        getInstrumentation().callActivityOnPause(wiFiDirectActivity);
        getInstrumentation().waitForIdleSync();
        assertFalse(wiFiDirectActivity.isDiscoveryListenerInitialised());
    }

    /**
     * Assert that, on resume of the application, the discovery listener is reinitialised
     */
    @Test
    public void testOnResume() {
        getInstrumentation().callActivityOnResume(wiFiDirectActivity);
        getInstrumentation().waitForIdleSync();
        assertTrue(wiFiDirectActivity.isDiscoveryListenerInitialised());
    }
}
