package com.helgi.flappybirdgame;


import com.helgi.framework.Game;
import com.helgi.framework.Graphics;
import com.helgi.framework.Image;
import com.helgi.framework.Screen;
import com.helgi.framework.Graphics.ImageFormat;

public class LoadingScreen extends Screen {
    public LoadingScreen(Game game) {
        super(game);
    }

    // When the loading screen update is called MainMenuScreen
    // at the bottom of the case (ie this case only runs once)
    @Override
    public void update(float deltaTime) {
        /*
        Here we load the files that will be used in the application.
        * All files must be in the "assets" folder of the project (project).
        * Such a "loadast" game. Lets all pictures and sounds
        * in the parameters that are defined in Assets.java
        * Create a copy of the paint drops we need.
        * LoadingScreen took a "game" and want to get the graphics as a
        * getGraphics () provides.
        */

        Graphics g = game.getGraphics();
        
        //////////////////
        // MainMenuScreen
        //////////////////
        Assets.background 		= g.newImage("Background.png", ImageFormat.RGB565);
        Assets.backgroundBase	= g.newImage("BackgroundBase.png", ImageFormat.RGB565);
        Assets.flappyBirdLogo 	= g.newImage("MainMenuScreen/FlappyBirdLogo.png", ImageFormat.ARGB4444);
        Assets.start 			= g.newImage("MainMenuScreen/Start.png", ImageFormat.RGB565);
        
        
        ///////////////
        // ScoreScreen
        ///////////////
        //Assets.back 			= g.newImage("ScoreScreen/Back.png", ImageFormat.RGB565);
        //Assets.score 			= g.newImage("MainMenuScreen/Score.png", ImageFormat.RGB565);
        //Assets.scoreSmall		= g.newImage("ScoreScreen/ScoreSmall.png", ImageFormat.RGB565);
        
        //////////////
        // GameScreen
        //////////////
        Assets.getReady 		= g.newImage("GameScreen/GetReady.png", ImageFormat.ARGB4444);
        Assets.getReadyTap		= g.newImage("GameScreen/GetReadyTap.png", ImageFormat.ARGB4444);
        Assets.pauseButton		= g.newImage("GameScreen/PauseButton.png", ImageFormat.RGB565);

        Assets.bird1 			= g.newImage("GameScreen/Bird/Pigeon_down_trans.png", ImageFormat.ARGB4444);
        Assets.bird2 			= g.newImage("GameScreen/Bird/Pigeon_mid_trans.png", ImageFormat.ARGB4444);
        Assets.bird3 			= g.newImage("GameScreen/Bird/Pigeon_up_trans.png", ImageFormat.ARGB4444);
        Assets.pipe  			= g.newImage("GameScreen/Pipe.png", ImageFormat.ARGB4444);
        
        ////////////////
        // PausedScreen
        ////////////////
        Assets.playButton		= g.newImage("PausedScreen/PlayButton.png", ImageFormat.RGB565);
        Assets.paused			= g.newImage("PausedScreen/Paused.png", ImageFormat.ARGB4444);
        
        //////////////////
        // GameOverScreen
        //////////////////
        Assets.gameOver			= g.newImage("GameOverScreen/GameOver.png", ImageFormat.ARGB4444);
        Assets.GameOverScore	= g.newImage("GameOverScreen/GameOverScore.png", ImageFormat.RGB565);
        Assets.MedalNone		= g.newImage("GameOverScreen/MedalNone.png", ImageFormat.ARGB4444);
        Assets.MedalBronze		= g.newImage("GameOverScreen/MedalBronze.png", ImageFormat.ARGB4444);
        Assets.MedalSilver		= g.newImage("GameOverScreen/MedalSilver.png", ImageFormat.ARGB4444);
        Assets.MedalPremium		= g.newImage("GameOverScreen/MedalPremium.png", ImageFormat.ARGB4444);
        Assets.ok 				= g.newImage("GameOverScreen/Ok.png", ImageFormat.RGB565);
        
        // An example of creating a sound
        //Assets.click = game.getAudio().createSound("explode.ogg");

        // Here's the run after the game has been loaded.
        // ie, skips to MainMenuScreen.java
        game.setScreen(new MainMenuScreen(game));


    }


    @Override
    public void paint(float deltaTime) {
	    // Make a black square so the app for the app is not displayed
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
    	

    }
}
