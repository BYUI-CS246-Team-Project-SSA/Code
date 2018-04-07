package com.epicstudios.messengerpigeon.flappybirdgame;

import android.graphics.Matrix;
import com.epicstudios.messengerpigeon.framework.Graphics; // In order to create Graphics g
import com.epicstudios.messengerpigeon.framework.Game; // to retrieve the drops like drawImage
import com.epicstudios.messengerpigeon.framework.implementation.Animation;
import com.epicstudios.messengerpigeon.framework.Screen;

public class Bird {
	  
	Graphics g;
	double res;
	double gameWidth;
	double gameHeight;
	Matrix matrix; // Consider how the pixels are arranged in the rotated image
	public double X;
	public double Y;
	private double rotation;
	private double YSpeed;
	private double rotSpeed;
	private Animation anim;
	
	/* X is [0;800]
	 * Y is [0;1280]
	 * YSpeed [-0.9; 0.6] and is Flappy's speed in y-direction
	 * rotation is in the range [-20&deg;;20&deg;] and is the strategy of Flappy
	 * rotSpeed is the speed at which Flappy is spinning
	 * lastUp is a time when up() was last activated
	 */
	
	public Bird (Game game, Screen scr) {
		g = game.getGraphics();
		gameWidth = scr.gameWidth;
		gameHeight = scr.gameHeight;
		res = scr.res;
	    X = gameWidth/2.055;
	    Y = gameHeight/2.33;
	    YSpeed = 0;
	    rotation = 0;
	    rotSpeed = 0;
	    matrix = new Matrix();
	    anim = new Animation();
	    anim.addFrame(Assets.bird1, 40);
		anim.addFrame(Assets.bird2, 40);
		anim.addFrame(Assets.bird3, 40);
	}

	public void update(float deltaTime) {
	    manageAccAndRot(deltaTime); // Acceleration downward and rotation
	    anim.update(deltaTime);  
	}
	
	public void draw() {
		g.drawImage(anim.getImage(), matrix, X-5, Y, rotation, res, true);
	}
	  
	public void up() {
	    if (Y > gameHeight/20) {
	      YSpeed = gameHeight/60; // Acceleration upwards
	      Y -= gameHeight/200;
	      rotSpeed = 18; 
	  }
	}
	  
	public void manageAccAndRot(float deltaTime) {
		if (YSpeed > -200) YSpeed -= 0.6*deltaTime;// Acceleration downward, YSpeed
		Y -= YSpeed;
		
		if (rotSpeed > -4) rotSpeed -= 0.24*deltaTime; // The maximum clockwise rotation speed is |-4|
	    if (-90 < rotation || rotSpeed > 0) rotation += rotSpeed;
	    if (rotation > 45) {rotation = 45; rotSpeed = 0;} // So the bird does not get in a corner
	}
	  
	  
	public double getX() {return X;}
	public double getY() {return Y;}
}