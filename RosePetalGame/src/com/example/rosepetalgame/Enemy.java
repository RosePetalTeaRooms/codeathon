package com.example.rosepetalgame;

import java.util.LinkedList;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;

public class Enemy extends Player {
	
	public int lives = 3;
	
	Enemy()
	{
		setWidth(64.f);
		setHeight(96.0f);
		setUV(0,640,128,192);
		setAnimation(0,7,5);
		speed_ = 1.0f;
		xspeed_ = 0.f;
		yspeed_ = 0.f;
		setOrigin(0,height_/2); //Sets the rotation point about the head of the tadpole.
		collisionSetVals(30,30,30,30);
	}
	
	

	private float speed_,yspeed_,xspeed_;
	public boolean touchingplayer = false;
	public static float goalx_,goaly_;
	LinkedList<Pather> pather_ref_; // When player follows points. This will link to that list in memory.
	
	@Override
	public void draw(GL10 gl) {
		setTowardsPoint(goalx_,goaly_);
		moveTowardsPoint(); // If applicable follow path of points.
		super.draw(gl);
	}

	
	@Override
	public void setTowardsPoint(float X,float Y) { //Set point player moves to.
		float angle = 0.f;
		if (!(x_ == X)) { 
			//Ensuring we don't get undefined error with our trig.
			
			float xlength = x_ - X;
			float ylength = y_ - Y;
			
			angle = (float)Math.atan(ylength/xlength);
			
			xspeed_ = FloatMath.cos(angle)*speed_;
			yspeed_ = FloatMath.sin(angle)*speed_;
		
			if (X < x_) {
				xspeed_ = - xspeed_;
				yspeed_ = - yspeed_;
			}//Ensure that negative angles are also taken into account.
			
			// Set our sprite to follow a mouse at this angle.
		} else {
			if (y_ < Y) { //Settup for pure y direction (no x movement).
				yspeed_ = speed_;
			} else {
				yspeed_ = -speed_;
			}
		}
		
		goalx_ = X;
		goaly_ = Y;
		
		{
			float fixed_angle = (angle*180.f)/((float)Math.PI); 
			//Convert angle from radians to degrees.
			fixed_angle -= 90; //Sets 0 degrees to be facing north. 
			if (X < x_) {
				fixed_angle = fixed_angle - 180.f;
			} //Allows for angles on left side of X axis relative to player.
			
			rotation_ = fixed_angle;
		}
		
	}
	
	private boolean moveTowardsPoint() { //Goes to point player should move to.
	x_ += xspeed_;
	y_ += yspeed_; //Move towards goal point.
		
		if ((xspeed_ > 0) && (x_ >= goalx_)) {
			xspeed_ = 0;
			x_ = goalx_;
		} else if ((xspeed_ < 0) && (x_ <= goalx_)) {
			xspeed_ = 0;
			x_ = goalx_;
		} //If X is on or has passed the click point. Stop moving.
		
		if ((yspeed_ > 0) && (y_ >= goaly_)) {
			yspeed_ = 0;
			y_ = goaly_;
		} else if ((yspeed_ < 0) && (y_ <= goaly_)) {
			yspeed_ = 0;
			y_ = goaly_;
		} //If Y is on or has passed the click point. Stop moving.
		
		
		if (x_ != goalx_) {
			return false;
		}
		if (y_ != goaly_) {
			return false;
		}
		return true;
	}


}
