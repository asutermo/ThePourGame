package org.android.pourgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class TheBeerActivity extends Activity implements
OrientationListener, OnGestureListener {
	private static final int SWIPE_MIN = 120;
	private static final int SWIPE_MAX_OFF = 250;
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
		// Check movement along the Y-axis. If it exceeds SWIPE_MAX_OFF_PATH, then dismiss the swipe.
        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF)
            return false;

        // Swipe from left to right.
        // The swipe needs to exceed a certain distance (SWIPE_MIN_DISTANCE) and a certain velocity (SWIPE_THRESHOLD_VELOCITY).
        if (e2.getX() - e1.getX() > SWIPE_MIN
                && Math.abs(velocityX) > SWIPE_THRESH_VEL) {
            previous();
            return true;
        }

        return false;
	}
	
	public void previous()
	{
		Intent previous = new Intent(getApplicationContext(), ThePourGameActivity.class);
		startActivity(previous);
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
