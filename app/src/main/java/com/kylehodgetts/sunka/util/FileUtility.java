package com.kylehodgetts.sunka.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Kyle Hodgetts
 * @version 1.0
 * Provides the functionality to save game files and read them from internal storage
 */
public class FileUtility {

    /**
     * Save a file to internal storage
     * @param context   Current Application Context
     * @param fileName  Name of the file to be saved
     * @param save      The object to be saved
     */
    public static void saveGame(Context context, String fileName, Object save) {
        File file = new File(context.getFilesDir(), fileName);
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
            outputStream.writeObject(save);
            outputStream.flush();
            outputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read file from internal storage
     * @param context   Current Application Context
     * @param fileName  Name of file to be loaded
     * @return          <code>GameState</code> found in file or new <code>GameState</code>
     *                  if one isn't found
     */
    public static Object readFromSaveFile(Context context, String fileName) {
        Object object = null;
        File file = new File(context.getFilesDir(), fileName);
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            object = objectInputStream.readObject();
            file.delete();
            inputStream.close();
            objectInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return object;
        }
        return object;
    }
}
