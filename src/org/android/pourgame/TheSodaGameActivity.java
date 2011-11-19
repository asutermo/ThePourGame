package org.android.pourgame;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorEvent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class TheSodaGameActivity extends DrinkActivity implements
OnGestureListener {
	private static Context CONTEXT;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.soda);
        CONTEXT = this;
        gestureDetector = new GestureDetector(this, this);
        Log.d("Soda Game", "Soda Game Created");
    }
	
	@Override 
    public boolean onTouchEvent(MotionEvent me){ 
      this.gestureDetector.onTouchEvent(me);
     return super.onTouchEvent(me); 
    }

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		Log.d("onFling - Soda Game", "Measured fling");
		if (e2.getY() - e1.getY() > SWIPE_MIN && Math.abs(velocityY) > SWIPE_THRESH_VEL)
		{
			down();
			return true;
		}

        return false;
	}
	
	public void down()
	{
		Log.d("beerGame", "Loading beer pouring game");
		Intent up = new Intent(getApplicationContext(), TheBeerActivity.class);
		startActivity(up);
		finish();
		overridePendingTransition(R.anim.push_bottom_in, R.anim.push_bottom_out);
	}
	
	@Override
	public void onBackPressed() {
		Log.d("beerGame", "Loading beer pouring game");
		Intent back = new Intent(getApplicationContext(), TheBeerActivity.class);
		startActivity(back);
		finish();
		overridePendingTransition(R.anim.push_bottom_in, R.anim.push_bottom_out);
	}
	
	public static Context getContext() {
		return CONTEXT;
	}
	

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
	}

}
