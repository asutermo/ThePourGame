package org.android.pourgame;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

public class SodaView extends FluidView {
	
	SodaRenderer renderer;

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
		Log.d("BeerView", "Created and running");
		renderer = new SodaRenderer();
		renderer.initSquare(context);
		setRenderer(renderer);
		// TODO Auto-generated constructor stub
	}
	
	public void fillGlass(float amount) {
		renderer.incrementGlassFill(amount);
	}
}
