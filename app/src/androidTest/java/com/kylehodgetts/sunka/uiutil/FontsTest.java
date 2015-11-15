package com.kylehodgetts.sunka.uiutil;

import android.graphics.Typeface;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;

/**
 * @author Kyle Hodgetts
 * @version 1.0
 * Responsible for testing the <code>Fonts</code> utility class
 */
public class FontsTest extends AndroidTestCase {
    private MockContext context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = new MockContext();
    }

    public void testGetTitleFont() throws Exception {
        Typeface expected = Typeface.createFromAsset(getContext().getAssets(), String.format("fonts/%s", "fordscript.ttf"));
        Typeface actual = Fonts.getTitleFont(getContext());
        assertEquals(expected.getStyle(), actual.getStyle());
    }

    public void testGetButtonFont() throws Exception {
        Typeface expected = Typeface.createFromAsset(getContext().getAssets(), String.format("fonts/%s", "fordscript.ttf"));
        Typeface actual = Fonts.getTitleFont(getContext());
        assertEquals(expected.getStyle(), actual.getStyle());
    }
}