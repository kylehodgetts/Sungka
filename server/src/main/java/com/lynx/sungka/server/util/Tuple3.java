package com.lynx.sungka.server.util;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <-INPUT DESC->
 */
public class Tuple3<X,Y,Z> {

    private X x;
    private Y y;
    private Z z;


    public Tuple3(X x, Y y, Z z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public X getX() {
        return x;
    }

    public Y getY() {
        return y;
    }

    public Z getZ() {
        return z;
    }
}
