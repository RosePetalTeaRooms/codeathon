package com.example.rosepetalgame;

import com.samsung.spensdk.SCanvasView;

import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class MainActivity extends Activity {
	
	 /** The OpenGL view */
	 private GLSurfaceView glSurfaceView; 
	 //Main GL application, deals with interfacing the API to Java.
	 private GlRenderer glRender; 
	 // Render system. 
	 private GameLogic gamelogic;
	 TouchTracker touch_tracker;
	 
	 private MediaPlayer m_mediaPlayer;
	 //Spen View.
	 private SCanvasView mSCanvas; 
	
	 //Frame Layout contains both our OpenGL and our Spen views.
	 private FrameLayout container_all_;
	
	 
	 
	 @Override
    public void onCreate(Bundle savedInstanceState) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //Forces always landscape.
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
	       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	        					WindowManager.LayoutParams.FLAG_FULLSCREEN);
       
       m_mediaPlayer = new MediaPlayer();
	   m_mediaPlayer = MediaPlayer.create(this,R.raw.background);
	   m_mediaPlayer.start();
		
       InitGLSettings(); //Initilize all the gubbins we need for opengl.
       
       	gamelogic = new GameLogic(this,glRender);
       	gamelogic.feedRenderer();
       	touch_tracker = new TouchTracker(gamelogic,gamelogic.getPlayer(),this); 
		mSCanvas.setSPenTouchListener(touch_tracker);
						
		m_mediaPlayer.setLooping(true);	
		startMP();	
				
		drawAlgae();
		drawBarriers();
		drawEnemies();
		/*
		(new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				while(true)
				{
					gamelogic.doLogic();
				}
			}
		})).start();
		*/
	 }
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    /** Remember to resume the glSurface  */
	@Override
	protected void onResume() {
		super.onResume();
		m_mediaPlayer.start();
		glSurfaceView.onResume();
		gamelogic.feedRenderer(); //Update surface view if needs to get links to data once again.
		//super.onResume();
	}

	/** Also pause the glSurface  */
	@Override
	protected void onPause() {
		if(m_mediaPlayer.isPlaying())
		 {
		 //stop and give option to start again
			m_mediaPlayer.pause();
		 }
		super.onPause();
		glSurfaceView.onPause();
	}
	
	void startMP()
	{
		m_mediaPlayer.start();	
	}
	

	private void InitGLSettings() { //Remove Canvas and instead Initialize GL to screen.
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		//Find window coords.
		
			container_all_ = new FrameLayout(this);
			glSurfaceView = new GLSurfaceView(this); 
			//Setup surface view to begin GL stuff.
			
	        glRender = new GlRenderer((float)metrics.widthPixels,(float)metrics.heightPixels,this);
	        //Set coordinates of OpenGL area based on screen coords.
	        
	        glSurfaceView.setRenderer(glRender);
	        //glSurfaceView.setPreserveEGLContextOnPause(true);
	        container_all_.addView(glSurfaceView,0);
	        mSCanvas = new SCanvasView(this);      
	        container_all_.addView(mSCanvas,1);    
	        setContentView(container_all_);
	  	}
	
	
	public void drawAlgae() {
		gamelogic.algae_control.addAlgae(150.f,150.f);
		gamelogic.algae_control.addAlgae(350.f,150.f);
		gamelogic.algae_control.addAlgae(600.f,100.f);
		gamelogic.algae_control.addAlgae(800.f,323.f);
		gamelogic.algae_control.addAlgae(70.f,277.f);
	}
	
	public void drawBarriers() {
		gamelogic.barrier_control.addBarrier(300.f,300.f);
		gamelogic.barrier_control.addBarrier(600.f,600.f);
		
		OutsideBarrier t = new OutsideBarrier();

		gamelogic.barrier_control.addBarrier(t);

		 t.setX(900);
		 t.setY(400);
	}
	
	public void drawEnemies() 
	{
		gamelogic.enemy_control.addEnemy(300,300);
		gamelogic.enemy_control.addEnemy(700,700);
		//glRender.enemy_control_.addEnemy(500,500);
		//glRender.enemy_control_.addEnemy(600,600);
		//glRender.enemy_control_.addEnemy(700,700);
	}

	@Override
	protected void onStop()
	{
		m_mediaPlayer.stop();
		m_mediaPlayer.release();
		super.onStop();
	}
	@Override
	protected void onSaveInstanceState(Bundle bundle)
	{
		//bundle.put
		super.onSaveInstanceState(bundle);
	}
	@Override
	protected void onRestoreInstanceState(Bundle bundle)
	{
		super.onRestoreInstanceState(bundle);
	}
	
}


