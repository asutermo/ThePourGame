package org.android.pourgame;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLU;
import android.util.Log;



public class BeerRenderer extends FluidRenderer {
	
	public BeerSquare square;
	private float angle = 90;
	private int width, height;
	private float emptiness = 1.0f;
	
	public void initSquare(Context context) {
		square = new BeerSquare();
		square.loadBitmaps(context);
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
		square.draw(gl);
		// Restore the last matrix.
		gl.glPopMatrix();

//		if(emptiness > 0)
//		  emptiness -= 0.002f;
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
	
}
