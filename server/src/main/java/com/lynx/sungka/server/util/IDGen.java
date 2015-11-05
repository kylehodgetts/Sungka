package com.lynx.sungka.server.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <-INPUT DESC->
 */
public class IDGen {

    private static IDGen instance = new IDGen();

    private AtomicInteger offset = new AtomicInteger(0);

    private IDGen() {
    }

    private String generateId(){
        long now = System.currentTimeMillis()*10000;
        return (now + (offset.incrementAndGet()%10000)) + "-ID";
    }

    public static String genId(){
        return instance.generateId();
    }
}
