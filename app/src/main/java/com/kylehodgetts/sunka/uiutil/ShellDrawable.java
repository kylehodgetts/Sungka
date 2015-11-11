package com.kylehodgetts.sunka.uiutil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import java.util.Random;

/**
 * Created by CBaker on 08/11/2015.
 */
public class ShellDrawable extends View {

    public static String[] colours = {"#00FF33", "#0066CC", "#00FF99", "#660099", "#CCFF00", "#FF6600", "#FF0099"};

    private ShapeDrawable shell;
    private int shellX;
    private int shellY;
    private int width;
    private int length;


    public ShellDrawable(Context context, int x, int y, int width, int length) {
        super(context);
        this.shellX = x;
        this.shellY = y;
        this.width = width;
        this.length = length;
        setMinimumWidth(width);
        setMinimumHeight(length);

        shell = new ShapeDrawable(new OvalShape());
        shell.setBounds(x, y, x + width, y + length);
        Random random = new Random();
        shell.getPaint().setColor(Color.parseColor(colours[random.nextInt(colours.length)]));  //sets colour randomly
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getShellX() {
        return shellX;
    }

    public void setShellX(int x) {
        this.shellX = x;
    }

    public int getShellWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getShellY() {
        return shellY;
    }

    public void setShellY(int y) {
        this.shellY = y;
    }

    public void setColour(String newColour) {
        this.shell.getPaint().setColor(Color.parseColor(newColour));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        shell.draw(canvas);
    }
}
