package com.epicstudios.messengerpigeon.framework;

import com.epicstudios.messengerpigeon.framework.Graphics.ImageFormat;
import android.graphics.Bitmap;

public interface Image {

	public int getWidth();
    public int getHeight();
    public ImageFormat getFormat();
    public void dispose();
    public Bitmap getBitmap();
}
