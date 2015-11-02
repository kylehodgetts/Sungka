package com.kylehodgetts.sunka.util;

import com.kylehodgetts.sunka.model.Board;

import java.util.ArrayList;

/**
 * Created by Peace on 02/11/2015.
 */
public class Tree<T> {

    public Node<T> root;

    public Tree(T rootBoard) {
        root = new Node<T>();
        root.value = 0;
        root.children = new ArrayList<Node<T>>();
    }

    public int put(int evaluation, Board[] path){

        Node current = root;

        for(int i=0;i<path.length;i++){
            if(current.children.get(i)==null){
                current.children.add(i, new Node());
            }
            current = (Node) current.children.get(i);
        }
        int ret = current.value;
        current.value = evaluation;
        return ret;
    }

}
