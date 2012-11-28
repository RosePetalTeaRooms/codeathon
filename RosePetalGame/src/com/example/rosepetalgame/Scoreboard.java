package com.example.rosepetalgame;

import javax.microedition.khronos.opengles.GL10;

 public class Scoreboard extends Sprite
 {
	
	 private int score_;
	 private int X = 0;
	 private int Y = 832;
	 private int w = 64;
	 private final float screenx_ = 20.f;
	 private final float screeny_ = 90.f;
	 private final float fontsize_ = 24.f;
	 private GameLogic game_logic_;
	 private int units_;
	 private int tens_;
	 private int hundreds_;
	
	 private Sprite tensview_;
	 private Sprite unitsview_;
	 private Sprite hundredsview_;
	 
	 
	 public Scoreboard(GameLogic logic) {
		 game_logic_ = logic;
		 tensview_ = new Sprite();
		 unitsview_ = new Sprite();
		 hundredsview_ = new Sprite();
		 game_logic_.addSprite(tensview_);
		 game_logic_.addSprite(unitsview_);
		 game_logic_.addSprite(hundredsview_);
		 unitsview_.setX(screenx_ + fontsize_*2);
		 unitsview_.setY(screeny_);
		 unitsview_.setWidth(fontsize_);
		 unitsview_.setHeight(fontsize_);
		 
		 tensview_.setX(screenx_ + fontsize_);
		 tensview_.setY(screeny_);
		 tensview_.setWidth(fontsize_);
		 tensview_.setHeight(fontsize_);
		 

		 hundredsview_.setX(screenx_);
		 hundredsview_.setY(screeny_);
		 hundredsview_.setWidth(fontsize_);
		 hundredsview_.setHeight(fontsize_);
	 }
	
	 public void getScore(int score) {
		 score_ = score;
		 units_ =	score_%10;
		 tens_ = 	score_/10;
		 hundreds_ = score_/100;
	 }
	
	
	 @Override 
	 public void draw(GL10 gl) {
		 setBoxScore(unitsview_,units_);
		 setBoxScore(tensview_,tens_);
		 setBoxScore(hundredsview_,hundreds_);
	 	super.draw(gl);
	 
	 }
	 
	 public void setBoxScore(Sprite temp_sprite,int val) {
		 temp_sprite.setUV(X+w*val,Y,w,w);
	 }
	 
	 
 }