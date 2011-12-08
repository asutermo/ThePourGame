package org.android.pourgame;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public abstract class FluidView extends GLSurfaceView{
	protected FluidRenderer renderer;
	
	public FluidView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public FluidView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public FluidView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	protected void fillGlass(float amount) {
		renderer.incrementGlassFill(amount);
	}
}
