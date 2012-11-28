package com.example.rosepetalgame;

import java.util.LinkedList;

public class EnemyControl {
	public static float mousex,mousey;
	private LinkedList<Enemy> enemy_list_;
	private Player player_;
	private GameLogic game_logic_;
	
	
	public EnemyControl(Player getplayer,GameLogic logic) {
		player_ = getplayer;
		game_logic_ = logic;
		enemy_list_ = new LinkedList<Enemy>();
	}
	
	public Enemy addEnemy(float X,float Y) {
		Enemy tempEnemy = new Enemy();
		tempEnemy.setX(X);	
		tempEnemy.setY(Y);
		enemy_list_.add(tempEnemy);
		game_logic_.addSprite(tempEnemy);
		return tempEnemy;
	}
	
	public Enemy addEnemy(Enemy tempEnemy) {
		enemy_list_.add(tempEnemy);
		game_logic_.addSprite(tempEnemy);
		return tempEnemy;
	}
	
	
	
	public void killEnemy() {
		for (int i = 0;i < enemy_list_.size();i++) {
			Enemy tempEnemy = enemy_list_.get(i);
			if (tempEnemy != null) {
				if (tap(tempEnemy))  {
					if (tempEnemy.lives <= 0) {
						game_logic_.removeSprite(tempEnemy);
						enemy_list_.remove(tempEnemy); //Remove a point from the list.
						i --; //Backtrack list.
					} else {
						tempEnemy.lives --;
						tempEnemy.setWidth(tempEnemy.getWidth()*0.95f);
						tempEnemy.setHeight(tempEnemy.getHeight()*0.95f);
					}
					return;
				}
			}
		}
	}
	
	
	public boolean tap(Enemy temp_enemy) {
		return temp_enemy.ifCollision(mousex,mousey);
	}
	
	
	public void resolveEnemyCollisions() {
		for (int i = 0;i < enemy_list_.size();i++) {
			Enemy tempEnemy = enemy_list_.get(i);
			if (checkCollisionPlayerEnemy(tempEnemy)) {
				if (!tempEnemy.touchingplayer){
					game_logic_.final_score -= 10;
					if (game_logic_.final_score < 0) {
						game_logic_.final_score = 0;
					}	
					tempEnemy.touchingplayer = true;
				}
			} else {
				tempEnemy.touchingplayer = false;
			}
		}
	}
	
	
	public boolean checkCollisionPlayerEnemy(Enemy temp_enemy) {
		float player_rightbound = player_.getX()+player_.leftcollision_;
		float player_leftbound = 	player_.getX()-player_.rightcollision_;
		float player_topbound = player_.getY()-player_.topcollision_;
		float player_bottombound = player_.getY()+player_.bottomcollision_;
		
		float enemy_rightbound = temp_enemy.getX()+temp_enemy.leftcollision_;
		float enemy_leftbound = temp_enemy.getX()-temp_enemy.rightcollision_;
		float enemy_topbound = temp_enemy.getY()-temp_enemy.topcollision_;
		float enemy_bottombound = temp_enemy.getY()+temp_enemy.bottomcollision_;
					
		if (player_leftbound > enemy_rightbound) {
			return false;
		}
		
		if (player_rightbound < enemy_leftbound) {
			return false;
		}
		
		if (player_topbound > enemy_bottombound) {
			return false;
		}
		
		if (player_bottombound < enemy_topbound) {
			return false;
		}
		
		return true;
	}
	

}
