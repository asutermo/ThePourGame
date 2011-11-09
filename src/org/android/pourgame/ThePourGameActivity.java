package org.android.pourgame;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

public class ThePourGameActivity extends Activity implements OrientationListener, OnGestureListener {
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
        if (OrientationManager.isSupported()) {
            OrientationManager.startListening(this);
        }
    }
 
    protected void onDestroy() {
        super.onDestroy();
        if (OrientationManager.isListening()) {
            OrientationManager.stopListening();
        }
 
    }
    
	@Override
    public void onOrientationChanged(float azimuth, float pitch, float roll)
	{
        
    }
 
    
    @Override
    public void onBottomUp() {
    }
 
    @Override
    public void onLeftUp() {
    }
 
    @Override
    public void onRightUp() {
    }
 
    @Override
    public void onTopUp() {
    }

	public static Context getContext() {
		return CONTEXT;
	}



	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}