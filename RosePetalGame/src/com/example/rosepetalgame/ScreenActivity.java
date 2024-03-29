package com.example.rosepetalgame;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ScreenActivity extends Activity {
	
	private Button btnClick;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) 
	    {
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
	       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	        					WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    	
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_menu); 
	        
	        btnClick = (Button)findViewById(R.id.button);
	        
	        btnClick.setOnClickListener(new OnClickListener()
	        {        	
	        	@Override 
	        	public void onClick(View v)
	        	{
	        		Intent MainActivity = new Intent(ScreenActivity.this, MainActivity.class);
	        		startActivity(MainActivity);
	        	}
	        });
	        
	    }

}
