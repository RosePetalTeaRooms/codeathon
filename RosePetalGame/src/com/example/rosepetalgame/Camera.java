package com.example.rosepetalgame;


import javax.microedition.khronos.opengles.GL10;

public class Camera {
	public Camera() {
		x_ = 0;
		y_ = 0;
		z_ = 1;
		rotation_ = 0;
	}
	
	public void setGL(GL10 gl)
	{
		gl_ = gl;	
	}
	
	public void enableCamera() {
		gl_.glPushMatrix();
		gl_.glTranslatef(-x_,-y_,0);
		gl_.glRotatef(rotation_,0,0,1);
		gl_.glScalef(z_,z_,1.f);
	}
	
	public void disableCamera() {
		gl_.glPopMatrix();
	}
	
	public void setX(float x) {
		x_ = x;
	}
	
	public void setY(float y) {
		y_ = y;
	}
	
	public void setZ(float z) {
		z_ = z;
	}
	
	public void setXY(float x,float y) {
		x_ = x;
		y_ = y;
	}
	
	public void addX(float x) {
		x_ += x;
	}
	
	public void addY(float y) {
		y_ += y;
	}
	
	public void addZ(float z) {
		z_ += z;
	}
	
	public void addXY(float x,float y) {
		x_ += x;
		y_ += y;
	}
	
	public float getX() {
		return x_;
	}
	
	public float getY() {
		return y_;
	}
	
	public float getZ() {
		return z_;
	}
	
	
	private float x_,y_,z_,rotation_;
	private GL10 gl_;
}
