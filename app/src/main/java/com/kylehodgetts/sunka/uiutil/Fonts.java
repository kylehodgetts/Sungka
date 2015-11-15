package com.kylehodgetts.sunka.uiutil;

import android.content.Context;
import android.graphics.Typeface;

/**
 * @author Kyle Hodgetts
 * @version 1.0
 * Utility class to provide fonts to the UI.
 */
public class Fonts {
    /**
     *
     * @param c current application context
     * @return  title font from assets
     */
    public static Typeface getTitleFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), String.format("fonts/%s", "fordscript.ttf"));
    }

    /**
     *
     * @param c current application context
     * @return  button font from assets
     */
    public static Typeface getButtonFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), String.format("fonts/%s", "special_elite.ttf"));
    }
}
