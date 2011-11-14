package org.android.pourgame;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;


public class TheBeerActivity extends Activity implements
OrientationListener, OnGestureListener {
	private static final int SWIPE_MIN = 120;
	private static final int SWIPE_MAX_OFF = 300;
	private static final int SWIPE_THRESH_VEL = 200;
	private GestureDetector gestureDetector;
	private static Context CONTEXT;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beer);
        CONTEXT = this;
        gestureDetector = new GestureDetector(this, this);
    }
	
	@Override 
    public boolean onTouchEvent(MotionEvent me){ 
      this.gestureDetector.onTouchEvent(me);
     return super.onTouchEvent(me); 
    }

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		
		//
		if (e2.getY() - e1.getY() > SWIPE_MIN && Math.abs(velocityY) > SWIPE_THRESH_VEL && Math.abs(e2.getX()-e1.getX()) < SWIPE_MAX_OFF)
		{
			up();
			return true;
		}

        // Swipe from left to right.
        // The swipe needs to exceed a certain distance (SWIPE_MIN_DISTANCE) and a certain velocity (SWIPE_THRESHOLD_VELOCITY).
        if (e2.getX() - e1.getX() > SWIPE_MIN
                && Math.abs(velocityX) > SWIPE_THRESH_VEL && Math.abs(e2.getY()-e1.getY()) < SWIPE_MAX_OFF) {
            right();
            return true;
        }

        return false;
	}
	
	public void up()
	{
		Log.d("sodaGame", "Loading soda pouring game");
		Intent up = new Intent(getApplicationContext(), TheSodaGameActivity.class);
		startActivity(up);
		finish();
		overridePendingTransition(R.anim.push_bottom_in, R.anim.push_bottom_out);
	}
	public void right()
	{
		Log.d("Main Screen", "Loading main screen");
		Intent previous = new Intent(getApplicationContext(), ThePourGameActivity.class);
		startActivity(previous);
		finish();

		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
	
	@Override
	public void onBackPressed() {
		Intent back = new Intent(getApplicationContext(), ThePourGameActivity.class);
		startActivity(back);
		finish();
	}
	
	public static Context getContext() {
		return CONTEXT;
	}

	

	/* (non-Javadoc)
	* @see org.android.pourgame.OrientationListener#onOrientationChanged(float, float, float)
	*/
	@Override
	public void onOrientationChanged(float azimuth, float pitch, float roll) {
	// TODO Auto-generated method stub
	
	}
	
	/* (non-Javadoc)
	* @see org.android.pourgame.OrientationListener#onTopUp()
	*/
	@Override
	public void onTopUp() {
	// TODO Auto-generated method stub
	
	}
	
	/* (non-Javadoc)
	* @see org.android.pourgame.OrientationListener#onBottomUp()
	*/
	@Override
	public void onBottomUp() {
	// TODO Auto-generated method stub
	
	}
	
	/* (non-Javadoc)
	* @see org.android.pourgame.OrientationListener#onRightUp()
	*/
	@Override
	public void onRightUp() {
	// TODO Auto-generated method stub
	
	}
	
	/* (non-Javadoc)
	* @see org.android.pourgame.OrientationListener#onLeftUp()
	*/
	@Override
	public void onLeftUp() {
	// TODO Auto-generated method stub
	
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

}
