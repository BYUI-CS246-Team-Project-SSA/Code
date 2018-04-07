package com.epicstudios.messengerpigeon.framework;

import android.graphics.Paint;

public interface Game {
	// �ll �essi f�ll �arf
	
	public Audio getAudio();

    public Input getInput();

    public FileIO getFileIO();

    public Graphics getGraphics();
    
    public Paint getFlappyFont();
    public Paint getFlappyFont_Ri();
    
    public Paint getFlappyFont_Bc();
    public Paint getFlappyFont_Bc_Ri();
    
    //public SharedPreferences getSharedPreferences(String a, int b);

    public void setScreen(Screen screen);

    public Screen getCurrentScreen();

    public Screen getInitScreen();
	
}
