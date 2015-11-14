package com.kylehodgetts.sunka.util;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * A simple tuple of two elements
 */
public class Tuple2<X,Y> {
    private X x;
    private Y y;

    /**
     * Default constructor
     * @param x x value
     * @param y y value
     */
    public Tuple2(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    /** @return value of x */
    public X getX() {
        return x;
    }

    /** @return value of y */
    public Y getY() {
        return y;
    }
}
