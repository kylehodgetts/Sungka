package com.kylehodgetts.sunka.model;

import com.kylehodgetts.sunka.uiutil.ShellDrawable;

/**
 * Created by CBaker on 13/11/2015.
 */
public class ShellReference {
    private ShellDrawable shell;
    private int x,y;

    public ShellReference(ShellDrawable shell) {
        this.shell = shell;
        this.x = 0;
        this.y = 0;
    }

    public void incrementX(){
        x++;
    }

    public void incrementY(){
        y++;
    }

    public void decrementX(){
        x--;
    }

    public void decrementY(){
        y--;
    }

    public ShellDrawable getShell() {
        return shell;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
