package com.kylehodgetts.sunka.controller.wifi;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;

/**
 * @author Kyle Hodgetts
 * @version 1.0
 * Test solution for the Found Service class
 */
public class FoundServiceTest extends TestCase {
    private FoundService foundService;

    private static final String EXPECTED_SERVICE_NAME = "Test Service";
    private static final InetAddress EXPECTED_ADDRESS = null;
    private static final int EXPECTED_PORT = 8888;

    /**
     * Initialise <code>FoundService</code>
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        foundService = new FoundService("Test Service", null, 8888);
    }

    /**
     * Assert that the correct service name is returned
     * @throws Exception
     */
    @Test
    public void testGetName() throws Exception {
        assertEquals(EXPECTED_SERVICE_NAME, foundService.getName());
    }

    /**
     * Assert that the correct service address is returned
     * @throws Exception
     */
    @Test
    public void testGetAddress() throws Exception {
        assertEquals(EXPECTED_ADDRESS, foundService.getAddress());
    }

    /**
     * Assert that the correct service port is returned
     * @throws Exception
     */
    @Test
    public void testGetPort() throws Exception {
        assertEquals(EXPECTED_PORT, foundService.getPort());
    }
}
