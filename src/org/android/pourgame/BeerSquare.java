package org.android.pourgame;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

public class BeerSquare extends Square {

	public void loadBitmaps(Context context) {
		beverageBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.beer);
		headBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.head);
	}
	
}
