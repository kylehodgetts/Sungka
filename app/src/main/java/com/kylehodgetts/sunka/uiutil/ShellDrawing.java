package com.kylehodgetts.sunka.uiutil;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;

import com.kylehodgetts.sunka.R;

public class ShellDrawing {

    public ShellDrawing( int numbShell){
        //Converts to bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.smallshell);
        //makes a copy of bitmap and copies it to the canvas
         Canvas canvas = new Canvas(bitmap.copy(Bitmap.Config.ARGB_8888, true));
    }
}
