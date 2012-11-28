package com.example.rosepetalgame;
import java.util.LinkedList;
import java.util.Vector;

import com.samsung.spensdk.applistener.SPenTouchListener;

import android.content.Context;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;

final class TouchTracker
implements SPenTouchListener
{
	 private GameLogic game_logic_;  //The render controller.
	 private Player player_; //The physical player character.
	 private boolean pressed_on_player; // boolean to see if player has been touched.
	 private boolean movement_locked_;
	 private final float pather_gapsize = 10.f;
	 private boolean button_pressed_;
	 private boolean tapped_;
	 private MediaPlayer m_mediaPlayer;
	 private Context ActivityContext;
	 //This is the value of the distance there must be between pathers. 
	 
	 private final int max_pathers = 39;
	 //Maximum amount of pathers that must be on the screen.
	 LinkedList<Pather> pather_list; //Saves all pathers for future reference.
	 
	public TouchTracker(GameLogic logic,Player player,Context context) { 
		game_logic_ = logic;
		player_ = player;
		pressed_on_player = false;
		pather_list = new LinkedList<Pather>();
		button_pressed_ = false;
		ActivityContext = context;
		movement_locked_ = false;
		tapped_ = false;
		
		m_mediaPlayer = new MediaPlayer();
		m_mediaPlayer = MediaPlayer.create(ActivityContext,R.raw.drop);
		
	}

	public boolean onTouchPen(View v, MotionEvent event) {
		float[] coords = game_logic_.getRender().unproject(event.getX(),event.getY());
		
		float worldx = coords[0] + game_logic_.camera_.getX();
		float worldy = coords[1] + game_logic_.camera_.getY();
		
		if (event.getAction() == MotionEvent.ACTION_DOWN) 
		{
			
			EnemyControl.mousex = worldx;
			EnemyControl.mousey = worldy;
			if (!tapped_) {
				game_logic_.enemy_control.killEnemy();
			} else {
				tapped_ = true;
			}
			
			
			if (pather_list.size() == 0) {
				movement_locked_ = false;
			}
			
			if (player_.ifCollision(worldx,worldy))
			{
				pressed_on_player = true; //Player has been clicked on, state this.
			} 
				else
			{
				pressed_on_player = false; //Player has not been clicked on.
			}
			
			m_mediaPlayer.start();
			
		}
		else if (event.getAction() == MotionEvent.ACTION_MOVE) { 
			// If this is the player tracing finger on screen.
			if (pressed_on_player && !movement_locked_) { //Player must be starting point for path.
				if (pather_list.size() == 0) {  
				//If list is empty, automatically make new item
					if (!game_logic_.barrier_control.resolveBarrierCollisions(worldx,worldy)) {
						//Ensure no barriers at these X/Y coordinates.
						Pather temp_pather = new Pather(game_logic_);
						temp_pather.x_ = player_.getX();
						temp_pather.y_ = player_.getY();
						game_logic_.addSprite(temp_pather);
						pather_list.add(temp_pather);
						addPathers(worldx,worldy);
					}
				} else if (calcCheckDistance(pather_list.getLast(),worldx,worldy)) {
				// Or there is a sizeable distance between the this point and the last point, then make a new item. 
					addPathers(worldx,worldy);
				}
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) { 
			tapped_ = false;
			//When user removes their finger.
			player_.followPoints(pather_list); 
			if (pather_list.size() != 0) {
				movement_locked_ = true;
			}
			
			
			//Passes the pather list to the player. Player will delete list elements when done with them.
		}
		return true;
	}
	
	private boolean calcCheckDistance(Pather oldpather,float X,float Y) {
		//Returns whether the gap between pathers is larger than the required minimum
		//gap size aquired from the pather_gapsize variable.
		float xlength = X - oldpather.getX();
		float ylength = Y - oldpather.getY();
		float hyp = (xlength*xlength) + (ylength*ylength);
		return (hyp > (pather_gapsize*pather_gapsize)) ? true : false; 
	}
	
	
	private void addPathers(float touchX,float touchY) {
		//Adds the needed set of pathers between the last drawn pather and the newest point.
		Pather lastpather = pather_list.getLast(); //Get the last draw pather.
		float angle = 0;
		float gradient = 0;
		
		
		{
			float X = lastpather.getX() - touchX;
			float Y = lastpather.getY() - touchY;
			gradient = Y/X;
			angle = (float)Math.atan(gradient); //Discover our angle from last pather to X/Y value.
		} //Work out the angle and gradient of the line between the two points.
		if (angle != 0) {
			float xamount = FloatMath.cos(angle)*pather_gapsize; 
			//What is the x increment that'll appear between each pather.
			if (touchX < lastpather.getX()) {
				xamount = -xamount;
			} //Negate the increment if the touch X is to the left of the old point.
			
			float xpoint = lastpather.getX() + xamount; // Starting X coordinate of the newest pather.
			
			while ((xamount > 0 && xpoint < touchX) // Draw pathers from Left->Right until they overtake touch point.
					|| (xamount < 0 && xpoint > touchX)) { //Or draw pathers Right<-Left until they overtake touch point.
				if (pather_list.size() > max_pathers) {
					break;
				} //If there are too many pathers on screen do not draw any more.
				
				float ypoint = gradient*(xpoint - touchX) + touchY;

				//Using good old "y - b = m(x-a)" equation. Calculate the corresponding y coordinate to each increment of x. 
				
				if (!game_logic_.barrier_control.resolveBarrierCollisions(xpoint,ypoint)) {Pather temp_path = new Pather(game_logic_);
				//Ensure there is no barrier in the way.	
					temp_path.setX(xpoint);
					temp_path.setY(ypoint); //Make a new pather for the X/Y we've calculated, place it in this brilliant point.
					game_logic_.addSprite(temp_path); //Add this pather to the screen.
					pather_list.add(temp_path); //Record pather for future reference.
					xpoint += xamount; //Move onto next pather positon.
				} else {
					return; //Don't allow any drawing through barriers.
				}
			}
		} else {
			//TODO: Create a function that allows for the creation of pathers in a vertical line. 
			//(Otherwise while loop will be infinite).
		}
	}

	@Override
	public void onTouchButtonDown(View arg0, MotionEvent arg1) {
		button_pressed_ = true;
	}

	@Override
	public void onTouchButtonUp(View arg0, MotionEvent arg1) {
		button_pressed_ = false;
	}

	@Override
	public boolean onTouchFinger(View v, MotionEvent event) {
		return onTouchPen(v,event);
	}

	@Override
	public boolean onTouchPenEraser(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}
}