package com.lynx.sungka.server.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * The is generation for id
 */
public class IDGen {

    private static IDGen instance = new IDGen();

    private AtomicInteger offset = new AtomicInteger(0);

    /**
     * Private constructor to prevent multiple instances
     */
    private IDGen() {
    }

    /**
     * Generates the id
     * @return the generated id
     */
    private String generateId(){
        long now = System.currentTimeMillis()*10000;
        return (now + (offset.incrementAndGet()%10000)) + "-ID";
    }

    /**
     * Accessor the to the instance to generate id
     * @return the generated ID
     */
    public static String genId(){
        return instance.generateId();
    }
}
