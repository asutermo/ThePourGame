package org.android.pourgame;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

public class BeerView extends FluidView{

	public BeerView(Context context) {
		super(context);
		this.setZOrderMediaOverlay(false);
		this.setZOrderOnTop(false);
		renderer = new BeerRenderer();
		renderer.initSquare(context);
		setRenderer(renderer);
		// TODO Auto-generated constructor stub
	}
	
	public BeerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.d("BeerView", "Created and running");
		renderer = new BeerRenderer();
		renderer.initSquare(context);
		setRenderer(renderer);
		// TODO Auto-generated constructor stub
	}
	
}
