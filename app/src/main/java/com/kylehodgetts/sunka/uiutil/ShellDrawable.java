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
 * Class to dynamically draw a shells for the game board as a {@link View} of it's own
 *
 * @author Charlie Baker
 * @version 1.1
 */
public class ShellDrawable extends View {

    // Colour array to randomly pick one of these to colour the shells as they the object is contructed.
    public static String[] colours = {"#00FF33", "#0066CC", "#00FF99", "#660099", "#CCFF00", "#FF6600", "#FF0099"};

    private ShapeDrawable shell;
    private int shellX;
    private int shellY;
    private int width;
    private int length;

    /**
     * Default constructor to create a coloured shell drawable.
     *
     * @param context current context/activity
     * @param x x position to be drawn at
     * @param y y position to be drawn at
     * @param width width of the shell
     * @param length height of the shell
     */
    public ShellDrawable(Context context, int x, int y, int width, int length) {
        super(context);
        this.shellX = x;
        this.shellY = y;
        this.width = width;
        this.length = length;
        setMinimumWidth(width);
        setMinimumHeight(length);

        shell = new ShapeDrawable(new OvalShape()); // creates new oval shape to represent the shell
        shell.setBounds(x, y, x + width, y + length);
    }

    /**
     * Get's the Shell's length/height
     * @return shell's height
     */
    public int getLength() {
        return length;
    }

    /**
     * Set's the Shell's length/height
     * @param length
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Get's the Shell's x value
     * @return
     */
    public int getShellX() {
        return shellX;
    }

    /**
     * Get's the Shell's width
     * @return an integer of the Shell's width
     */
    public int getShellWidth() {
        return width;
    }

    /**
     * Set's the width of the Shell
     * @param width new width value as an integer
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Get's the Shell's y value
     * @return the Shell's y value
     */
    public int getShellY() {
        return shellY;
    }

    /**
     * Set's the shell to a new colour
     * @param newColourInt integer representing a colour from the {@link Color} class
     */
    public void setColour(int newColourInt) {
        this.shell.getPaint().setColor(newColourInt);
    }

    /**
     * Set's the shell to a random colour
     */
    public void setRandomColour() {
        Random random = new Random();
        shell.getPaint().setColor(Color.parseColor(colours[random.nextInt(colours.length)]));  //sets colour randomly
    }

    /**
     * Get's the colour of the shell
     * @return the colour of the shell
     */
    public int getColour() {
        return shell.getPaint().getColor();
    }

    /**
     * Draw's the shell on a canvas
     * @param canvas canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        shell.draw(canvas);
    }
}
