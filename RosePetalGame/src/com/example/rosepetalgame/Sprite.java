package com.example.rosepetalgame;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

public class Sprite 
{
	
	protected static int texwidth_ = 1024;
	protected static int texheight_ = 1024;
	
	public Sprite() 
	{
		// a float has 4 bytes so we allocate for each coordinate 4 bytes
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		// allocates the memory from the byte buffer
		vertexBuffer = byteBuffer.asFloatBuffer();
		// fill the vertexBuffer with the vertices
		vertexBuffer.put(vertices);
		// set the cursor position to the beginning of the buffer
		vertexBuffer.position(0);
		
		//Resuse old Byte buffer variable, try to save on data for efficency!
		byteBuffer = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuffer.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);
		
		//////////////////Basic settings.
		rotation_ = 180.f;
		x_ = 0.f;
		y_ = 0.f; //Setup defaults.
		width_ = 1.f;
		height_ = 1.f;
		
		topcollision_ = 0;
		bottomcollision_ = 0;
		rightcollision_ = 0;
		leftcollision_ = 0;
		
		originoffset_x = 0.f;
		originoffset_y = 0.f;
		red_ = 1.f;
		green_ = 1.f;
		blue_ = 1.f;
		alpha_ = 1.f;
		
		u_ = 0.f;
		v_ = 0.f;
		uwidth_ = 1.f;
		vheight_ = 1.f;
		
