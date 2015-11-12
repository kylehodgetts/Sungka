package com.kylehodgetts.sunka.controller.wifi;

import java.net.InetAddress;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * Contains the name, address and port of a found service
 */
public class FoundService {

    private String name;
    private InetAddress address;
    private int port;

    /**
     * Constructs a new found service
     * @param name      The service's name
     * @param address   The service's <code>InetAddress</code>
     * @param port      The service's registered port
     */
    public FoundService(String name, InetAddress address, int port) {
        this.name = name;
        this.address = address;
        this.port = port;
    }
    
    /**
     *
     * @return The service name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return The service address
     */
    public InetAddress getAddress() {
        return address;
    }

    /**
     *
     * @return The service port
     */
    public int getPort() {
        return port;
    }
}
