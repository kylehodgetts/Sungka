package com.kylehodgetts.sunka.util;

import android.test.InstrumentationTestCase;
import android.test.mock.MockContext;

import com.kylehodgetts.sunka.BoardActivity;
import com.kylehodgetts.sunka.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Kyle Hodgetts
 * @version 1.0
 * Responsible for testing the <code>FileUtility</code> class
 */
public class FileUtilityTest extends InstrumentationTestCase {
    private MockContext context;
    private File testFile;
    private static final String FILE_NAME = "TestFile";

    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;

    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    /**
     * Set up a Mocked Context and a file to test saving and reading
     * @throws Exception
     */
    @Before
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = new MockContext();
        testFile = new File(context.getFilesDir(), FILE_NAME);
    }

    /**
     * After each test has completed, delete the test file
     * @throws Exception
     */
    @After
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        if(testFile.exists()){
            testFile.delete();
        }
    }

    /**
     * Assert that, upon saving a file, the contents of that file are what is expected
     * @throws Exception
     */
    @Test
    public void testSaveGame() throws Exception {
        String expected = "TEST";
        FileUtility.saveGame(context, FILE_NAME, expected);
        fileInputStream = new FileInputStream(testFile);
        objectInputStream = new ObjectInputStream(fileInputStream);
        String actual = objectInputStream.readObject().toString();
        fileInputStream.close();
        objectInputStream.close();
        assertTrue(expected.equals(actual));
    }

    /**
     * Assert that, upon reading from a file, what is returned is expected
     * @throws Exception
     */
    @Test
    public void testReadFromSaveFile() throws Exception {
        fileOutputStream = new FileOutputStream(testFile);
        objectOutputStream = new ObjectOutputStream(fileOutputStream);
        String expected = "TEST";
        objectOutputStream.writeObject(expected);
        objectOutputStream.close();
        fileOutputStream.close();

        String actual = (String) FileUtility.readFromSaveFile(context, FILE_NAME);
        assertTrue(actual.equals(expected));
    }

    @Test
    public void testPlaySound() throws Exception {
        assertFalse(FileUtility.playSound(getInstrumentation().getContext(), 0));
        assertTrue(FileUtility.playSound(getInstrumentation().getContext(), R.raw.applause));
    }
}