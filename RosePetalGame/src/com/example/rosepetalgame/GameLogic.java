package com.example.rosepetalgame;

import java.util.LinkedList;
import java.util.Random;

import android.content.Context;

public class GameLogic 
{
	public AlgaeControl algae_control;
	public BarrierControl barrier_control;
	public EnemyControl enemy_control;
	
	public int final_score;
	public int screensize_width_;
	public int screensize_height_;
	public Scoreboard scoreboard;
	public Camera camera_; //Initialized when the surface is created, not in the constructor!
	
	private LinkedList<Sprite> sprite_list_;
	private Context context_;
	private Player player_;
	private Background background_;
	private GlRenderer render_;
	/** Constructor to set the handed over context */
	
	public GameLogic(Context context,GlRenderer render) 
	{
		context_ = context;
		render_ = render;
		Initialize();
		getScreenSize();
	}

	public Sprite addSprite() 
	{
		Sprite tempSprite = new Sprite();
		sprite_list_.add(tempSprite);
		return tempSprite;
	}
	
	public void getScreenSize()
	{
		background_ = new Background((int)render_.pixelscreenwidth_,(int)render_.pixelscreenheight_);
	}
	
	public Sprite addSprite(Sprite newSprite)
	{
		sprite_list_.add(newSprite);
		return newSprite;
	}
	
	public Sprite addSprite(float X,float Y) 
	{
		Sprite tempSprite = new Sprite();
		tempSprite.setX(X);
		tempSprite.setY(Y);
		sprite_list_.add(tempSprite);
		return tempSprite;
	}
	
	
	public void removeSprite(Sprite removed_spirte) 
	{
		sprite_list_.remove(removed_spirte);
	}
	
	private void Initialize() 
	{
		sprite_list_ = new LinkedList<Sprite>();
		algae_control = new AlgaeControl(player_,this,context_);
		barrier_control = new BarrierControl(player_,this);
		enemy_control = new EnemyControl(player_,this);
		final_score = 0;
		camera_ = new Camera();
		scoreboard = new Scoreboard(this);
		addSprite(scoreboard);
		
		player_ = new Player();
	    player_.setX(500.f);
	    player_.setY(500.f);
		addSprite(player_);
	    
	} //Initalization system to be called by constructors.
	
	public void RandomizeAll() 
	{
		Random generate = new Random();
		if (generate.nextInt(30) == 0) 
		{
			if (generate.nextInt(2) == 0) 
			{
				Algae temp = new Algae();
				temp.setX(generate.nextInt(1000));
				temp.setY(generate.nextInt(1000));
				algae_control.addAlgae(temp);
			} 
			else 
			{
				Enemy temp = new Enemy();
				temp.setX(generate.nextInt(-40));
				temp.setY(generate.nextInt(1040));
				enemy_control.addEnemy(temp);
			}
		}
	}
	
	public void doLogic()
	{
		Enemy.goalx_ = player_.getX();
		Enemy.goaly_ = player_.getY();
		algae_control.resolveAlgaeCollisions(); // See what algae are up to. Detect collisions.
		enemy_control.resolveEnemyCollisions();
		scoreboard.getScore(final_score);
	}
	
	public void feedRenderer() 
	{
		render_.setCamera(camera_);
		render_.setSpriteList(sprite_list_);
		render_.setContext(context_);
		render_.setBackground(background_);
	}
	
	public Player getPlayer() 
	{
		return player_;
	}
	
	public GlRenderer getRender()
	{
		return render_;
	}
}