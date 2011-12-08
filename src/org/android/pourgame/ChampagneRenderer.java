package org.android.pourgame;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLU;
import android.util.Log;



public class ChampagneRenderer extends FluidRenderer {
	
	protected void initSquare(Context context) {
		beverageSquare = new Square();
		//square = new ChampagneSquare();
		beverageSquare.loadBitmap(context, R.drawable.champagne);
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		// Clears the screen and depth buffer.
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		// Replace the current matrix with the identity matrix
		gl.glLoadIdentity();
		// Rotate square counter-clockwise 90 degrees.
		// Translate by half the width of the screen.
		// Scale by __________
		gl.glTranslatef(-emptiness, 0, 0);
		gl.glRotatef(angle, 0, 0, 1);
		// Draw square.
		beverageSquare.draw(gl);
		// Restore the last matrix.			
	}
	
}
