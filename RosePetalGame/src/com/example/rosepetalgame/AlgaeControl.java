package com.example.rosepetalgame;

import java.util.LinkedList;

import android.content.Context;
import android.media.MediaPlayer;

public class AlgaeControl {
	private LinkedList<Algae> algae_list_;
	private Player player_;
	private GameLogic game_logic_;
	private int algae_score_amount_;
	private MediaPlayer mediaPlayer_;
	
	public AlgaeControl(Player getplayer,GameLogic logic,Context context) {
		player_ = getplayer;
		game_logic_ = logic;
		algae_list_ = new LinkedList<Algae>();
		algae_score_amount_ = 10;
		mediaPlayer_ = new MediaPlayer();
		mediaPlayer_ = MediaPlayer.create(context,R.raw.win);
	}
	
	public Algae addAlgae(float X,float Y) {
		Algae tempAlgae = new Algae();
		tempAlgae.setX(X);	
		tempAlgae.setY(Y);
		algae_list_.add(tempAlgae);
		game_logic_.addSprite(tempAlgae);
		return tempAlgae;
	}
	
	public Algae addAlgae(Algae tempAlgae) {
		algae_list_.add(tempAlgae);
		game_logic_.addSprite(tempAlgae);
		return tempAlgae;
	}
	
	public void resolveAlgaeCollisions() {
		for (int i = 0;i < algae_list_.size();i++) {
			Algae tempAlgae = algae_list_.get(i);
			if (checkCollisionPlayerAlgae(tempAlgae)) {
				game_logic_.removeSprite(tempAlgae);
				algae_list_.remove(tempAlgae); //Remove a point from the list.
				i --; //Backtrack list.
				game_logic_.final_score += algae_score_amount_;
				mediaPlayer_.start();
			}
		}
	}
	
	
	public boolean checkCollisionPlayerAlgae(Algae temp_algae) {
		float player_rightbound = player_.getX()+player_.leftcollision_;
		float player_leftbound = 	player_.getX()-player_.rightcollision_;
		float player_topbound = player_.getY()-player_.topcollision_;
		float player_bottombound = player_.getY()+player_.bottomcollision_;
		
		float algae_rightbound = temp_algae.getX()+temp_algae.leftcollision_;
		float algae_leftbound = temp_algae.getX()-temp_algae.rightcollision_;
		float algae_topbound = temp_algae.getY()-temp_algae.topcollision_;
		float algae_bottombound = temp_algae.getY()+temp_algae.bottomcollision_;
					
		if (player_leftbound > algae_rightbound) {
			return false;
		}
		
		if (player_rightbound < algae_leftbound) {
			return false;
		}
		
		if (player_topbound > algae_bottombound) {
			return false;
		}
		
		if (player_bottombound < algae_topbound) {
			return false;
		}
		
		return true;
	}
	
}
