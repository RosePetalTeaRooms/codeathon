package com.example.rosepetalgame;

public class Barrier extends Sprite {
	public Barrier() {
		setWidth(40.f);
		setHeight(40.f);
		
		setUV(256,512,64,64);
		setAnimation(0,0,0);
		collisionSetVals(40,40,40,40);
	}
}
