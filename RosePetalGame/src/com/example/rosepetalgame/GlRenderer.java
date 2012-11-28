package com.example.rosepetalgame;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.opengl.GLSurfaceView.Renderer;

import java.util.LinkedList;

public class GlRenderer implements Renderer {
	private static int[] viewport = new int[16];  
	private static float[] modelViewMatrix = new float[16];  
	private static float[] projectionMatrix = new float[16];  
	private static float[] pointInPlane = new float[16];  
	
	
	
	public final float pixelscreenwidth_ = 1280;
	public final float pixelscreenheight_ = 800; 
	//This is the platform we're aiming for, any other size platforms will have to be scaled to fit this box.
	
	private float actualscreenwidth_,actualscreenheight_; //Screen box to be rendering to.
	private boolean first_run; //Is this instance running for first time. 
	
	private Camera camera_;
	private LinkedList<Sprite> sprite_list_;
	private Background background_;
	private Context context_;
	private GL10 gl_;
	private int[] texturesheet_width_ = new int[1];
	private int[] texturesheet_height_ = new int[1];
	protected int[] textures = new int[1];
	private int top_,bottom_,left_,right_;
	
	
	public GlRenderer(float width, float height,Context context) {
		actualscreenwidth_ = width;
		actualscreenheight_ = height;
		context_ = context;
		Initialize();
	}

	public void onDrawFrame(GL10 gl) {
		//RandomizeAll();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		setupViewSystem(gl);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBindTexture(GL10.GL_TEXTURE_2D,textures[0]);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		//Start drawing background.
		gl.glEnable(GL10.GL_BLEND); 
		gl.glLoadIdentity();// Reset the Modelview Matrix
		background_.draw(gl);
		camera_.enableCamera(); //Push matrix. Do camera transforms.
		
		((GL11) gl_).glGetIntegerv(GL11.GL_VIEWPORT, viewport, 0);
		((GL11) gl_).glGetFloatv(GL11.GL_MODELVIEW_MATRIX, modelViewMatrix, 0);
		((GL11) gl_).glGetFloatv(GL11.GL_PROJECTION_MATRIX, projectionMatrix, 0); 
		// Record our final matrix for screen coordinate conversion purposes.
		
		for (int i = 0;i < sprite_list_.size();i++) {
			Sprite tempSprite = sprite_list_.get(i);
			tempSprite.draw(gl);
		}
		gl.glDisable(GL10.GL_TEXTURE_2D);
		//gl.glDisable(GL10.GL_ALPHA);
		gl.glDisable(GL10.GL_BLEND);
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		first_run = true;
		/*gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearColor(1.0f, 0.0f, 1.0f, 0.5f); 	//Black Background
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
		// create nearest filtered texture
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		loadGLTexture(gl);
		*/
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		first_run = true;
		// Load the texture for the square
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearColor(1.0f, 0.0f, 1.0f, 0.5f); 	//Black Background
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
		// create nearest filtered texture
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		loadGLTexture(gl);
	}
	
	private void setupViewSystem(GL10 gl) {
		if (first_run) {
			gl_ = gl;
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();// OpenGL docs.
			// Sets the current view port to the new size.
			top_ = 0;
			bottom_ = 0;
			left_ = 0;
			right_ = 0;
			float ratiopixels = (pixelscreenheight_)/(pixelscreenwidth_);
			float ratioscreen = (actualscreenheight_/actualscreenwidth_);
			if (ratiopixels != ratioscreen) //If the projected screen ratio and actual screen ratio is different. 
			{
				float screenwidthdiff = pixelscreenwidth_-actualscreenwidth_;
				float screenheightdiff = pixelscreenheight_-actualscreenheight_;
				if (screenheightdiff > screenwidthdiff)
				{
					bottom_ = (int)actualscreenheight_;
					right_ = (int)(actualscreenwidth_*(1/ratiopixels));
					left_ = (int)((actualscreenwidth_-right_)/2);
				}
				else
				{
					right_ = (int)actualscreenwidth_;
					bottom_ = (int)(actualscreenheight_*ratiopixels);
					top_ = (int)((actualscreenheight_-bottom_)/2);
				}
			} else {
				bottom_ = (int)actualscreenheight_;
				right_ = (int)actualscreenwidth_;
			} //If ratio is correct, print onto the entire screen.
			
			gl.glViewport(left_,top_,right_,bottom_);
			// clear Screen and Depth Buffer
			GLU.gluOrtho2D(gl,0,pixelscreenwidth_,pixelscreenheight_,0);
			camera_.setGL(gl);
			first_run = false;
		}
	}
	
	private void Initialize() {
		first_run = true;
	} //Initalization system to be called by constructors.
	
	
	public void loadGLTexture(GL10 gl) {
		// loading texture
		Bitmap bitmap = BitmapFactory.decodeResource(context_.getResources(),
				R.drawable.fish6);
		texturesheet_width_[0] = bitmap.getWidth(); //Get the size of this texture.
		texturesheet_height_[0] = bitmap.getHeight();
		Sprite.setTexture(texturesheet_width_[0],texturesheet_height_[0]); //Pass bitmap size to sprite class.
		// generate one texture pointer
		gl.glGenTextures(1, textures, 0);
		// ...and bind it to our array
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		// Use Android GLUtils to specify a two-dimensional texture image from our bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		// Clean up
		bitmap.recycle();
		
		 if (textures[0] == 0)
		    {
		        throw new RuntimeException("Error loading texture.");
		    }
	}	
	
	public void setSpriteList(LinkedList<Sprite> list)
	{
		sprite_list_ = list;
	}
	
	public void setCamera(Camera camera)
	{
		camera_ = camera;
	}
	
	public void setContext(Context context)
	{
		context_ = context;
	}
	
	public void setBackground(Background background)
	{
		background_ = background;
	}
	
	public float[] unproject(float x, float y) {
		gl_.glPushMatrix(); 
		gl_.glLoadIdentity();
		
	    int check = GLU.gluUnProject(
	    		x, viewport[3]-y,0.f, 
	           modelViewMatrix, 0,
	           projectionMatrix, 0, 
	           viewport, 0, 
	           pointInPlane, 0);
	    
	     gl_.glPopMatrix();
	     
	    
	     if (check != GL10.GL_TRUE) 
	    {
	    		 //Log item here
	    }
    	 
    	float[] output = new float[2];
    	output[0] = pointInPlane[0];
    	output[1] = pointInPlane[1];
	    return output;
	}
}