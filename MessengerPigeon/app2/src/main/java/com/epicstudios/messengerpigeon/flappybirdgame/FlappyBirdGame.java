package com.epicstudios.messengerpigeon.flappybirdgame;

import com.epicstudios.messengerpigeon.framework.Screen;
import com.epicstudios.messengerpigeon.framework.implementation.AndroidGame;

public class FlappyBirdGame extends AndroidGame {
	
	// LoadingScreen.
	@Override
    public Screen getInitScreen() {
        return new LoadingScreen(this);
	}
}
