package com.kylehodgetts.sunka.uiutil;

import android.graphics.Typeface;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;

import org.junit.Test;

/**
 * @author Kyle Hodgetts
 * @version 1.0
 * Responsible for testing the <code>Fonts</code> utility class
 */
public class FontsTest extends AndroidTestCase {

    /**
     * Asserts that the correct font is returned
     * @throws Exception
     */
    @Test
    public void testGetTitleFont() throws Exception {
        Typeface expected = Typeface.createFromAsset(getContext().getAssets(), String.format("fonts/%s", "fordscript.ttf"));
        Typeface actual = Fonts.getTitleFont(getContext());
        assertEquals(expected.getStyle(), actual.getStyle());
    }

    /**
     * Asserts that the correct font is returned
     * @throws Exception
     */
    @Test
    public void testGetButtonFont() throws Exception {
        Typeface expected = Typeface.createFromAsset(getContext().getAssets(), String.format("fonts/%s", "fordscript.ttf"));
        Typeface actual = Fonts.getTitleFont(getContext());
        assertEquals(expected.getStyle(), actual.getStyle());
    }
}
