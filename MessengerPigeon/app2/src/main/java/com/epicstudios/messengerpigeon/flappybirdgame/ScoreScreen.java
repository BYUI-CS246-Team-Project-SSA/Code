package com.epicstudios.messengerpigeon.flappybirdgame;

import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;

import com.epicstudios.messengerpigeon.framework.Game;
import com.epicstudios.messengerpigeon.framework.Graphics;
import com.epicstudios.messengerpigeon.framework.Screen;
import com.epicstudios.messengerpigeon.framework.Input.TouchEvent;


public class ScoreScreen extends Screen {
	// Target Parameters
	Paint paint; // This is the font in use
	
    public ScoreScreen(Game game) {
        super(game);
        
        // Configure all we want for font in scores
        paint = new Paint();
        paint.setTextSize(60);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
    }

    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        // When you press somewhere on the screen, len> 0, and the drive is executed
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) { 
            	
            	///////////////
              	// BACK button
              	///////////////
                if (inBounds(event, gameWidth/1.8, gameHeight/1.33, res*230, res*77)) {
                    // Back to MainMenuScreen
                    game.setScreen(new MainMenuScreen(game));              
                }
            }
        }
    }


    private boolean inBounds(TouchEvent event, double x, double y, double width, double height) {
        if (event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1)
            return true;
        else
            return false;
    }

    @Override
    public void paint(float deltaTime) {
    	Graphics g = game.getGraphics();
    	
    	////////////////////////////////////////////////////////////////
    	// Here we have all our ScoreScreen elements, please, etc.
    	////////////////////////////////////////////////////////////////
    	
    	// The scale starts at the top left of the window
        // ie. grid. The scale is 800 x 1280 on S2
    	
    	g.drawImage(Assets.background, matrix, 0, 0, 0, res*1.04, false);
    	g.drawImage(Assets.backgroundBase, matrix, 0, gameHeight/1.1445, 0, res*1.04*0.9615, false);
    	//g.drawImage(Assets.scoreSmall, matrix, gameWidth/4, gameHeight/1.3, 0, res, false);
        g.drawImage(Assets.flappyBirdLogo, matrix, gameWidth/13.71, gameHeight/16, 0, res, false);
        g.drawImage(Assets.bird2, matrix, gameWidth/1.16, gameHeight/10, 0, res, true);
        //g.drawImage(Assets.back, matrix, gameWidth/1.8, gameHeight/1.33, 0, res, false);
        
        // here we insert a plug that will insert all the points
    }


    @Override
    public void pause() {
    }


    @Override
    public void resume() {


    }


    @Override
    public void dispose() {


    }


    @Override
    public void backButton() {
        //Display "Exit Game?" Box
    }
}
