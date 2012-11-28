package com.example.rosepetalgame;

import javax.microedition.khronos.opengles.GL10;

public class Floatilla extends Sprite {
	private float speed_;
	
	Floatilla() {
		speed_ = 3;
	}
	
	@Override
	public void draw(GL10 gl) {
		move();
		super.draw(gl); //Calls parent function
	}
	
	private void move() {
		x_ += speed_;
		rotation_ ++;
		
	}
	
}
