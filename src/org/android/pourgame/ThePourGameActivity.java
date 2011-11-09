package org.android.pourgame;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

public class ThePourGameActivity extends Activity implements OnGestureListener {
    /** Called when the activity is first created. */
	private static Context CONTEXT;
	private static final int SWIPE_MIN = 120;
	private static final int SWIPE_MAX_OFF = 250;
	private static final int SWIPE_THRESH_VEL = 200;
	private GestureDetector gestureDetector;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        CONTEXT = this;
        gestureDetector = new GestureDetector(this, this);
        
    }



    protected void onResume() {
        super.onResume();
    }
 
    protected void onDestroy() {
        super.onDestroy();
 
    }

	public static Context getContext() {
		return CONTEXT;
	}



	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
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