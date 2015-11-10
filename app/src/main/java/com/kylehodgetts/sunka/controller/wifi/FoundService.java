package com.kylehodgetts.sunka.controller.wifi;

import java.net.InetAddress;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <-INPUT DESC->
 */
public class FoundService {

    private String name;
    private InetAddress address;
    private int port;


    public FoundService(String name, InetAddress address, int port) {
        this.name = name;
        this.address = address;
        this.port = port;
    }


    public String getName() {
        return name;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}
