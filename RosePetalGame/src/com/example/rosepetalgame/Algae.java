package com.example.rosepetalgame;

public class Algae extends Sprite {
	public Algae() {
		setWidth(10.f);
		setHeight(10.f);
		
		setUV(448,512,32,32);
		setAnimation(0,3,5);
		collisionSetVals(5,5,5,5);
	}
}
