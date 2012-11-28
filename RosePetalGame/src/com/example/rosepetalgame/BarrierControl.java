package com.example.rosepetalgame;

import java.util.LinkedList;

public class BarrierControl {
	LinkedList<Barrier> barrier_list_;
	Player player_;
	GameLogic game_logic_;
	public BarrierControl(Player getplayer,GameLogic logic) {
		player_ = getplayer;
		game_logic_ = logic;
		barrier_list_ = new LinkedList<Barrier>();
	}
	
	public Barrier addBarrier(float X,float Y) {
		Barrier tempBarrier = new Barrier();
		tempBarrier.setX(X);	
		tempBarrier.setY(Y);
		barrier_list_.add(tempBarrier);
		game_logic_.addSprite(tempBarrier);
		return tempBarrier;
	}
	
	public Barrier addBarrier(Barrier tempBarrier) {
		barrier_list_.add(tempBarrier);
		game_logic_.addSprite(tempBarrier);
		return tempBarrier;
	}
	
	public boolean resolveBarrierCollisions(float X,float Y) {
		for (int i = 0;i < barrier_list_.size();i++) {
			Barrier tempBarrier = barrier_list_.get(i);
			if (tempBarrier.ifCollision(X, Y)) {
				return true;
			}
		}
		return false;
	}
}
	