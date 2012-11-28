package com.example.rosepetalgame;

public class Background extends Sprite {
	public Background(int ScreenWidth,int ScreenHeight) {
		setUV(0,0,1024,512);
		width_ =  (float)ScreenWidth;
		height_ = (float)ScreenHeight;
		x_ = 0.f;
		y_ = 0.f;
	}
}
