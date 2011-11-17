package org.android.pourgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;

public class TheChampaigneActivity extends Activity implements OnGestureListener 
{
	private static final int SWIPE_MIN = 120;
	private static final int SWIPE_THRESH_VEL = 200;
	private GestureDetector gestureDetector;
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
			up();
			return true;
		}

        return false;
	}
	
	public void up()
	{
		Log.d("beerGame", "Loading beer pouring game");
		Intent up = new Intent(getApplicationContext(), TheBeerActivity.class);
		startActivity(up);
		finish();
		overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
	}
	
	@Override
	public void onBackPressed() {
		Intent back = new Intent(getApplicationContext(), TheBeerActivity.class);
		startActivity(back);
		finish();
		overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
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