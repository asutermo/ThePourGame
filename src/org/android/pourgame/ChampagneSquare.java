package org.android.pourgame;

import android.content.Context;
import android.graphics.BitmapFactory;

public class ChampagneSquare extends Square {
	
	public void loadBitmaps(Context context) {
		//beverageBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.champagne);
		headBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.head);
	}
}
