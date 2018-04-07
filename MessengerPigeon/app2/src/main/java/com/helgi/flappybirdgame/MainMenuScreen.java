package com.helgi.flappybirdgame;

import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;

import com.helgi.framework.Game;
import com.helgi.framework.Graphics;
import com.helgi.framework.Image;
import com.helgi.framework.Screen;
import com.helgi.framework.Input.TouchEvent;

public class MainMenuScreen extends Screen {
	// Target Parameters
	Paint font; // This is the font in use
	
    public MainMenuScreen(Game game) {
        super(game);
        
        /* An example of font settings
        font = new Paint();
        font.setTextSize(30);
        font.setTextAlign(Paint.Align.CENTER);
        font.setAntiAlias(true);
        font.setColor(Color.WHITE);
        
        then it is done for example:
        Graphics g = game.getGraphics();
         g.drawString("Tap each side of the screen to", 400, 650, paint); */
    }


    /* When LoadingScreen calls MainMenuScreen, this method runs. It constantly calls
     * update process until something happens
     */
    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        // When you press somewhere on the screen, len> 0, and the drive is executed
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
            	
            	/////////////////////////////////////////////////////////
            	// Here's what happens when you press keys, etc.
            	/////////////////////////////////////////////////////////
            	
            	// If the touchscreen detects contact on a surface having a hub (0.0), width 250 and height 250
                // Then it is called GameScreen which is the game itself running. (0,0) is at the top left of
                // Android device window.
                // Must set this to the correct value, each key, etc.
            	
            	///////////////
            	// START button
            	///////////////
                if (inBounds(event, gameWidth/6, gameHeight/1.33, res*230, res*77)) {
                	// Start playback
                    game.setScreen(new GameScreen(game));              
                }
                /*///////////////
              	// SCORE button
              	///////////////
                  if (inBounds(event, gameWidth/1.8, gameHeight/1.33, res*230, res*77)) {
                  	// Start playback
                      game.setScreen(new ScoreScreen(game));              
                  }*/
            }
        }
    }


    private boolean inBounds(TouchEvent event, double x, double y, double width,
            double height) {
        if (event.x > x && event.x < x + width - 1 && event.y > y
                && event.y < y + height - 1)
            return true;
        else
            return false;
    }


    @Override
    public void paint(float deltaTime) {
    	Graphics g = game.getGraphics();
    	
    	////////////////////////////////////////////////////////////////
    	// Here we have all our MainMenu elements, thank you etc.
    	////////////////////////////////////////////////////////////////
    	
    	// The scale starts at the top left of the window
        // ie. grid. The scale is 800 x 1280 on S2
    	
        g.drawImage(Assets.background, matrix, 0, 0, 0, res*1.04, false);
        g.drawImage(Assets.backgroundBase, matrix, 0, gameHeight/1.1445, 0, res*1.04*0.9615, false);
        g.drawImage(Assets.flappyBirdLogo, matrix, gameWidth/13.71, gameHeight/3.9, 0, res, false);
        g.drawImage(Assets.bird2, matrix, gameWidth/1.247, gameHeight/3.70, 0, res, false);
        g.drawImage(Assets.start, matrix, gameWidth/6, gameHeight/1.33, 0, res, false);
        //g.drawImage(Assets.score, matrix, gameWidth/1.8, gameHeight/1.33, 0, res, false);
        
        // Draws a filled box
        //g.drawRect (0, 0, 780, 1260, Color.BLACK);
        // This is an example of when a string is written on the screen.
        // What, location and font. The font font is a parameter converter in this class
        // g.drawString("Tap to play.", 240, 400, font);
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
