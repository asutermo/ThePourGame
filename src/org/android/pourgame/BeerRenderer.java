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
	
	public void initSquare(Context context) {
		square = new BeerSquare();
		square.loadBitmaps(context);
	}
	
}
