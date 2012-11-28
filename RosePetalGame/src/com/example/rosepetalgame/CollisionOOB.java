package com.example.rosepetalgame;

import android.util.FloatMath;

public class CollisionOOB {
	private final int vectorsize_ = 8;
	private final float PI_DIVIDED_180_ = 0.01745329251994329576923690768489f; 
	//Constant PI divided by 80 to save calculation time.
	private float[] vectorlist_;
	private float Axis1_x_;
	private float Axis1_y_;
	private float Axis2_x_;
	private float Axis2_y_;
	
	private float width_;
	private float height_;
	
	public CollisionOOB()
	{
		vectorlist_ = new float[vectorsize_];
	}
	
	public boolean getCollisionSprite(Sprite sprite1,Sprite sprite2)
	{	
		
		width_ = sprite2.getWidth();
		height_ = sprite2.getHeight();
		
		setSpriteCorners(sprite1); //Get the 4 unrotated corners into our array.
		rotatePoints(sprite1.getRotation(),0,0); //Make our points the rotated corners around our sprite.
		rotatePoints(sprite2.getRotation(),sprite2.getX(),sprite2.getY());
		//Offset our corners so that our second sprite becomes the axis in which they are rotated about.
		
		Axis1_x_ = vectorlist_[2] - vectorlist_[0];
		Axis1_y_ = vectorlist_[3] - vectorlist_[1];
		Axis2_x_ = vectorlist_[4] - vectorlist_[2];
		Axis2_y_ = vectorlist_[5] - vectorlist_[3];
		
		if (compareXAxisBox(vectorlist_[0],vectorlist_[1],Axis1_x_,Axis1_y_)) return true;
		if (compareXAxisBox(vectorlist_[6],vectorlist_[7],Axis1_x_,Axis1_y_)) return true;
		if (compareXAxisBox(vectorlist_[0],vectorlist_[1],Axis2_x_,Axis2_y_)) return true;
		if (compareXAxisBox(vectorlist_[2],vectorlist_[3],Axis2_x_,Axis2_y_)) return true;

		if (compareYAxisBox(vectorlist_[0],vectorlist_[1],Axis1_x_,Axis1_y_)) return true;
		if (compareYAxisBox(vectorlist_[6],vectorlist_[7],Axis1_x_,Axis1_y_)) return true;
		if (compareYAxisBox(vectorlist_[0],vectorlist_[1],Axis2_x_,Axis2_y_)) return true;
		if (compareYAxisBox(vectorlist_[2],vectorlist_[3],Axis2_x_,Axis2_y_)) return true;
		
		return false;
	}
	
	private void rotatePoints(float degrees,float centrex,float centrey)
	{	
		float radrot = (degrees)*PI_DIVIDED_180_; //Find radians of our rotation.
		float sinans = FloatMath.sin(radrot);
		float cosans = FloatMath.cos(radrot); //Initlaize our rotation based on other sprite's rotation.
		for (int i =0;i < vectorsize_-1; i +=2)
		{
			float tempvec1 = vectorlist_[i] - centrex;
			float tempvec2 = vectorlist_[i+1] - centrey;
			vectorlist_[i] = (cosans*tempvec1) - (sinans*tempvec2); 
			vectorlist_[i+1] = (sinans*tempvec1) + (cosans*tempvec2); 
		} //Update our points with the new rotation.		
		
	}
	
	private void setSpriteCorners(Sprite sprite1)
	{
		vectorlist_[0] = (sprite1.getX() - sprite1.getWidth() );
		vectorlist_[1] = (sprite1.getY() - sprite1.getHeight());
		//Point 1 Top left.
		vectorlist_[2] = (sprite1.getX() + sprite1.getWidth()); 
		vectorlist_[3] = (sprite1.getY() - sprite1.getHeight());
		//Point 2 Top Right.
		vectorlist_[4] = (sprite1.getX() + sprite1.getWidth()); 
		vectorlist_[5] = (sprite1.getY() + sprite1.getHeight());
		//Point 3 Bottom Right.
		vectorlist_[6] = (sprite1.getX() - sprite1.getWidth()); 
		vectorlist_[7] = (sprite1.getY() + sprite1.getHeight());
		//Point 4 Bottom Left.
		//Feed our 4 points into a vector array.
	}
	
	private boolean compareXAxisBox(float Px,float Py,float axis1,float axis2)
	{
		return CompareAxis(Px,Py,width_,height_,axis1,axis2);
	}
	
	private boolean compareYAxisBox(float Px,float Py,float axis1,float axis2)
	{
		return CompareAxis(Py,Px,height_,width_,axis2,axis1);
	}
	
	private boolean CompareAxis(float point1,float point2,float length1,
								float length2,float axis1,float axis2)
	{
		float boundscheck1 = -(point1+length1)/axis1; 
		//Checks vector equation of both sprites' vectors. Ensures that the line can only intersect 0<t<1 
		if (boundscheck1 < 0.f || boundscheck1 > 1.f)
		{
			return false;
		}
		
		float boundscheck2 = (point2+length2+ (boundscheck1*axis2))/(2*length2);
		//If the first line intersects happily, ensure the second line also intersects within its own bounds.
		if (boundscheck2 < 0.f || boundscheck2 > 1.f)
		{
			return false;
		}
		return true;	
	}
	
	
}
