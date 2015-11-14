package com.kylehodgetts.sunka.uiutil;

import com.kylehodgetts.sunka.BoardActivity;

import junit.framework.TestCase;

import org.junit.BeforeClass;

/**
 * Test's the Shell Drawable view is instantiated correctly and its method to amend it work correctly.
 *
 * @author Charlie Baker
 * @version 1.0
 */
public class ShellDrawableTest extends TestCase {

    private ShellDrawable shellDrawable;    // field to hold the Shell Drawbable object

    /**
     * Desfault method to construct an instance of a shell to in order to perform tests on its methods
     * @throws Exception
     */
    @BeforeClass
    public void setUp() throws Exception {
        super.setUp();
        shellDrawable = new ShellDrawable(new BoardActivity(), 0, 0, 40, 20);
    }

    /**
     * Test's to see if the shell's get length method returns the correct value
     * @throws Exception
     */
    public void testGetLength() throws Exception {
        assertEquals(20, shellDrawable.getLength());
    }

    /**
     * Test's to see if the set method sets the shell's length to the new value
     * @throws Exception
     */
    public void testSetLength() throws Exception {
        shellDrawable.setLength(30);
        assertEquals(30, shellDrawable.getLength());
    }

    /**
     * Test's to see if the correct shell X coordinate is returned by the get method
     * @throws Exception
     */
    public void testGetShellX() throws Exception {
        assertEquals(0, shellDrawable.getShellX());
    }

    /**
     * Test's to see if the get shell width method returns the correct value
     *
     * @throws Exception
     */
    public void testGetShellWidth() throws Exception {
        assertEquals(40, shellDrawable.getShellWidth());
    }

    /**
     * Test's to see if the set width method correctly set's the shells width to the new value
     * @throws Exception
     */
    public void testSetWidth() throws Exception {
        shellDrawable.setWidth(50);
        assertEquals(50, shellDrawable.getShellWidth());
    }

    /**
     * Test's to see if the get shell Y position returns the correct value
     * @throws Exception
     */
    public void testGetShellY() throws Exception {
        assertEquals(0, shellDrawable.getShellY());
    }

    /**
     * Test's to check if shell is instantiated and drawn on a canvass
     * @throws Exception
     */
    public void testOnDraw() throws Exception {
        assertNotNull(shellDrawable);
    }
}