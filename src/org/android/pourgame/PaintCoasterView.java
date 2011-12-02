package org.android.pourgame;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Path.Direction;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.util.Log;

public class PaintCoasterView extends View {
	private int xMin = 0;          // This view's bounds
	private int xMax;
	private int yMin = 0;
	private int yMax;
	private float ballRadius = 150; // Ball's radius
	private float ballX = ballRadius + 100;  // Ball's center (x,y)
	private float ballY = ballRadius + 175;
	private float previousX, previousY;
	private float ballSpeedX = 0;  // Ball's speed (x,y)
	private float ballSpeedY = 0;
	private Path circle = new Path();
	private Path llabel = new Path(), rlabel = new Path(), tlabel = new Path(), blabel = new Path();
	private RectF ballBounds;      // Needed for Canvas.drawOval
	private Paint cPaint, tPaint, lPaint;           // The paint (e.g. style, color) used for drawing
	private String QUOTE = "College is like a fountain of Knowledge, and the students there like to drink.";
	private final float SLOW_DOWN_FACTOR = 0.75f;
	private Context context;
	private Display display;
	private Drawable logo, pourbeer, satellite;

	// Constructor
	public PaintCoasterView(Context context) {
		super(context);
		Log.d("paintView", "Making Coaster");
		this.context = context;
		ballBounds = new RectF();
		cPaint = new Paint();
		cPaint.setColor(Color.rgb(205, 201, 201));
		tPaint = new Paint();
		tPaint.setColor(Color.rgb(100,100,100));
		tPaint.setTextSize((float) 28);
		lPaint = new Paint();
		lPaint.setColor(Color.rgb(100,100,100));
		lPaint.setTextSize((float) 36);
		this.setFocusableInTouchMode(true);
		circle.addCircle(ballX, ballY, ballRadius, Direction.CW);
		
		display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		setSideLabel(rlabel, display.getWidth() - 30, display.getHeight()/2 - 120, false);
		setSideLabel(llabel, 30, display.getHeight()/2 - 15, true);
		setTBLabel(tlabel, display.getWidth()/2-80, 40, true);
		setTBLabel(blabel, display.getWidth()/2-80, display.getHeight()-90, false);
		Resources res = getResources();
        logo = res.getDrawable(R.drawable.logo);
        pourbeer = res.getDrawable(R.drawable.pourbeer);
        satellite = res.getDrawable(R.drawable.satellite);
	}

	// Called back to draw the view. Also called after invalidate().
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawTextOnPath("Game", llabel, 0, 0, lPaint);
		canvas.drawTextOnPath("Brewery Map", rlabel, 0, 0, lPaint);
		canvas.drawTextOnPath("Beerdar", tlabel, 0, 0, lPaint);
		canvas.drawTextOnPath("Gestures Help", blabel, 0, 0, lPaint);
		//canvas.drawBitmap(((BitmapDrawable) pourbeer).getBitmap(), 10, display.getHeight()/2 - 175, lPaint);
		//canvas.drawBitmap(((BitmapDrawable) satellite).getBitmap(), display.getWidth()-100, display.getHeight()/2, lPaint);
		
		
		// Draw the ball
		ballBounds.set(ballX-ballRadius, ballY-ballRadius, ballX+ballRadius, ballY+ballRadius);
		canvas.drawOval(ballBounds, cPaint);
		circle.reset();
		circle.addCircle(ballX, ballY, ballRadius, Direction.CW);
		
		//Draws the logo on the circle
		
		canvas.drawBitmap(((BitmapDrawable) logo).getBitmap(), ballX - 50, ballY - 75, cPaint);
		
		canvas.drawTextOnPath(QUOTE, circle, 0, 25, tPaint);
		
		// Update the position of the ball, including collision detection and reaction.
		update();
		if(ballSpeedX > 0)
		  ballSpeedX -= SLOW_DOWN_FACTOR;
		else if(ballSpeedX < 0)
			ballSpeedX += SLOW_DOWN_FACTOR;
		if(ballSpeedY > 0)
		  ballSpeedY -= SLOW_DOWN_FACTOR;
		else if(ballSpeedY < 0)
			ballSpeedY += SLOW_DOWN_FACTOR;

