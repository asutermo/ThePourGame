package org.android.pourgame;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

public abstract class FluidRenderer implements Renderer {
	
	protected Square beverageSquare;
	protected float angle = 90;
	protected int width, height;
	protected float emptiness = 1.0f;
	protected float headedness = 1.0f;
	
	protected void initSquare(Context context) {
		beverageSquare = new BeerSquare();
		beverageSquare.loadBitmap(context, R.drawable.beer);
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Set the background color to black ( rgba ).
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);  
		// Enable Smooth Shading, default not really needed.
		gl.glShadeModel(GL10.GL_SMOOTH);
		// Depth buffer setup.
		gl.glClearDepthf(1.0f);
		// Enables depth testing.
		gl.glEnable(GL10.GL_DEPTH_TEST);
		// The type of depth testing to do.
		gl.glDepthFunc(GL10.GL_LEQUAL);
		// Really nice perspective calculations.
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, 
                          GL10.GL_NICEST);
	}
	
	public void incrementGlassFill(float amount) {
		emptiness -= amount;
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		// Clears the screen and depth buffer.
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		// Replace the current matrix with the identity matrix
		gl.glLoadIdentity();
		// Save the current matrix.
		gl.glPushMatrix();
		
		// Rotate square counter-clockwise 90 degrees.
		// Translate by half the width of the screen.
		// Scale by __________
		gl.glTranslatef(-emptiness, 0, 0);
		gl.glRotatef(angle, 0, 0, 1);
		// Draw square.
		beverageSquare.draw(gl);
		// Restore the last matrix.
		gl.glPopMatrix();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// Sets the current view port to the new size.
		this.width = width;
		this.height = height;
		gl.glViewport(0, 0, width, height);

		// Select the projection matrix
		gl.glMatrixMode(GL10.GL_PROJECTION);
		// Reset the projection matrix
		gl.glLoadIdentity();
		// Calculate the aspect ratio of the window
		gl.glOrthof(0.0f, 1.0f, 0.0f, 1.0f, -1.0f, 1.0f);
		// Select the modelview matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		// Reset the modelview matrix
		gl.glLoadIdentity();
	}

	public boolean glassOverflowing() {
		return headedness < 0 || emptiness < 0;
	}
	
	public float getScore() {
		if(headedness == 1)
		  return (-emptiness+1)*1000;
		return (-headedness+1)*1000;
	}
	
	public boolean needToStopRendering() {
		return glassOverflowing() || emptiness < -0.1 || headedness < -0.1; 
	}
	
	public void resetGame(Context context) {
		initSquare(context);
		emptiness = 1.0f;
		headedness = 1.0f;
	}
}