		delay_ = 0;
		currentdelay_ = 0;
		maxframe_ = 0;
		currentframe_ = 0;
		minframe_ = 0; //Animation
	}
	
	public static void setTexture(int width,int height) 
	{
		texwidth_ = width;
		texheight_ = height; //Get the texture size.
	} //Get texture for all objects that will inheret from this class.

	/** The draw method for the square with the GL context */
	public void draw(GL10 gl) 
	{
		animate(); //Sorts out our new UV coordinates.
		gl.glPushMatrix(); //Save old matrix for rendering stuff.
		gl.glTranslatef(x_,y_,0); //Sprite's final X/Y coordinates.
		gl.glRotatef(rotation_,0,0,1); //Sprite's rotation.
		gl.glTranslatef(-originoffset_x,-originoffset_y,0);//Sprite's centre of origin.
		gl.glScalef(width_,height_,1.f); //Sprite's width/height.
		/*So our matrices are backwards due to the way OpenGL draws. Just bear with it.
		 * First we scale the object to its true height. Change the sprite's center of origin.
		 * We then rotate about this centere of origin.
		 * We then move the sprite to it's actual X/Y coordinates.
		 * Anything after this are view/global transforms controlled by caller function. 
		 */
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glFrontFace(GL10.GL_CW); //Front face only.
		// set the colour for the square
		gl.glColor4f(red_,green_,blue_,alpha_);

		// Point to our vertex buffer
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		
		// Draw the vertices as triangle strip
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);

		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glPopMatrix(); //Revert back to old matrix.
	}
	
	
		
	public void setUV(int u, int v,int u_width,int v_height) 
	{
		u_ = (float) u;
		v_ = (float) v;
		uwidth_ = (float) u_width;
		vheight_ = (float) v_height;
		updateTexCoords();
	}
	
	//Getters of X/Y/ROtation.
	public float getX() 
	{
		return x_;
	}
	
	public float getY() 
	{
		return y_;
	}
	
	public float getRotation() 
	{
		return rotation_;
	}
	
	public float getWidth() 
	{
		return width_;
	}
	
	public float getHeight() 
	{
		return height_;
	}
	
	//Setters of X/Y/Rotation
	public void setX(float amount) 
	{
		x_ = amount;
	}
	
	public void setY(float amount) 
	{
		y_ = amount;
	}
	
	public void setRotation(float amount) 
	{
		rotation_ = amount;
		
		while (rotation_ > 360.f) 
		{
			rotation_ -= 360.f;
		}
		
		while (rotation_ < 0.f) 
		{
			rotation_ += 360.f;
		}
	}
	
	public void setWidth(float amount) 
	{
		width_ = amount;
	}
	
	public void setHeight(float amount) 
	{
		height_ = amount;
	}
	
	
	//appenders of X/Y/Rotation.
	public void addX(float amount) 
	{
		x_ += amount;
	}
	
	public void addY(float amount) 
	{
		y_ += amount;
	}
	
	public void addRotation(float amount) 
	{
		rotation_ += amount;
		
		while (rotation_ > 360.f) 
		{
			rotation_ -= 360.f;
		}
		
		while (rotation_ < 0.f) 
		{
			rotation_ += 360.f;
		}
	}
	
	public boolean ifCollision(float x,float y) 
	{
		if (x > (x_+rightcollision_)) return false;
		
		if (x < x_-leftcollision_) return false;
		
		if (y > (y_+bottomcollision_)) return false;
		
		if (y < y_-topcollision_) return false;
		
		return true;
	}
	
	public void setOrigin(float X,float Y) 
	{
		//Sets the offset from the centre of the sprite where the sprite will rotate around
		originoffset_x = X;
		originoffset_y = Y;
	}
	

	public void setRGBA(float R,float G,float B,float A) 
	{
		if (R > 1.f) 
		{
			R = 1.f;
		} 
		else if (R < 0.f) 
		{
			R = 0.f;
		}
		
		if (G > 1.f) 
		{
			G = 1.f;
		} 
		else if (G < 0.f) 
		{
			G = 0.f;
		}
		
		if (B > 1.f) 
		{
			B = 1.f;
		} 
		else if (R < 0.f) 
		{
			B = 0.f;
		}
		
		if (A > 1.f) 
		{
			A = 1.f;
		} 
		else if (A < 0.f) 
		{
			A = 0.f;
		}
		
		red_ = R;
		blue_ = B;
		green_ = G;
		alpha_ = A;
	}
	
	public void setRGBA256(int R,int G,int B,int A) 
	{
		if (R > 256)
		{
			R = 256;
		} 
		else if (R < 0) 
		{
			R = 0;
		}
		
		if (G > 256) 
		{
			G = 256;
		} 
		else if (G < 0) 
		{
			G = 0;
		}
		
		if (B > 256) 
		{
			B = 256;
		} 
		else if (B < 0) 
		{
			B = 0;
		}
		
		if (A > 256) 
		{
			A = 256;
		} 
		else if (A < 0) 
		{
			A = 0;
		}
		
		red_ = ((float)R/256.f);
		blue_ = ((float)B/256.f);
		green_ = ((float)G/256.f);
		alpha_ = ((float)A/256.f);
		//Normalize integers between 0 and 1.
	}
	
	public void setAnimation(int minframe,int maxframe,int maxdelay) 
	{
		maxframe_ = maxframe;
		minframe_ = minframe;
		delay_ = maxdelay;
		currentdelay_ = 0;
		currentframe_ = minframe;
	}
	
	public void collisionSetVals(float top,float bottom,float left,float right) 
	{
		topcollision_ = top;
		bottomcollision_ = bottom;
		leftcollision_ = left;
		rightcollision_ = right;
	}
	
	protected void animate() 
	{
		//Used by sprite class to animate each iteration.
		if ((currentdelay_ > delay_)) 
		{
			if (maxframe_ != minframe_) 
			{ //Ensure there is something to animate first.
 				//Only animate when this function is called X amount of times. Also animate if there's more than 1 frame.
				
				currentframe_ ++;
				if (currentframe_ > maxframe_) 
				{
					currentframe_ = minframe_;
				} //Keeps the frames looping.
				
				animated_u_ = u_ + ( uwidth_ * currentframe_ );
				updateTexBuffer();
				//Moves the u coordinates forward by the width of this texture section each time this is called.
				
				
				// Mapping coordinates for the vertices
				
				//Remap our texture array with correct coordinates.
			}
			currentdelay_ = 0;
		}
		currentdelay_ ++;
	}
	
	protected void updateTexCoords() 
	{
		//Normalizes the inputted texture pixel coordinates to a float between 0 and 1.
		
		u_ = u_ / (float)texwidth_;
		v_ = v_ / (float)texheight_;
		uwidth_ = uwidth_ / (float)texwidth_;
		vheight_ = vheight_ / (float)texheight_;
		animated_u_ = u_;
		animated_v_ = v_;
		updateTexBuffer();
	}
	
	protected void updateTexBuffer() 
	{
		//Updates the array fed into OpenGL for texture rendering.
		texture[0] = animated_u_+uwidth_; 
		texture[1] = animated_v_+vheight_;
		texture[2] = animated_u_+uwidth_;
		texture[3] = animated_v_;
		texture[4] = animated_u_;
		texture[5] = animated_v_+vheight_;
		texture[6] = animated_u_;
		texture[7] = animated_v_;
		textureBuffer.put(texture);
		textureBuffer.position(0);
	}
	
	protected FloatBuffer vertexBuffer;	// buffer holding the vertices
	protected FloatBuffer textureBuffer;	// buffer holding the texture coordinates
	protected float x_,y_,width_,height_,rotation_; // Sprite orientation.
	protected float originoffset_x, originoffset_y; //Sprite rotation centre.
	
	protected float red_,green_,blue_,alpha_; //Colourization.
	
	protected float u_,v_,uwidth_,vheight_; //Texture coordinates
	protected float animated_u_,animated_v_;
	protected int delay_,currentdelay_,maxframe_,currentframe_,minframe_; //Animation
	
	public float topcollision_,bottomcollision_,rightcollision_,leftcollision_;
	
	
	protected float texture[] = 
		{
			// Mapping coordinates for the vertices
			1.0f, 1.0f,		// top left		(V2)
			1.0f, 0.0f,		// bottom left	(V1)
			0.0f, 1.0f,		// top right	(V4)
			0.0f, 0.0f		// bottom right	(V3)
		};
	
	protected float vertices[] = 
		{
			-1.f, -1.f,  0.0f,		// V1 - top left
			-1.f, +1.f,  0.0f,		// V2 - bottom left
			+1.f, -1.f,  0.0f,		// V3 - bottom right
			+1.f, +1.f,  0.0f			// V4 - top right
		};

}