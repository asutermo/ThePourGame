package org.android.pourgame;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.WindowManager;

public class BeerView extends FluidView{

	private Display display;
	private MainThread thread;

	public BeerView(Context context) {
		super(context);
		// adding the callback (this) to the surface holder to intercept events
		display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		getHolder().addCallback(this);
		setMinimumWidth(display.getWidth());
		setMinimumHeight(display.getHeight());
		// make the GamePanel focusable so it can handle events
		setFocusable(true);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// create the game loop thread
		thread = new MainThread(getHolder(), this);
		
		// at this point the surface is created and
		// we can safely start the game loop
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
		boolean retry = true;
		while (retry) {
			try {
				thread.setRunning(false);
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {

		}
		return true;
	}
	public void render(Canvas canvas) {

		canvas.drawColor(Color.BLACK);
		
		// display border
		Paint paint = new Paint();
		paint.setColor(Color.GREEN);
		canvas.drawLines(new float[]{
				0,0, canvas.getWidth()-1,0, 
				canvas.getWidth()-1,0, canvas.getWidth()-1,canvas.getHeight()-1, 
				canvas.getWidth()-1,canvas.getHeight()-1, 0,canvas.getHeight()-1,
				0,canvas.getHeight()-1, 0,0
		}, paint);
	}

	/**
	 * This is the game update method. It iterates through all the objects
	 * and calls their update method if they have one or calls specific
	 * engine's update method.
	 */
	public void update() {

	}

	private void displayFps(Canvas canvas, String fps) {
		if (canvas != null && fps != null) {
			Paint paint = new Paint();
			paint.setARGB(255, 255, 255, 255);
			canvas.drawText(fps, this.getWidth() - 50, 20, paint);
		}
	}

}