		if( (ballSpeedX < 1 && ballSpeedX > 0) || (ballSpeedX < 0 && ballSpeedX > -1) )
			ballSpeedX = 0;
		if( (ballSpeedY < 1 && ballSpeedY > 0) || (ballSpeedY < 0 && ballSpeedY > -1) )
			ballSpeedY = 0;
		// Delay
		try {  
			Thread.sleep(30);  
		} catch (InterruptedException e) { }

		invalidate();  // Force a re-draw
	}

	// Detect collision and update the position of the ball.
	private void update() {
		// Get new (x,y) position
		ballX += ballSpeedX;
		ballY += ballSpeedY;
		if(hitLeftWall())
			((ThePourGameActivity) this.context).left();
		if(hitRightWall())
			((ThePourGameActivity)this.context).right();
		if(hitTopWall())
			((ThePourGameActivity)this.context).up();
		if (hitBottomWall())
			((ThePourGameActivity)this.context).down();
		// Detect collision and react
		if (ballX + ballRadius > xMax) {
			ballSpeedX = -ballSpeedX;
			ballX = xMax-ballRadius;
		} else if (ballX - ballRadius < xMin) {
			ballSpeedX = -ballSpeedX;
			ballX = xMin+ballRadius;
		}
		if (ballY + ballRadius > yMax) {
			ballSpeedY = -ballSpeedY;
			ballY = yMax - ballRadius;
		} else if (ballY - ballRadius < yMin) {
			ballSpeedY = -ballSpeedY;
			ballY = yMin + ballRadius;
		}
	}

	// Called back when the view is first created or its size changes.
	@Override
	public void onSizeChanged(int w, int h, int oldW, int oldH) {
		// Set the movement bounds for the ball
		xMax = w-1;
		yMax = h-1;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float currentX = event.getX();
		float currentY = event.getY();
		float deltaX, deltaY;
		float scalingFactor = 50f / ((xMax > yMax) ? yMax : xMax);
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			// Modify rotational angles according to movement
			deltaX = currentX - previousX;
			deltaY = currentY - previousY;
			ballSpeedX += deltaX * scalingFactor;
			ballSpeedY += deltaY * scalingFactor;
		
		case MotionEvent.ACTION_DOWN:
			ballX = currentX;
			ballY = currentY;
		}	
		// Save current x, y
		previousX = currentX;
		previousY = currentY;

		return true;  // Event handled
	}

	public boolean onLongTouchEvent(MotionEvent event) {
		float currentX = event.getX();
		float currentY = event.getY();
		if( (currentX < ballX - ballRadius) && (currentX > ballX + ballRadius) && (currentY < ballY - ballRadius) && (currentY > ballY + ballRadius) ) {
			ballSpeedX = 0;
			ballSpeedY = 0;
		}
		return true;
	}
	
	public void setSideLabel(Path path, float x, float y, boolean flip) {
		path.setLastPoint(x, y);
		if(flip)
			path.lineTo(x, y-250);
		else
			path.lineTo(x, y+250);
	}
	
	public void setTBLabel(Path path, float x, float y, boolean tb) {
		path.setLastPoint(x, y);
		
		if (tb)
			path.lineTo(x+150, y);
		else
			path.lineTo(x+150, y);
	}
	
	public boolean hitLeftWall() {
		//Log.d("leftWall", "Checking ballX " + ballX);
		if(ballX-ballRadius <= 0 )
	      return true;
		return false;
	}
	
	public boolean hitRightWall() {
		//Log.d("rightWall", "Checking ballX " + ballX);
		if(ballX+ballRadius >= this.getWidth())
			return true;
		return false;
	}
	
	public boolean hitTopWall()
	{
		if(ballY-ballRadius <= 0)
			return true;
		return false;
	}
	
	public boolean hitBottomWall()
	{
		if (ballY+ballRadius >= this.getHeight())
			return true;
		return false;
		
	}
}