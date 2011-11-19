package org.android.pourgame;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;

public class TheChampaigneActivity extends DrinkActivity implements OnGestureListener 
{
	private static Context CONTEXT;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.champaigne);
        CONTEXT = this;
        gestureDetector = new GestureDetector(this, this);
        Log.d("Champaigne Game", "Champaigne Game Created");
    }
	
	@Override 
    public boolean onTouchEvent(MotionEvent me){ 
      this.gestureDetector.onTouchEvent(me);
     return super.onTouchEvent(me); 
    }

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		
		Log.d("onFling-Champaigne Game", "Measured fling");
		//from bottom to up
		if (e1.getY() - e2.getY() > SWIPE_MIN && Math.abs(velocityY) > SWIPE_THRESH_VEL)
		{
			Log.d("beerGame", "Loading beer pouring game");
			Intent up = new Intent(getApplicationContext(), TheBeerActivity.class);
			transition(up);
			animate(R.anim.push_up_in, R.anim.push_up_out);
			return true;
		}

        return false;
	}
	
	@Override
	public void onBackPressed() {
		Log.d("Back", "Loading beer pouring game");
		Intent back = new Intent(getApplicationContext(), TheBeerActivity.class);
		transition(back);
		animate(R.anim.push_up_in, R.anim.push_up_out);
	}
	
	public static Context getContext() {
		return CONTEXT;
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
	}

}