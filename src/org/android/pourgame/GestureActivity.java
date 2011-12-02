package org.android.pourgame;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;

public class GestureActivity extends DrinkActivity implements OnGestureListener
{
	protected static Context CONTEXT;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gesture);
        CONTEXT = this;
        gestureDetector = new GestureDetector(this, this);
        Log.d("Gesture Helper", "Gesture Helper Created");
        
    }
	
	@Override 
    public boolean onTouchEvent(MotionEvent me){ 
      this.gestureDetector.onTouchEvent(me);
     return super.onTouchEvent(me); 
    }

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		
		Log.d("onFling-Gesture help", "Measured fling");
		//from bottom to up
		if (e1.getY() - e2.getY() > SWIPE_MIN && Math.abs(velocityY) > SWIPE_THRESH_VEL)
		{
			Log.d("Main Screen", "Loading main screen");
			Intent up = new Intent(getApplicationContext(), ThePourGameActivity.class);
			transition(up);
			animate(R.anim.push_up_in, R.anim.push_up_out);
			return true;
		}

        return false;
	}
	
	@Override
	public void onBackPressed() {
		Log.d("Main Screen", "Loading main screen");
		Intent back = new Intent(getApplicationContext(), ThePourGameActivity.class);
		transition(back);
		animate(R.anim.push_up_in, R.anim.push_up_out);
	}
	
	public Context getContext() {
		return CONTEXT;
	}

	@Override
	public void onSensorChanged(SensorEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
