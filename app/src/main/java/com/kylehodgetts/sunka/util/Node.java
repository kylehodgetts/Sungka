package com.kylehodgetts.sunka.util;

import java.util.ArrayList;


public class Node<T> {

    public ArrayList<Node<T>> children;
    public Node<T> parent;
    public int value;
}
