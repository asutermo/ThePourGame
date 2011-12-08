package org.android.pourgame;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

public class SodaView extends FluidView {

	public SodaView(Context context) {
		super(context);
		this.setZOrderMediaOverlay(false);
		this.setZOrderOnTop(false);
		renderer = new SodaRenderer();
		renderer.initSquare(context);
		setRenderer(renderer);
		// TODO Auto-generated constructor stub
	}
	
	public SodaView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.d("SodaView", "Created and running");
		renderer = new SodaRenderer();
		renderer.initSquare(context);
		setRenderer(renderer);
		// TODO Auto-generated constructor stub
	}
	
}
