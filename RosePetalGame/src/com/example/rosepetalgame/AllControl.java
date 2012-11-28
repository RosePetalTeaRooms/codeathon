package com.example.rosepetalgame;

import java.util.LinkedList;

public class AllControl<T extends Sprite> 
{
	LinkedList<T> T_list_;
	Player player_;
	GlRenderer render_;
	public AllControl(Player getplayer,GlRenderer render) 
	{
		player_ = getplayer;
		render_ = render;
		T_list_ = new LinkedList<T>();
	}
	/*
	public void addItem(T tempT) 
	{
		T_list_.add(tempT);
		render_.addSprite(tempT);
	}
	
	public boolean resolvePointItemollisionXY(float X,float Y) 
	{
		for (int i = 0;i < T_list_.size();i++) 
		{
			T tempT = T_list_.get(i);
			if (tempT.ifCollision(X, Y)) 
			{
				return true;
			}
		}
		return false;
	}
	
	
	public void killItem(T tempT) 
	{
		T_list_.remove(tempT);
	}
	
	
	public void resolveSpriteItemCollisions(Sprite temp_sprite) 
	{
		for (int i = 0;i < T_list_.size();i++) 
		{
			T tempT = T_list_.get(i);
			if (checkCollisionPlayerEnemy(tempEnemy)) 
			{
				if (!tempEnemy.touchingplayer)
				{
					render_.final_score -= 10;
					if (render_.final_score < 0) 
					{
						render_.final_score = 0;
					}	
					tempEnemy.touchingplayer = true;
				}
			} 
			else 
			{
				tempEnemy.touchingplayer = false;
			}
		}
	}*/
}
