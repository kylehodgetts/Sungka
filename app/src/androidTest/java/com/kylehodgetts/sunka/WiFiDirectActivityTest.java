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

    public void testPreConditions() throws Exception {
        assertNotNull(wiFiDirectActivity);
        assertNotNull(listView);
        assertNotNull(button);
    }

    public void testHostButton_Layout() {
        final View decorView = wiFiDirectActivity.getWindow().getDecorView();
        ViewAsserts.assertOnScreen(decorView, button);

        final ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
        assertEquals(layoutParams.width, WindowManager.LayoutParams.MATCH_PARENT);
        assertEquals(layoutParams.height, WindowManager.LayoutParams.WRAP_CONTENT);

        assertEquals(button.getText().toString(), "Host");
    }

    public void testHostButton_onClick() {
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

    public void testFoundServicesList_Layout() {
        final View decorView = wiFiDirectActivity.getWindow().getDecorView();
        ViewAsserts.assertOnScreen(decorView, listView);

        final ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();

        assertNotNull(layoutParams);
        assertEquals(layoutParams.width, WindowManager.LayoutParams.MATCH_PARENT);
        assertEquals(layoutParams.height, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void testFoundServicesList_FoundServicesAppear() {
        assertEquals(listView.getAdapter().getCount(), list.size());
        assertEquals(((NsdServiceInfo)listView.getItemAtPosition(0)).getServiceName(), nsdServiceInfo.getServiceName());
    }

    public void testFoundServicesList_ClickService() {
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

}
