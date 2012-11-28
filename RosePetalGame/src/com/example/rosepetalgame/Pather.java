package com.example.rosepetalgame;

public class Pather extends Sprite 
{
	
	private GameLogic game_logic_;
	
	public Pather(GameLogic logic) 
	{ //Pass in renderer that will be drawing this functon
		setWidth(4.f);
		setHeight(4.f);
		red_ = 1.f;
		green_ = 1.f;
		blue_ = 1.f;
		alpha_ = 1.f;
		game_logic_ = logic; //Gets a link to parent on startup.
		
		setUV(576,512,16,16);
		setAnimation(0,4,5);
		//collisionCropAmount(-20.f,-20.f);
	}
	
	public void removefromDraw() 
	{
		game_logic_.removeSprite(this); // Tell renderer to stop drawing me.
	}
}
