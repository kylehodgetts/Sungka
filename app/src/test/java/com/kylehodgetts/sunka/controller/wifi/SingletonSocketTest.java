package com.kylehodgetts.sunka.controller.wifi;

import junit.framework.TestCase;

import java.net.Socket;

/**
 * Created by kylehodgetts on 09/11/2015.
 */
public class SingletonSocketTest extends TestCase {

    private SingletonSocket singletonSocket;

    public void setUp() throws Exception {
        super.setUp();
        singletonSocket.setSocket(new Socket());
    }

    public void testGetSocket() throws Exception {
        assertNotNull(singletonSocket.getSocket());
    }

    public void testSetSocket() throws Exception {
        singletonSocket.setSocket(null);
        assertNull(singletonSocket.getSocket());
        singletonSocket.setSocket(new Socket());
        assertNotNull(singletonSocket.getSocket());
    }
}