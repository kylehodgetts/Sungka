package com.kylehodgetts.sunka.controller.wifi;

import junit.framework.TestCase;

import org.junit.BeforeClass;
import org.junit.Test;

import java.net.Socket;

/**
 * @author Kyle Hodgetts
 * @version 1.0
 * Test solution responsible for testing the <code>SingletonSocket</code> singleton class
 */
public class SingletonSocketTest extends TestCase {

    private SingletonSocket singletonSocket;

    /**
     * Initialise the socket
     * @throws Exception
     */
    @BeforeClass
    public void setUp() throws Exception {
        super.setUp();
        singletonSocket.setSocket(new Socket());
    }

    /**
     * Assert that the socket is returned
     * @throws Exception
     */
    @Test
    public void testGetSocket() throws Exception {
        assertNotNull(singletonSocket.getSocket());
    }

    /**
     * Assert that setting a socket enables a valid to be returned
     * Setting a socket to null invokes the class's private constructor
     * @throws Exception
     */
    public void testSetSocket() throws Exception {
        singletonSocket.setSocket(null);
        assertNotNull(singletonSocket.getSocket());
        singletonSocket.setSocket(new Socket());
        assertNotNull(singletonSocket.getSocket());
    }
}
