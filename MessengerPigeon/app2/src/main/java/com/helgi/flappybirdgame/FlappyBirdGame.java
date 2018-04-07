package com.helgi.flappybirdgame;

import com.helgi.framework.Screen;
import com.helgi.framework.implementation.AndroidGame;

public class FlappyBirdGame extends AndroidGame {
	
	// LoadingScreen.
	@Override
    public Screen getInitScreen() {
        return new LoadingScreen(this);
	}
}
