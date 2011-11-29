package org.android.pourgame;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.WindowManager;

public class BeerView extends FluidView {

	private Display display;
	private MainThread thread;
	//private Particle[] particles;
	private int num_particles; 
	private ParticleHydroDynamics sph;

	public BeerView(Context context) {
		super(context);
		// adding the callback (this) to the surface holder to intercept events
		display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		getHolder().addCallback(this);
		int height, width;
		height = display.getHeight();
		width = display.getWidth();
		setMinimumWidth(width);
		setMinimumHeight(height);
		this.
		sph = new ParticleHydroDynamics(width, height);
		
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
	
	public void drawProjectile(double angle, Canvas canvas, Paint paint) {
		Log.w(this.getClass().getName(),"drawProjectile called");
		float mUi = 5;
		float mGravity = 9.81f;
		float mX1, mY1;
		mX1 = mY1 = 100;
		float mUx = (float) (mUi*Math.acos(angle));
		float mUy = (float) (mUi*Math.asin(angle));


		Log.d(this.getClass().getName(), "Value of mUx: " + Double.toString(mUx));
		Log.d(this.getClass().getName(), "Value of mUy: " + Double.toString(mUy));
		float now = 0f;
		for(int i = 1; i <= 10; i++) {
			Log.w(this.getClass().getName(),"In plotting points loop");
			mX1 = mUx*now;
			mY1 = mUy*now + (mGravity/2)*now*now;

			Log.d(this.getClass().getName(), "Value of mX1: " + mX1);
			Log.d(this.getClass().getName(), "Value of mY1: " + mY1);
			canvas.drawCircle(mX1, mY1, 4, paint);
			now += 1f; // or whatever time increment you want
		}
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
		//Log.d("BeerView", Integer.toString(canvas.getHeight()));
		//Log.d("BeerView", Integer.toString(canvas.getWidth()));
		Paint painter = new Paint();
		//painter.setColor(Color.BLUE);
//		for(int i = 0; i < sph.num_particles; ++i) {
//			int x = (int)(sph.width*((sph.particles[i].x + 1.0f)/2)),
//			y = (int)(sph.height*(1.0f - (sph.particles[i].y + 1.0f)/2));
//
//			if(sph.particles[i].density > (3*sph.rest_density)) painter.setColor(Color.RED);
//			else if(sph.particles[i].density > (2.5*sph.rest_density)) painter.setColor(Color.YELLOW);
//			else if(sph.particles[i].density > (2*sph.rest_density)) painter.setColor(Color.GREEN);
//			else if(sph.particles[i].density > (1.5*sph.rest_density)) painter.setColor(Color.CYAN);
//			else painter.setColor(Color.BLUE);
//
//			canvas.drawRect(x - 1, y - 1, 2, 2, painter);
//		}

		painter.setColor(Color.WHITE);

		int off = 2, half_off = 1;
		for(int i = 0, x = 0; i < (sph.scalar_field.length - 1); ++i, x += off) {
			for(int j = 0, y = 0; j < (sph.scalar_field.length - 1); ++j, y += off)
			{
				int idx = 0;

				if(sph.scalar_field[i][j] > 0.1f) idx = (idx|0x000f);
				if(sph.scalar_field[i][j + 1] > 0.1f) idx = (idx|0x00f0);
				if(sph.scalar_field[i + 1][j + 1] > 0.1f) idx = (idx|0x0f00);
				if(sph.scalar_field[i + 1][j] > 0.1f) idx = (idx|0xf000);

				if(idx == 0x0000 || idx == 0xffff) continue;
				else if(idx == 0xfff0 || idx == 0x000f)// || 
					canvas.drawLine(x + half_off, y, x, y + half_off, painter);
				else if(idx == 0xff0f || idx == 0x00f0)// || 
					canvas.drawLine(x, y + half_off, x + half_off, y + off, painter);
				else if(idx == 0xf0ff || idx == 0x0f00)// || 
					canvas.drawLine(x + half_off, y + off, x + off, y + half_off, painter);
				else if(idx == 0x0fff || idx == 0xf000)
					canvas.drawLine(x + off, y + half_off, x + half_off, y, painter);
				else if(idx == 0x00ff || idx == 0xff00)
					canvas.drawLine(x + half_off, y, x + half_off, y + off, painter);
				else if(idx == 0xf00f || idx == 0x0ff0)
					canvas.drawLine(x, y + half_off, x + off, y + half_off, painter);
				else if(idx == 0x0f0f) {
					canvas.drawLine(x + half_off, y, x, y + half_off, painter);
					canvas.drawLine(x + half_off, y + off, x + off, y + half_off, painter);
				}
				else if(idx == 0xf0f0)
				{
					canvas.drawLine(x, y + half_off, x + half_off, y + off, painter);
					canvas.drawLine(x + off, y + half_off, x + half_off, y, painter);
				}
			}
		}
		Log.d("BeerView", "Lines drawn");
	}

	/**
	 * This is the game update method. It iterates through all the objects
	 * and calls their update method if they have one or calls specific
	 * engine's update method.
	 */
	public void update() {
		sph.computeDensity();
		sph.computeForces();
		sph.integrate();
		sph.computeScalarField();
		Canvas canvas = new Canvas();
		render(canvas);
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
	}
}
