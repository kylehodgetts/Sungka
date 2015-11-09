package com.kylehodgetts.sunka.controller.wifi;

import junit.framework.TestCase;

import java.net.InetAddress;

/**
 * @author Kyle Hodgetts
 */
public class FoundServiceTest extends TestCase {
    private FoundService foundService;

    private static final String EXPECTED_SERVICE_NAME = "Test Service";
    private static final InetAddress EXPECTED_ADDRESS = null;
    private static final int EXPECTED_PORT = 8888;

    public void setUp() throws Exception {
        super.setUp();
        foundService = new FoundService("Test Service", null, 8888);
    }

    public void testGetName() throws Exception {
        assertEquals(EXPECTED_SERVICE_NAME, foundService.getName());
    }

    public void testGetAddress() throws Exception {
        assertEquals(EXPECTED_ADDRESS, foundService.getAddress());
    }

    public void testGetPort() throws Exception {
        assertEquals(EXPECTED_PORT, foundService.getPort());
    }
}
