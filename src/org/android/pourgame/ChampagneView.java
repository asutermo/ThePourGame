package org.android.pourgame;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

public class ChampagneView extends FluidView{

	public ChampagneView(Context context) {
		super(context);
		this.setZOrderMediaOverlay(false);
		this.setZOrderOnTop(false);
		renderer = new ChampagneRenderer();
		renderer.initSquare(context);
		setRenderer(renderer);
		// TODO Auto-generated constructor stub
	}
	
	public ChampagneView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.d("ChampagneView", "Created and running");
		renderer = new ChampagneRenderer();
		renderer.initSquare(context);
		setRenderer(renderer);
		// TODO Auto-generated constructor stub
	}

}